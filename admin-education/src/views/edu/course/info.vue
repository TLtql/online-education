<template>
  <div class="app-container">

      <h2 style="text-align: center;">发布新课程</h2>

      <el-steps :active="1" process-status="wait" align-center style="margin-bottom: 40px;">
        <el-step title="填写课程基本信息"/>
        <el-step title="创建课程大纲"/>
        <el-step title="最终发布"/>
      </el-steps>

      <el-form label-width="120px" :model="courseInfo" :rules="rules" ref="courseInfo">

        <el-form-item label="课程标题" prop="title">
          <el-input v-model="courseInfo.title" placeholder=" 示例：机器学习项目课：从基础到搭建项目视频课程。专业名称注意大小写"/>
        </el-form-item>

        <!-- 所属分类 TODO -->
        <!-- 所属分类：级联下拉列表 -->
        <!-- 一级分类 -->
        <el-form-item label="课程类别" prop="subjectId">
          <el-select
            @change="subjectLevelOneChanged"
            v-model="courseInfo.subjectParentId"
            placeholder="请选择">
            <el-option
              v-for="subject in subjectOne"
              :key="subject.id"
              :label="subject.title"
              :value="subject.id"/>
          </el-select>
          <!-- 二级分类 -->
          <el-select v-model="courseInfo.subjectId" placeholder="请选择">
            <el-option
              v-for="subject in subjectTwo"
              :key="subject.value"
              :label="subject.title"
              :value="subject.id"/>
          </el-select>

        </el-form-item>


        <!-- 课程讲师 TODO -->
        <!-- 课程讲师 -->
        <el-form-item label="课程讲师" prop="teacherId">
          <el-select
            v-model="courseInfo.teacherId"
            placeholder="请选择">
            <el-option
              v-for="teacher in teacherList"
              :key="teacher.id"
              :label="teacher.name"
              :value="teacher.id"/>
          </el-select>
        </el-form-item>

        <el-form-item label="总课时" prop="lessonNum">
          <el-input-number :min="0" v-model="courseInfo.lessonNum" controls-position="right" placeholder="请填写课程的总课时数"/>
        </el-form-item>

        <!-- 课程简介 TODO -->
        <!-- 课程简介-->
        <el-form-item label="课程简介" prop="description">
            <tinymce :height="300" v-model="courseInfo.description"/>
        </el-form-item>

        <!-- 课程封面 TODO -->
        <!-- 课程封面-->
        <el-form-item label="课程封面">

          <el-upload
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
            :action="BASE_API+'/ossservice/file/upload?host=cover'"
            class="avatar-uploader">
            <img :src="courseInfo.cover" style="width: 250px;">
          </el-upload>

        </el-form-item>

        <el-form-item label="课程价格" prop="price">
          <el-input-number :min="0" v-model="courseInfo.price" controls-position="right" placeholder="免费课程请设置为0元"/> 元
        </el-form-item>

        <el-form-item>
          <el-button :disabled="saveBtnDisabled" type="primary" @click="next('courseInfo')">保存并下一步</el-button>
        </el-form-item>
      </el-form>
    </div>
</template>

<script>
  import subject from '@/api/edu/subject'
  import teacher from '@/api/edu/teacher'
  import course from '@/api/edu/course'
  import Tinymce from '@/components/Tinymce'

  const defaultForm = {
      subjectId: '',
      cover: process.env.OSS_PATH + '/edu/cover/default.png'
  }

  export default {
    components: { Tinymce },
    data() {
      return {
        saveBtnDisabled: false, // 保存按钮是否禁用
        courseInfo: defaultForm,
        subjectOne: [],
        subjectTwo: [],
        teacherList: [],
        BASE_API: process.env.BASE_API,
        rules: {
          title: [
            { required: true, message: '请输入课程标题', trigger: 'blur' }
          ],
          subjectId: [
            { required: true, message: '请选择课程类别', trigger: 'change' }
          ],
          teacherId: [
            { required: true, message: '请选择讲师', trigger: 'change' }
          ],
          lessonNum: [
            { required: true, message: '请输入课程课时', trigger: 'blur' }
          ],
          description: [
            { required: true, message: '请输入课程简介', trigger: 'change' }
          ],
          price: [
            { required: true, message: '请输入课程价格', trigger: 'blur' }
          ],
        }
      }
    },
    watch: {
      $route(to, from) {
        console.log('watch $route')
        this.init()
      }
    },
    created() {
      console.log('info created')
      this.init()
    },

    methods: {
      //页面初始化
      init(){
        //路径中有ID 属性则为修改，否则为新增
        if(this.$route.params && this.$route.params.id){
          const id = this.$route.params.id
          console.log(id)
          this.getCourse(id)
        }else{
          this.courseInfo = { ...defaultForm }
          //获取课程分类
          this.initSubject()
          //获取讲师列表
          this.initTeacherList()
        }

      },
      initSubject(){
        subject.getSubjectList()
          .then(response => {
            this.subjectOne = response.data.subjectList
          })
      },
      //当一级下拉框改变时，更新二级下拉框
      subjectLevelOneChanged(value){
        for(let i=0; i < this.subjectOne.length; i++){
          if(this.subjectOne[i].id === value){
            this.subjectTwo = this.subjectOne[i].children
            this.courseInfo.subjectId = ''
          }
        }
      },
      initTeacherList(){
        teacher.getTacherList()
          .then(response => {
            this.teacherList = response.data.items
          })
      },
      //获取课程信息实现表单回显
      getCourse(id){
        course.getCourseInfo(id)
          .then(response => {
            this.courseInfo = response.data.course
            //初始化分类列表
            subject.getSubjectList()
              .then(responseSubject => {
                this.subjectOne = responseSubject.data.subjectList
                for(let i = 0; i < this.subjectOne.length; i++){
                  if(this.courseInfo.subjectParentId === this.subjectOne[i].id){
                    this.subjectTwo = this.subjectOne[i].children
                  }
                }
              })
            //获取讲师列表
            this.initTeacherList()
            this.$message({
              type: 'info',
              message: '请修改信息'
            })
          })
      },
      next(formName) {
        console.log('next')
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.saveBtnDisabled = true
            //路径中有ID 属性则为修改，否则为新增
            if(this.$route.params && this.$route.params.id){
              const id = this.$route.params.id
              console.log(id)
              this.updateCourse(id)
            }else{
              this.saveCourse()
            }
          } else {
            console.log('error submit!!');
            return false;
          }
        })
      },
      //保存课程信息
      saveCourse(){
        course.saveCourseInfo(this.courseInfo)
          .then(response => {
            this.$message({
              type: 'success',
              message: '课程信息保存成功'
            })
            return response
          }).then(response => {
            this.$router.push({ path: '/edu/course/chapter/' + response.data.courseId })
          }).catch(error => {
            this.$message({
              type: 'error',
              message: '课程信息保存失败'
            })
          })
      },
      //修改课程信息
      updateCourse(id){
        course.updateCourseInfo(id,this.courseInfo)
          .then(() => {
            this.$message({
              type: 'success',
              message: '课程信息更新成功'
            })
          }).then(() => {
            this.$router.push({ path: '/edu/course/chapter/' + id })
          })
      },
      //上传图片前置判断
      beforeAvatarUpload(file){
        const isJPG = file.type === 'image/jpeg'
        const isLt2M = file.size / 1024 / 1024 < 2

        if (!isJPG) {
          this.$message.error('上传头像图片只能是 JPG 格式!')
        }
        if (!isLt2M) {
          this.$message.error('上传头像图片大小不能超过 2MB!')
        }
        return isJPG && isLt2M
      },
      //上传成功后
      handleAvatarSuccess(response){
        console.log(response.data.url)
        this.courseInfo.cover = response.data.url
        this.$message({
          type: 'success',
          message: '课程封面上传成功'
        })
      }

    }
  }
</script>

<style scoped>
.tinymce-container {
  line-height: 29px;
}
</style>
