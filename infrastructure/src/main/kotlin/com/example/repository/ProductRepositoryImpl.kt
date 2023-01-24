package com.example.repository

import RepositoryUtil
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.example.entity.Language
import com.example.entity.Product
import com.example.entity.ProductDescription
import java.lang.Exception

class ProductRepositoryImpl(
    private val connection: DatabaseConnection
): ProductRepository {
    override suspend fun createProduct(product: Product): Either<ProductRepository.CreateFailure, Product> {
        return try {
            val productQuery = """
            INSERT INTO products (id, price, commission, is_examined) VALUES (?, ?, ?, ?)
            RETURNING id, price, commission, is_examined;
        """.trimIndent()
            val valuesPlaceholder =
                generateSequence { "(?, ?, ?, ?, ?)" }.take(
                    product.descriptions.size
                ).joinToString(",")
            val params = product.descriptions.asSequence().map {
                sequenceOf(
                    it.id,
                    it.productId,
                    it.language,
                    it.title,
                    it.content
                )
            }.flatten().toList()
            val descriptionQuery = """
                INSERT INTO product_descriptions (id, product_id, language, title, content) VALUES $valuesPlaceholder
                RETURNING id, product_id, language, title, content
            """.trimIndent()
            connection.inTransaction {
                val productResult = it.sendPreparedStatement(
                    productQuery,
                    listOf(product.id, product.price, product.commission, product.isExamined)
                )
                val descriptionsResult = it.sendPreparedStatement(descriptionQuery, params)
                val pureProduct = RepositoryUtil.queryResultMapper(productResult, ProductWithoutDescription::class)!!
                val descriptions = RepositoryUtil.queryResultMapperList(descriptionsResult, ProductDescription::class)!!
                Product(
                    id = pureProduct.id,
                    descriptions = descriptions,
                    price = pureProduct.price,
                    commission = pureProduct.commission,
                    isExamined = pureProduct.isExamined
                ).right()
            }
        } catch (e: Exception) {
            ProductRepository.CreateFailure.DBError(e.message.orEmpty()).left()
        }
    }

    override suspend fun updateProduct(product: Product.Update): Either<ProductRepository.UpdateFailure, Product> {
        val (id, price, commission, isExamined) = product
        val setTargets = mutableListOf<String>()
        val params = mutableListOf<String>()
        price?.let {
            setTargets.add("price = ?")
            params.add(it.toString())
        }
        commission?.let {
            setTargets.add("commission = ?")
            params.add(it.toString())
        }
        isExamined?.let {
            setTargets.add("is_examined = ?")
            params.add(it.toString())
        }
        if(setTargets.size == 0) {
            return ProductRepository.UpdateFailure.AllFieldsNull("At least one field should not be null").left()
        }
        val query = """
            UPDATE products
            SET ${setTargets.joinToString(",")}
            WHERE id = ?
            RETURNING id, price, commission, is_examined;
        """.trimIndent()
        val descriptionQuery = """
            SELECT id, product_id, language, title, content FROM product_descriptions
            WHERE product_id = ?;
        """.trimIndent()
        return try {
            connection.inTransaction {
                val productResult = it.sendPreparedStatement(query, params + product.id)
                val pureProduct = RepositoryUtil.queryResultMapper(productResult, ProductWithoutDescription::class)!!
                val descriptionsResult = it.sendPreparedStatement(descriptionQuery, listOf(product.id))
                val descriptions = RepositoryUtil.queryResultMapperList(descriptionsResult, ProductDescription::class)!!
                Product(
                    id = pureProduct.id,
                    descriptions = descriptions,
                    price = pureProduct.price,
                    commission = pureProduct.commission,
                    isExamined = pureProduct.isExamined
                ).right()
            }
        } catch (e: Exception) {
            ProductRepository.UpdateFailure.DBError(e.message.orEmpty()).left()
        }
    }

    override suspend fun findProductsByIsExamined(isExamined: Boolean): Either<ProductRepository.FindFailure, List<Product>> {
        val query = """
            SELECT p.id, price, commission, is_examined, pd.id as description_id, language, title, content
            FROM products p JOIN product_descriptions pd
            ON p.id = pd.product_id
            WHERE is_examined = ?;
        """.trimIndent()
        return try {
            val result = connection.sendPreparedStatement(query, listOf(isExamined))
            val productJoinDescription = RepositoryUtil.queryResultMapperList(result, ProductJoinDescription::class)
            val grouping = productJoinDescription.groupBy(
                {
                    ProductWithoutDescription(it.id, it.price, it.commission, it.isExamined)
                },
                {
                    ProductDescription(
                        it.descriptionId,
                        it.id,
                        it.language,
                        it.title,
                        it.content
                    )
                }
            )
            grouping.map {
                Product(
                    id = it.key.id,
                    descriptions = it.value,
                    price = it.key.price,
                    commission = it.key.commission,
                    isExamined = it.key.isExamined
                )
            }.right()
        } catch (e: Exception) {
            ProductRepository.FindFailure.DBError(e.message.orEmpty()).left()
        }
    }

    override suspend fun createDescription(productDescription: ProductDescription): Either<ProductRepository.CreateFailure, ProductDescription> {
        val (id, productId, language, title, content) = productDescription
        val query = """
            INSERT INTO product_descriptions (id, product_id, language, title, content)
            VALUES (?, ?, ?, ?, ?)
            RETURNING id, product_id, language, title, content
        """.trimIndent()
        return try {
            val result = connection.sendPreparedStatement(query, listOf(id, productId, language, title, content))
            RepositoryUtil.queryResultMapper(result, ProductDescription::class)!!.right()
        } catch (e: Exception) {
            ProductRepository.CreateFailure.DBError(e.message.orEmpty()).left()
        }
    }

    override suspend fun updateDescription(productDescription: ProductDescription.Update): Either<ProductRepository.UpdateFailure, ProductDescription> {
        val (id, language, title, content) = productDescription
        val query = """
            UPDATE product_descriptions
            SET
                language = ?,
                title = ?,
                content = ? 
            WHERE id = ?
            RETURNING id, product_id, language, title, content;
        """.trimIndent()
        return try {
            val result = connection.sendPreparedStatement(query, listOf(language, title, content, id))
            RepositoryUtil.queryResultMapper(result, ProductDescription::class)!!.right()
        } catch (e: Exception) {
            ProductRepository.UpdateFailure.DBError(e.message.orEmpty()).left()
        }
    }

    override suspend fun deleteDescriptionById(id: String): Either<ProductRepository.DeleteFailure, Unit> {
        val query = """
            DELETE FROM product_descriptions
            WHERE id = ?;
        """.trimIndent()
        return try {
            connection.sendPreparedStatement(query, listOf(id))
            Unit.right()
        } catch (e: Exception) {
            ProductRepository.DeleteFailure.DBError(e.message.orEmpty()).left()
        }
    }

    private suspend fun findDescriptionsByProductId(productId: String): Either<ProductRepository.FindFailure, List<ProductDescription>> {
        val query = """
            SELECT id, product_id, language, title, content FROM product_descriptions
            WHERE product_id = ?;
        """.trimIndent()
        return try {
            val result = connection.sendPreparedStatement(query, listOf(productId))
            RepositoryUtil.queryResultMapperList(result, ProductDescription::class).right()
        } catch (e: Exception) {
            ProductRepository.FindFailure.DBError(e.message.orEmpty()).left()
        }
    }

    data class ProductJoinDescription(
        val id: String,
        val price: Double,
        val commission: Double?,
        val isExamined: Boolean,
        val descriptionId: String,
        val language: Language,
        val title: String,
        val content: String
    )
    data class ProductWithoutDescription(
        val id: String,
        val price: Double,
        val commission: Double?,
        val isExamined: Boolean
    )
}