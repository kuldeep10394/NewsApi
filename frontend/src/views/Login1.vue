<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-100">
    <div class="w-full max-w-md p-6 bg-white rounded shadow">
      <h2 class="text-2xl font-bold mb-4 text-center">Login</h2>

      <div >
        <input v-model="username" placeholder="Username" class="border p-2 w-full mb-3" />
        <input v-model="password" type="password" placeholder="Password" class="border p-2 w-full mb-3" />

        <div class="flex gap-2">
          <button @click="login" class="flex-1 bg-blue-600 text-white p-2 rounded">Login</button>
          <button @click="register" class="flex-1 bg-gray-600 text-white p-2 rounded">Register</button>
        </div>

        <p v-if="error" class="text-red-500 mt-2">{{ error }}</p>
      </div>

      
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const router = useRouter()
const username = ref('')
const password = ref('')
const token = ref(localStorage.getItem('token'))
const message = ref('')
const error = ref('')

async function login() {
  try {
    const res = await axios.post('/api/auth/login', {
      username: username.value,
      password: password.value
    })

    token.value = res.data.token
    localStorage.setItem('token', token.value)
debugger
    // Redirect to dashboard after successful login
    router.push(res.data.redirectUrl || '/dashboard')
  } catch (e) {
    // Show backend error if available
    error.value = e.response?.data?.error || 'Login failed'
  }
}

async function register() {
  try {
    await axios.post('/api/auth/register', {
      username: username.value,
      password: password.value
    })
    alert('Registered successfully, please login')
  } catch (e) {
    alert(e.response?.data?.error || 'Registration failed')
  }
}

async function fetchHello() {
  try {
    const res = await axios.get('/api/hello', {
      headers: { Authorization: `Bearer ${token.value}` }
    })
    message.value = res.data
  } catch (e) {
    alert('Request failed')
  }
}

function logout() {
  token.value = null
  localStorage.removeItem('token')
  message.value = ''
  error.value = ''
  router.push('/login')
}
</script>

<style scoped>
/* optional Tailwind tweaks or local styles */
</style>