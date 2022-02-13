import request from '@/utils/request'

export default {
  createNative(price) {
    return request({
      url: `/userservice/pay-log/createNative/${price}`,
      method: 'get'
    })
  },
  queryPayStatus(orderNo) {
    return request({
      url: `/userservice/pay-log/queryPayStatus/${orderNo}`,
      method: 'get'
    })
  }
}