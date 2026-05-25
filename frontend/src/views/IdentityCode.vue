<template>
  <div>
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: '/Welcome' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>身份码</el-breadcrumb-item>
    </el-breadcrumb>

    <el-card style="margin-top: 20px; max-width: 420px;">
      <template #header>
        <span style="font-size: 16px; font-weight: bold;">我的身份码</span>
      </template>

      <div v-if="userInfo" class="identity-content">
        <el-descriptions :column="1" border size="small" style="margin-bottom: 20px;">
          <el-descriptions-item label="姓名">{{ userInfo.realName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="手机号">{{ userInfo.phone || '-' }}</el-descriptions-item>
        </el-descriptions>

        <div v-if="userInfo.phone" style="text-align: center;">
          <svg ref="barcodeRef"></svg>
          <p style="margin-top: 8px; font-size: 16px; letter-spacing: 2px; color: #303133;">
            {{ userInfo.phone }}
          </p>
          <p style="font-size: 12px; color: #909399; margin-top: 8px;">
            出库时向驿站工作人员出示此码即可
          </p>
        </div>
        <el-empty v-else description="未绑定手机号" />
      </div>

      <div v-else style="text-align: center; padding: 40px 0;">
        <el-icon :size="40" color="#c0c4cc"><Loading /></el-icon>
        <p style="color: #909399;">加载中...</p>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { Loading } from '@element-plus/icons-vue'
import request from '@/utils/request'
import JsBarcode from 'jsbarcode'

const barcodeRef = ref(null)
const userInfo = ref(null)

onMounted(async () => {
  try {
    const res = await request.get('/auth/current')
    userInfo.value = res.data
    if (userInfo.value.phone) {
      await nextTick()
      JsBarcode(barcodeRef.value, userInfo.value.phone, {
        format: 'CODE128',
        width: 2,
        height: 80,
        displayValue: false,
        fontSize: 16,
        margin: 10
      })
    }
  } catch {
    ElMessage.error('获取用户信息失败')
  }
})
</script>

<style scoped>
.identity-content {
  padding: 0;
}
</style>
