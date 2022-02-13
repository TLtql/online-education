import request from '@/utils/request'


export default {
  removeVideo(videoId){
    return request({
      url: `/vodService/video/deleteVideo/${videoId}`,
      method: 'delete',
    })
  }
}
