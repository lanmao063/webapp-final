/**
 * 格式化日期字符串，将 ISO 8601 格式转换为可读格式
 * 例如: "2025-05-30T10:30:00" -> "2025-05-30 10:30:00"
 * @param {string} dateStr - 日期字符串
 * @param {string} fallback - 日期为空时的占位符，默认 '-'
 * @returns {string} 格式化后的日期字符串
 */
export function formatDateTime(dateStr, fallback = '-') {
  if (!dateStr) return fallback
  // 用正则精确匹配 ISO 格式（如 "2025-05-30T10:30:00"），避免误伤含 T 的普通字符串
  if (!/^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}/.test(dateStr)) return dateStr
  try {
    const d = new Date(dateStr)
    if (isNaN(d.getTime())) return fallback
    const pad = (n) => String(n).padStart(2, '0')
    return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`
  } catch {
    return fallback
  }
}

/**
 * 递归遍历数据，将所有 ISO 8601 日期字符串转为可读格式
 * 作为 axios 响应拦截器使用，自动处理所有 API 返回的日期字段
 */
export function transformDates(data) {
  if (data === null || data === undefined) return data
  if (typeof data === 'string') return formatDateTime(data)
  if (Array.isArray(data)) return data.map(transformDates)
  if (typeof data === 'object') {
    const result = {}
    for (const key of Object.keys(data)) {
      result[key] = transformDates(data[key])
    }
    return result
  }
  return data
}
