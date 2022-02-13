import request from '@/utils/request'

export default {
  getCoursePage(page, limit, courseQuery) {
    return request({
      url: `/eduservice/front-course/${page}/${limit}`,
      method: 'post',
      data: courseQuery
    })
  },
  getCourseById(id){
    return request({
      url: `/eduservice/front-course/getCourseInfo/${id}`,
      method: 'get'
    })
  },
  getSubjectList(){
    return request({
      url: `/eduservice/edu-subject/getSubject`,
      method: 'get'
    })
  },
  getCourseInfoById(courseId){
    return request({
      url: `/eduservice/front-course/getCourseInfo/${courseId}`,
      method: 'get'
    })
  }
}
