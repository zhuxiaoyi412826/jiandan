CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    `password` VARCHAR(100) NOT NULL COMMENT '密码',
    `email` VARCHAR(100) COMMENT '邮箱',
    `phone` VARCHAR(20) COMMENT '手机号',
    `nickname` VARCHAR(50) COMMENT '昵称',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

CREATE TABLE IF NOT EXISTS `product` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '商品ID',
    `name` VARCHAR(200) NOT NULL COMMENT '商品名称',
    `description` TEXT COMMENT '商品描述',
    `price` DECIMAL(10,2) NOT NULL COMMENT '商品价格',
    `stock` INT NOT NULL DEFAULT 0 COMMENT '库存数量',
    `image` VARCHAR(500) COMMENT '商品图片',
    `category` VARCHAR(50) COMMENT '商品分类',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品表';

CREATE TABLE IF NOT EXISTS `cart` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '购物车ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `product_id` BIGINT NOT NULL COMMENT '商品ID',
    `quantity` INT NOT NULL DEFAULT 1 COMMENT '数量',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
    FOREIGN KEY (`product_id`) REFERENCES `product`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='购物车表';

CREATE TABLE IF NOT EXISTS `order` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单ID',
    `order_no` VARCHAR(50) NOT NULL UNIQUE COMMENT '订单号',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `total_amount` DECIMAL(10,2) NOT NULL COMMENT '订单总金额',
    `status` INT NOT NULL DEFAULT 0 COMMENT '订单状态：0待支付，1已支付，2已发货，3已完成，4已取消',
    `shipping_address` VARCHAR(500) COMMENT '收货地址',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单表';

CREATE TABLE IF NOT EXISTS `order_item` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单项ID',
    `order_id` BIGINT NOT NULL COMMENT '订单ID',
    `product_id` BIGINT NOT NULL COMMENT '商品ID',
    `product_name` VARCHAR(200) NOT NULL COMMENT '商品名称',
    `product_price` DECIMAL(10,2) NOT NULL COMMENT '商品价格',
    `quantity` INT NOT NULL COMMENT '数量',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (`order_id`) REFERENCES `order`(`id`),
    FOREIGN KEY (`product_id`) REFERENCES `product`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单项表';

INSERT INTO `user` (`username`, `password`, `email`, `phone`, `nickname`) VALUES
('admin', '123456', 'admin@example.com', '13800138000', '管理员'),
('user1', '123456', 'user1@example.com', '13900139001', '用户1'),
('user2', '123456', 'user2@example.com', '13900139002', '用户2')
ON DUPLICATE KEY UPDATE password = VALUES(password), email = VALUES(email), phone = VALUES(phone), nickname = VALUES(nickname);

INSERT INTO `product` (`name`, `description`, `price`, `stock`, `image`, `category`) VALUES
('iPhone 15 Pro', '苹果最新款手机，搭载A17 Pro芯片', 8999.00, 100, 'https://images.unsplash.com/photo-1695450374748-4e5316914a47', '手机'),
('MacBook Pro 14', '苹果笔记本电脑，M3 Pro芯片', 14999.00, 50, 'https://images.unsplash.com/photo-1517336714731-489689fd1ca8', '电脑'),
('AirPods Pro 2', '苹果无线蓝牙耳机，主动降噪', 1899.00, 200, 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e', '配件'),
('iPad Air', '苹果平板电脑，M2芯片', 4799.00, 80, 'https://images.unsplash.com/photo-1510573585146-6356a6199901', '平板'),
('Apple Watch Series 9', '苹果智能手表，健康监测', 3199.00, 150, 'https://images.unsplash.com/photo-1523275335684-37898b6baf30', '穿戴'),
('华为Mate 60 Pro', '华为旗舰手机，麒麟9000S芯片', 6999.00, 120, 'https://images.unsplash.com/photo-1698223927713-7494586e7580', '手机'),
('小米14', '小米旗舰手机，骁龙8 Gen3芯片', 3999.00, 180, 'https://images.unsplash.com/photo-1670272513634-1b0058f3c461', '手机'),
('索尼WH-1000XM5', '索尼无线降噪耳机', 2499.00, 60, 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e', '配件')
ON DUPLICATE KEY UPDATE description = VALUES(description), price = VALUES(price), stock = VALUES(stock), image = VALUES(image), category = VALUES(category);
