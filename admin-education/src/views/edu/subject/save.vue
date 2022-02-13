<template>
    <div class="app-container">
      <el-form label-width="120px">
        <el-form-item label="信息描述">
          <el-tag type="info">excel模版说明</el-tag>
          <el-tag>
            <i class="el-icon-download"/>
            <a :href="OSS_PATH + '/edu/课程分类模板.xlsx'">点击下载模版</a>
          </el-tag>

        </el-form-item>

        <el-form-item label="选择Excel">
          <el-upload
            ref="upload"
            :auto-upload="false"
            :on-success="fileUploadSuccess"
            :on-error="fileUploadError"
            :disabled="importBtnDisabled"
            :limit="1"
            :action="BASE_API+'/eduservice/edu-subject/addSubject'"
            name="file"
            accept="application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet">
            <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
            <el-button
              :loading="loading"
              style="margin-left: 10px;"
              size="small"
              type="success"
              :disabled="importBtnDisabled"
              @click="submitUpload">{{ fileUploadBtnText }}</el-button>
            <div slot="tip" class="el-upload__tip">只能上传Excel文件</div>
          </el-upload>
        </el-form-item>
      </el-form>
    </div>
</template>

<script>
  export default {
    data() {
      return {
        BASE_API: process.env.BASE_API, // 接口API地址
        OSS_PATH: process.env.OSS_PATH, // 阿里云OSS地址
        fileUploadBtnText: '上传到服务器', // 按钮文字
        importBtnDisabled: false, // 按钮是否禁用,
        loading: false
      }
    },
    created() {

    },
    methods:{
      fileUploadSuccess(response){
        if(response.success = true){
          this.fileUploadBtnText = "上传成功"
          this.loading = false
          this.$message({
            type: 'success',
            message: '课程分类添加成功'
          })
          this.$router.push({path : '/edu/subject/list'})
        }
      },
      fileUploadError(response){
          this.fileUploadBtnText = "上传失败,请刷新重新上传"
          this.loading = false
          this.$message({
            type: 'error',
            message: '课程分类添加失败'
          })
          this.importBtnDisabled = false
      },
      submitUpload(){
        this.loading = true
        this.fileUploadBtnText = "正在上传.."
        this.importBtnDisabled = true
        this.$refs.upload.submit()
      }
    }
  }
</script>

<style>
</style>
