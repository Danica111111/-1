<template>
  <div class="home-page">
    <div class="banner">
      <div>
        <h1>汽车租赁系统</h1>
        <p>在线租车 · 自助选车 · 智能推荐</p>
      </div>
    </div>

    <div class="search-card">
      <div class="tab">
        <span class="active">租车</span>
      </div>

      <div class="form-row">
        <div class="form-item">
          <label>取车时间</label>
          <input type="datetime-local" v-model="form.startTime" />
        </div>

        <div class="form-item">
          <label>还车时间</label>
          <input type="datetime-local" v-model="form.endTime" />
        </div>
      </div>

      <div class="form-row">
        <div class="form-item">
          <label>取还城市</label>
          <select v-model="form.city">
            <option value="">请选择取还城市</option>
            <option value="重庆">重庆</option>
            <option value="成都">成都</option>
            <option value="贵阳">贵阳</option>
            <option value="昆明">昆明</option>
            <option value="西安">西安</option>
          </select>
        </div>

        <div class="form-item">
          <label>取车地点</label>
          <select v-model="form.location">
            <option value="">请选择取车地点</option>
            <option value="重庆理工大学花溪校区">重庆理工大学花溪校区</option>
            <option value="重庆北站">重庆北站</option>
            <option value="重庆西站">重庆西站</option>
            <option value="江北机场">江北机场</option>
            <option value="观音桥商圈">观音桥商圈</option>
            <option value="解放碑">解放碑</option>
          </select>
        </div>
      </div>

      <button class="choose-btn" @click="goCars">立即选车</button>
    </div>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const form = reactive({
  startTime: '2026-06-12T05:54',
  endTime: '2026-06-12T18:54',
  city: '重庆',
  location: '重庆理工大学花溪校区'
})

function goCars() {
  if (!form.startTime) {
    alert('请选择取车时间')
    return
  }

  if (!form.endTime) {
    alert('请选择还车时间')
    return
  }

  if (!form.city) {
    alert('请选择取还城市')
    return
  }

  if (!form.location) {
    alert('请选择取车地点')
    return
  }

  localStorage.setItem('rentStartTime', form.startTime)
  localStorage.setItem('rentEndTime', form.endTime)
  localStorage.setItem('rentCity', form.city)
  localStorage.setItem('rentLocation', form.location)

  router.push('/cars')
}
</script>

<style scoped>
.home-page {
  padding: 30px;
  background: #f5f7fa;
  min-height: 100vh;
}

.banner {
  height: 250px;
  background: linear-gradient(135deg, #28aeea, #ffde22);
  border-radius: 18px;
  color: white;
  display: flex;
  align-items: center;
  padding-left: 70px;
  box-shadow: 0 8px 20px #ddd;
}

.banner h1 {
  font-size: 52px;
  margin-bottom: 18px;
}

.banner p {
  font-size: 28px;
}

.search-card {
  margin-top: -55px;
  background: white;
  border-radius: 22px;
  padding: 40px;
  box-shadow: 0 5px 20px #ddd;
  position: relative;
}

.tab {
  display: flex;
  gap: 40px;
  margin-bottom: 35px;
  font-size: 28px;
}

.tab span {
  cursor: pointer;
}

.tab .active {
  font-weight: bold;
  border-bottom: 6px solid #ffd400;
  padding-bottom: 6px;
}

.form-row {
  display: flex;
  gap: 40px;
  margin-bottom: 28px;
}

.form-item {
  flex: 1;
}

.form-item label {
  display: block;
  margin-bottom: 12px;
  color: #666;
  font-size: 20px;
}

.form-item input,
.form-item select {
  width: 100%;
  height: 58px;
  padding-left: 18px;
  box-sizing: border-box;
  font-size: 18px;
  border: 1px solid #999;
  background: white;
}

.choose-btn {
  width: 100%;
  height: 70px;
  background: linear-gradient(90deg, #ffe833, #ffd400);
  border: none;
  border-radius: 10px;
  font-size: 30px;
  font-weight: bold;
  cursor: pointer;
  margin-top: 15px;
}

.choose-btn:hover {
  background: linear-gradient(90deg, #ffd400, #ffc400);
}
</style>