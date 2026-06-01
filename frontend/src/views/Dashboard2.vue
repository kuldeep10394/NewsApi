<template>
  <div class="min-h-screen bg-gray-50">
    <!-- Header -->
    <header class="bg-blue-700 text-white py-4 shadow-md flex justify-between items-center px-6">
      <h1 class="text-2xl font-bold">AI News Dashboard</h1>
      <div class="flex gap-2">
        <input
          v-model="query"
          type="text"
          placeholder="Search latest news..."
          class="px-3 py-2 rounded-lg text-black"
        />
        <button
          @click="fetchChatGPTNews"
          class="bg-white text-blue-700 px-4 py-2 rounded-lg font-semibold"
        >
          Search
        </button>
      </div>
    </header>

    <!-- Results -->
    <div class="p-6">
      <h2 class="text-xl font-semibold mb-4 text-gray-800">
        {{ query ? `Results for "${query}"` : 'Enter a topic to get AI-curated news' }}
      </h2>
      <div v-if="loading" class="text-gray-500">Loading news...</div>
      <div v-else-if="news" class="bg-white p-4 rounded shadow whitespace-pre-line">
        {{ news }}
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'

const query = ref('')
const news = ref('')
const loading = ref(false)

async function fetchChatGPTNews() {
  if (!query.value) return alert('Please enter a search topic.')
  loading.value = true
  news.value = ''
  try {
    const res = await axios.get(`http://localhost:8080/api/chatgpt/news`, {
      params: { query: query.value }
    })
    news.value = res.data
  } catch (e) {
    console.error(e)
    alert('Failed to fetch AI news.')
  } finally {
    loading.value = false
  }
}
</script>
