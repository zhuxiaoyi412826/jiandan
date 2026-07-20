<template>
  <div v-if="product" class="product-detail">
    <div class="product-image-container">
      <img :src="product.image" :alt="product.name" class="product-image">
    </div>
    <div class="product-info">
      <h1 class="product-name">{{ product.name }}</h1>
      <p class="product-desc">{{ product.description }}</p>
      <div class="product-price">¥{{ product.price.toFixed(2) }}</div>
      <div class="product-stock">库存: {{ product.stock }}</div>
      <div class="product-category">分类: {{ product.category }}</div>
      <div class="quantity-selector">
        <label>数量</label>
        <button @click="decreaseQuantity">-</button>
        <span>{{ quantity }}</span>
        <button @click="increaseQuantity">+</button>
      </div>
      <button class="add-cart-btn" @click="addToCart">加入购物车</button>
      <button class="buy-now-btn" @click="buyNow">立即购买</button>
    </div>
  </div>
  <div v-else class="loading">加载中...</div>
</template>

<script setup>
import { ref, onMounted, inject } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { productApi, cartApi, orderApi } from '../api'

const route = useRoute()
const router = useRouter()
const product = ref(null)
const quantity = ref(1)
const updateCartCount = inject('updateCartCount')
const currentUser = inject('currentUser')

onMounted(() => {
  loadProduct()
})

const loadProduct = async () => {
  const id = route.params.id
  try {
    const res = await productApi.getProductById(id)
    if (res.data.code === 200) {
      product.value = res.data.data
    }
  } catch (e) {
    console.error(e)
  }
}

const decreaseQuantity = () => {
  if (quantity.value > 1) {
    quantity.value--
  }
}

const increaseQuantity = () => {
  if (product.value && quantity.value < product.value.stock) {
    quantity.value++
  }
}

const addToCart = async () => {
  if (!currentUser.value) {
    router.push('/login')
    return
  }
  try {
    await cartApi.addToCart(currentUser.value.id, product.value.id, quantity.value)
    updateCartCount()
    alert('已添加到购物车')
  } catch (e) {
    console.error(e)
    alert('添加失败')
  }
}

const buyNow = async () => {
  if (!currentUser.value) {
    router.push('/login')
    return
  }
  const address = prompt('请输入收货地址：')
  if (!address) return
  try {
    await cartApi.addToCart(currentUser.value.id, product.value.id, quantity.value)
    await orderApi.createOrder(currentUser.value.id, address)
    updateCartCount()
    alert('下单成功！')
    router.push('/orders')
  } catch (e) {
    console.error(e)
    alert('下单失败')
  }
}
</script>

<style scoped>
.product-detail {
  display: flex;
  gap: 3rem;
  padding: 2rem;
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.05);
}
.product-image-container {
  flex: 1;
}
.product-image {
  width: 100%;
  max-width: 500px;
  height: 400px;
  object-fit: cover;
  border-radius: 12px;
}
.product-info {
  flex: 1;
}
.product-name {
  font-size: 2rem;
  font-weight: 700;
  color: #333;
  margin-bottom: 1rem;
}
.product-desc {
  font-size: 1rem;
  color: #666;
  line-height: 1.6;
  margin-bottom: 1.5rem;
}
.product-price {
  font-size: 2.5rem;
  font-weight: 700;
  color: #e74c3c;
  margin-bottom: 1rem;
}
.product-stock, .product-category {
  font-size: 0.95rem;
  color: #555;
  margin-bottom: 0.5rem;
}
.quantity-selector {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin: 1.5rem 0;
}
.quantity-selector label {
  font-weight: 500;
  color: #333;
}
.quantity-selector button {
  width: 36px;
  height: 36px;
  border: 1px solid #ddd;
  background: white;
  border-radius: 6px;
  font-size: 1.2rem;
  cursor: pointer;
  transition: all 0.2s;
}
.quantity-selector button:hover {
  background: #f5f5f5;
  border-color: #667eea;
}
.quantity-selector span {
  font-size: 1.2rem;
  font-weight: 600;
  min-width: 30px;
  text-align: center;
}
.add-cart-btn {
  width: 100%;
  padding: 1rem;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  margin-bottom: 1rem;
  transition: transform 0.2s;
}
.add-cart-btn:hover {
  transform: translateY(-2px);
}
.buy-now-btn {
  width: 100%;
  padding: 1rem;
  background: #e74c3c;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: transform 0.2s;
}
.buy-now-btn:hover {
  transform: translateY(-2px);
}
.loading {
  text-align: center;
  padding: 3rem;
  color: #999;
}
</style>
