package com.example.service

import arrow.core.Either
import com.example.entity.Language
import com.example.entity.Product
import com.example.entity.ProductDescription

interface ProductService {
    suspend fun createProduct(command: CreateProductCommand): Either<CreateProductFailure, Product>
    suspend fun updateCommission(command: UpdateCommissionCommand): Either<UpdateCommissionFailure, Product>
    suspend fun getExaminedProducts(language: Language?): Either<GetExaminedProductFailure, List<Product>>
    suspend fun getUnexaminedProducts(): Either<GetUnexaminedProductFailure, List<Product>>
    suspend fun createDescription(command: CreateDescriptionCommand): Either<CreateDescriptionFailure, ProductDescription>
    suspend fun examineProduct(productId: String): Either<ExamineProductFailure, Product>
    suspend fun updateDescription(command: UpdateDescriptionCommand): Either<UpdateDescriptionFailure, ProductDescription>
    suspend fun deleteDescription(descriptionId: String): Either<DeleteDescriptionFailure, Unit>

    data class CreateProductCommand(
        val title: String,
        val content: String,
        val price: Double
    )
    data class UpdateCommissionCommand(
        val id: String,
        val commission: Double
    )
    data class CreateDescriptionCommand(
        val productId: String,
        val language: Language,
        val title: String,
        val content: String
    )
    data class UpdateDescriptionCommand(
        val id: String,
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
}