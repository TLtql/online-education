import request from '@/utils/request'

export default {
  userBuyCourse(userOrder) {
    return request({
      url: `/userservice/user-order/create-order`,
      method: 'post',
      data: userOrder
    })
  },
  getOrderStatus(userId,courseId){
    return request({
      url: `/userservice/user-order/existOrder/${userId}/${courseId}`,
      method: 'get',
    })
  },
  getUserOrder(){
    return request({
      url: `/userservice/user-order/getUserOrder`,
      method: 'get',
    })
  },
  removeUserOrder(id){
    return request({
      url: `/userservice/user-order/deleteOrder/${id}`,
      method: 'delete',
    })
  }
}
