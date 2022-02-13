<template>
  <div class="main">
    <div class="title">
      <a class="active" href="/login">登录</a>
      <span>·</span>
      <a href="/sign/register">注册</a>
    </div>

    <div class="sign-up-container">
      <el-form ref="userForm" :model="user">

        <el-form-item class="input-prepend restyle" prop="mobile" :rules="[{ required: true, message: '请输入手机号码', trigger: 'blur' },{validator: checkPhone, trigger: 'blur'}]">
          <div >
            <el-input type="text" placeholder="手机号" v-model="user.mobile"/>
            <i class="iconfont icon-phone" />
          </div>
        </el-form-item>

        <el-form-item class="input-prepend" prop="password" :rules="[{ required: true, message: '请输入密码', trigger: 'blur' }]">
          <div>
            <el-input type="password" placeholder="密码" v-model="user.password"/>
            <i class="iconfont icon-password"/>
          </div>
        </el-form-item>

        <div class="btn">
          <input type="button" class="sign-in-button" value="登录" @click="submitLogin()">
        </div>
      </el-form>
      <!-- 更多登录方式 -->
      <div class="more-sign">
        <h6>社交帐号登录</h6>
        <ul>
          <li><a id="weixin" class="weixin" href="javascript:void(0);" @click="wxLogin()"><i class="iconfont icon-weixin"/></a></li>
        </ul>
      </div>
    </div>


    <el-dialog
      title="微信账号登录"
      :visible.sync="centerDialogVisible"
      width="50%"
      :before-close="cancelLogin"
      center>
      <div class="select-button" v-if="!wxcodeLogin&&!officialLogin">
        <h6 style="text-align: center;">注意:三种方式登录的账号为独立账号，账号消息不互通</h6>
        <div class="wxlogin-button">
          <input type="button" class="sign-up-button" value="微信扫码登录" @click="onWxcodeLogin()"/>
          <input type="button" class="sign-in-button" value="公众号动态码登录" @click="onOfficialLogin()"/>
        </div>
      </div>
      <div class="official-login" v-if="officialLogin">
        <div class="official-img">
          <img src="https://edu-tianle.oss-cn-beijing.aliyuncs.com/%E4%B8%AA%E4%BA%BA%E5%85%AC%E4%BC%97%E5%8F%B7.jpg"/>
        </div>
        <h5 style="text-align: center;font-size: 18px;">请使用微信扫码关注公众号，回复关键字“登录”获取动态码</h5>
        <form action="#" method="post" style="margin-top: 20px;">
          <label class="submit-code" >
            <span style="font-size: 15px;">请输入动态码:</span>
            <input type="text" placeholder="动态码" v-model="officialCode" value >
            <span>
              <el-button @click="officialCodeLogin()">登录</el-button>
            </span>
          </label>
        </form>
      </div>
      <div class="wxcode-login" v-show="wxcodeLogin">
        <div id="wx_login"></div>
      </div>
    </el-dialog>

  </div>
</template>

<script>
  import '~/assets/css/sign.css'
  import '~/assets/css/iconfont.css'
  import cookie from 'js-cookie'
  import loginApi from '@/api/login'

  export default {
    layout: 'sign',

    data () {
      return {
        user:{
          mobile:'',
          password:''
        },
        loginInfo:{},
        centerDialogVisible: false,
        wxcodeLogin: false,
        officialLogin: false,
        officialCode: ''
      }
    },
    methods: {
      submitLogin(){
        loginApi.userLogin(this.user)
          .then(response => {
            // 把token 放到cookie中
            cookie.set("user_token", response.data.data.token, {domain:'localhost'})
            // 根据token 获取用户信息
            loginApi.getUserInfo()
              .then(response => {
                this.loginInfo = response.data.data.user
                // 把用户信息 放到cookie中
                cookie.set("user_info", JSON.stringify(this.loginInfo), {domain:'localhost'})
                this.$message({
                  type: 'success',
                  message: "登录成功"
                })
                // 跳转到首页面
                this.$router.push({path: '/'})
              })
          })
      },
      checkPhone (rule, value, callback) {
        //debugger
        if (!(/^1[34578]\d{9}$/.test(value))) {
          return callback(new Error('手机号码格式不正确'))
        }
        return callback()
      },
      cancelLogin(){
        this.officialLogin = false
        this.wxcodeLogin = false
        this.centerDialogVisible = false
      },
      wxLogin(){
        this.centerDialogVisible = true
        this.$refs['userForm'].clearValidate('mobile')
        this.$refs['userForm'].clearValidate('password')
      },
      onOfficialLogin(){
        this.officialLogin = true
      },
      onWxcodeLogin(){
        this.wxcodeLogin = true
        var obj = new WxLogin({
        	self_redirect:false,
        	id:"wx_login",
        	appid: "wxed9954c01bb89b47",
        	scope: "snsapi_login",
        	state: "state",
        	redirect_uri: encodeURIComponent("http://localhost:8160/api/user/wx/callback")
        });
      },
      // 公众号动态码登录
      officialCodeLogin(){
        if (this.officialCode){
          loginApi.userOfficialLogin(this.officialCode)
            .then(response => {
              // 把token 放到cookie中
              cookie.set("user_token", response.data.data.token, {domain:'localhost'})
              // 根据token 获取用户信息
              loginApi.getUserInfo()
                .then(response => {
                  this.loginInfo = response.data.data.user
                  // 把用户信息 放到cookie中
                  cookie.set("user_info", JSON.stringify(this.loginInfo), {domain:'localhost'})
                  this.$message({
                    type: 'success',
                    message: "登录成功"
                  })
                  // 跳转到首页面
                  this.$router.push({path: '/'})
                })
            })
        } else {
          this.$message({
            type: 'error',
            message: '请输入动态码'
          })
        }
      }
    }
  }
</script>
<style scoped>
   .el-form-item__error{
    z-index: 9999999;
  }
  .wxlogin-button{
    width: 50%;
    margin-left: auto;
    margin-right:auto;
    margin-top: 40px;
  }
  .select-button{
    height: 400px;
  }
  .official-login{
    height: 400px;
  }
  .official-img img{
    margin-left: 25%;
    width: 50%;
  }

  .submit-code{
    margin-left: 22%;
  }

  .submit-code input {
    width: 25%;
    height: 35px;
    padding: 4px 12px 4px 15px;
    border: 1px solid #c8c8c8;
    background-color: #F8F8F8;
  }
  #wx_login {
    margin-left: 25%;
  }
  .weixin{
    margin-left: 30px;
  }


</style>
