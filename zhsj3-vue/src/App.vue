<template>
  <div>
    <nav class="nav">
      <router-link to="/orders">订单中心</router-link>
      <router-link to="/ai">AI 智能选车</router-link>
    </nav>

    <router-view />
  </div>
</template>

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
</style>