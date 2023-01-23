package com.example.routing.dto.mapper

import com.example.routing.dto.Language
import com.example.routing.dto.request.CreateProductDescriptionRequest
import com.example.routing.dto.request.CreateProductRequest
import com.example.routing.dto.request.UpdateProductDescriptionRequest
import com.example.routing.dto.response.ProductDescriptionResponse
import com.example.routing.dto.response.ProductResponse
import com.example.service.ProductService
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)

interface ProductMapper {
    fun toCreateProductCommand(
        request: CreateProductRequest
    ): ProductService.CreateProductCommand
    fun toCreateDescriptionCommand(
        request: CreateProductDescriptionRequest
    ): ProductService.CreateDescriptionCommand
    fun toUpdateDescriptionCommand(
        request: UpdateProductDescriptionRequest
    ): ProductService.UpdateDescriptionCommand

    fun toProductDescriptionResponse(
        result: ProductService.ProductDescriptionResult
    ): ProductDescriptionResponse
    @Mapping(target = "isExamined", source = "examined")
    fun toProductResponse(
        result: ProductService.ProductResult
    ): ProductResponse

}