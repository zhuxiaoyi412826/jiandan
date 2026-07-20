import axios from 'axios'

const request = axios.create({
  baseURL: '/api',
  timeout: 10000
})

export const userApi = {
  login(username, password) {
    return request.post('/users/login', { username, password })
  },
  register(user) {
    return request.post('/users/register', user)
  },
  getAllUsers() {
    return request.get('/users')
  },
  getUserById(id) {
    return request.get(`/users/${id}`)
  },
  updateUser(id, user) {
    return request.put(`/users/${id}`, user)
  },
  deleteUser(id) {
    return request.delete(`/users/${id}`)
  }
}

export const productApi = {
  getAllProducts(category) {
    return request.get('/products', { params: { category } })
  },
  getAllCategories() {
    return request.get('/products/categories')
  },
  getProductById(id) {
    return request.get(`/products/${id}`)
  },
  addProduct(product) {
    return request.post('/products', product)
  },
  updateProduct(id, product) {
    return request.put(`/products/${id}`, product)
  },
  deleteProduct(id) {
    return request.delete(`/products/${id}`)
  }
}

export const cartApi = {
  getCart(userId) {
    return request.get(`/cart/${userId}`)
  },
  addToCart(userId, productId, quantity) {
    return request.post('/cart/add', null, { params: { userId, productId, quantity } })
  },
  updateCartItem(userId, productId, quantity) {
    return request.put('/cart/update', null, { params: { userId, productId, quantity } })
  },
  removeFromCart(userId, productId) {
    return request.delete('/cart/remove', { params: { userId, productId } })
  },
  clearCart(userId) {
    return request.delete(`/cart/clear/${userId}`)
  }
}

export const orderApi = {
  createOrder(userId, shippingAddress) {
    return request.post('/orders/create', null, { params: { userId, shippingAddress } })
  },
  getUserOrders(userId) {
    return request.get(`/orders/${userId}`)
  },
  getOrderDetail(orderId) {
    return request.get(`/orders/detail/${orderId}`)
  },
  updateOrderStatus(orderId, status) {
    return request.put(`/orders/${orderId}/status`, null, { params: { status } })
  }
}
