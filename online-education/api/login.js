import request from '@/utils/request'

export default {
  // 登录
  userLogin(userInfo) {
    return request({
      url: `/userservice/user-member/login`,
      method: 'post',
      data: userInfo
    })
  },
  // 根据 token 获取用户信息
  getUserInfo(){
    return request({
      url: `/userservice/user-member/getUserInfo`,
      method: 'get'
    })
  },
  // 公众号动态码登录
  userOfficialLogin(loginCode){
    return request({
      url: `/userservice/user-member/login/${loginCode}`,
      method: 'post'
    })
  }
}
