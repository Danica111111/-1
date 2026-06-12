<template>
  <div class="page">
    <h2>地图定位 / 取车地点</h2>

    <div class="tool-card">
      <div class="info">
        <p>当前城市：{{ city || '重庆市' }}</p>
        <p>当前位置：{{ address || '重庆理工大学花溪校区' }}</p>
        <p>经度：{{ lng || '未获取' }}</p>
        <p>纬度：{{ lat || '未获取' }}</p>
      </div>

      <div class="btn-group">
        <button @click="locateMe">获取我的位置</button>
        <button @click="setDefaultLocation">使用默认取车点</button>
        <button @click="goCars">去选车</button>
      </div>
    </div>

    <div id="mapContainer"></div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import L from 'leaflet'
import 'leaflet/dist/leaflet.css'

const router = useRouter()

const city = ref('重庆市')
const address = ref('重庆理工大学花溪校区')
const lng = ref('')
const lat = ref('')

let map = null
let marker = null

// 重庆理工大学花溪校区附近坐标
const defaultLat = 29.458232451628852
const defaultLng = 106.52682592795

onMounted(() => {
  initMap()
})

function initMap() {
  map = L.map('mapContainer').setView([defaultLat, defaultLng], 15)

  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '© OpenStreetMap'
  }).addTo(map)

  setMarker(defaultLat, defaultLng, '默认取车点：重庆理工大学花溪校区')

  lat.value = defaultLat
  lng.value = defaultLng
}

function setMarker(latitude, longitude, text) {
  if (marker) {
    map.removeLayer(marker)
  }

  const carIcon = L.divIcon({
    html: '<div class="map-marker">🚗</div>',
    className: '',
    iconSize: [40, 40],
    iconAnchor: [20, 20]
  })

  marker = L.marker([latitude, longitude], {
    icon: carIcon
  }).addTo(map)

  marker.bindPopup(text).openPopup()

  map.setView([latitude, longitude], 16)
}

function locateMe() {
  if (!navigator.geolocation) {
    alert('当前浏览器不支持定位功能')
    return
  }

  navigator.geolocation.getCurrentPosition(
      position => {
        const latitude = position.coords.latitude
        const longitude = position.coords.longitude

        lat.value = latitude
        lng.value = longitude
        city.value = '当前位置'
        address.value = '浏览器定位位置'

        setMarker(latitude, longitude, '我的当前位置')

        alert('定位成功')
      },
      error => {
        console.error(error)
        alert('定位失败，请检查浏览器定位权限')
      },
      {
        enableHighAccuracy: true,
        timeout: 10000,
        maximumAge: 0
      }
  )
}

function setDefaultLocation() {
  city.value = '重庆市'
  address.value = '重庆理工大学花溪校区'
  lat.value = defaultLat
  lng.value = defaultLng

  setMarker(defaultLat, defaultLng, '默认取车点：重庆理工大学花溪校区')
}

function goCars() {
  router.push('/cars')
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

.tool-card {
  background: white;
  padding: 25px 30px;
  border-radius: 14px;
  box-shadow: 0 0 15px #ddd;
  margin-bottom: 25px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.info p {
  margin: 8px 0;
  font-size: 18px;
}

.btn-group {
  display: flex;
  gap: 15px;
}

.btn-group button {
  padding: 12px 24px;
  border: none;
  border-radius: 6px;
  font-size: 16px;
  cursor: pointer;
}

.btn-group button:nth-child(1) {
  background: #409eff;
  color: white;
}

.btn-group button:nth-child(2) {
  background: #ffd400;
  color: #333;
}

.btn-group button:nth-child(3) {
  background: #67c23a;
  color: white;
}

#mapContainer {
  width: 100%;
  height: 560px;
  border-radius: 14px;
  overflow: hidden;
  box-shadow: 0 0 15px #ddd;
  background: white;
}

:deep(.map-marker) {
  width: 38px;
  height: 38px;
  background: #ffd400;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  border: 3px solid white;
  box-shadow: 0 0 10px #999;
}
</style>