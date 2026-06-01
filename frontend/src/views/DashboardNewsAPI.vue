<template>
  <div class="min-h-screen bg-gray-100">
    <!-- 🌟 HEADER BAR -->
    <header class="bg-gradient-to-r from-blue-600 via-indigo-500 to-purple-600 shadow-lg">
      <div class="max-w-7xl mx-auto px-6 py-4 flex flex-col md:flex-row justify-between items-center gap-4">
        <!-- Logo + Title -->
        <div class="flex items-center gap-3">
          <h1 class="text-2xl font-bold text-white">SmartNews Dashboard</h1>
        </div>

        <!-- 🔎 Search + Category + Logout -->
        <div class="flex flex-col sm:flex-row items-center gap-3 w-full md:w-auto">
          <!-- Search Bar -->
          <div class="flex bg-white rounded-lg shadow-md overflow-hidden w-full sm:w-80">
            <input
              v-model="searchQuery"
              @keyup.enter="searchNews"
              type="text"
              placeholder="Search news..."
              class="px-3 py-2 w-full text-gray-700 outline-none"
            />
            <button
              @click="searchNews"
              class="bg-indigo-600 hover:bg-indigo-700 text-white px-4 transition"
            >
              🔍
            </button>
          </div>

          <!-- Category Selector -->
          <select
            v-model="selectedCategory"
            @change="fetchNews"
            class="border border-gray-300 rounded-lg p-2 text-gray-700 focus:ring focus:ring-blue-300"
          >
            <option value="">All</option>
            <option value="technology">Technology</option>
            <option value="sports">Sports</option>
            <option value="business">Business</option>
            <option value="entertainment">Entertainment</option>
            <option value="science">Science</option>
            <option value="health">Health</option>
          </select>

          <!-- Logout -->
          <button
            @click="logout"
            class="bg-red-600 hover:bg-red-700 text-white px-4 py-2 rounded-lg shadow transition"
          >
            Logout
          </button>
        </div>
      </div>
    </header>

    <!-- 🌍 MAIN CONTENT -->
    <main class="max-w-7xl mx-auto p-6">
      <h2 class="text-3xl font-semibold mb-6 text-gray-800 text-center">
        📰 {{ headingText }}
      </h2>

      <div v-if="loading" class="text-center text-gray-500 py-10">
        Loading latest news...
      </div>

      <div v-else-if="articles.length === 0" class="text-center text-gray-600 py-10">
        No articles found.
      </div>

      <div v-else class="grid sm:grid-cols-2 lg:grid-cols-3 gap-6">
        <div
          v-for="(article, index) in articles"
          :key="index"
          class="bg-white rounded-2xl shadow-lg overflow-hidden hover:shadow-2xl transition duration-300"
        >
          <img
            v-if="article.urlToImage"
            :src="article.urlToImage"
            alt="News"
            class="w-full h-48 object-cover"
          />
          <div class="p-5 flex flex-col justify-between h-full">
            <div>
              <h3 class="text-lg font-bold text-gray-800 mb-2 line-clamp-2">
                {{ article.title }}
              </h3>
              <p class="text-gray-600 text-sm mb-4 line-clamp-3">
                {{ article.description }}
              </p>
            </div>
            <a
              :href="article.url"
              target="_blank"
              class="bg-blue-600 hover:bg-blue-700 text-white text-center px-4 py-2 rounded-lg transition"
            >
              Read More
            </a>
          </div>
        </div>
      </div>

      <!-- 🎬 Video Section -->
      <section class="mt-12">
        <h2 class="text-2xl font-bold text-gray-700 mb-4 text-center">🎬 Trending Videos</h2>
        <div class="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
          <iframe
            v-for="(video, index) in videos"
            :key="'v' + index"
            :src="video"
            frameborder="0"
            allowfullscreen
            class="rounded-2xl shadow-lg w-full h-64"
          ></iframe>
        </div>
      </section>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const router = useRouter()
const articles = ref([])
const videos = ref([
  'https://www.youtube.com/embed/21X5lGlDOfg',
  'https://www.youtube.com/embed/TcMBFSGVi1c',
  'https://www.youtube.com/embed/dQw4w9WgXcQ'
])
const selectedCategory = ref('')
const searchQuery = ref('')
const loading = ref(true)

const headingText = computed(() => {
  if (searchQuery.value && searchQuery.value.trim()) {
    return `Search Results for "${searchQuery.value}"`
  } else if (selectedCategory.value) {
    return `Top ${selectedCategory.value} Headlines`
  }
  return 'Top Latest Headlines'
})

async function fetchNews() {
  loading.value = true
  try {
    // ✅ Reset search when category changes
    searchQuery.value = ''

    const res = await axios.get(`/api/news/top${selectedCategory.value ? '?category=' + selectedCategory.value : ''}`, {
      headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
    })
    articles.value = res.data.articles || []
  } catch (err) {
    console.error('Error fetching news:', err)
  } finally {
    loading.value = false
  }
}

async function searchNews() {
  if (!searchQuery.value.trim()) {
    alert('Please enter a keyword to search.')
    return
  }

  loading.value = true
  try {
    const res = await axios.get(`/api/news/top?query=${encodeURIComponent(searchQuery.value)}`, {
      headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
    })
    articles.value = res.data.articles || []
  } catch (err) {
    console.error('Error searching news:', err)
  } finally {
    loading.value = false
  }
}

function logout() {
  localStorage.removeItem('token')
  router.push('/login')
}

onMounted(fetchNews)

</script>

<style scoped>
h1, h2, h3 {
  font-family: 'Poppins', sans-serif;
}
.line-clamp-2 {
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}
.line-clamp-3 {
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}
</style>
