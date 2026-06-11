<template>
  <div class="page">
    <h2>AI 智能选车助手</h2>

    <div class="card">
      <p class="tip">
        请输入你的租车需求，例如：周末家庭出游三天，需要坐5人，预算400元每天。
      </p>

      <textarea
          v-model="requirement"
          placeholder="请输入你的租车需求"
      ></textarea>

      <br />

      <button @click="handleRecommend">获取 AI 推荐</button>
    </div>

    <div class="result" v-if="result">
      <h3>推荐结果</h3>
      <pre>{{ result }}</pre>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { recommendCar } from '../api/ai'

const requirement = ref('')
const result = ref('')

async function handleRecommend() {
  if (!requirement.value.trim()) {
    alert('请输入租车需求')
    return
  }

  result.value = 'AI 正在分析，请稍候...'

  const res = await recommendCar({
    requirement: requirement.value
  })

  if (res.code === 200) {
    result.value = res.data
  } else {
    result.value = res.message
  }
}
</script>

<style scoped>
.page {
  padding: 30px;
}

.card {
  width: 650px;
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 0 10px #ddd;
}

.tip {
  color: #666;
  margin-bottom: 12px;
}

textarea {
  width: 100%;
  height: 160px;
  padding: 12px;
  font-size: 16px;
  box-sizing: border-box;
  resize: none;
}

button {
  margin-top: 15px;
  padding: 10px 20px;
  background: #409eff;
  color: white;
  border: none;
  cursor: pointer;
  border-radius: 4px;
}

.result {
  width: 650px;
  margin-top: 25px;
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 0 10px #ddd;
}

pre {
  white-space: pre-wrap;
  line-height: 1.8;
  font-size: 15px;
}
</style>