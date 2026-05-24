<template>
  <div class="admin-container">
    <!-- Step 1: 通行码验证 -->
    <el-card v-if="!verified" shadow="never" style="max-width: 400px; margin: 100px auto;">
      <template #header><span>管理员入口</span></template>
      <el-form :model="passForm" @keyup.enter="handleVerify">
        <el-form-item>
          <el-input v-model="passForm.passcode" type="password" placeholder="请输入通行码" show-password clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleVerify" :loading="verifyLoading" style="width: 100%;">验证</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- Step 2: 数据注入表单 -->
    <div v-else>
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item>数据管理</el-breadcrumb-item>
        <el-breadcrumb-item>包裹初始化</el-breadcrumb-item>
      </el-breadcrumb>

      <el-card shadow="never" style="max-width: 600px;">
        <template #header>
          <span>创建包裹记录</span>
          <el-tag type="success" size="small" style="margin-left: 10px;">已通过验证</el-tag>
        </template>
        <el-form :model="form" :rules="rules" ref="formRef" label-width="110px">
          <el-form-item label="快递单号" prop="trackingNumber">
            <el-input v-model="form.trackingNumber" placeholder="如: SF1234567890" />
          </el-form-item>
          <el-form-item label="包裹名称" prop="packageName">
            <el-input v-model="form.packageName" placeholder="如: iPhone 15" />
          </el-form-item>
          <el-form-item label="重量(kg)">
            <el-input-number v-model="form.weight" :precision="2" :min="0" :step="0.5" style="width: 100%;" />
          </el-form-item>
          <el-form-item label="体积(cm³)">
            <el-input-number v-model="form.volume" :precision="0" :min="0" :step="1000" style="width: 100%;" />
          </el-form-item>
          <el-divider content-position="left">收件人信息</el-divider>
          <el-form-item label="收件人姓名" prop="receiverName">
            <el-input v-model="form.receiverName" placeholder="必填" />
          </el-form-item>
          <el-form-item label="收件人电话" prop="receiverPhone">
            <el-input v-model="form.receiverPhone" placeholder="必填" />
          </el-form-item>
          <el-form-item label="收件人地址">
            <el-input v-model="form.receiverAddress" placeholder="选填" />
          </el-form-item>
          <el-divider content-position="left">寄件人信息（选填）</el-divider>
          <el-form-item label="寄件人姓名">
            <el-input v-model="form.senderName" />
          </el-form-item>
          <el-form-item label="寄件人电话">
            <el-input v-model="form.senderPhone" />
          </el-form-item>
          <el-form-item label="寄件人地址">
            <el-input v-model="form.senderAddress" />
          </el-form-item>
          <el-form-item label="备注">
            <el-input v-model="form.notes" type="textarea" :rows="2" />
          </el-form-item>
          <el-form-item>
            <el-checkbox v-model="form.createInbound">同时创建入库预录入记录</el-checkbox>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSubmit" :loading="submitLoading">创建包裹</el-button>
          </el-form-item>
        </el-form>
      </el-card>

      <!-- 创建结果 -->
      <el-card v-if="result" shadow="never" style="max-width: 600px; margin-top: 16px;">
        <template #header><span>创建成功</span></template>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="包裹ID">{{ result.id }}</el-descriptions-item>
          <el-descriptions-item label="快递单号">{{ result.trackingNumber }}</el-descriptions-item>
          <el-descriptions-item label="入库记录ID" v-if="result.inboundId">{{ result.inboundId }}</el-descriptions-item>
          <el-descriptions-item label="下一步">快递员可使用"快递入库"功能扫描此单号</el-descriptions-item>
        </el-descriptions>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const verified = ref(false)
const verifyLoading = ref(false)
const submitLoading = ref(false)
const result = ref(null)
const formRef = ref(null)
const passForm = reactive({ passcode: '' })

const form = reactive({
  trackingNumber: '',
  packageName: '',
  weight: null,
  volume: null,
  receiverName: '',
  receiverPhone: '',
  receiverAddress: '',
  senderName: '',
  senderPhone: '',
  senderAddress: '',
  notes: '',
  createInbound: true
})

const rules = {
  trackingNumber: [{ required: true, message: '请输入快递单号', trigger: 'blur' }],
  packageName: [{ required: true, message: '请输入包裹名称', trigger: 'blur' }],
  receiverName: [{ required: true, message: '请输入收件人姓名', trigger: 'blur' }],
  receiverPhone: [
    { required: true, message: '请输入收件人电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
  ]
}

const handleVerify = async () => {
  if (!passForm.passcode) {
    ElMessage.warning('请输入通行码')
    return
  }
  verifyLoading.value = true
  try {
    // 通过任意请求验证通行码，这里先暂存，提交时一起发送
    verified.value = true
  } catch {} finally { verifyLoading.value = false }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  try { await formRef.value.validate() } catch { return }
  submitLoading.value = true
  try {
    const res = await request.post('/admin/package-init', {
      ...form,
      passcode: passForm.passcode
    })
    result.value = res.data
    ElMessage.success('包裹创建成功')
  } catch {} finally { submitLoading.value = false }
}
</script>

<style scoped>
.admin-container { min-height: 100vh; background: #f5f7fa; padding: 20px; }
.breadcrumb { margin-bottom: 16px; }
</style>
