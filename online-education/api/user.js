import request from '@/utils/request'

export default {
  updateUserById(userInfo) {
    return request({
      url: `/userservice/user-member/updateUserInfo`,
      method: 'put',
      data: userInfo
    })
  },
  updateMobile(mobile,code){
    return request({
      url: `/userservice/user-member/updateMobile/${mobile}/${code}`,
      method: 'put'
    })
  },
  deleteUser(){
    return request({
      url: `/userservice/user-member/removeUser`,
      method: 'delete'
    })
  }
}
