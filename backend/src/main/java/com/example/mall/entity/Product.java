package com.example.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("product")
@Schema(description = "商品信息")
public class Product {

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "商品ID")
    private Long id;

    @TableField("name")
    @Schema(description = "商品名称")
    private String name;

    @TableField("description")
    @Schema(description = "商品描述")
    private String description;

    @TableField("price")
    @Schema(description = "商品价格")
    private BigDecimal price;

    @TableField("stock")
    @Schema(description = "库存数量")
    private Integer stock;

    @TableField("image")
    @Schema(description = "商品图片URL")
    private String image;

    @TableField("category")
    @Schema(description = "商品分类")
    private String category;

    @TableField("created_at")
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;

}
