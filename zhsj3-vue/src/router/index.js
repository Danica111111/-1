import { createRouter, createWebHistory } from 'vue-router'

import LoginView from '../views/LoginView.vue'
import RegisterView from '../views/RegisterView.vue'
import HomeView from '../views/HomeView.vue'
import CarListView from '../views/CarListView.vue'
import OrderView from '../views/OrderView.vue'
import AiAssistantView from '../views/AiAssistantView.vue'
import ProfileView from '../views/ProfileView.vue'
import MapLocationView from '../views/MapLocationView.vue'

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    component: LoginView
  },
  {
    path: '/register',
    component: RegisterView
  },
  {
    path: '/home',
    component: HomeView
  },
  {
    path: '/cars',
    component: CarListView
  },
  {
    path: '/orders',
    component: OrderView
  },
  {
    path: '/ai',
    component: AiAssistantView
  },
  {
    path: '/profile',
    component: ProfileView
  },
  {
    path: '/map',
    component: MapLocationView
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router