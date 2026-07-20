# Simple Mall 简单商城项目

基于 Vue 3 + Spring Boot + MyBatis-Plus + MySQL 的简单电商商城系统。

## 技术栈

### 前端
- **框架**: Vue 3 + Vite
- **路由**: Vue Router
- **HTTP**: Axios
- **UI**: Element Plus

### 后端
- **框架**: Spring Boot 3.2.0
- **持久层**: MyBatis-Plus 3.5.5
- **数据库**: MySQL 8.0+
- **构建工具**: Maven
- **JDK**: 17

## 项目结构

```
ceshi/
├── backend/                    # 后端 Spring Boot 项目
│   ├── src/main/java/com/example/mall/
│   │   ├── MallApplication.java       # 启动类
│   │   ├── controller/                # 控制层
│   │   │   ├── UserController.java
│   │   │   ├── ProductController.java
│   │   │   ├── CartController.java
│   │   │   └── OrderController.java
│   │   ├── service/                   # 服务层
│   │   │   ├── IUserService.java
│   │   │   ├── impl/UserServiceImpl.java
│   │   │   ├── IProductService.java
│   │   │   ├── impl/ProductServiceImpl.java
│   │   │   ├── ICartService.java
│   │   │   ├── impl/CartServiceImpl.java
│   │   │   ├── IOrderService.java
│   │   │   └── impl/OrderServiceImpl.java
│   │   ├── mapper/                    # 数据访问层
│   │   ├── entity/                    # 实体类
│   │   ├── dto/                       # 数据传输对象
│   │   ├── vo/                        # 视图对象
│   │   ├── common/                    # 公共类（统一响应）
│   │   └── config/                    # 配置类
│   ├── src/main/resources/
│   │   ├── application.yml            # 应用配置
│   │   └── schema.sql                 # 数据库初始化脚本
│   └── pom.xml
└── frontend/                   # 前端 Vue 项目
    ├── src/
    │   ├── views/                     # 页面组件
    │   │   ├── Home.vue               # 首页商品列表
    │   │   ├── Login.vue              # 登录页
    │   │   ├── Register.vue           # 注册页
    │   │   ├── Cart.vue               # 购物车
    │   │   ├── Orders.vue             # 订单列表
    │   │   └── ProductDetail.vue      # 商品详情
    │   ├── router/                    # 路由配置
    │   ├── api/                       # API 接口
    │   ├── App.vue                    # 根组件
    │   └── main.js                    # 入口文件
    ├── index.html
    ├── vite.config.js
    └── package.json
```

## 功能模块

| 模块 | 功能说明 |
|------|---------|
| 用户管理 | 注册、登录、查询、修改、删除 |
| 商品管理 | 商品列表、分类筛选、详情查看 |
| 购物车 | 添加商品、修改数量、删除、清空 |
| 订单管理 | 创建订单、订单列表、状态更新 |

## 环境要求

- **JDK**: 17+
- **Maven**: 3.6+
- **Node.js**: 16+
- **MySQL**: 8.0+

## 快速开始

### 1. 数据库准备

创建数据库并执行初始化脚本：

```sql
CREATE DATABASE simple_mall DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

初始化脚本位于 `backend/src/main/resources/schema.sql`，项目首次启动时会自动执行。

### 2. 后端启动

```bash
# 进入后端目录
cd backend

# 修改数据库连接配置
# 编辑 src/main/resources/application.yml
# url: jdbc:mysql://localhost:3306/simple_mall
# username: your_username
# password: your_password

# 编译运行
mvn spring-boot:run
```

后端服务地址: http://localhost:8080

### 3. 前端启动

```bash
# 进入前端目录
cd frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

前端服务地址: http://localhost:3000

## API 接口

| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/users/login` | POST | 用户登录 |
| `/api/users/register` | POST | 用户注册 |
| `/api/users` | GET | 用户列表 |
| `/api/users/{id}` | GET | 用户详情 |
| `/api/users/{id}` | PUT | 更新用户 |
| `/api/users/{id}` | DELETE | 删除用户 |
| `/api/products` | GET | 商品列表 |
| `/api/products/{id}` | GET | 商品详情 |
| `/api/cart` | GET | 购物车列表 |
| `/api/cart` | POST | 添加购物车 |
| `/api/cart/{id}` | PUT | 更新购物车 |
| `/api/cart/{id}` | DELETE | 删除购物车项 |
| `/api/orders` | GET | 订单列表 |
| `/api/orders` | POST | 创建订单 |
| `/api/orders/{id}` | PUT | 更新订单状态 |

## 测试账号

| 用户名 | 密码 | 说明 |
|--------|------|------|
| admin | 123456 | 管理员 |
| user1 | 123456 | 普通用户 |
| user2 | 123456 | 普通用户 |

## 常见问题

### 1. Maven 报错"不支持发行版本 17"

确保 `JAVA_HOME` 指向 JDK 17：

```bash
# Windows PowerShell
$env:JAVA_HOME = "D:\software\jdk\jdk17"
$env:PATH = "D:\software\jdk\jdk17\bin;" + $env:PATH
mvn -version
```

### 2. 中文乱码

确保以下环节编码一致：
- 数据库：`utf8mb4`
- JDBC URL：`characterEncoding=utf8mb4`
- Maven：`project.build.sourceEncoding=UTF-8`
- 终端：执行 `chcp 65001`

### 3. 数据库连接失败

检查 MySQL 服务是否启动，以及 `application.yml` 中的连接配置是否正确。

## License

MIT
