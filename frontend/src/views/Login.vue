<template>
  <div class="login-container">
    <div class="login-form">
      <h2>用户登录</h2>
      <div class="form-group">
        <label>用户名</label>
        <input v-model="username" type="text" placeholder="请输入用户名">
      </div>
      <div class="form-group">
        <label>密码</label>
        <input v-model="password" type="password" placeholder="请输入密码">
      </div>
      <button class="login-btn" @click="login">登录</button>
      <p class="register-link">
        还没有账号？<router-link to="/register">立即注册</router-link>
      </p>
      <div v-if="error" class="error-message">{{ error }}</div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { userApi } from '../api'

const router = useRouter()
const username = ref('')
const password = ref('')
const error = ref('')

const login = async () => {
  if (!username.value || !password.value) {
    error.value = '请输入用户名和密码'
    return
  }
  try {
    const res = await userApi.login(username.value, password.value)
    if (res.data.code === 200) {
      localStorage.setItem('user', JSON.stringify(res.data.data))
      router.push('/')
    } else {
      error.value = res.data.message
    }
  } catch (e) {
    error.value = '登录失败，请稍后重试'
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 70vh;
}
.login-form {
  background: white;
  padding: 2.5rem;
  border-radius: 12px;
  box-shadow: 0 10px 40px rgba(0,0,0,0.1);
  width: 100%;
  max-width: 400px;
}
.login-form h2 {
  text-align: center;
  margin-bottom: 2rem;
  color: #333;
}
.form-group {
  margin-bottom: 1.5rem;
}
.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  color: #555;
  font-weight: 500;
}
.form-group input {
  width: 100%;
  padding: 0.8rem;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 1rem;
  transition: border-color 0.3s;
}
.form-group input:focus {
  outline: none;
  border-color: #667eea;
}
.login-btn {
  width: 100%;
  padding: 0.9rem;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
}
.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 20px rgba(102, 126, 234, 0.4);
}
.register-link {
  text-align: center;
  margin-top: 1.5rem;
  color: #666;
}
.register-link a {
  color: #667eea;
  text-decoration: none;
  font-weight: 500;
}
.register-link a:hover {
  text-decoration: underline;
}
.error-message {
  text-align: center;
  color: #e74c3c;
  margin-top: 1rem;
  padding: 0.5rem;
  background: #fee;
  border-radius: 4px;
}
</style>
