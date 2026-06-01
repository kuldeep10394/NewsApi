<template>
  <div class="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-600 via-indigo-500 to-purple-600">
    <div class="bg-white shadow-2xl rounded-2xl w-full max-w-md p-8">
      <h2 class="text-3xl font-bold text-center mb-6 text-gray-800">
        🔐 SmartNews Login
      </h2>

      <!-- 🌟 LOGIN FORM -->
      <div v-if="!token" class="space-y-4">
        <input
          v-model="username"
          placeholder="Username"
          class="w-full px-4 py-2 border rounded-lg focus:ring focus:ring-blue-300 outline-none"
        />
        <input
          v-model="password"
          type="password"
          placeholder="Password"
          class="w-full px-4 py-2 border rounded-lg focus:ring focus:ring-blue-300 outline-none"
        />

        <div class="mt-6 flex flex-col gap-3">
          <button
            @click="login"
            class="w-full bg-blue-600 hover:bg-blue-700 text-white py-2 rounded-lg shadow-lg transition"
          >
            Login
          </button>
          <button
            @click="register"
            class="w-full bg-gray-600 hover:bg-gray-700 text-white py-2 rounded-lg shadow-lg transition"
          >
            Register
          </button>
        </div>
      </div>

      <!-- 🌟 DASHBOARD OPTIONS (VISIBLE AFTER LOGIN) -->
      <div v-else class="mt-6">
        <h3 class="text-lg font-semibold text-gray-800 text-center mb-3">
          Welcome, {{ usernameDisplay }} 👋  
        </h3>
        <p class="text-sm text-gray-500 text-center mb-5">
          Choose your dashboard to continue
        </p>

        <div class="flex flex-col gap-3">
          <button
            @click="goToDashboard('newsapi')"
            class="w-full bg-indigo-600 hover:bg-indigo-700 text-white py-2 rounded-lg shadow transition"
          >
            📰 NewsAPI Dashboard
          </button>
          <button
            @click="goToDashboard('rss')"
            class="w-full bg-green-600 hover:bg-green-700 text-white py-2 rounded-lg shadow transition"
          >
            🌍 RSS + AI Dashboard
          </button>
        </div>

        <button
          @click="logout"
          class="w-full mt-6 bg-red-500 hover:bg-red-600 text-white py-2 rounded-lg shadow transition"
        >
          Logout
        </button>
      </div>

      <!-- ❌ ERROR MESSAGE -->
      <p v-if="error" class="mt-4 text-center text-red-600 font-medium">
        {{ error }}
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const router = useRouter()
const username = ref('')
const password = ref('')
const token = ref(localStorage.getItem('token'))
const error = ref('')

const usernameDisplay = computed(() => localStorage.getItem('username') || username.value)

/** 🟢 LOGIN */
async function login() {
  error.value = ''
  try {
    const res = await axios.post('/api/auth/login', {
      username: username.value,
      password: password.value
    })
    token.value = res.data.token
    localStorage.setItem('token', token.value)
    localStorage.setItem('username', username.value)
  } catch (e) {
    console.error('Login error:', e)
    error.value = 'Invalid credentials. Please try again.'
  }
}

/** 🟣 REGISTER */
async function register() {
  error.value = ''
  try {
    await axios.post('/api/auth/register', {
      username: username.value,
      password: password.value
    })
    alert('🎉 Registration successful! You can now log in.')
  } catch (e) {
    console.error('Registration error:', e)
    error.value = 'Registration failed. Username may already exist.'
  }
}

/** 🟠 GO TO DASHBOARD */
function goToDashboard(type) {
  if (!token.value) {
    error.value = 'Please log in first.'
    return
  }

  if (type === 'rss') {
    localStorage.setItem('lastDashboard', 'rss')
    router.push('/rss-dashboard')
  } else {
    localStorage.setItem('lastDashboard', 'newsapi')
    router.push('/dashboard')
  }
}

/** 🔴 LOGOUT */
function logout() {
  localStorage.removeItem('token')
  localStorage.removeItem('username')
  token.value = null
}
</script>

<style scoped>
body {
  font-family: 'Poppins', sans-serif;
}
</style>
