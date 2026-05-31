import axios from 'axios'
import { ElMessage } from 'element-plus'
import { clearAuth } from './auth'
import { transformDates } from './date'

const request = axios.create({
  baseURL: '/webapp',
  timeout: 15000,
  withCredentials: true
})
//响应拦截器，每个通过 request 发出的 API 调用，后端返回的数据先经过拦截器处理，然后才交给调用方。
request.interceptors.response.use(
  response => {
    const data = response.data
    if (data.code !== 200) {
      ElMessage.error(data.message || '请求失败')
      return Promise.reject(new Error(data.message))
    }
    // 自动将返回值中所有 ISO 日期字符串转为可读格式
    return transformDates(data)
  },
  error => {
    if (error.response && error.response.status === 401) {
      clearAuth()
      ElMessage.error('登录已过期，请重新登录')
      window.location.href = '/#/login'
    } else {
      ElMessage.error(error.message || '网络错误')
    }
    return Promise.reject(error)
  }
)

export default request
