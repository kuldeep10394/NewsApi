// frontend/src/router/index.js
import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import DashboardNewsAPI from '../views/DashboardNewsAPI.vue'
import DashboardRSS from '../views/DashboardRSS.vue'
const routes = [
  { path: '/login', component: Login, meta: { requiresAuth: false } },
  { path: '/dashboard', component: DashboardNewsAPI, meta: { requiresAuth: true } },
  { path: '/rss-dashboard', component: DashboardRSS, meta: { requiresAuth: true } },
  
]


const router = createRouter({
  history: createWebHistory(),
  routes,
});

// Global navigation guard
function isAuthenticated() {
  const token = localStorage.getItem('token')
  if (!token) return false
  try {
    const payload = JSON.parse(atob(token.split('.')[1]))
    return payload.exp * 1000 > Date.now()
  } catch (e) {
    return false
  }
}

router.beforeEach((to, from, next) => {
  // if route has meta.requiresAuth = false, skip auth check
  if (to.meta.requiresAuth === false) {
    next()
    return
  }

  // for all other routes, enforce authentication
  if (!isAuthenticated()) {
    localStorage.removeItem('token')
    next('/login')
  } else {
    next()
  }
})




export default router;