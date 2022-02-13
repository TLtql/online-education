import request from '@/utils/request'

export default {
  getComment(page,limit,courseId) {
    return request({
      url: `/eduservice/front-comment/${page}/${limit}/${courseId}`,
      method: 'get'
    })
  },
  addComment(comment){
    return request({
      url: `/eduservice/front-comment/saveComment`,
      method: 'post',
      data: comment
    })
  },
  remoceUserComment(id){
    return request({
      url: `/eduservice/front-comment/deleteComment/${id}`,
      method: 'delete'
    })
  },
  getUserCommentList(){
    return request({
      url: `/eduservice/front-comment/getUserComment`,
      method: 'get'
    })
  }
}