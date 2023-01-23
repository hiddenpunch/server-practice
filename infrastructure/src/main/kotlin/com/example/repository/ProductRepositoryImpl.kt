package com.example.repository

import arrow.core.Either
import com.example.entity.Product
import com.example.entity.ProductDescription

class ProductRepositoryImpl: ProductRepository {
    override suspend fun createProduct(product: Product): Either<ProductRepository.CreateFailure, Product> {
        TODO("Not yet implemented")
    }

    override suspend fun updateProduct(product: Product.Update): Either<ProductRepository.UpdateFailure, Product> {
        TODO("Not yet implemented")
    }

    override suspend fun findProductsByIsExamined(isExamined: Boolean): Either<ProductRepository.FindFailure, List<Product>> {
        TODO("Not yet implemented")
    }

    override suspend fun createDescription(productDescription: ProductDescription): Either<ProductRepository.CreateFailure, ProductDescription> {
        TODO("Not yet implemented")
    }

    override suspend fun updateDescription(productDescription: ProductDescription.Update): Either<ProductRepository.UpdateFailure, ProductDescription> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteDescriptionById(id: String): Either<ProductRepository.DeleteFailure, Unit> {
        TODO("Not yet implemented")
    }
}