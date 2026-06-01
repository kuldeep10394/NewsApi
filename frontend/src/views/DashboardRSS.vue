<template>
  <div class="min-h-screen bg-gray-100">
    <!-- 🌍 HEADER -->
    <header class="bg-gradient-to-r from-green-500 via-emerald-600 to-teal-700 shadow-lg">
      <div class="max-w-7xl mx-auto px-6 py-4 flex flex-col sm:flex-row justify-between items-center gap-4">
        <h1 class="text-2xl font-bold text-white">🌍 AI-News-Dashboard</h1>

        <div class="flex flex-col sm:flex-row gap-3 items-center w-full sm:w-auto">
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
              @click="searchArticles"
              class="bg-indigo-600 hover:bg-indigo-700 text-white px-4 transition"
            >
              🔍
            </button>
          </div>
		  
          <!-- 📰 Dropdown for news source -->
          <select
            v-model="selectedSource"
            @change="filterBySource"
            class="border border-gray-300 rounded-lg p-2 text-gray-700 focus:ring focus:ring-green-400"
          >
            <option value="">All Sources</option>
            <option value="BBC">BBC</option>
            <option value="CNN">CNN</option>
            <option value="NDTV">NDTV</option>
            <option value="TOI">Times of India</option>
          </select>

          

          <!-- Other buttons -->
          <button
            @click="router.push('/dashboard')"
            class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg shadow transition"
          >
            📰 NewsAPI Dashboard
          </button>

          <button
            @click="logout"
            class="bg-red-600 hover:bg-red-700 text-white px-4 py-2 rounded-lg shadow transition"
          >
            Logout
          </button>
        </div>
      </div>
    </header>

    <!-- 🧠 MAIN CONTENT -->
    <main class="max-w-7xl mx-auto p-6">
      <h2 class="text-3xl font-semibold mb-6 text-gray-800 text-center">
        🧠 AI-Curated RSS News
      </h2>

      <!-- 🔄 Loading Skeleton -->
      <div v-if="loading" class="grid sm:grid-cols-2 lg:grid-cols-3 gap-6 animate-pulse">
        <div
          v-for="n in 6"
          :key="n"
          class="bg-white rounded-2xl shadow p-4 flex flex-col space-y-3"
        >
          <div class="bg-gray-300 h-48 w-full rounded-lg"></div>
          <div class="h-5 bg-gray-300 rounded w-3/4"></div>
          <div class="h-3 bg-gray-200 rounded w-full"></div>
          <div class="h-3 bg-gray-200 rounded w-5/6"></div>
        </div>
      </div>

      <!-- ⚠️ No Articles -->
      <div
        v-else-if="!articles || articles.length === 0"
        class="text-center text-gray-600 py-10"
      >
        No articles found.
      </div>

      <!-- ✅ Articles Grid -->
      <div v-else class="grid sm:grid-cols-2 lg:grid-cols-3 gap-6">
        <div
          v-for="(article, index) in articles"
          :key="index"
          class="bg-white rounded-2xl shadow-lg overflow-hidden hover:shadow-2xl transition duration-300 flex flex-col min-h-[460px] cursor-pointer"
          @click="openArticle(article)"
        >
          <!-- 🖼️ IMAGE -->
          <img
            :src="getImageUrl(article.imageUrl)"
            alt="News Image"
            class="w-full h-48 object-cover"
            @error="handleImageError"
          />

          <!-- 🧾 CONTENT -->
          <div class="p-5 flex flex-col flex-grow justify-between">
            <div class="flex-grow">
              <h3 class="text-lg font-bold text-gray-800 mb-2 line-clamp-2">
                {{ article.title }}
              </h3>

              <p class="text-sm text-gray-500 mb-1">
                <strong>Source:</strong> {{ article.source }}
              </p>
			  <p class="text-xs text-gray-400 mb-2">
				🕒 Published {{ timeAgo(article.publishedAt) }}
			  </p>

              <p
                class="text-gray-600 text-sm mb-3 line-clamp-3"
                v-html="article.description"
              ></p>

              <!-- 🧠 AI SUMMARY -->
              <div
                v-if="article.ai_summary"
                class="bg-gray-50 p-3 rounded-lg border-l-4 border-green-400 mb-3"
              >
                <h4 class="font-semibold text-green-700">AI Summary:</h4>
                <p class="text-gray-700">{{ article.ai_summary }}</p>
              </div>
            </div>

            <!-- 🔗 READ FULL ARTICLE BUTTON -->
            <button
              class="bg-gradient-to-r from-green-500 to-emerald-600 hover:from-green-600 hover:to-emerald-700 text-white text-center px-4 py-2 mt-3 rounded-lg transition block font-medium shadow"
            >
              🔗 Read Full Article
            </button>
          </div>
        </div>
      </div>
    </main>

    <!-- 📰 MODAL FOR FULL ARTICLE -->
    <div
      v-if="showModal"
      class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
      @click.self="closeModal"
    >
      <div class="bg-white w-11/12 md:w-3/4 lg:w-1/2 rounded-2xl shadow-2xl overflow-y-auto max-h-[90vh] p-6 relative">
        <!-- Close button -->
        <button
          @click="closeModal"
          class="absolute top-3 right-3 text-gray-500 hover:text-gray-700 text-2xl"
        >
          ✖
        </button>

        <!-- Image -->
        <img
          v-if="selectedArticle?.imageUrl"
          :src="getImageUrl(selectedArticle.imageUrl)"
          class="w-full h-64 object-cover rounded-lg mb-4"
        />

        <!-- Title -->
        <h2 class="text-2xl font-bold text-gray-800 mb-2">
          {{ selectedArticle?.title }}
        </h2>

        <!-- Source -->
        <p class="text-sm text-gray-500 mb-4">
          <strong>Source:</strong> {{ selectedArticle?.source }}
        </p>

        <!-- Description -->
        <div
          class="text-gray-700 mb-4"
          v-html="selectedArticle?.description"
        ></div>

        <!-- AI Summary -->
        <div
          v-if="selectedArticle?.ai_summary"
          class="bg-gray-50 p-3 rounded-lg border-l-4 border-green-400 mb-3"
        >
          <h4 class="font-semibold text-green-700">AI Summary:</h4>
          <p class="text-gray-700">{{ selectedArticle.ai_summary }}</p>
        </div>

        <!-- Visit Link -->
        <a
          :href="selectedArticle?.link"
          target="_blank"
          class="inline-block bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg mt-2 shadow transition"
        >
          Open Original Source
        </a>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const router = useRouter()
const articles = ref([])
const loading = ref(true)
const searchQuery = ref('')
const selectedSource = ref('')
const showModal = ref(false)
const selectedArticle = ref(null)
const defaultImage = 'https://via.placeholder.com/600x300?text=No+Image'

// 🖼️ Handle missing/broken images
function getImageUrl(url) {
  return url && url.startsWith('http') ? url : defaultImage
}
function handleImageError(event) {
  event.target.src = defaultImage
}

// 🌍 Fetch RSS News (with or without keyword)
async function searchArticles() {
  loading.value = true
  try {
    const url = searchQuery.value
      ? `http://localhost:8080/api/rss/search?keyword=${encodeURIComponent(searchQuery.value)}`
      : 'http://localhost:8080/api/rss/all'

    const res = await axios.get(url, {
      headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
    })
    const fetchedArticles = Array.isArray(res.data) ? res.data : []

    // ✅ Sort: news with images first
    articles.value = fetchedArticles
      .filter(a => !selectedSource.value || a.source === selectedSource.value)
      .sort((a, b) => {
        const hasImageA = a.imageUrl && a.imageUrl.startsWith('http')
        const hasImageB = b.imageUrl && b.imageUrl.startsWith('http')
        return hasImageB - hasImageA
      })
  } catch (err) {
    console.error('❌ Error fetching RSS news:', err)
    articles.value = []
  } finally {
    loading.value = false
  }
}

// 📰 Filter by selected source
function filterBySource() {
  searchArticles()
}

// 🪟 Open/close modal
function openArticle(article) {
  selectedArticle.value = article
  showModal.value = true
}
function closeModal() {
  showModal.value = false
  selectedArticle.value = null
}

// 🚪 Logout
function logout() {
  localStorage.removeItem('token')
  router.push('/login')
}
function timeAgo(dateString) {
  if (!dateString) return '';
  const publishedDate = new Date(dateString);
  const now = new Date();
  const diffMs = now - publishedDate;
  const diffSec = Math.floor(diffMs / 1000);
  const diffMin = Math.floor(diffSec / 60);
  const diffHour = Math.floor(diffMin / 60);
  const diffDay = Math.floor(diffHour / 24);

  if (diffSec < 60) return `${diffSec} seconds ago`;
  if (diffMin < 60) return `${diffMin} minutes ago`;
  if (diffHour < 24) return `${diffHour} hours ago`;
  return `${diffDay} days ago`;
}
onMounted(searchArticles)
</script>

<style scoped>
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
img {
  transition: transform 0.3s ease;
}
img:hover {
  transform: scale(1.02);
}
@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.5; }
}
.animate-pulse {
  animation: pulse 1.5s ease-in-out infinite;
}
</style>
