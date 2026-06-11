<template>
  <div class="page">
    <h2>我的订单 / 订单中心</h2>

    <div class="top-bar">
      <button @click="loadUserOrders">查询我的订单</button>
      <button @click="loadAllOrders">查询所有订单</button>
    </div>

    <table>
      <thead>
      <tr>
        <th>订单编号</th>
        <th>用户ID</th>
        <th>车辆ID</th>
        <th>开始时间</th>
        <th>预计归还时间</th>
        <th>实际归还时间</th>
        <th>押金</th>
        <th>总费用</th>
        <th>订单状态</th>
        <th>操作</th>
      </tr>
      </thead>

      <tbody>
      <tr v-for="order in orders" :key="order.id">
        <td>{{ order.id }}</td>
        <td>{{ order.userId }}</td>
        <td>{{ order.carId }}</td>
        <td>{{ formatTime(order.startTime) }}</td>
        <td>{{ formatTime(order.expectedReturnTime) }}</td>
        <td>{{ formatTime(order.actualReturnTime) || '未归还' }}</td>
        <td>{{ order.deposit }}</td>
        <td>{{ order.totalFee || '未结算' }}</td>
        <td>{{ getStatusText(order.status) }}</td>
        <td>
          <button
              v-if="order.status === 'RENTING'"
              @click="handleReturn(order.id)"
          >
            归还车辆
          </button>

          <button
              v-if="order.status === 'RENTING'"
              @click="handlePayDeposit(order.id)"
          >
            支付押金
          </button>

          <span v-if="order.status === 'FINISHED'">已完成</span>
          <span v-if="order.status === 'RESERVED'">已预约</span>
        </td>
      </tr>
      </tbody>
    </table>

    <div v-if="orders.length === 0" class="empty">
      暂无订单数据
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import {
  getUserOrders,
  getAllOrders,
  returnCar,
  payDeposit
} from '../api/order'

const orders = ref([])

onMounted(() => {
  loadUserOrders()
})

async function loadUserOrders() {
  const userId = localStorage.getItem('userId') || 1
  const res = await getUserOrders(userId)

  if (res.code === 200) {
    orders.value = res.data
  } else {
    alert(res.message)
  }
}

async function loadAllOrders() {
  const res = await getAllOrders()

  if (res.code === 200) {
    orders.value = res.data
  } else {
    alert(res.message)
  }
}

async function handleReturn(orderId) {
  const flag = confirm('确定要归还这辆车吗？')

  if (!flag) {
    return
  }

  const res = await returnCar(orderId)
  alert(res.message)

  if (res.code === 200) {
    loadUserOrders()
  }
}

async function handlePayDeposit(orderId) {
  const res = await payDeposit(orderId)
  alert(res.message)
}

function getStatusText(status) {
  if (status === 'RENTING') return '租赁中'
  if (status === 'FINISHED') return '已完成'
  if (status === 'RESERVED') return '预约中'
  return status
}

function formatTime(time) {
  if (!time) {
    return ''
  }

  return time.replace('T', ' ')
}
</script>

<style scoped>
.page {
  padding: 30px;
}

h2 {
  margin-bottom: 20px;
}

.top-bar {
  margin-bottom: 20px;
}

.top-bar button {
  margin-right: 10px;
  padding: 8px 16px;
  cursor: pointer;
}

table {
  width: 100%;
  border-collapse: collapse;
  background: white;
}

th,
td {
  border: 1px solid #ddd;
  padding: 10px;
  text-align: center;
  font-size: 14px;
}

th {
  background: #f3f6f9;
}

button {
  padding: 6px 10px;
  margin: 3px;
  cursor: pointer;
}

.empty {
  margin-top: 20px;
  color: #888;
}
</style>