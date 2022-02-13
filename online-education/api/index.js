import request from '@/utils/request'

export default {
  getIndexData() {
    return request({
      url: `/eduservice/front-index`,
      method: 'get'
    })
  },
  // 用户签到
  userSignIn(id){
    return request({
      url: `/eduservice/front-index/signIn/${id}`,
      method: 'post'
    })
  },
  // 判断用户是否签过到
  isUserSignIn(){
    return request({
      url: `/eduservice/front-index/isSignIn`,
      method: 'get'
    })
  }
}
