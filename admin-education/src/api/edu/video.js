import request from '@/utils/request'


export default {
  saveVideoInfo(videoInfo){
    return request({
      url: `/eduservice/edu-video/addVideo`,
      method: 'post',
      data: videoInfo
    })
  },
  updateVideoInfo(videoInfoForm){
    return request({
      url: `/eduservice/edu-video`,
      method: 'put',
      data: videoInfoForm
    })
  },
  getOneVideoInfo(videoId){
    return request({
      url: `/eduservice/edu-video/${videoId}`,
      method: 'get',
    })
  },
  deleteVideoById(videoId){
    return request({
      url: `/eduservice/edu-video/${videoId}`,
      method: 'delete',
    })
  }
}
