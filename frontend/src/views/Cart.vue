<template>
  <div class="cart-container">
    <h2>购物车</h2>
    <div v-if="cartItems.length > 0" class="cart-items">
      <div v-for="item in cartItems" :key="item.cartId" class="cart-item">
        <img :src="item.productImage" :alt="item.productName" class="cart-item-image">
        <div class="cart-item-info">
          <h3>{{ item.productName }}</h3>
          <div class="cart-item-price">¥{{ item.productPrice.toFixed(2) }}</div>
          <div class="quantity-selector">
            <button @click="updateQuantity(item, item.quantity - 1)">-</button>
            <span>{{ item.quantity }}</span>
            <button @click="updateQuantity(item, item.quantity + 1)">+</button>
          </div>
          <div class="cart-item-total">小计: ¥{{ item.totalPrice.toFixed(2) }}</div>
        </div>
        <button class="remove-btn" @click="removeItem(item)">删除</button>
      </div>
    </div>
    <div v-else class="empty-cart">
      <p>购物车为空</p>
      <router-link to="/">去购物</router-link>
    </div>
    <div v-if="cartItems.length > 0" class="cart-summary">
      <div class="total-amount">
        总计: <span>¥{{ totalAmount.toFixed(2) }}</span>
      </div>
      <button class="checkout-btn" @click="checkout">结算</button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, inject } from 'vue'
import { useRouter } from 'vue-router'
import { cartApi, orderApi } from '../api'

const router = useRouter()
const cartItems = ref([])
const updateCartCount = inject('updateCartCount')
const currentUser = inject('currentUser')

onMounted(() => {
  loadCart()
})

const loadCart = async () => {
  if (!currentUser.value) {
    router.push('/login')
    return
  }
  try {
    const res = await cartApi.getCart(currentUser.value.id)
    if (res.data.code === 200) {
      cartItems.value = res.data.data
    }
  } catch (e) {
    console.error(e)
  }
}

const totalAmount = computed(() => {
  return cartItems.value.reduce((sum, item) => sum + parseFloat(item.totalPrice), 0)
})

const updateQuantity = async (item, quantity) => {
  if (quantity < 1) return
  try {
    await cartApi.updateCartItem(currentUser.value.id, item.productId, quantity)
    loadCart()
    updateCartCount()
  } catch (e) {
    console.error(e)
  }
}

const removeItem = async (item) => {
  try {
    await cartApi.removeFromCart(currentUser.value.id, item.productId)
    loadCart()
    updateCartCount()
  } catch (e) {
    console.error(e)
  }
}

const checkout = async () => {
  const address = prompt('请输入收货地址：')
  if (!address) return
  try {
    await orderApi.createOrder(currentUser.value.id, address)
    cartItems.value = []
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
.cart-container {
  background: white;
  border-radius: 12px;
  padding: 2rem;
  box-shadow: 0 2px 10px rgba(0,0,0,0.05);
}
.cart-container h2 {
  font-size: 1.8rem;
  font-weight: 700;
  color: #333;
  margin-bottom: 2rem;
}
.cart-items {
  margin-bottom: 2rem;
}
.cart-item {
  display: flex;
  align-items: center;
  gap: 1.5rem;
  padding: 1.5rem;
  border-bottom: 1px solid #eee;
}
.cart-item-image {
  width: 100px;
  height: 100px;
  object-fit: cover;
  border-radius: 8px;
}
.cart-item-info {
  flex: 1;
}
.cart-item-info h3 {
  font-size: 1.1rem;
  font-weight: 600;
  color: #333;
  margin-bottom: 0.5rem;
}
.cart-item-price {
  font-size: 1.2rem;
  font-weight: 700;
  color: #e74c3c;
}
.quantity-selector {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  margin: 1rem 0;
}
.quantity-selector button {
  width: 28px;
  height: 28px;
  border: 1px solid #ddd;
  background: white;
  border-radius: 4px;
  font-size: 1rem;
  cursor: pointer;
}
.quantity-selector button:hover {
  border-color: #667eea;
}
.quantity-selector span {
  font-size: 1rem;
  font-weight: 600;
  min-width: 25px;
  text-align: center;
}
.cart-item-total {
  font-size: 1rem;
  font-weight: 600;
  color: #333;
}
.remove-btn {
  padding: 0.5rem 1rem;
  background: #f5f5f5;
  color: #666;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.2s;
}
.remove-btn:hover {
  background: #e74c3c;
  color: white;
}
.empty-cart {
  text-align: center;
  padding: 4rem;
  color: #999;
}
.empty-cart p {
  font-size: 1.2rem;
  margin-bottom: 1rem;
}
.empty-cart a {
  color: #667eea;
  text-decoration: none;
  font-weight: 600;
}
.empty-cart a:hover {
  text-decoration: underline;
}
.cart-summary {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 1.5rem;
  border-top: 2px solid #eee;
}
.total-amount {
  font-size: 1.5rem;
  font-weight: 700;
  color: #333;
}
.total-amount span {
  color: #e74c3c;
}
.checkout-btn {
  padding: 1rem 2.5rem;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: transform 0.2s;
}
.checkout-btn:hover {
  transform: translateY(-2px);
}
</style>
