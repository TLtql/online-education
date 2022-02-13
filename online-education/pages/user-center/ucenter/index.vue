<template>
  <div>
    <div class="head-table">我的信息</div>
    <div class="show">
      <p style="margin-left:6%;margin-top: 5%">昵称:<span style="margin-left: 3%;line-height:40px">{{loginInfo.nickname}}</span></p>
      <p style="margin-left:6%;margin-top: 5%">性别:
        <span style="margin-left: 3%;line-height:40px" v-if="loginInfo.sex == 2">
        男</span>
        <span style="margin-left: 3%;line-height:40px" v-if="loginInfo.sex == 1">
        女</span>
        <span style="margin-left: 3%;line-height:40px" v-if="!loginInfo.sex">
        暂无</span>
      </p>
      <p style="margin-left:6%;margin-top: 5%">年龄:
        <span style="margin-left: 3%;line-height:40px" v-if="loginInfo.age">
          {{loginInfo.age}}</span>
        <span style="margin-left: 3%;line-height:40px" v-if="!loginInfo.age">
          暂无</span>
      </p>
      <p style="margin-left:6%;margin-top: 5%" v-if="loginInfo.mobile">手机:
        <span style="margin-left: 3%;line-height:40px">{{loginInfo.mobile}}</span>
        <span style="margin-left: 3%;line-height:40px">
          <el-button type="text" icon="el-icon-edit" @click="updateMobileFormVisible = true,mobileSaveBtnDisabled = false">修改</el-button>
        </span>
      </p>
      <p style="margin-left:6%;margin-top: 5%" v-if="!loginInfo.mobile">手机:
        <span style="margin-left: 3%;line-height:40px">暂无</span>
        <span style="margin-left: 3%;line-height:40px">
          <el-button type="text" icon="el-icon-edit" @click="updateMobileFormVisible = true,mobileSaveBtnDisabled = false">绑定手机号</el-button>
        </span>
      </p>
      <p style="margin-left:6%;margin-top: 5%">签名:
        <span style="margin-left: 3%;line-height:40px" v-if="loginInfo.sign">
          {{loginInfo.sign}}</span>
        <span style="margin-left: 3%;line-height:40px" v-if="!loginInfo.sign">
          暂无个性签名</span>
      </p>
      <p style="margin-left:6%;margin-top: 10px">
        <el-button type="primary" icon="el-icon-edit" @click="showUserForm()">编辑个人信息
        </el-button>
      </p>
    </div>
    <!-- 修改个人信息表单 -->
    <el-dialog :visible.sync="updateUserFormVisible" title="编辑个人信息" :before-close="cancelUpdateUserInfo">
      <el-form :model="newUserInfo" :rules="rules" ref="newUserInfo" label-width="120px">

        <!-- 用户头像 -->
        <el-form-item label="用户头像">
            <!-- 头衔缩略图 -->
            <pan-thumb :image="String(newUserInfo.avatar)"/>
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
                           :url="'http://localhost:9001'+'/ossservice/file/upload?host=user'"
                           field="file"
                           @close="close"
                           @crop-upload-success="cropSuccess"/>
        </el-form-item>

        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="newUserInfo.nickname"/>
        </el-form-item>

        <el-form-item label="性别">
          <el-radio-group v-model="newUserInfo.sex">
            <el-radio :label="2">男</el-radio>
            <el-radio :label="1">女</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="年龄">
          <el-input-number v-model="newUserInfo.age" :min="1" controls-position="right"/>
        </el-form-item>

        <el-form-item label="签名">
          <el-input v-model="newUserInfo.sign"/>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="updateUserFormVisible = false,newUserInfo = {}">取 消</el-button>
        <el-button :disabled="saveBtnDisabled" type="primary" @click="updateUserInfo('newUserInfo')">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 修改手机号表单 -->
    <el-dialog :visible.sync="updateMobileFormVisible" title="修改绑定手机号" width="25%" :before-close="cancelUpdateMobile">
      <div class="sign-up-container">
        <el-form ref="userForm" :model="mobileForm">
          <el-form-item prop="mobile" :rules="[{ required: true, message: '请输入手机号码', trigger: 'blur' },{validator: checkPhone, trigger: 'blur'}]">
            <div>
              <el-input type="text" prefix-icon="el-icon-mobile-phone" placeholder="手机号" v-model="mobileForm.mobile"/>
            </div>
          </el-form-item>

          <el-form-item prop="code" :rules="[{ required: true, message: '请输入验证码', trigger: 'blur' }]">
            <div style="width: 100%;display: block;float: left;position: relative">
              <el-input type="text" prefix-icon="el-icon-key" placeholder="验证码" v-model="mobileForm.code"/>
              <i class="iconfont icon-phone"/>
            </div>
            <div class="btn" style="position:absolute;right: 0px;top: 2px;width: 28%;">
              <a href="javascript:" type="button" @click="getCodeFun()" :value="codeTest" style="border: none;background-color: none">{{codeTest}}</a>
            </div>
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
          <el-button @click="updateMobileFormVisible = false,mobileForm = {}">取 消</el-button>
          <el-button :disabled="mobileSaveBtnDisabled" type="primary" @click="editMobile">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
//还是等跳转到这里的时候再来加载数据好一点
import ImageCropper from '@/components/ImageCropper'
import PanThumb from '@/components/PanThumb'


import loginApi from '@/api/login'
import cookie from 'js-cookie'
import userApi from '@/api/user'
import registerApi from '@/api/register'
export default {
  components: {ImageCropper, PanThumb},
  data(){
    return{
      loginInfo: {},
      newUserInfo: {},
      updateUserFormVisible: false,
      updateMobileFormVisible: false,
      imagecropperShow: false,//上传弹窗是否显示
      imagecropperKey: 0,//组件唯一标识
      saveBtnDisabled: false,
      mobileSaveBtnDisabled: false,
      isExist: '', //手机号是否被注册
      sending: true,    //是否发送验证码
      second: 60,        //倒计时间
      codeTest: '获取验证码',
      mobileForm: {},
      rules: {
        nickname: [
          { required: true, message: '请输入昵称', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.init()
  },
  methods:{
    init(){
      var jsonStr = cookie.get('user_info')
      if(jsonStr){
        this.loginInfo = JSON.parse(jsonStr)
      }
    },
    showUserForm(){
      this.saveBtnDisabled = false
      this.updateUserFormVisible = true
      this.newUserInfo = { ...this.loginInfo }
    },
    updateUserInfo(formName){
      this.saveBtnDisabled = true
      this.$refs[formName].validate((valid) => {
        if (valid) {
            userApi.updateUserById(this.newUserInfo)
              .then(() => {
                this.updateUserFormVisible = false
                this.$message({
                  type: 'success',
                  message: '修改信息成功'
                })
                location.reload()
              })

        } else {
            console.log('error submit!!');
            return false;
        }
      })
    },
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
      this.newUserInfo.avatar = data.data.url
      this.$message({
        type: 'success',
        message: '图片上传成功！'
      })
    },
    getCodeFun(){
      if (!this.sending)
        return
      this.$refs.userForm.validateField('mobile', (errMsg) => {
        if (errMsg == '') {
          registerApi.getMobile(this.mobileForm.mobile)
            .then(res => {
              this.sending = false
              this.timeDown()
            })
        }
      })
    },
    // 倒计时
    timeDown() {
      let result = setInterval(() => {
        --this.second
        this.codeTest = this.second
        if (this.second < 1) {
          clearInterval(result)
          this.sending = true
          //this.disabled = false;
          this.second = 60
          this.codeTest = "获取验证码"
        }
      }, 1000)
    },
    checkPhone (rule, value, callback) {
      //debugger
      if (!(/^1[34578]\d{9}$/.test(value))) {
        return callback(new Error('手机号码格式不正确'))
      }

      registerApi.existMobile(this.mobileForm.mobile)
        .then(response => {
          this.isExist = response.data.data.exist
        }).then(() => {
          if(this.isExist){
            return callback(new Error('手机号已被注册'))
          } else {
            return callback()
          }
        })
    },
    editMobile(){
      this.mobileSaveBtnDisabled = true
      userApi.updateMobile(this.mobileForm.mobile, this.mobileForm.code)
        .then(() => {
          this.updateMobileFormVisible = false
          this.$message({
            type: 'success',
            message: '修改信息成功'
          })
          location.reload()
        })
    },
    cancelUpdateMobile(){
      // 清除检验消息
      this.$refs['userForm'].clearValidate('mobile')
      this.$refs['userForm'].clearValidate('code')
    },
    cancelUpdateUserInfo(){
      this.$refs['newUserInfo'].clearValidate('nickname')
    }
  }
}
</script>

<style scoped>
.show{
  background-color: white;
  margin: 10px auto;
  width: 80%;
  height: 480px;
  border: 5px solid rgba(0, 0, 0, 0.4);
  transition: all 0.9s;
  border-radius: 10px;

}
.head-table{
  font-family: 楷体;
  text-align: center;
  margin-top: 20px;
  font-size: 30px;
}

/* .show:hover{
  box-shadow: 0px 15px 30px rgba(0, 0, 0, 0.4);
  margin-top: 90px;
} */
</style>
