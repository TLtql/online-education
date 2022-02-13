import request from '@/utils/request'

export function login(username, password) {
  return request({
    url: '/eduservice/user/login',
    method: 'post',
    data: {
      username,
      password
    }
  })
}

export function getInfo() {
  return request({
    url: '/eduservice/user/info',
    method: 'get'
  })
}

export function logout() {
  return request({
    url: '/eduservice/user/logout',
    method: 'post'
  })
}
