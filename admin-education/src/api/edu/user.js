import request from '@/utils/request'


export default {
  getUserListPage(page,limit,userQuery){
    return request({
      url: `/userservice/user-member/pageList/${page}/${limit}`,
      method: 'post',
      data: userQuery
    })
  },
  // 封禁用户
  banUserById(id, time){
    return request({
      url: `/userservice/user-member/banUser/${id}/${time}`,
      method: 'put'
    })
  },
  getBlockStatus(id){
    return request({
      url: `/userservice/user-member/getBlockTime/${id}`,
      method: 'get'
    })
  },
  // 解封用户
  relieveUser(id){
    return request({
      url: `/userservice/user-member/relieveUser/${id}`,
      method: 'put'
    })
  },
  // 删除用户
  removeUserById(id){
    return request({
      url: `/userservice/user-member/removeUser/${id}`,
      method: 'delete'
    })
  }
}
