package com.example.mall.controller;

import com.example.mall.common.Result;
import com.example.mall.entity.Product;
import com.example.mall.service.IProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Result<List<Product>> getAllProducts(@RequestParam(required = false) String category) {
        if (category != null && !category.isEmpty()) {
            return Result.success(productService.getByCategory(category));
        }
        return Result.success(productService.list());
    }

    @GetMapping("/categories")
    public Result<List<String>> getAllCategories() {
        return Result.success(productService.getAllCategories());
    }

    @GetMapping("/{id}")
    public Result<Product> getProductById(@PathVariable Long id) {
        return Result.success(productService.getById(id));
    }

    @PostMapping
    public Result<Product> addProduct(@RequestBody Product product) {
        productService.save(product);
        return Result.success("新增成功", product);
    }

    @PutMapping("/{id}")
    public Result<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        product.setId(id);
        productService.updateById(product);
        return Result.success("更新成功", productService.getById(id));
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteProduct(@PathVariable Long id) {
        productService.removeById(id);
        return Result.success(null);
    }

}
