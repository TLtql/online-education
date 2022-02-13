import request from '@/utils/request'

export default {

  getPlayAuth(vid) {
    return request({
      url: `/vodService/video/getPlayAuth/${vid}`,
      method: 'get'
    })
  }

}
