package com.example.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.mall.entity.Product;

import java.util.List;

public interface IProductService extends IService<Product> {
    List<Product> getByCategory(String category);
    List<String> getAllCategories();
}
