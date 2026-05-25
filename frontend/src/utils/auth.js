const USER_KEY = 'parcel_locker_user'
const REMEMBER_KEY = 'parcel_locker_remember'

export function getUserInfo() {
  const user = sessionStorage.getItem(USER_KEY)
  if (user) {
    try { return JSON.parse(user) } catch { return null }
  }
  return null
}

export function setUserInfo(user) {
  sessionStorage.setItem(USER_KEY, JSON.stringify(user))
}

export function clearAuth() {
  sessionStorage.removeItem(USER_KEY)
}

export function saveCredentials(username, password) {
  localStorage.setItem(REMEMBER_KEY, JSON.stringify({ username, password }))
}

export function getSavedCredentials() {
  const data = localStorage.getItem(REMEMBER_KEY)
  if (data) {
    try { return JSON.parse(data) } catch { return null }
  }
  return null
}

export function clearCredentials() {
  localStorage.removeItem(REMEMBER_KEY)
}
