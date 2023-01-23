package com.example.repository

import arrow.core.Either
import com.example.entity.Product
import com.example.entity.ProductDescription

interface ProductRepository {
    suspend fun createProduct(product: Product): Either<CreateFailure, Product>
    suspend fun updateProduct(product: Product.Update): Either<UpdateFailure, Product>
    suspend fun findProductsByIsExamined(isExamined: Boolean): Either<FindFailure, List<Product>>
    suspend fun createDescription(productDescription: ProductDescription): Either<CreateFailure, ProductDescription>
    suspend fun updateDescription(productDescription: ProductDescription.Update): Either<UpdateFailure, ProductDescription>
    suspend fun deleteDescriptionById(id: String): Either<DeleteFailure, Unit>

    sealed interface CreateFailure {
        data class DBError(val message: String): CreateFailure
    }
    sealed interface UpdateFailure {
        data class DBError(val message: String): UpdateFailure
    }
    sealed interface DeleteFailure {
        data class DBError(val message: String): DeleteFailure
    }
    sealed interface FindFailure {
        data class DBError(val message: String): FindFailure
    }
}