import { createRouter, createWebHistory } from 'vue-router'
import GebotView from '../views/GebotView.vue'

import { useLogin } from '@/services/useLogin'
const { logindata } = useLogin()

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('../views/AngeboteView.vue')
    },
    {
      path: '/about',
      name: 'about',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../views/AboutView.vue')
    },
    {
      path: '/gebot/:angebotidstr',
      name: 'gebot',
      component: GebotView,
      props: true
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/LoginView.vue')
    }
  ]
})

router.beforeEach( async (to, from) => {
  if (!logindata.loggedin && to.name !== 'login') {
  return { name: 'login' } }
  })

export default router
