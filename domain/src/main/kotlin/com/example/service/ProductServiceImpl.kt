package com.example.service

import arrow.core.Either
import arrow.core.computations.either
import com.example.entity.Language
import com.example.entity.Product
import com.example.entity.ProductDescription
import com.example.repository.ProductRepository
import java.util.UUID

class ProductServiceImpl(private val repository: ProductRepository) : ProductService {
    override suspend fun createProduct(command: ProductService.CreateProductCommand): Either<ProductService.CreateProductFailure, Product> = either {
        val productId = UUID.randomUUID().toString()
        val description = ProductDescription(
            id = UUID.randomUUID().toString(),
            productId = productId,
            language = Language.KR,
            title = command.title,
            content = command.content
        )
        val product = Product(
            id = productId,
            descriptions = listOf(description),
            price = command.price,
            commission = null,
            isExamined = false
        )
        repository.createProduct(product).mapLeft { 
            when (it) {
                is ProductRepository.CreateFailure.DBError -> ProductService.CreateProductFailure.InternalError(it.message)
            }
        }.bind()
    }

    override suspend fun updateCommission(command: ProductService.UpdateCommissionCommand): Either<ProductService.UpdateCommissionFailure, Product> = either {
        val updateProduct = Product.Update(
            id = command.id,
            commission = command.commission
        )
        repository.updateProduct(updateProduct).mapLeft {
            when (it) {
                is ProductRepository.UpdateFailure.DBError -> ProductService.UpdateCommissionFailure.InternalError(it.message)
                is ProductRepository.UpdateFailure.AllFieldsNull -> ProductService.UpdateCommissionFailure.InternalError(it.message)
            }
        }.bind()
    }

    override suspend fun getExaminedProducts(language: Language?): Either<ProductService.GetExaminedProductFailure, List<Product>> = either {
        val products = repository.findProductsByIsExamined(true).mapLeft {
            when (it) {
                is ProductRepository.FindFailure.DBError -> ProductService.GetExaminedProductFailure.InternalError(it.message)
            }
        }.bind()
        if (language != null) {
            products.map {
                it.copy(
                    descriptions = it.descriptions.filter { it.language == language }
                )
            }
        } else products
    }

    override suspend fun getUnexaminedProducts(): Either<ProductService.GetUnexaminedProductFailure, List<Product>> = either {
        repository.findProductsByIsExamined(false).mapLeft {
            when (it) {
                is ProductRepository.FindFailure.DBError -> ProductService.GetUnexaminedProductFailure.InternalError(it.message)
            }
        }.bind()
    }

    override suspend fun createDescription(command: ProductService.CreateDescriptionCommand): Either<ProductService.CreateDescriptionFailure, ProductDescription> = either {
        val description = ProductDescription(
            id = UUID.randomUUID().toString(),
            productId = command.productId,
            language = command.language,
            title = command.title,
            content = command.content
        )
        repository.createDescription(description).mapLeft {
            when (it) {
                is ProductRepository.CreateFailure.DBError -> ProductService.CreateDescriptionFailure.InternalError(it.message)
            }
        }.bind()
    }

    override suspend fun examineProduct(productId: String): Either<ProductService.ExamineProductFailure, Product> = either {
        val updateProduct = Product.Update(
            id = productId,
            isExamined = true
        )
        repository.updateProduct(updateProduct).mapLeft {
            when (it) {
                is ProductRepository.UpdateFailure.DBError -> ProductService.ExamineProductFailure.InternalError(it.message)
                is ProductRepository.UpdateFailure.AllFieldsNull -> ProductService.ExamineProductFailure.InternalError(it.message)
            }
        }.bind()
    }

    override suspend fun updateDescription(command: ProductService.UpdateDescriptionCommand): Either<ProductService.UpdateDescriptionFailure, ProductDescription> = either {
        val description = ProductDescription.Update(
            id = command.id,
            language = command.language,
            title = command.title,
            content = command.content
        )
        repository.updateDescription(description).mapLeft {
            when (it) {
                is ProductRepository.UpdateFailure.DBError -> ProductService.UpdateDescriptionFailure.InternalError(it.message)
                is ProductRepository.UpdateFailure.AllFieldsNull -> ProductService.UpdateDescriptionFailure.InternalError(it.message)
            }
        }.bind()
    }

    override suspend fun deleteDescription(descriptionId: String): Either<ProductService.DeleteDescriptionFailure, Unit> = either {
        repository.deleteDescriptionById(descriptionId).mapLeft {
            when (it) {
                is ProductRepository.DeleteFailure.DBError -> ProductService.DeleteDescriptionFailure.InternalError(it.message)
            }
        }.bind()
    }
}