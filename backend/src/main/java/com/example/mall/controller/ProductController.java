package com.example.mall.controller;

import com.example.mall.common.Result;
import com.example.mall.entity.Product;
import com.example.mall.service.IProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "商品管理", description = "商品的查询、分类、详情、新增、修改、删除接口")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "获取商品列表", description = "获取所有商品，支持按分类筛选")
    @GetMapping
    public Result<List<Product>> getAllProducts(@Parameter(description = "商品分类（可选）") @RequestParam(required = false) String category) {
        if (category != null && !category.isEmpty()) {
            return Result.success(productService.getByCategory(category));
        }
        return Result.success(productService.list());
    }

    @Operation(summary = "获取所有商品分类", description = "返回系统中所有商品的分类列表")
    @GetMapping("/categories")
    public Result<List<String>> getAllCategories() {
        return Result.success(productService.getAllCategories());
    }

    @Operation(summary = "根据ID查询商品", description = "根据商品ID获取商品详细信息")
    @GetMapping("/{id}")
    public Result<Product> getProductById(@Parameter(description = "商品ID") @PathVariable Long id) {
        return Result.success(productService.getById(id));
    }

    @Operation(summary = "新增商品", description = "添加一个新商品")
    @PostMapping
    public Result<Product> addProduct(@RequestBody Product product) {
        productService.save(product);
        return Result.success("新增成功", product);
    }

    @Operation(summary = "更新商品信息", description = "根据商品ID更新商品信息")
    @PutMapping("/{id}")
    public Result<Product> updateProduct(@Parameter(description = "商品ID") @PathVariable Long id, @RequestBody Product product) {
        product.setId(id);
        productService.updateById(product);
        return Result.success("更新成功", productService.getById(id));
    }

    @Operation(summary = "删除商品", description = "根据商品ID删除商品")
    @DeleteMapping("/{id}")
    public Result<Void> deleteProduct(@Parameter(description = "商品ID") @PathVariable Long id) {
        productService.removeById(id);
        return Result.success(null);
    }

}
