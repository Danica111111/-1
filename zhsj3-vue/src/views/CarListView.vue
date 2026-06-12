<template>
  <div class="page">
    <h2>车辆列表</h2>

    <div class="rent-info">
      <p>取还城市：{{ rentCity || '未选择' }}</p>
      <p>取车地点：{{ rentLocation || '未选择' }}</p>
      <p>取车时间：{{ formatShowTime(rentStartTime) || '未选择' }}</p>
      <p>还车时间：{{ formatShowTime(rentEndTime) || '未选择' }}</p>
    </div>

    <!-- 搜索区域 -->
    <div class="search-box">
      <input v-model="keyword" placeholder="请输入品牌、车型或类型" />
      <button @click="loadCars">查询</button>
      <button @click="resetSearch">显示全部</button>
    </div>

    <!-- 筛选和排序区域 -->
    <div class="filter-box">
      <div class="type-list">
        <button
            v-for="type in carTypes"
            :key="type"
            :class="{ active: currentType === type }"
            @click="currentType = type"
        >
          {{ type }}
        </button>
      </div>

      <div class="sort-box">
        <select v-model="sortType">
          <option>默认排序</option>
          <option>价格从低到高</option>
          <option>价格从高到低</option>
        </select>
      </div>
    </div>

    <!-- 车辆列表 -->
    <div class="car-list">
      <div class="car-card" v-for="car in filteredCars" :key="car.id">
        <div class="image-box">
          <img :src="getImageUrl(car.imageUrl)" />
        </div>

        <div class="car-info">
          <h3>{{ car.brand }} {{ car.model }}</h3>

          <p class="desc">
            {{ car.carType }} / {{ car.seats }} 座
          </p>

          <div class="tag-box">
            <span>{{ car.carType }}</span>
            <span>{{ car.seats }}座</span>
            <span>{{ getStatusText(car.status) }}</span>
          </div>

          <p class="description">
            {{ car.description }}
          </p>

          <div class="bottom">
            <div class="price">
              日租金 <strong>￥{{ car.dailyPrice }}</strong>
              <span>/天</span>
            </div>

            <div class="status" :class="car.status">
              {{ getStatusText(car.status) }}
            </div>
          </div>

          <div class="btn-box">
            <button
                class="rent-btn"
                :disabled="car.status !== 'AVAILABLE'"
                @click="handleRent(car.id)"
            >
              租赁
            </button>

            <button
                class="reserve-btn"
                :disabled="car.status === 'RESERVED'"
                @click="handleReserve(car.id)"
            >
              预约
            </button>
          </div>
        </div>
      </div>
    </div>

    <div v-if="filteredCars.length === 0" class="empty">
      暂无符合条件的车辆
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { getCarList, searchCars } from '../api/car'
import { rentCar, reserveCar } from '../api/order'

const cars = ref([])
const keyword = ref('')

const rentStartTime = ref('')
const rentEndTime = ref('')
const rentCity = ref('')
const rentLocation = ref('')

const currentType = ref('全部车型')
const sortType = ref('默认排序')

const carTypes = [
  '全部车型',
  '经济型',
  '舒适型',
  '商务型',
  '豪华型'
]

onMounted(() => {
  rentStartTime.value = localStorage.getItem('rentStartTime') || ''
  rentEndTime.value = localStorage.getItem('rentEndTime') || ''
  rentCity.value = localStorage.getItem('rentCity') || ''
  rentLocation.value = localStorage.getItem('rentLocation') || ''

  loadCars()
})

const filteredCars = computed(() => {
  let list = [...cars.value]

  if (currentType.value !== '全部车型') {
    list = list.filter(car => car.carType === currentType.value)
  }

  if (sortType.value === '价格从低到高') {
    list.sort((a, b) => Number(a.dailyPrice) - Number(b.dailyPrice))
  }

  if (sortType.value === '价格从高到低') {
    list.sort((a, b) => Number(b.dailyPrice) - Number(a.dailyPrice))
  }

  return list
})

async function loadCars() {
  let res

  if (keyword.value.trim()) {
    res = await searchCars(keyword.value)
  } else {
    res = await getCarList()
  }

  if (res.code === 200) {
    cars.value = res.data
  } else {
    alert(res.message)
  }
}

function resetSearch() {
  keyword.value = ''
  currentType.value = '全部车型'
  sortType.value = '默认排序'
  loadCars()
}

function getStatusText(status) {
  if (status === 'AVAILABLE') return '可租'
  if (status === 'RENTED') return '已租'
  if (status === 'RESERVED') return '已预约'
  return status
}

function getImageUrl(imageUrl) {
  if (!imageUrl) {
    return ''
  }

  if (imageUrl.startsWith('http')) {
    return imageUrl
  }

  return '/' + imageUrl
}

async function handleRent(carId) {
  const userId = localStorage.getItem('userId') || 1

  if (!rentStartTime.value || !rentEndTime.value) {
    alert('请先在首页选择取车时间和还车时间')
    return
  }

  const res = await rentCar({
    userId,
    carId,
    startTime: formatSubmitTime(rentStartTime.value),
    expectedReturnTime: formatSubmitTime(rentEndTime.value),
    pickupLocation: rentLocation.value,
    returnLocation: rentLocation.value
  })

  alert(res.message)

  if (res.code === 200) {
    loadCars()
  }
}

async function handleReserve(carId) {
  const userId = localStorage.getItem('userId') || 1

  if (!rentStartTime.value) {
    alert('请先在首页选择取车时间')
    return
  }

  const res = await reserveCar({
    userId,
    carId,
    reserveTime: formatSubmitTime(rentStartTime.value),
    pickupLocation: rentLocation.value,
    returnLocation: rentLocation.value
  })

  alert(res.message)

  if (res.code === 200) {
    loadCars()
  }
}

function formatSubmitTime(time) {
  if (!time) {
    return ''
  }

  if (time.includes('T')) {
    return time.replace('T', ' ') + ':00'
  }

  return time
}

function formatShowTime(time) {
  if (!time) {
    return ''
  }

  return time.replace('T', ' ')
}
</script>

<style scoped>
.page {
  padding: 30px;
  background: #f5f7fa;
  min-height: 100vh;
}

h2 {
  font-size: 32px;
  margin-bottom: 25px;
}

.rent-info {
  background: white;
  padding: 18px 22px;
  border-radius: 10px;
  box-shadow: 0 0 10px #e5e5e5;
  margin-bottom: 20px;
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 12px;
}

.rent-info p {
  margin: 0;
  color: #333;
  font-size: 15px;
}

/* 搜索区域 */
.search-box {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.search-box input {
  width: 360px;
  height: 42px;
  padding-left: 14px;
  font-size: 15px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.search-box button {
  height: 44px;
  margin-left: 10px;
  padding: 0 20px;
  cursor: pointer;
  border: 1px solid #ccc;
  background: white;
  border-radius: 4px;
}

.search-box button:hover {
  background: #f0f0f0;
}

/* 筛选区域 */
.filter-box {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
  background: white;
  padding: 15px;
  border-radius: 8px;
  box-shadow: 0 0 10px #e5e5e5;
}

.type-list {
  display: flex;
  gap: 10px;
}

.type-list button {
  padding: 8px 18px;
  border: 1px solid #ddd;
  background: white;
  border-radius: 20px;
  cursor: pointer;
}

.type-list button.active {
  background: #ffd400;
  border-color: #ffd400;
  color: #333;
  font-weight: bold;
}

.sort-box select {
  height: 36px;
  padding: 0 12px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

/* 车辆列表 */
.car-list {
  display: flex;
  flex-direction: column;
  gap: 22px;
}

.car-card {
  display: flex;
  background: white;
  border-radius: 14px;
  padding: 20px;
  box-shadow: 0 5px 18px #e1e1e1;
  transition: 0.2s;
}

.car-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 24px #d0d0d0;
}

.image-box {
  width: 260px;
  height: 160px;
  background: #f0f0f0;
  border-radius: 10px;
  overflow: hidden;
  flex-shrink: 0;
}

.image-box img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.car-info {
  flex: 1;
  padding-left: 25px;
}

.car-info h3 {
  font-size: 26px;
  margin: 5px 0 8px;
}

.desc {
  color: #666;
  font-size: 15px;
}

.tag-box {
  display: flex;
  gap: 8px;
  margin: 12px 0;
}

.tag-box span {
  border: 1px solid #ddd;
  padding: 4px 10px;
  border-radius: 4px;
  color: #666;
  font-size: 13px;
}

.description {
  color: #555;
  margin: 12px 0;
}

.bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 10px;
}

.price {
  color: #666;
}

.price strong {
  color: #ff5a00;
  font-size: 30px;
  margin-left: 8px;
}

.price span {
  color: #999;
  margin-left: 4px;
}

.status {
  padding: 6px 14px;
  border-radius: 18px;
  font-weight: bold;
}

.status.AVAILABLE {
  background: #e8f8ef;
  color: #18a058;
}

.status.RENTED {
  background: #fff1f0;
  color: #d93026;
}

.status.RESERVED {
  background: #fff7e6;
  color: #d48806;
}

.btn-box {
  margin-top: 18px;
}

.btn-box button {
  padding: 9px 20px;
  margin-right: 12px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.rent-btn {
  background: #409eff;
  color: white;
}

.reserve-btn {
  background: #ffd400;
  color: #333;
}

.btn-box button:disabled {
  background: #ccc;
  color: #666;
  cursor: not-allowed;
}

.empty {
  margin-top: 30px;
  color: #999;
  text-align: center;
  font-size: 18px;
}
</style>