<template>
  <div class="page">
    <div class="container">
      <h2 class="title">我的订单</h2>

      <div v-if="orders.length > 0" class="order-list">
        <div class="order-card" v-for="order in orders" :key="order.id">
          <!-- 顶部 -->
          <div class="card-header">
            <div class="car-title">
              {{ getCarTitle(order.carId) }}
              <span class="tag">本人用车</span>
            </div>

            <div class="order-status" :class="order.status">
              {{ getStatusText(order.status) }}
            </div>
          </div>

          <div class="divider"></div>

          <!-- 信息区域 -->
          <div class="info-list">
            <div class="info-item">
              <span class="label">下单时间</span>
              <span class="value">{{ formatTime(order.createTime || order.startTime) }}</span>
            </div>

            <div class="info-item">
              <span class="label">取车</span>
              <span class="value">
    {{ formatTime(order.startTime) }}
    {{ getPickupLocation(order) }}
  </span>
            </div>

            <div class="info-item">
              <span class="label">还车</span>
              <span class="value">
    {{ formatTime(order.expectedReturnTime) || '未填写' }}
    {{ getReturnLocation(order) }}
  </span>
            </div>
          </div>

          <!-- 金额 -->
          <div class="amount-row">
            <span class="amount-label">订单金额</span>
            <span class="amount-value">￥{{ getOrderAmount(order) }}</span>
          </div>

          <!-- 操作区域 -->
          <div class="action-row">
            <button
                v-if="order.status === 'RESERVED'"
                class="cancel-btn"
                @click="handleCancelReserve(order.id)"
            >
              取消预约
            </button>

            <template v-if="order.status === 'RENTING'">
              <button class="pay-btn" @click="handlePayDeposit(order.id)">
                支付押金
              </button>
              <button class="return-btn" @click="handleReturn(order.id)">
                归还车辆
              </button>
            </template>

            <span v-if="order.status === 'FINISHED'" class="finish-text">
              订单已完成
            </span>

            <span v-if="order.status === 'CANCELLED'" class="cancel-text">
              订单已取消
            </span>
          </div>
        </div>
      </div>

      <div v-else class="empty">
        当前界面没有更多相关数据
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import {
  getUserOrders,
  returnCar,
  payDeposit,
  cancelReserve
} from '../api/order'

const orders = ref([])

// 车辆名称映射
const carMap = {
  1: '丰田卡罗拉',
  2: '本田雅阁',
  3: '别克GL8',
  4: '宝马3系',
  5: '宝马3系',
  6: '日产轩逸',
  7: '大众朗逸',
  8: '奥迪A4',
  9: '奔驰C级',
  10: '特斯拉Model 3'
}

onMounted(() => {
  loadUserOrders()
})

async function loadUserOrders() {
  const userId = localStorage.getItem('userId') || 1
  const res = await getUserOrders(userId)

  if (res.code === 200) {
    orders.value = res.data || []
  } else {
    alert(res.message)
  }
}

function getCarTitle(carId) {
  return carMap[carId] || `车辆ID ${carId}`
}

function getPickupLocation(order) {
  return order.pickupLocation || '未填写'
}

function getReturnLocation(order) {
  return order.returnLocation || order.pickupLocation || '未填写'
}

function getOrderAmount(order) {
  if (order.totalFee) return order.totalFee
  if (order.deposit) return order.deposit
  return 0
}

function getStatusText(status) {
  if (status === 'RESERVED') return '待取车'
  if (status === 'RENTING') return '租赁中'
  if (status === 'FINISHED') return '已完成'
  if (status === 'CANCELLED') return '已取消'
  return status
}

function formatTime(time) {
  if (!time) return ''
  return String(time).replace('T', ' ')
}

async function handleReturn(orderId) {
  const flag = confirm('确定要归还这辆车吗？')
  if (!flag) return

  const res = await returnCar(orderId)
  alert(res.message)

  if (res.code === 200) {
    loadUserOrders()
  }
}

async function handlePayDeposit(orderId) {
  const res = await payDeposit(orderId)
  alert(res.message)

  if (res.code === 200) {
    loadUserOrders()
  }
}

async function handleCancelReserve(orderId) {
  const flag = confirm('确定要取消这个预约吗？')
  if (!flag) return

  const res = await cancelReserve(orderId)
  alert(res.message)

  if (res.code === 200) {
    loadUserOrders()
  }
}
</script>

<style scoped>
.page {
  min-height: 100vh;
  background: #f5f7fa;
  padding: 40px 20px;
  box-sizing: border-box;
}

.container {
  width: 100%;
  max-width: 980px;
  margin: 0 auto;
}

.title {
  font-size: 34px;
  font-weight: bold;
  color: #222;
  margin-bottom: 30px;
}

.order-list {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.order-card {
  background: #fff;
  border-radius: 20px;
  padding: 28px 30px;
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.06);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.car-title {
  font-size: 34px;
  font-weight: 700;
  color: #111;
  display: flex;
  align-items: center;
  gap: 12px;
}

.tag {
  font-size: 15px;
  color: #888;
  background: #f2f2f2;
  padding: 6px 12px;
  border-radius: 999px;
  font-weight: normal;
}

.order-status {
  font-size: 20px;
  font-weight: 700;
}

.order-status.RESERVED {
  color: #d4a106;
}

.order-status.RENTING {
  color: #1677ff;
}

.order-status.FINISHED {
  color: #18a058;
}

.order-status.CANCELLED {
  color: #999;
}

.divider {
  height: 1px;
  background: #ececec;
  margin: 20px 0 24px;
}

.info-list {
  display: flex;
  flex-direction: column;
  gap: 18px;
}

.info-item {
  display: flex;
  align-items: flex-start;
  font-size: 18px;
  line-height: 1.7;
}

.label {
  width: 90px;
  color: #666;
  flex-shrink: 0;
}

.value {
  color: #333;
  word-break: break-all;
}

.amount-row {
  margin-top: 28px;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 16px;
}

.amount-label {
  font-size: 20px;
  color: #666;
}

.amount-value {
  font-size: 26px;
  font-weight: bold;
  color: #111;
}

.action-row {
  margin-top: 28px;
  display: flex;
  gap: 14px;
  align-items: center;
  flex-wrap: wrap;
}

.cancel-btn,
.pay-btn,
.return-btn {
  border: none;
  padding: 12px 22px;
  border-radius: 10px;
  font-size: 16px;
  cursor: pointer;
}

.cancel-btn {
  background: #f56c6c;
  color: #fff;
}

.pay-btn {
  background: #67c23a;
  color: #fff;
}

.return-btn {
  background: #409eff;
  color: #fff;
}

.finish-text {
  color: #18a058;
  font-size: 18px;
  font-weight: 700;
}

.cancel-text {
  color: #999;
  font-size: 18px;
  font-weight: 700;
}

.empty {
  text-align: center;
  color: #aaa;
  font-size: 22px;
  margin-top: 80px;
}

/* 兼容小屏 */
@media (max-width: 768px) {
  .page {
    padding: 20px 12px;
  }

  .title {
    font-size: 26px;
  }

  .order-card {
    padding: 20px 18px;
  }

  .car-title {
    font-size: 24px;
    flex-wrap: wrap;
  }

  .order-status {
    font-size: 18px;
  }

  .info-item {
    font-size: 16px;
  }

  .label {
    width: 70px;
  }

  .amount-label {
    font-size: 18px;
  }

  .amount-value {
    font-size: 24px;
  }
}
</style>