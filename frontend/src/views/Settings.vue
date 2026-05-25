<template>
  <div>
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: '/Welcome' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>设置</el-breadcrumb-item>
    </el-breadcrumb>

    <el-card style="margin-top: 20px; max-width: 500px;">
      <template #header><span style="font-size: 16px; font-weight: bold;">更改头像</span></template>

      <div style="display: flex; align-items: center; gap: 20px; margin-bottom: 10px;">
        <el-avatar :src="getAvatarUrl(selectedAvatar)" :size="72">
          {{ getAvatarUrl(selectedAvatar) ? '' : (userInfo?.username?.charAt(0) || 'U') }}
        </el-avatar>
        <div>
          <p style="margin: 0; font-size: 14px; color: #909399;">从下方选择头像</p>
          <p style="margin: 4px 0 0; font-size: 12px; color: #c0c4cc;">点击即可选中</p>
        </div>
      </div>

      <div class="avatar-grid">
        <div
          v-for="av in availableAvatars"
          :key="av.key"
          class="avatar-item"
          :class="{ selected: selectedAvatar === av.key }"
          @click="selectedAvatar = av.key"
        >
          <el-image :src="av.url" fit="cover" style="width: 56px; height: 56px; border-radius: 50%;" />
        </div>
        <div
          class="avatar-item default-item"
          :class="{ selected: selectedAvatar === '' }"
          @click="selectedAvatar = ''"
        >
          <el-avatar :size="56">{{ userInfo?.username?.charAt(0) || 'U' }}</el-avatar>
          <span style="font-size: 11px; color: #909399; margin-top: 4px;">默认</span>
        </div>
      </div>
    </el-card>

    <el-card style="margin-top: 20px; max-width: 500px;">
      <template #header>
        <span style="font-size: 16px; font-weight: bold;">个人信息</span>
      </template>

      <el-form :model="form" ref="formRef" label-width="80px" :rules="formRules">
        <el-form-item label="用户名">
          <el-input :model-value="userInfo?.username" disabled />
        </el-form-item>
        <el-form-item label="角色">
          <el-input :model-value="roleLabel" disabled />
        </el-form-item>
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="form.realName" placeholder="请输入真实姓名" clearable />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号" clearable />
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入地址" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSave" :loading="saving">保存修改</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'
import { getAvatarUrl, getAvatarKeys } from '@/utils/avatar'

const formRef = ref(null)
const userInfo = ref(null)
const saving = ref(false)
const selectedAvatar = ref('')

const form = reactive({
  realName: '',
  phone: '',
  address: ''
})

const roleLabelMap = { REGULAR: '普通用户', COURIER: '快递员', MANAGER: '驿站管理员' }
const roleLabel = ref('')

const availableAvatars = ref([])
const keys = getAvatarKeys()
availableAvatars.value = keys.map(k => ({ key: k, url: getAvatarUrl(k) }))

const formRules = {
  phone: [
    { required: true, message: '手机号不能为空', trigger: 'blur' },
    { pattern: /^1\d{10}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ]
}

onMounted(async () => {
  try {
    const res = await request.get('/auth/current')
    userInfo.value = res.data
    roleLabel.value = roleLabelMap[userInfo.value.role] || ''
    form.realName = userInfo.value.realName || ''
    form.phone = userInfo.value.phone || ''
    form.address = userInfo.value.address || ''
    selectedAvatar.value = userInfo.value.avatar || ''
  } catch {
    ElMessage.error('获取用户信息失败')
  }
})

const handleSave = async () => {
  if (!formRef.value) return
  try {
    await formRef.value.validate()
  } catch {
    return
  }
  saving.value = true
  try {
    await request.put('/auth/profile', {
      realName: form.realName,
      phone: form.phone,
      address: form.address,
      avatar: selectedAvatar.value
    })
    ElMessage.success('个人信息修改成功')
    window.dispatchEvent(new Event('user-profile-updated'))
  } catch {
    // error handled by interceptor
  } finally {
    saving.value = false
  }
}
</script>

<style scoped>
.avatar-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-top: 16px;
}
.avatar-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 8px;
  border-radius: 8px;
  cursor: pointer;
  border: 2px solid transparent;
  transition: border-color 0.2s;
}
.avatar-item:hover { border-color: #c0c4cc; }
.avatar-item.selected { border-color: #409eff; background: #ecf5ff; }
.avatar-item.default-item { min-width: 70px; }
</style>
