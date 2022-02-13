import request from '@/utils/request'


export default {
  getBannerList(page, limit){
    return request({
      url: `/eduservice/admin-banner/${page}/${limit}`,
      method: 'get',
    })
  },
  removeBannerById(id){
    return request({
      url: `/eduservice/admin-banner/${id}`,
      method: 'delete',
    })
  },
  getBannerById(id){
    return request({
      url: `/eduservice/admin-banner/${id}`,
      method: 'get',
    })
  },
  updateBannerById(bannerInfo){
    return request({
      url: `/eduservice/admin-banner/update`,
      method: 'put',
      data: bannerInfo
    })
  },
  saveBannerInfo(bannerInfo){
    return request({
      url: `/eduservice/admin-banner/save-banner`,
      method: 'post',
      data: bannerInfo
    })
  }
}
