<template>
  <div class="page-container" v-loading="loading">
    <el-breadcrumb separator="/" class="page-breadcrumb">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>库存盘点</el-breadcrumb-item>
    </el-breadcrumb>

    <div class="stat-grid">
      <el-card v-for="card in cards" :key="card.label" shadow="hover" class="stat-card">
        <p class="stat-card-label">{{ card.label }}</p>
        <p class="stat-card-value" :style="{ color: card.color }">{{ card.value }}</p>
      </el-card>
    </div>

    <el-card shadow="never" class="filter-card">
      <el-form inline>
        <el-form-item label="统计维度">
          <el-select v-model="chartType" @change="fetchChart" style="width: 130px;">
            <el-option label="月度统计" value="monthly" />
            <el-option label="日度统计" value="daily" />
          </el-select>
        </el-form-item>
        <el-form-item label="年份">
          <el-input-number v-model="year" :min="2020" :max="2030" @change="fetchChart" />
        </el-form-item>
        <el-form-item v-if="chartType === 'daily'" label="月份">
          <el-select v-model="month" @change="fetchChart" style="width: 100px;">
            <el-option v-for="m in 12" :key="m" :label="m + '月'" :value="m" />
          </el-select>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" v-loading="chartLoading">
      <template #header><span>{{ chartTitle }}</span></template>
      <div ref="chartRef" class="chart-container"></div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, onUnmounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import request from '@/utils/request'

const year = ref(new Date().getFullYear())
const month = ref(new Date().getMonth() + 1)
const chartType = ref('monthly')
const chartRef = ref(null)
const loading = ref(false)
const chartLoading = ref(false)
let chartInstance = null
let resizeHandler = null

const chartTitle = computed(() => {
  if (chartType.value === 'daily') {
    return year.value + '年' + month.value + '月 日度统计'
  }
  return year.value + '年 月度统计'
})

const cards = reactive([
  { label: '在库包裹', value: 0, color: '#e6a23c' },
  { label: '已取件', value: 0, color: '#67c23a' },
  { label: '未处理异常', value: 0, color: '#f56c6c' }
])

const fetchOverview = async () => {
  try {
    const res = await request.get('/statistics/overview')
    cards[0].value = res.data.totalInWarehouse
    cards[1].value = res.data.totalPickedUp
    cards[2].value = res.data.unresolvedErrors
  } catch {}
}

const fetchChart = async () => {
  chartLoading.value = true
  try {
    const params = chartType.value === 'daily'
      ? { year: year.value, month: month.value }
      : { year: year.value }
    const url = chartType.value === 'daily' ? '/statistics/daily-chart' : '/statistics/chart'
    const res = await request.get(url, { params })
    const { labels, enterData, pickupData } = res.data
    if (!chartInstance) {
      chartInstance = echarts.init(chartRef.value)
    }
    chartInstance.setOption({
      tooltip: { trigger: 'axis' },
      legend: { data: ['入库', '取件'] },
      xAxis: { type: 'category', data: labels },
      yAxis: { type: 'value' },
      series: [
        { name: '入库', type: 'bar', data: enterData, color: '#409eff' },
        { name: '取件', type: 'line', data: pickupData, color: '#67c23a' }
      ]
    })
  } catch {} finally {
    chartLoading.value = false
  }
}

onMounted(async () => {
  loading.value = true
  await fetchOverview()
  await nextTick()
  await fetchChart()
  loading.value = false

  resizeHandler = () => chartInstance?.resize()
  window.addEventListener('resize', resizeHandler)
})

onUnmounted(() => {
  if (chartInstance) {
    chartInstance.dispose()
    chartInstance = null
  }
  if (resizeHandler) {
    window.removeEventListener('resize', resizeHandler)
  }
})
</script>

<style scoped>
.chart-container {
  height: 400px;
}
.filter-card {
  margin-bottom: var(--spacing-md);
}
</style>
