import empty1 from '@/assets/empty/empty1.png'
import empty2 from '@/assets/empty/empty2.png'
import empty3 from '@/assets/empty/empty3.png'

const emptyMap = { empty1, empty2, empty3 }

export function getEmptyImage(key) {
  return emptyMap[key] || null
}
