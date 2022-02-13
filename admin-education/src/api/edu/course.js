import request from '@/utils/request'


export default {
  saveCourseInfo(courseInfoVo){
    return request({
      url: `/eduservice/edu-course/saveCourseInfo`,
      method: 'post',
      data: courseInfoVo
    })
  },
  getCourseInfo(id){
    return request({
      url: `/eduservice/edu-course/${id}`,
      method: 'get',
    })
  },
  updateCourseInfo(id,courseInfoVo){
    return request({
      url: `/eduservice/edu-course/${id}`,
      method: 'put',
      data: courseInfoVo
    })
  },
  fetchCoursePublishInfoById(courseId){
    return request({
      url: `/eduservice/edu-course/getCoursePublish/${courseId}`,
      method: 'get',
    })
  },
  publishCourse(courseId){
    return request({
      url: `/eduservice/edu-course/publishCourse/${courseId}`,
      method: 'put',
    })
  },
  getCourseListPage(page,limit,searchObj){
    return request({
      url: `/eduservice/edu-course/pageCourseList/${page}/${limit}`,
      method: 'get',
      params: searchObj
    })
  },
  deleteCourse(courseId){
    return request({
      url: `/eduservice/edu-course/${courseId}`,
      method: 'delete',
    })
  },
  // 课程下架
  soldOutCourseById(courseId){
    return request({
      url: `/eduservice/edu-course/soldOutCourse/${courseId}`,
      method: 'put',
    })
  }
}
