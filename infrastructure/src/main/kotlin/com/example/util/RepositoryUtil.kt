import com.github.jasync.sql.db.QueryResult
import com.github.jasync.sql.db.RowData
import com.github.jasync.sql.db.general.ArrayRowData
import java.time.LocalDateTime
import java.util.stream.Collectors
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.jvm.jvmName
import kotlinx.serialization.SerialName

object RepositoryUtil {
    fun <T : Any> queryResultMapper(queryResult: QueryResult, kClass: KClass<T>): T? {
        val constructor = kClass.primaryConstructor ?: throw Exception("${kClass.jvmName} no default constructor")
        val constructorParams = constructor.parameters

        return when (queryResult.rows.size) {
            0 -> null
            1 -> createClassByRowData(kClass, constructorParams, queryResult.rows.first())
            else -> throw Exception("Query result exceeds 1, QueryResult Rows = $queryResult.rows.size") // TODO Error 처리필요
        }
    }

    fun <T : Any> queryResultMapperList(queryResult: QueryResult, kClass: KClass<T>): List<T> {
        val constructor = kClass.primaryConstructor ?: throw Exception("${kClass.jvmName} no default constructor")
        val constructorParams = constructor.parameters

        return queryResult.rows.map { createClassByRowData(kClass, constructorParams, it) }
    }

    private fun <T : Any> createClassByRowData(kClass: KClass<T>, params: List<KParameter>, rowData: RowData): T {

        rowData as ArrayRowData // rowData.mapping 을 쓰기 위해서 ArrayRowData로 cast 해야함

        // 결과값의 column의 타당성 검사
        // 1) 생성자 parameter의 값이 null
        val constructorParams: List<String> = params.map {
            if (it.name == null) {
                throw Exception("constructor parameter name is null") // TODO
            } else {
                it.name!!.camelToSnakeCase()
            }
        }

        // 2) Query 결과값의 column이 생성자의 파라미터에 모두 mapping이 되는지 validation check
        rowData.mapping.forEach {
            if (!constructorParams.contains(it.key)) {
                throw Exception("Query result column{=${it.key}} is not found in constructor parameter{=$constructorParams} ")
            }
        }

        val argMap = params.associateWith {
            val constructSnakeCaseParam = it.name.toString().camelToSnakeCase()

            if (rowData.mapping.contains(constructSnakeCaseParam)) {
                if (it.type.jvmErasure.isSubclassOf(Enum::class)) { // Enum 처리
                    getEnumValue(
                        it.type.jvmErasure.jvmName,
                        rowData[constructSnakeCaseParam] as String?
                    )
                } else if ((it.type.jvmErasure == List::class || it.type.jvmErasure == Set::class) && rowData[constructSnakeCaseParam] != null) {
                    // convert list String to List
                    val reg = "\"(.*?)\"".toRegex()
                    val regList = if (rowData[constructSnakeCaseParam] is List<*>) {
                        rowData[constructSnakeCaseParam] as List<String>
                    } else reg.findAll(rowData[constructSnakeCaseParam]!! as String).map { it.value }.toList()

                    // TODO 리펙토링 필요, 모든 케이스
                    // List의 generic Type이 Enum인경우, String이 아니고 해당 Enum type으로 변경해줘야한다.
                    // List의 generic Type이 Any일때는 arguments가 여러개 일 수 있으므로, 사이즈가 1 && Enum인 경우만 변환해준다.
                    if (it.type.arguments.size == 1 && it.type.arguments[0].type!!.jvmErasure.isSubclassOf(Enum::class)) {
                        regList.stream().map { e ->
                            getEnumValue(
                                it.type.arguments[0].type!!.jvmErasure.jvmName,
                                e.replace("\"", "")
                            )
                        }.collect(
                            Collectors.toCollection {
                                if (it.type.jvmErasure == List::class) ArrayList()
                                else HashSet()
                            }
                        )
                    } else {
                        regList.stream().map { e -> e.replace("\"", "") }
                            .collect(
                                Collectors.toCollection {
                                    if (it.type.jvmErasure == List::class) ArrayList()
                                    else HashSet()
                                }
                            )
                    }
                } else if (it.type.jvmErasure == Boolean::class) {
                    rowData.getBoolean(constructSnakeCaseParam)
                } else if (it.type.jvmErasure == LocalDateTime::class) {
                    rowData.getDate(constructSnakeCaseParam)
                } else {
                    rowData[constructSnakeCaseParam]
                }
            } else {
                null
            }
        }

        if (kClass.primaryConstructor == null) throw Exception("primaryConstructor is null")
        return kClass.primaryConstructor!!.callBy(argMap)
    }

    private fun getEnumValue(enumClassName: String, enumValue: String?): Any? {
        return try {
            return if (enumValue.isNullOrEmpty()) null
            else Class.forName(enumClassName).enumConstants?.run {
                // clz must be Array<Enum<*>> type
                @Suppress("UNCHECKED_CAST")
                (this as Array<Enum<*>>).first {
                    it.name == enumValue || it::class.java.getField(it.name)
                        .annotations
                        .filterIsInstance<SerialName>()
                        .any { serialName -> serialName.value == enumValue }
                }
            }
        } catch (e: Exception) {
            null
        }
    }

    // String extensions
    fun String.camelToSnakeCase(): String {
        val camelRegex = "(?<=[a-zA-Z0-9])[A-Z]".toRegex()
        return camelRegex.replace(this) {
            "_${it.value}"
        }.lowercase()
    }

    fun getSerializedName(e: Enum<*>): String? {
        return try {
            val f = e.javaClass.getField(e.name)
            f.getAnnotation(SerialName::class.java).value
        } catch (ignored: NoSuchFieldException) {
            null
        }
    }
}
