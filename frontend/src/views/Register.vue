<template>
  <div class="register-container">
    <div class="register-form">
      <h2>用户注册</h2>
      <div class="form-group">
        <label>用户名</label>
        <input v-model="user.username" type="text" placeholder="请输入用户名">
      </div>
      <div class="form-group">
        <label>密码</label>
        <input v-model="user.password" type="password" placeholder="请输入密码">
      </div>
      <div class="form-group">
        <label>邮箱</label>
        <input v-model="user.email" type="email" placeholder="请输入邮箱">
      </div>
      <div class="form-group">
        <label>手机号</label>
        <input v-model="user.phone" type="tel" placeholder="请输入手机号">
      </div>
      <div class="form-group">
        <label>昵称</label>
        <input v-model="user.nickname" type="text" placeholder="请输入昵称">
      </div>
      <button class="register-btn" @click="register">注册</button>
      <p class="login-link">
        已有账号？<router-link to="/login">立即登录</router-link>
      </p>
      <div v-if="error" class="error-message">{{ error }}</div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { userApi } from '../api'

const router = useRouter()
const user = reactive({
  username: '',
  password: '',
  email: '',
  phone: '',
  nickname: ''
})
const error = ref('')

const register = async () => {
  if (!user.username || !user.password) {
    error.value = '用户名和密码不能为空'
    return
  }
  try {
    const res = await userApi.register(user)
    if (res.data.code === 200) {
      router.push('/login')
    } else {
      error.value = res.data.message
    }
  } catch (e) {
    error.value = '注册失败，请稍后重试'
  }
}
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 70vh;
}
.register-form {
  background: white;
  padding: 2.5rem;
  border-radius: 12px;
  box-shadow: 0 10px 40px rgba(0,0,0,0.1);
  width: 100%;
  max-width: 450px;
}
.register-form h2 {
  text-align: center;
  margin-bottom: 2rem;
  color: #333;
}
.form-group {
  margin-bottom: 1.2rem;
}
.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  color: #555;
  font-weight: 500;
}
.form-group input {
  width: 100%;
  padding: 0.7rem;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 1rem;
  transition: border-color 0.3s;
}
.form-group input:focus {
  outline: none;
  border-color: #667eea;
}
.register-btn {
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
  margin-top: 0.5rem;
}
.register-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 20px rgba(102, 126, 234, 0.4);
}
.login-link {
  text-align: center;
  margin-top: 1.5rem;
  color: #666;
}
.login-link a {
  color: #667eea;
  text-decoration: none;
  font-weight: 500;
}
.login-link a:hover {
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
