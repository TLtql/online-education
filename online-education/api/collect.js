import request from '@/utils/request'

export default {
  getUserCollectStatus(userId,courseId) {
    return request({
      url: `/userservice/user-collect/${userId}/${courseId}`,
      method: 'get'
    })
  },
  addUserCollect(collect){
    return request({
      url: `/userservice/user-collect/saveCollect`,
      method: 'post',
      data: collect
    })
  },
  getUserCollectList(){
    return request({
      url: `/userservice/user-collect/getCollect`,
      method: 'get'
    })
  },
  remoceUserCollect(id){
    return request({
      url: `/userservice/user-collect/deleteCollect/${id}`,
      method: 'delete'
    })
  },
  cancelCollect(userId,courseId){
    return request({
      url: `/userservice/user-collect/deleteCollect/${userId}/${courseId}`,
      method: 'delete'
    })
  }
}
