<template>
  <div class="app-container" >
    <el-form label-width="150px" :model="bannerInfo" :rules="rules" ref="bannerInfo">
      <el-form-item label="banner标题" prop="title">
        <el-input v-model="bannerInfo.title"/>
      </el-form-item>
      <el-form-item label="banner链接地址" prop="linkUrl">
        <el-input v-model="bannerInfo.linkUrl"/>
      </el-form-item>
      <el-form-item label="banner排序" prop="sort">
        <el-input-number v-model="bannerInfo.sort" controls-position="right" :min="0"/>
      </el-form-item>

      <el-form-item label="banner图片">

        <el-upload
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload"
          :action="BASE_API+'/ossservice/file/upload?host=banner'"
          class="avatar-uploader">
          <img :src="bannerInfo.imageUrl" style="width: 250px;">
        </el-upload>

      </el-form-item>

      <el-form-item>
        <el-button :disabled="saveBtnDisabled" type="primary" @click="saveOrUpdate('bannerInfo')">保存</el-button>
      </el-form-item>

    </el-form>
  </div>
</template>

<script>
  import banner from '@/api/edu/banner'
  export default {
    data(){
      return {
        bannerInfo: {},
        BASE_API: process.env.BASE_API,
        OSS_PATH: process.env.OSS_PATH,
        saveBtnDisabled: false,
        rules: {
          title: [
             { required: true, message: '请输入banner名称', trigger: 'blur' }
          ],
          linkUrl: [
            { required: true, message: '请输入链接地址', trigger: 'blur' }
          ],
          sort: [
            { required: true, message: '请选择排序', trigger: 'blur' }
          ]
        }
      }
    },
    watch:{//监听路由是否变化
      $route(to, from){
        this.init()
      }
    },
    created() {
      console.log('save created')
      this.init()
    },
    methods:{
      init(){
        //路径中有ID 属性则为修改，否则为新增
        if(this.$route.params && this.$route.params.id){
          const id = this.$route.params.id
          this.getBanner(id)
          this.$message({
            type: 'info',
            message: '请修改信息'
          })
        }else{
          this.bannerInfo = { imageUrl: this.OSS_PATH + '/edu/banner/default.jpg' }
        }
      },
      getBanner(id){
        banner.getBannerById(id)
          .then(response => {
            this.bannerInfo = response.data.banner
          })
      },
      //图片上传前置操作
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
     //图片上传成功后
     handleAvatarSuccess(response){
       console.log(response.data.url)
       this.bannerInfo.imageUrl = response.data.url
       this.$message({
         type: 'success',
         message: 'banner图片上传成功'
       })
     },
     saveOrUpdate(formName){
       console.log('save submit')
       this.$refs[formName].validate((valid) => {
         if (valid) {
           this.saveBtnDisabled = true
           //路径中有ID 属性则为修改，否则为新增
           if(this.$route.params && this.$route.params.id){
             const id = this.$route.params.id
             console.log(id)
             this.updateBanner(id)
           }else{
             this.saveBanner()
           }
         } else {
           console.log('error submit!!');
           return false;
         }
       })
     },
     updateBanner(id){
       banner.updateBannerById(this.bannerInfo)
        .then(() => {
          this.$message({
            type: 'success',
            message: 'Banner信息更新成功'
          })
        }).then(() => {
            this.$router.push({ path: '/edu/banner/list'})
        })
     },
     saveBanner(){
       banner.saveBannerInfo(this.bannerInfo)
          .then(() => {
            this.$message({
              type: 'success',
              message: 'Banner信息保存成功'
            })
          }).then(() => {
              this.$router.push({ path: '/edu/banner/list'})
          })
     }
    }
  }
</script>

<style>
</style>
