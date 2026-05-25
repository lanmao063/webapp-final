import bg1 from '@/assets/background/neu1.jpg'
import bg2 from '@/assets/background/neu2.jpg'
import bg3 from '@/assets/background/neu3.jpg'
import bg4 from '@/assets/background/neu4.jpg'

const backgrounds = [bg1, bg2, bg3, bg4]

export function getRandomBackground() {
  const index = Math.floor(Math.random() * backgrounds.length)
  return backgrounds[index]
}
