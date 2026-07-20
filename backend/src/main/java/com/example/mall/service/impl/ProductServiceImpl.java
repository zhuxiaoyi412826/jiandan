package com.example.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mall.entity.Product;
import com.example.mall.mapper.ProductMapper;
import com.example.mall.service.IProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {

    @Override
    public List<Product> getByCategory(String category) {
        return baseMapper.selectList(new LambdaQueryWrapper<Product>()
                .eq(Product::getCategory, category));
    }

    @Override
    public List<String> getAllCategories() {
        List<Product> products = baseMapper.selectList(null);
        return products.stream()
                .map(Product::getCategory)
                .distinct()
                .collect(Collectors.toList());
    }

}
