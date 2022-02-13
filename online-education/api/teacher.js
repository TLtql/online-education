import request from '@/utils/request'

export default {
  getTeacher(page, limit) {
    return request({
      url: `/eduservice/front-teacher/${page}/${limit}`,
      method: 'get'
    })
  },
  getTeacherById(id){
    return request({
      url: `/eduservice/front-teacher/getTeacherInfo/${id}`,
      method: 'get'
    })
  }
}
