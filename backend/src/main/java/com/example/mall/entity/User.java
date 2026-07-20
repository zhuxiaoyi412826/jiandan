package com.example.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user")
@Schema(description = "用户信息")
public class User {

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "用户ID")
    private Long id;

    @TableField("username")
    @Schema(description = "用户名")
    private String username;

    @TableField("password")
    @Schema(description = "密码")
    private String password;

    @TableField("email")
    @Schema(description = "邮箱")
    private String email;

    @TableField("phone")
    @Schema(description = "手机号")
    private String phone;

    @TableField("nickname")
    @Schema(description = "昵称")
    private String nickname;

    @TableField("created_at")
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;

}
