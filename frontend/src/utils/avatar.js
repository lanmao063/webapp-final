let ctx = null
try {
  ctx = require.context('@/assets/avatars', false, /\.(png|jpe?g|gif|svg)$/)
} catch {}

const keys = ctx ? ctx.keys() : []

export function getAvatarUrl(key) {
  if (!key) return ''
  if (key.startsWith('data:')) return key
  if (ctx) {
    try {
      return ctx(key)
    } catch {
      return ''
    }
  }
  return ''
}

export function getAvatarKeys() {
  return keys
}
