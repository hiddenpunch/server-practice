package com.example.service

import arrow.core.Either
import com.example.entity.Language

interface ProductService {
    suspend fun createProduct(command: CreateProductCommand): Either<CreateProductFailure, ProductResult>
    suspend fun updateCommission(commission: Double): Either<UpdateCommissionFailure, ProductResult>
    suspend fun getExaminedProducts(): Either<GetExaminedProductFailure, List<ProductResult>>
    suspend fun getUnexaminedProducts(): Either<GetUnexaminedProductFailure, List<ProductResult>>
    suspend fun createDescription(command: CreateDescriptionCommand): Either<CreateDescriptionFailure, ProductDescriptionResult>
    suspend fun examineProduct(productId: String): Either<ExamineProductFailure, ProductResult>
    suspend fun updateDescription(command: UpdateDescriptionCommand): Either<UpdateDescriptionFailure, ProductDescriptionResult>
    suspend fun deleteDescription(descriptionId: String): Either<DeleteDescriptionFailure, Unit>

    data class CreateProductCommand(
        val title: String,
        val content: String,
        val price: Double
    )
    data class CreateDescriptionCommand(
        val language: Language,
        val title: String,
        val content: String
    )
    data class UpdateDescriptionCommand(
        val language: Language,
        val title: String,
        val content: String
    )

    sealed interface CreateProductFailure {
        data class InternalError(val message: String): CreateProductFailure
    }
    sealed interface UpdateCommissionFailure {
        data class InternalError(val message: String): UpdateCommissionFailure
    }
    sealed interface GetExaminedProductFailure {
        data class InternalError(val message: String): GetExaminedProductFailure
    }
    sealed interface GetUnexaminedProductFailure {
        data class InternalError(val message: String): GetUnexaminedProductFailure
    }
    sealed interface CreateDescriptionFailure {
        data class InternalError(val message: String): CreateDescriptionFailure
    }
    sealed interface ExamineProductFailure {
        data class InternalError(val message: String): ExamineProductFailure
    }
    sealed interface UpdateDescriptionFailure {
        data class InternalError(val message: String): UpdateDescriptionFailure
    }
    sealed interface DeleteDescriptionFailure {
        data class InternalError(val message: String): DeleteDescriptionFailure
    }

    data class ProductResult(
        val id: String,
        val descriptions: List<ProductDescriptionResult>,
        val price: Double,
        val commission: Double?,
        val isExamined: Boolean
    )

    data class ProductDescriptionResult(
        val language: Language,
        val title: String,
        val content: String
    )
}