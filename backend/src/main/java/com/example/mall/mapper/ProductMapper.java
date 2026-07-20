package com.example.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.mall.entity.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
}
