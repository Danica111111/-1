<template>
  <div class="page">
    <h2>个人中心</h2>

    <div class="card">
      <div>
        <label>账号：</label>
        <input v-model="form.username" disabled />
      </div>

      <div>
        <label>昵称：</label>
        <input v-model="form.nickname" />
      </div>

      <div>
        <label>手机号：</label>
        <input v-model="form.phone" />
      </div>

      <div>
        <label>头像：</label>
        <input v-model="form.avatar" />
      </div>

      <button @click="handleUpdate">保存修改</button>
    </div>
  </div>
</template>

<script setup>
import { reactive, onMounted } from 'vue'
import { getUserInfo, updateUserInfo } from '../api/user'

const form = reactive({
  id: '',
  username: '',
  nickname: '',
  phone: '',
  avatar: ''
})

onMounted(() => {
  loadUserInfo()
})

async function loadUserInfo() {
  const userId = localStorage.getItem('userId') || 1
  const res = await getUserInfo(userId)

  if (res.code === 200) {
    Object.assign(form, res.data)
  } else {
    alert(res.message)
  }
}

async function handleUpdate() {
  const userId = localStorage.getItem('userId') || 1

  const res = await updateUserInfo(userId, {
    nickname: form.nickname,
    phone: form.phone,
    avatar: form.avatar
  })

  alert(res.message)
}
</script>

<style scoped>
.page {
  padding: 30px;
}

.card {
  width: 420px;
  padding: 25px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 0 10px #ddd;
}

.card div {
  margin-bottom: 15px;
}

label {
  display: inline-block;
  width: 80px;
}

input {
  width: 280px;
  height: 36px;
  padding-left: 10px;
}

button {
  margin-left: 80px;
  padding: 10px 20px;
  background: #409eff;
  color: white;
  border: none;
  cursor: pointer;
}
</style>