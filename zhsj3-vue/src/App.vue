<template>
  <div>
    <nav class="nav" v-if="showNav">
      <router-link to="/home">首页</router-link>
      <router-link to="/cars">车辆列表</router-link>
      <router-link to="/orders">订单中心</router-link>
      <router-link to="/ai">AI 智能选车</router-link>
      <router-link to="/map">地图定位</router-link>
      <router-link to="/profile">个人中心</router-link>
      <button @click="logout">退出登录</button>
    </nav>

    <router-view />
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const showNav = computed(() => {
  return route.path !== '/login' && route.path !== '/register'
})

function logout() {
  localStorage.removeItem('userId')
  localStorage.removeItem('username')
  router.push('/login')
}
</script>

<style>
body {
  margin: 0;
  background: #f5f7fa;
  font-family: Arial, "Microsoft YaHei", sans-serif;
}

.nav {
  height: 60px;
  background: #2c3e50;
  display: flex;
  align-items: center;
  padding: 0 30px;
  gap: 30px;
}

.nav a {
  color: white;
  text-decoration: none;
  font-size: 16px;
}

.nav a.router-link-active {
  font-weight: bold;
  color: #ffd04b;
}

.nav button {
  margin-left: auto;
  padding: 8px 16px;
  cursor: pointer;
}
</style>