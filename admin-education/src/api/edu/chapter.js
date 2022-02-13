import request from '@/utils/request'


export default {
  getChapterList(courseId){
    return request({
      url: `/eduservice/edu-chapter/chapterList/${courseId}`,
      method: 'get',
    })
  },
  getOneChapter(chapterId){
    return request({
      url: `/eduservice/edu-chapter/${chapterId}`,
      method: 'get',
    })
  },
  updateChapterById(chapter){
    console.log(chapter)
    return request({
      url: `/eduservice/edu-chapter/${chapter.id}`,
      method: 'put',
      data: chapter
    })
  },
  addChapter(chapter,courseId){
    return request({
      url: `/eduservice/edu-chapter/addChapter/${courseId}`,
      method: 'post',
      data: chapter
    })
  },
  deleteChapterById(chapterId){
    return request({
      url: `/eduservice/edu-chapter/${chapterId}`,
      method: 'delete',
    })
  }
}
