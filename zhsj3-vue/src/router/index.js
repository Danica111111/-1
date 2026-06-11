import { createRouter, createWebHistory } from 'vue-router'

import OrderView from '../views/OrderView.vue'
import AiAssistantView from '../views/AiAssistantView.vue'

const routes = [
  {
    path: '/',
    redirect: '/orders'
  },
  {
    path: '/orders',
    component: OrderView
  },
  {
    path: '/ai',
    component: AiAssistantView
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router