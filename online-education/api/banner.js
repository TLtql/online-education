import request from '@/utils/request'

export default {
  getBannerList() {
    return request({
      url: `/eduservice/front-banner/get-banner`,
      method: 'get'
    })
  }
}