<template>
  <div class="login-page">
    <div class="login-box">
      <h2>用户注册</h2>

      <input v-model="form.username" placeholder="请输入账号" />
      <input v-model="form.password" type="password" placeholder="请输入密码" />
      <input v-model="form.nickname" placeholder="请输入昵称" />
      <input v-model="form.phone" placeholder="请输入手机号" />

      <button @click="handleRegister">注册</button>

      <p>
        已有账号？
        <router-link to="/login">去登录</router-link>
      </p>
    </div>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import { register } from '../api/user'

const router = useRouter()

const form = reactive({
  username: '',
  password: '',
  nickname: '',
  phone: '',
  avatar: 'images/avatar1.jpg'
})

async function handleRegister() {
  if (!form.username || !form.password) {
    alert('请输入账号和密码')
    return
  }

  const res = await register(form)

  if (res.code === 200) {
    alert('注册成功，请登录')
    router.push('/login')
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