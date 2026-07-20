# Simple Mall 商城项目扩展规划路线图

> 本文档参考 [macrozheng/mall](https://github.com/macrozheng/mall) 和 [若依(RuoYi)](https://gitee.com/y_project/RuoYi) 商城系统，制定 Simple Mall 项目的功能扩展规划。

---

## 一、项目现状

### 1.1 已有功能

| 模块 | 功能 |
|------|------|
| 用户管理 | 注册、登录、CRUD |
| 商品管理 | 列表、分类、详情、CRUD |
| 购物车 | 添加、修改、删除、清空 |
| 订单管理 | 创建、查询、状态更新 |

### 1.2 技术栈

| 层级 | 技术 |
|------|------|
| 前端 | Vue 3 + Vite + Axios |
| 后端 | Spring Boot 3.2.0 + MyBatis-Plus 3.5.5 |
| 数据库 | MySQL 8.0 |
| 接口文档 | Knife4j 4.5.0 |

---

## 二、扩展规划总览

```
阶段一（短期）          阶段二（中期）            阶段三（长期）
基础功能完善    →    运营管理平台    →    高级电商能力
(1-2周)             (2-4周)               (1-2月)
```

---

## 三、阶段一：基础功能完善（短期）

### 3.1 安全认证模块

**参考：mall 的 Spring Security + JWT，若依的权限管理**

#### 3.1.1 Spring Security + JWT 认证

```
新增依赖：
- spring-boot-starter-security
- jjwt-api / jjwt-impl / jjwt-jackson

实现功能：
- 用户登录颁发 JWT Token
- Token 校验过滤器
- 密码加密存储（BCrypt）
- 接口权限控制
```

**实现要点：**

```java
// 1. Security 配置
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // 配置白名单路径：/api/users/login, /doc.html, /swagger-ui/**
    // 其他路径需认证
}

// 2. JWT 工具类
public class JwtTokenUtil {
    // 生成 Token
    // 解析 Token
    // 校验 Token
}

// 3. JWT 过滤器
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    // 从 Header 提取 Token
    // 校验并设置 SecurityContext
}
```

**数据库变更：**

```sql
-- 用户表增加角色字段
ALTER TABLE `user` ADD COLUMN `role` VARCHAR(20) DEFAULT 'user' COMMENT '角色';
-- 新增权限表
CREATE TABLE `role` (...);
CREATE TABLE `permission` (...);
CREATE TABLE `role_permission` (...);
```

#### 3.1.2 密码加密

```
当前问题：密码明文存储
解决方案：BCryptPasswordEncoder 加密
参考：mall 的 com.macro.mall.security.util.PasswordEncoder
```

### 3.2 商品模块增强

**参考：mall 的 PMS（商品管理系统）**

#### 3.2.1 商品分类管理

```
新增表结构：
- pms_category（商品分类表）
  - id, parent_id, name, level, sort, icon, status

实现功能：
- 树形分类展示
- 分类 CRUD
- 商品按分类筛选
```

#### 3.2.2 商品规格（SKU）

```
新增表结构：
- pms_sku（商品 SKU 表）
  - id, product_id, sp1, sp2, price, stock, pic

实现功能：
- 商品多规格支持
- 规格价格管理
- 规格库存管理
```

#### 3.2.3 品牌管理

```
新增表结构：
- pms_brand（品牌表）
  - id, name, first_letter, sort, logo, big_pic, show_status

实现功能：
- 品牌 CRUD
- 商品关联品牌
```

### 3.3 文件上传服务

```
实现功能：
- 商品图片上传
- 用户头像上传
- 通用文件存储

技术方案：
- 本地存储（开发阶段）
- 阿里云 OSS / MinIO（生产环境）

参考：mall 的 com.macro.mall.service.impl.MinIOServiceImpl
```

---

## 四、阶段二：运营管理平台（中期）

### 4.1 后台管理系统

**参考：mall-admin、若依管理系统**

#### 4.1.1 管理后台前端

```
技术选型：Vue 3 + Element Plus + Vue Router + Pinia

页面规划：
- 仪表盘（数据概览）
- 用户管理
- 商品管理（分类、品牌、SKU）
- 订单管理
- 营销管理
- 系统设置
```

#### 4.1.2 RBAC 权限管理

**参考：若依的权限模型**

```
数据库设计：
- sys_user（管理员表）
- sys_role（角色表）
- sys_menu（菜单表）
- sys_role_menu（角色菜单关联表）
- sys_user_role（用户角色关联表）

实现功能：
- 角色管理
- 菜单管理
- 按钮级权限控制
- 数据权限控制
```

### 4.2 订单模块增强

**参考：mall 的 OMS（订单管理系统）**

#### 4.2.1 订单状态机

```
订单状态流转：
  待支付 → 已支付 → 待发货 → 已发货 → 已完成
    ↓        ↓
  已取消   已退款

实现方式：
- 状态机模式
- 定时任务自动取消未支付订单
- 定时任务自动确认收货
```

#### 4.2.2 支付集成

```
支付方式：
- 支付宝支付（沙箱测试）
- 微信支付
- 模拟支付（开发调试）

实现方案：
- 统一下单接口
- 支付回调处理
- 订单状态更新

参考：mall 的 com.macro.mall.service.impl.AlipayServiceImpl
```

#### 4.2.3 物流管理

```
新增表结构：
- oms_logistics（物流表）
  - id, order_id, company, tracking_no, status

实现功能：
- 物流信息录入
- 物流轨迹查询（对接快递鸟/快递100 API）
- 发货通知
```

### 4.3 会员体系

**参考：mall 的 UMS（会员管理系统）**

```
新增功能：
- 会员等级（普通、银卡、金卡、钻石）
- 积分系统（签到、消费、活动）
- 会员权益（折扣、优先购、专属客服）

数据库设计：
- ums_member_level（会员等级表）
- ums_member（会员表）
- ums_integration_log（积分日志表）
```

### 4.4 优惠券模块

**参考：mall 的 SMS（营销管理系统）**

```
新增表结构：
- sms_coupon（优惠券表）
- sms_coupon_history（优惠券领取记录表）

实现功能：
- 优惠券创建（满减、折扣、立减）
- 优惠券领取
- 下单时使用优惠券
- 优惠券使用记录
```

---

## 五、阶段三：高级电商能力（长期）

### 5.1 搜索服务

**参考：mall 的 Elasticsearch 集成**

```
技术选型：Elasticsearch + Logstash + Kibana

实现功能：
- 商品全文搜索
- 搜索建议
- 搜索结果排序
- 搜索高亮显示
- 搜索过滤（价格、品牌、分类）

集成方案：
- Spring Data Elasticsearch
- Canal 同步 MySQL 数据到 ES
```

### 5.2 购物车优化

```
当前问题：购物车存储在数据库，性能差
优化方案：
- Redis 存储购物车
- 登录时合并购物车
- 购物车持久化

技术实现：
- Redis Hash 结构存储
- 购物车项序列化为 JSON
- 定时同步到数据库
```

### 5.3 秒杀活动

**参考：mall 的秒杀模块**

```
技术方案：
- Redis 预减库存
- 接口限流
- 异步下单（RabbitMQ）
- 防超卖（Redis + Lua）

新增表结构：
- sms_flash_promotion（秒杀活动表）
- sms_flash_promotion_session（秒杀场次表）
- sms_flash_promotion_product_relation（秒杀商品关联表）
```

### 5.4 消息通知

```
技术选型：RabbitMQ + 邮件/短信

应用场景：
- 订单创建通知
- 支付成功通知
- 发货通知
- 促销活动推送

实现方案：
- 消息队列解耦
- 邮件/短信模板
- 微信模板消息
```

### 5.5 数据统计

**参考：mall-admin 的数据面板**

```
统计维度：
- 销售额统计（日/周/月）
- 订单量统计
- 商品销量排行
- 用户增长统计
- 转化率分析

技术方案：
- 定时任务汇总数据
- 数据缓存（Redis）
- 图表展示（ECharts）
```

### 5.6 分布式架构演进

```
微服务拆分：
- mall-user-service（用户服务）
- mall-product-service（商品服务）
- mall-order-service（订单服务）
- mall-search-service（搜索服务）
- mall-admin-service（管理服务）

技术选型：
- Spring Cloud / Spring Cloud Alibaba
- Nacos（服务注册与配置中心）
- Gateway（API 网关）
- OpenFeign（服务间调用）
- Sentinel（限流熔断）
```

---

## 六、数据库扩展规划

### 6.1 表结构扩展

```
当前表：user, product, cart, order, order_item

阶段一新增：
- pms_category（商品分类）
- pms_brand（品牌）
- pms_sku（商品规格）
- sys_role（角色）
- sys_permission（权限）

阶段二新增：
- oms_logistics（物流）
- sms_coupon（优惠券）
- ums_member_level（会员等级）
- ums_integration_log（积分日志）

阶段三新增：
- sms_flash_promotion（秒杀活动）
- es_product_index（ES 商品索引）
```

### 6.2 数据库优化

```
- 读写分离（主从复制）
- 分库分表（ShardingSphere）
- 慢查询优化
- 索引优化
```

---

## 七、技术栈演进路线

| 层级 | 当前 | 阶段一 | 阶段二 | 阶段三 |
|------|------|--------|--------|--------|
| 安全 | 无 | Spring Security + JWT | RBAC 权限 | OAuth2 |
| 缓存 | 无 | Spring Cache | Redis 缓存 | Redis 集群 |
| 消息 | 无 | 无 | 无 | RabbitMQ |
| 搜索 | 数据库 LIKE | 数据库优化 | Elasticsearch | ES + Canal |
| 文件存储 | 无 | 本地/OSS | MinIO | OSS |
| 监控 | 无 | Actuator | Prometheus + Grafana | SkyWalking |
| 部署 | 手动 | Docker | Docker Compose | K8s |

---

## 八、参考项目

### 8.1 macrozheng/mall

- **GitHub**: https://github.com/macrozheng/mall
- **特点**: 最流行的 Spring Boot 电商系统，功能完整
- **参考模块**:
  - PMS（商品管理系统）
  - OMS（订单管理系统）
  - UMS（会员管理系统）
  - SMS（营销管理系统）
  - CMS（内容管理系统）

### 8.2 若依(RuoYi)

- **Gitee**: https://gitee.com/y_project/RuoYi
- **特点**: 优秀的后台管理框架，权限管理完善
- **参考模块**:
  - RBAC 权限管理
  - 代码生成器
  - 系统监控
  - 定时任务

### 8.3 其他参考

- **litemall**: https://github.com/linlinjava/litemall
- **mall-swarm**: https://github.com/macrozheng/mall-swarm（微服务版）
- **newbee-mall**: https://github.com/newbee-ltd/newbee-mall

---

## 九、实施优先级

### P0（必须）
1. ✅ Spring Security + JWT 认证
2. ✅ 密码加密
3. ✅ 商品分类管理
4. ✅ 文件上传服务

### P1（重要）
5. 后台管理系统
6. RBAC 权限管理
7. 订单状态机
8. 支付集成

### P2（增强）
9. 会员体系
10. 优惠券
11. 物流管理
12. 数据统计

### P3（高级）
13. Elasticsearch 搜索
14. 秒杀活动
15. 消息通知
16. 微服务拆分

---

## 十、总结

Simple Mall 项目当前是一个基础的电商原型，通过分阶段扩展，最终目标是建设一个功能完善的电商系统。

**核心原则：**
- 小步快跑，迭代开发
- 先能用，再优化
- 参考成熟方案，避免重复造轮子
- 保持代码简洁，避免过度设计

**每一步的验证标准：**
- 功能可用且经过测试
- 代码有注释和文档
- API 接口有 Knife4j 文档
- 数据库变更记录清晰
