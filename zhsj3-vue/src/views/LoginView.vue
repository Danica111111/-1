<template>
  <div class="login-page">
    <div class="login-box">
      <h2>汽车租赁系统登录</h2>

      <input v-model="form.username" placeholder="请输入账号" />
      <input v-model="form.password" type="password" placeholder="请输入密码" />

      <button @click="handleLogin">登录</button>

      <p>
        没有账号？
        <router-link to="/register">去注册</router-link>
      </p>
    </div>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import { login } from '../api/user'

const router = useRouter()

const form = reactive({
  username: '',
  password: ''
})

async function handleLogin() {
  if (!form.username || !form.password) {
    alert('请输入账号和密码')
    return
  }

  const res = await login(form)

  if (res.code === 200) {
    localStorage.setItem('userId', res.data.id)
    localStorage.setItem('username', res.data.username)

    alert('登录成功')
    router.push('/home')
  } else {
    alert(res.message)
  }
}
</script>

<style scoped>
.login-page {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: #f5f7fa;
}

.login-box {
  width: 360px;
  padding: 30px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 0 15px #ddd;
}

.login-box h2 {
  text-align: center;
  margin-bottom: 25px;
}

.login-box input {
  width: 100%;
  height: 40px;
  margin-bottom: 15px;
  padding-left: 10px;
  box-sizing: border-box;
}

.login-box button {
  width: 100%;
  height: 42px;
  background: #409eff;
  color: white;
  border: none;
  cursor: pointer;
}

.login-box p {
  text-align: center;
}
</style>