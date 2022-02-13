<template>
  <div class="app-container" >
      <el-form label-width="120px" :model="teacher" :rules="rules" ref="teacher">
        <el-form-item label="讲师名称" prop="name">
          <el-input v-model="teacher.name"/>
        </el-form-item>
        <el-form-item label="讲师排序">
          <el-input-number v-model="teacher.sort" controls-position="right" :min="0"/>
        </el-form-item>
        <el-form-item label="讲师头衔" prop="level">
          <el-select v-model="teacher.level" clearable placeholder="请选择">
            <!--
              数据类型一定要和取出的json中的一致，否则没法回填
              因此，这里value使用动态绑定的值，保证其数据类型是number
            -->
            <el-option :value="1" label="高级讲师"/>
            <el-option :value="2" label="首席讲师"/>
          </el-select>
        </el-form-item>
        <el-form-item label="讲师资历" prop="career">
          <el-input v-model="teacher.career"/>
        </el-form-item>
        <el-form-item label="讲师简介" prop="intro">
          <el-input v-model="teacher.intro" :rows="10" type="textarea"/>
        </el-form-item>

        <!-- 讲师头像：TODO -->
        <!-- 讲师头像 -->
        <el-form-item label="讲师头像">
            <!-- 头衔缩略图 -->
            <pan-thumb :image="String(teacher.avatar)"/>
            <!-- 文件上传按钮 -->
            <el-button type="primary" icon="el-icon-upload" @click="imagecropperShow=true">更换头像
            </el-button>
            <!--
            v-show：是否显示上传组件
            :key：类似于id，如果一个页面多个图片上传控件，可以做区分
            :url：后台上传的url地址
            @close：关闭上传组件
            @crop-upload-success：上传成功后的回调 -->
            <image-cropper
                           v-show="imagecropperShow"
                           :width="300"
                           :height="300"
                           :key="imagecropperKey"
                           :url="BASE_API+'/ossservice/file/upload?host=teacher'"
                           field="file"
                           @close="close"
                           @crop-upload-success="cropSuccess"/>
        </el-form-item>

        <el-form-item>
          <el-button :disabled="saveBtnDisabled" type="primary" @click="saveOrUpdate('teacher')">保存</el-button>
        </el-form-item>
      </el-form>
    </div>
</template>

<script>
import teacher from '@/api/edu/teacher'
import ImageCropper from '@/components/ImageCropper'
import PanThumb from '@/components/PanThumb'

export default {
  components: {ImageCropper, PanThumb},
  name: "save",
  data(){
    return {
      teacher: {
        sort: 1
      },
      saveBtnDisabled: false,//保存按钮是否禁用，避免表单重复提交
      imagecropperShow: false,//上传弹窗是否显示
      imagecropperKey: 0,//组件唯一标识
      BASE_API: process.env.BASE_API,
      OSS_PATH: process.env.OSS_PATH,
      rules: {
        name: [
          { required: true, message: '请输入讲师姓名', trigger: 'blur' },
        ],
        level: [
          { required: true, message: '请选择讲师头衔', trigger: 'change' }
        ],
        career: [
          { required: true, message: '请输入讲师资历', trigger: 'blur' }
        ],
        intro: [
          { required: false, message: '建议填写讲师简介，让学生更了解讲师', trigger: 'blur' }
        ]
      }
    }
  },
  created(){
    this.init()
  },
  watch:{//监听路由是否变化
    $route(to, from){
      this.init()
    }
  },
  methods:{
    //文件上传关闭执行方法
    close(){
      this.imagecropperShow = false
      this.imagecropperKey = this.imagecropperKey + 1
      this.$message({
        message: '已取消上传'
      })
    },
    cropSuccess(data){
      this.imagecropperShow = false
      this.imagecropperKey = this.imagecropperKey + 1
      this.teacher.avatar = data.url
      this.$message({
        type: 'success',
        message: '图片上传成功！'
      })
    },
    saveOrUpdate(formName){
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.saveBtnDisabled = true
          if(!this.teacher.id){
            this.saveData()
          } else{
            this.update()
          }
        } else {
          console.log('error submit!!');
          return false;
        }
      })
    },
    init(){
      //路径中有ID 属性则为修改，否则为新增
      if(this.$route.params && this.$route.params.id){
        const id = this.$route.params.id
        this.getTeacher(id)
      }else{
        this.teacher = {sort: 1, avatar: this.OSS_PATH + '/edu/default.jpg'}
      }
    },
    saveData(){
      teacher.saveTeacher(this.teacher)
      .then(() => {
        return this.$message({
          type: 'success',
          message: '保存成功！'
        })
      }).then(() => {
        this.$router.push({path: '/edu/teacher/table'})
      })
    },
    getTeacher(id){
      teacher.getTeacherbyId(id)
        .then(response => {
          this.teacher=response.data.teacher
        })
    },
    update(){
      teacher.updateTeacher(this.teacher)
        .then(() => {
          return this.$message({
              type: 'success',
              message: '修改成功！'
            })
          }).then(() => {
            this.$router.push({path: '/edu/teacher/table'})
          })
        }

    }
}


</script>

<style>
</style>
