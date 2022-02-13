import request from '@/utils/request'

export default {
  // 根据手机号发送验证码
  getMobile(phone) {
    return request({
      url: `vodService/sms/sendSms/${phone}`,
      method: 'get'
    })
  },
  // 用户注册
  userRegister(userInfo){
    return request({
      url: `/userservice/user-member/register`,
      method: 'post',
      data: userInfo
    })
  },
  // 检验手机号是否被注册
  existMobile(mobile){
    return request({
      url: `/userservice/user-member/existMobile/${mobile}`,
      method: 'get'
    })
  }
}
