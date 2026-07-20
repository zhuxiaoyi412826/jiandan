<template>
  <div class="home-container">
    <div class="category-filter">
      <button 
        v-for="cat in categories" 
        :key="cat"
        :class="['category-btn', { active: selectedCategory === cat }]"
        @click="selectCategory(cat)"
      >
        {{ cat }}
      </button>
      <button 
        :class="['category-btn', { active: !selectedCategory }]"
        @click="selectCategory(null)"
      >
        全部
      </button>
    </div>
    <div class="products-grid">
      <div v-for="product in products" :key="product.id" class="product-card">
        <img :src="product.image" :alt="product.name" class="product-image">
        <h3 class="product-name">{{ product.name }}</h3>
        <p class="product-desc">{{ product.description }}</p>
        <div class="product-price">¥{{ product.price.toFixed(2) }}</div>
        <div class="product-stock">库存: {{ product.stock }}</div>
        <button class="add-cart-btn" @click="addToCart(product)">加入购物车</button>
        <button class="view-detail-btn" @click="viewDetail(product.id)">查看详情</button>
      </div>
    </div>
    <div v-if="products.length === 0" class="empty-state">
      <p>暂无商品</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, inject } from 'vue'
import { useRouter } from 'vue-router'
import { productApi, cartApi } from '../api'

const router = useRouter()
const products = ref([])
const categories = ref([])
const selectedCategory = ref(null)
const updateCartCount = inject('updateCartCount')
const currentUser = inject('currentUser')

onMounted(() => {
  loadProducts()
  loadCategories()
})

const loadProducts = async () => {
  try {
    const res = await productApi.getAllProducts(selectedCategory.value)
    if (res.data.code === 200) {
      products.value = res.data.data
    }
  } catch (e) {
    console.error(e)
  }
}

const loadCategories = async () => {
  try {
    const res = await productApi.getAllCategories()
    if (res.data.code === 200) {
      categories.value = res.data.data
    }
  } catch (e) {
    console.error(e)
  }
}

const selectCategory = (cat) => {
  selectedCategory.value = cat
  loadProducts()
}

const addToCart = async (product) => {
  if (!currentUser.value) {
    router.push('/login')
    return
  }
  try {
    await cartApi.addToCart(currentUser.value.id, product.id, 1)
    updateCartCount()
    alert('已添加到购物车')
  } catch (e) {
    console.error(e)
    alert('添加失败')
  }
}

const viewDetail = (id) => {
  router.push(`/product/${id}`)
}
</script>

<style scoped>
.home-container {
  padding: 1rem;
}
.category-filter {
  display: flex;
  gap: 1rem;
  margin-bottom: 2rem;
  flex-wrap: wrap;
}
.category-btn {
  padding: 0.6rem 1.2rem;
  border: 2px solid #ddd;
  background: white;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s;
  font-weight: 500;
}
.category-btn:hover {
  border-color: #667eea;
  color: #667eea;
}
.category-btn.active {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-color: transparent;
}
.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 1.5rem;
}
.product-card {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 10px rgba(0,0,0,0.05);
  transition: transform 0.3s, box-shadow 0.3s;
}
.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 30px rgba(0,0,0,0.1);
}
.product-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
}
.product-name {
  padding: 1rem 1rem 0.5rem;
  font-size: 1.1rem;
  font-weight: 600;
  color: #333;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.product-desc {
  padding: 0 1rem;
  font-size: 0.85rem;
  color: #666;
  line-height: 1.4;
  height: 40px;
  overflow: hidden;
}
.product-price {
  padding: 0.5rem 1rem;
  font-size: 1.3rem;
  font-weight: 700;
  color: #e74c3c;
}
.product-stock {
  padding: 0 1rem;
  font-size: 0.8rem;
  color: #999;
}
.add-cart-btn {
  margin: 1rem;
  padding: 0.7rem;
  width: calc(100% - 2rem);
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 600;
  transition: transform 0.2s;
}
.add-cart-btn:hover {
  transform: translateY(-2px);
}
.view-detail-btn {
  margin: 0 1rem 1rem;
  padding: 0.6rem;
  width: calc(100% - 2rem);
  background: white;
  color: #667eea;
  border: 2px solid #667eea;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 500;
  transition: all 0.2s;
}
.view-detail-btn:hover {
  background: #667eea;
  color: white;
}
.empty-state {
  text-align: center;
  padding: 3rem;
  color: #999;
}
</style>
