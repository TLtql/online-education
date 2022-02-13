import request from '@/utils/request'


export default {
  getTacherList(){
    return request({
      url: `/eduservice/edu-teacher/findAll`,
      method: 'get',
    })
  },
  getTeacherList(page,limit,teacherQuery){
    return request({
      url: `/eduservice/edu-teacher/${page}/${limit}`,
      method: 'get',
      params: teacherQuery
    })
  },
  removeTeacherbyId(id){
    return request({
      url: `/eduservice/edu-teacher/${id}`,
      method: 'delete',
    })
  },
  saveTeacher(teacher){
    return request({
      url: `/eduservice/edu-teacher`,
      method: 'post',
      data: teacher
    })
  },
  getTeacherbyId(id){
    return request({
      url: `/eduservice/edu-teacher/${id}`,
      method: 'get'
    })
  },
  updateTeacher(teacher){
    return request({
      url: `/eduservice/edu-teacher/${teacher.id}`,
      method: 'put',
      data: teacher
    })
  }
}
