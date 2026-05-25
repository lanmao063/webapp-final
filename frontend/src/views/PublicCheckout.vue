<template>
  <div class="auth-container">
    <el-card class="checkout-card">
      <div class="auth-title">
        <el-icon :size="36" color="var(--color-primary)"><Box /></el-icon>
        <h2>莱鸟 · 包裹出库</h2>
      </div>
      <p class="auth-subtitle">无需登录，输入快递单号和收件人手机号即可取件</p>

      <el-form :model="form" ref="formRef" label-width="80px">
        <el-form-item label="快递单号">
          <el-input
            ref="inputRef"
            v-model="form.trackingNumber"
            placeholder="请输入或扫描快递单号"
            size="large"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input ref="phoneRef" v-model="form.phone" placeholder="请输入收件人手机号" size="large" clearable @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="large" @click="handleQuery" :loading="loading" class="w-full">
            查询包裹
          </el-button>
        </el-form-item>
      </el-form>

      <!-- 包裹信息 -->
      <div v-if="packageInfo" class="info-card">
        <el-descriptions :column="1" border size="small">
          <el-descriptions-item label="快递单号">{{ packageInfo.trackingNumber }}</el-descriptions-item>
          <el-descriptions-item label="包裹名称">{{ packageInfo.packageName }}</el-descriptions-item>
          <el-descriptions-item label="取件码">
            <el-tag type="success" size="large">{{ packageInfo.pickupCode }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="柜型">
            <el-tag :type="cabinetTag(packageInfo.cabinetType)" size="large">
              {{ cabinetLabel(packageInfo.cabinetType) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="入库时间">{{ packageInfo.enterTime }}</el-descriptions-item>
        </el-descriptions>
        <el-button type="success" size="large" @click="handleConfirm" :loading="loading" class="w-full" style="margin-top: 16px;">
          确认出库
        </el-button>
      </div>

      <!-- 出库结果 -->
      <div v-if="result" class="checkout-result">
        <el-alert title="出库成功！" type="success" :closable="false" show-icon center />
        <div class="result-detail">
          <p>取件码：<strong>{{ result.pickupCode }}</strong></p>
          <p v-if="result.remainingCount > 0">
            该收件人还有 <strong>{{ result.remainingCount }}</strong> 个包裹待取：
          </p>
          <p v-else>该收件人已无待取包裹</p>
          <div v-if="result.remainingCount > 0" class="remaining-list">
            <div v-for="(item, idx) in result.remainingPackages" :key="idx" class="remaining-item">
              <el-tag type="warning">{{ item.pickupCode }}</el-tag>
              <span class="remaining-tracking">{{ item.trackingNumber }}</span>
            </div>
          </div>
        </div>
        <p class="result-tip">信息将在 {{ countdown }} 秒后自动清除</p>
      </div>

      <div class="checkout-links">
        <el-button type="primary" link @click="$router.push('/login')">返回登录</el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted, onBeforeUnmount, nextTick, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { Box } from '@element-plus/icons-vue'
import request from '@/utils/request'

const formRef = ref(null)
const inputRef = ref(null)
const phoneRef = ref(null)
const loading = ref(false)

const form = reactive({
  trackingNumber: '',
  phone: ''
})

const packageInfo = ref(null)
const result = ref(null)
const countdown = ref(5)
let countdownTimer = null

const MIN_TRACKING_LEN = 12

onMounted(() => {
  nextTick(() => inputRef.value?.focus())
})

onBeforeUnmount(() => {
  clearTimer()
})

watch(() => form.trackingNumber, (val) => {
  if (val && val.trim().length >= MIN_TRACKING_LEN) {
    nextTick(() => phoneRef.value?.focus())
  }
})

const clearTimer = () => {
  if (countdownTimer) {
    clearInterval(countdownTimer)
    countdownTimer = null
  }
}

const resetAll = () => {
  clearTimer()
  form.trackingNumber = ''
  form.phone = ''
  packageInfo.value = null
  result.value = null
  nextTick(() => inputRef.value?.focus())
}

const handleQuery = async () => {
  if (!form.trackingNumber.trim()) {
    ElMessage.warning('请输入快递单号')
    return
  }
  if (!form.phone.trim()) {
    ElMessage.warning('请输入手机号')
    return
  }
  loading.value = true
  try {
    const res = await request.get('/inbound/public-search', {
      params: { trackingNumber: form.trackingNumber.trim(), phone: form.phone.trim() }
    })
    packageInfo.value = res.data
  } catch {
    packageInfo.value = null
  } finally {
    loading.value = false
  }
}

const handleConfirm = async () => {
  loading.value = true
  try {
    const res = await request.put('/inbound/public-checkout', {
      trackingNumber: form.trackingNumber.trim(),
      phone: form.phone.trim()
    })
    result.value = res.data
    packageInfo.value = null
    startCountdown()
  } catch {} finally {
    loading.value = false
  }
}

const startCountdown = () => {
  clearTimer()
  countdown.value = 5
  countdownTimer = setInterval(() => {
    countdown.value--
    if (countdown.value <= 0) resetAll()
  }, 1000)
}

const cabinetTag = (type) => type === 'SMALL' ? 'success' : type === 'MEDIUM' ? 'warning' : 'danger'
const cabinetLabel = (type) => type === 'SMALL' ? '小件' : type === 'MEDIUM' ? '中件' : '大件'
</script>

<style scoped>
.checkout-card {
  width: 520px;
  max-width: 92vw;
  border-radius: var(--radius-lg);
  box-shadow: 0 8px 32px rgba(0, 0, 0, .08);
}
.checkout-links {
  display: flex;
  justify-content: center;
  margin-top: var(--spacing-md);
}
.info-card {
  margin-top: var(--spacing-md);
  padding: 12px;
  background: var(--color-bg-hover);
  border-radius: var(--radius-md);
}
.checkout-result {
  margin-top: var(--spacing-md);
}
.result-detail {
  margin-top: 12px;
  padding: 12px;
  background: #f0f9eb;
  border-radius: var(--radius-md);
}
.result-detail p {
  margin: 6px 0;
  font-size: var(--font-size-base);
}
.remaining-list {
  margin-top: var(--spacing-sm);
}
.remaining-item {
  display: flex;
  align-items: center;
  gap: 10px;
  margin: 6px 0;
  padding: 6px 10px;
  background: #fdf6ec;
  border-radius: var(--radius-sm);
}
.remaining-tracking {
  font-size: var(--font-size-sm);
  color: var(--color-text-secondary);
}
.result-tip {
  text-align: center;
  font-size: var(--font-size-xs);
  color: var(--color-text-placeholder);
  margin-top: 12px;
}
</style>
