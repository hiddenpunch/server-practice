package com.example.service

import arrow.core.Either

class ProductServiceImpl : ProductService {
    override suspend fun createProduct(command: ProductService.CreateProductCommand): Either<ProductService.CreateProductFailure, ProductService.ProductResult> {
        TODO("Not yet implemented")
    }

    override suspend fun updateCommission(command: ProductService.UpdateCommissionCommand): Either<ProductService.UpdateCommissionFailure, ProductService.ProductResult> {
        TODO("Not yet implemented")
    }

    override suspend fun getExaminedProducts(): Either<ProductService.GetExaminedProductFailure, List<ProductService.ProductResult>> {
        TODO("Not yet implemented")
    }

    override suspend fun getUnexaminedProducts(): Either<ProductService.GetUnexaminedProductFailure, List<ProductService.ProductResult>> {
        TODO("Not yet implemented")
    }

    override suspend fun createDescription(command: ProductService.CreateDescriptionCommand): Either<ProductService.CreateDescriptionFailure, ProductService.ProductDescriptionResult> {
        TODO("Not yet implemented")
    }

    override suspend fun examineProduct(productId: String): Either<ProductService.ExamineProductFailure, ProductService.ProductResult> {
        TODO("Not yet implemented")
    }

    override suspend fun updateDescription(command: ProductService.UpdateDescriptionCommand): Either<ProductService.UpdateDescriptionFailure, ProductService.ProductDescriptionResult> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteDescription(descriptionId: String): Either<ProductService.DeleteDescriptionFailure, Unit> {
        TODO("Not yet implemented")
    }
}