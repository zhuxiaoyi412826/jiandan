<template>
  <div class="orders-container">
    <h2>我的订单</h2>
    <div v-if="orders.length > 0" class="orders-list">
      <div v-for="order in orders" :key="order.id" class="order-card">
        <div class="order-header">
          <div class="order-no">订单号: {{ order.orderNo }}</div>
          <div :class="['order-status', getStatusClass(order.status)]">{{ getStatusText(order.status) }}</div>
        </div>
        <div class="order-info">
          <div class="order-date">下单时间: {{ formatDate(order.createdAt) }}</div>
          <div class="order-address">收货地址: {{ order.shippingAddress }}</div>
          <div class="order-total">订单金额: ¥{{ order.totalAmount.toFixed(2) }}</div>
        </div>
        <div class="order-actions">
          <button v-if="order.status === 0" @click="payOrder(order)">支付</button>
          <button v-if="order.status === 1" @click="confirmOrder(order)">确认收货</button>
          <button v-if="order.status === 0" @click="cancelOrder(order)">取消订单</button>
        </div>
      </div>
    </div>
    <div v-else class="empty-orders">
      <p>暂无订单</p>
      <router-link to="/">去购物</router-link>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, inject } from 'vue'
import { useRouter } from 'vue-router'
import { orderApi } from '../api'

const router = useRouter()
const orders = ref([])
const currentUser = inject('currentUser')

onMounted(() => {
  loadOrders()
})

const loadOrders = async () => {
  if (!currentUser.value) {
    router.push('/login')
    return
  }
  try {
    const res = await orderApi.getUserOrders(currentUser.value.id)
    if (res.data.code === 200) {
      orders.value = res.data.data
    }
  } catch (e) {
    console.error(e)
  }
}

const getStatusText = (status) => {
  const map = {
    0: '待支付',
    1: '已支付',
    2: '已发货',
    3: '已完成',
    4: '已取消'
  }
  return map[status] || '未知'
}

const getStatusClass = (status) => {
  const map = {
    0: 'pending',
    1: 'paid',
    2: 'shipped',
    3: 'completed',
    4: 'cancelled'
  }
  return map[status] || ''
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleString('zh-CN')
}

const payOrder = async (order) => {
  try {
    await orderApi.updateOrderStatus(order.id, 1)
    alert('支付成功！')
    loadOrders()
  } catch (e) {
    console.error(e)
    alert('支付失败')
  }
}

const confirmOrder = async (order) => {
  try {
    await orderApi.updateOrderStatus(order.id, 3)
    alert('确认收货成功！')
    loadOrders()
  } catch (e) {
    console.error(e)
    alert('操作失败')
  }
}

const cancelOrder = async (order) => {
  if (!confirm('确定取消订单吗？')) return
  try {
    await orderApi.updateOrderStatus(order.id, 4)
    alert('订单已取消')
    loadOrders()
  } catch (e) {
    console.error(e)
    alert('操作失败')
  }
}
</script>

<style scoped>
.orders-container {
  padding: 1rem;
}
.orders-container h2 {
  font-size: 1.8rem;
  font-weight: 700;
  color: #333;
  margin-bottom: 2rem;
}
.orders-list {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}
.order-card {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 2px 10px rgba(0,0,0,0.05);
}
.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
  padding-bottom: 1rem;
  border-bottom: 1px solid #eee;
}
.order-no {
  font-size: 0.95rem;
  color: #666;
}
.order-status {
  padding: 0.4rem 1rem;
  border-radius: 20px;
  font-size: 0.85rem;
  font-weight: 600;
}
.order-status.pending {
  background: #fff3cd;
  color: #856404;
}
.order-status.paid {
  background: #d4edda;
  color: #155724;
}
.order-status.shipped {
  background: #cce5ff;
  color: #004085;
}
.order-status.completed {
  background: #e8f5e9;
  color: #2e7d32;
}
.order-status.cancelled {
  background: #f8d7da;
  color: #721c24;
}
.order-info {
  margin-bottom: 1.5rem;
}
.order-date, .order-address, .order-total {
  font-size: 0.95rem;
  color: #555;
  margin-bottom: 0.5rem;
}
.order-total {
  font-size: 1.1rem;
  font-weight: 700;
  color: #e74c3c;
}
.order-actions {
  display: flex;
  gap: 1rem;
}
.order-actions button {
  padding: 0.6rem 1.2rem;
  border: none;
  border-radius: 6px;
  font-size: 0.9rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}
.order-actions button:nth-child(1) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}
.order-actions button:nth-child(2) {
  background: #28a745;
  color: white;
}
.order-actions button:nth-child(3) {
  background: #f5f5f5;
  color: #666;
}
.order-actions button:hover {
  transform: translateY(-2px);
}
.empty-orders {
  text-align: center;
  padding: 4rem;
  color: #999;
}
.empty-orders p {
  font-size: 1.2rem;
  margin-bottom: 1rem;
}
.empty-orders a {
  color: #667eea;
  text-decoration: none;
  font-weight: 600;
}
.empty-orders a:hover {
  text-decoration: underline;
}
</style>
