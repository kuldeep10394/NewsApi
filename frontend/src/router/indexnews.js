// frontend/src/router/index.js
import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Dashboard from '../views/Dashboard.vue'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login,
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: Dashboard,  //This will be your main layout/content when logged in
    meta: { requiresAuth: true },  //Add meta field to indicate protected route
  },
   //... other authenticated routes
];

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
  if (to.path === '/login') {
    next()
  } else if (!isAuthenticated()) {
    localStorage.removeItem('token') // clear stale token
    next('/login')
  } else {
    next()
  }
})



export default router;