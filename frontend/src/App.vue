<template>
  <div class="app">
    <header class="header">
      <div class="header-content">
        <div class="logo" @click="goHome">
          <h1>🛒 简单商城</h1>
        </div>
        <nav class="nav">
          <router-link to="/">首页</router-link>
          <router-link to="/cart">购物车 ({{ cartCount }})</router-link>
          <router-link to="/orders">我的订单</router-link>
        </nav>
        <div class="user-info">
          <span v-if="currentUser">{{ currentUser.nickname }}</span>
          <router-link v-else to="/login">登录</router-link>
          <router-link v-if="!currentUser" to="/register">注册</router-link>
          <button v-if="currentUser" @click="logout">退出</button>
        </div>
      </div>
    </header>
    <main class="main">
      <router-view />
    </main>
    <footer class="footer">
      <p>&copy; 2024 简单商城. All rights reserved.</p>
    </footer>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { cartApi } from './api'

const router = useRouter()
const currentUser = ref(null)
const cartCount = ref(0)

onMounted(() => {
  const user = localStorage.getItem('user')
  if (user) {
    currentUser.value = JSON.parse(user)
    loadCartCount()
  }
})

const goHome = () => {
  router.push('/')
}

const logout = () => {
  localStorage.removeItem('user')
  currentUser.value = null
  cartCount.value = 0
  router.push('/login')
}

const loadCartCount = async () => {
  if (currentUser.value) {
    try {
      const res = await cartApi.getCart(currentUser.value.id)
      if (res.data.code === 200) {
        cartCount.value = res.data.data.length
      }
    } catch (e) {
      console.error(e)
    }
  }
}

const updateCartCount = () => {
  loadCartCount()
}

import { provide } from 'vue'
provide('updateCartCount', updateCartCount)
provide('currentUser', currentUser)
</script>

<style scoped>
.app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}
.header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 1rem 0;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
}
.header-content {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 2rem;
}
.logo {
  cursor: pointer;
}
.logo h1 {
  font-size: 1.5rem;
  font-weight: 600;
}
.nav {
  display: flex;
  gap: 2rem;
}
.nav a {
  color: white;
  text-decoration: none;
  font-weight: 500;
  transition: color 0.3s;
}
.nav a:hover {
  color: #ffd700;
}
.user-info {
  display: flex;
  gap: 1rem;
  align-items: center;
}
.user-info a {
  color: white;
  text-decoration: none;
  font-weight: 500;
}
.user-info button {
  background: rgba(255,255,255,0.2);
  border: none;
  color: white;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
  font-weight: 500;
  transition: background 0.3s;
}
.user-info button:hover {
  background: rgba(255,255,255,0.3);
}
.main {
  flex: 1;
  max-width: 1200px;
  margin: 0 auto;
  width: 100%;
  padding: 2rem;
}
.footer {
  background: #333;
  color: white;
  text-align: center;
  padding: 1.5rem;
  margin-top: auto;
}
</style>
