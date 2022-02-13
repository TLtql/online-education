<template>
  <client-only>
    <div class="in-wrap">
      <!-- 公共头引入 -->
      <header id="header">
        <section class="container">
          <h1 id="logo">
            <a href="/" title="首页">
              <img src="~/assets/img/logo.png" width="100%" alt="首页">
            </a>
          </h1>
          <div class="h-r-nsl">
            <ul class="nav">
              <router-link to="/" tag="li" active-class="current" exact>
                <a>首页</a>
              </router-link>
              <router-link to="/course" tag="li" active-class="current">
                <a>课程</a>
              </router-link>
              <router-link to="/teacher" tag="li" active-class="current">
                <a>名师</a>
              </router-link>
              <!-- <router-link to="/article" tag="li" active-class="current">
                <a>文章</a>
              </router-link>
              <router-link to="/qa" tag="li" active-class="current">
                <a>问答</a>
              </router-link> -->
            </ul>
            <!-- / nav -->
            <ul class="h-r-login">
                <li v-if="!loginInfo.id" id="no-login">
                    <a href="/sign/login" title="登录">
                        <em class="icon18 login-icon">&nbsp;</em>
                        <span class="vam ml5">登录</span>
                    </a>
                    |
                    <a href="/sign/register" title="注册">
                        <span class="vam ml5">注册</span>
                    </a>
                </li>
                <li v-if="loginInfo.id" id="is-login-one" class="mr10">
                    <a id="headerMsgCountId" href="#" title="消息">
                        <em class="icon18 news-icon">&nbsp;</em>
                    </a>
                    <q class="red-point" style="display: none">&nbsp;</q>
                </li>
                <li v-if="loginInfo.id && isSignIn == false" class="h-r-user is-login-three">
                  <a title="每日签到" id="signIn" href="javascript:void(0);" @click="signIn()">
                    <span id="userName" class="vam disIb">签到</span>
                  </a>
                </li>
                <li v-if="loginInfo.id && isSignIn == true" class="h-r-user is-login-three">
                  <a title="每日签到" id="signIn" href="javascript:void(0);" @click="signIn">
                    <span id="userName" class="vam disIb">已签到</span>
                  </a>
                </li>
                <li v-if="loginInfo.id" id="is-login-two" class="h-r-user">
                    <a href="/user-center/ucenter" title="个人中心">
                        <img
                             :src="loginInfo.avatar"
                             width="30"
                             height="30"
                             class="vam picImg"
                             alt
                             >
                        <span id="userName" class="vam">{{ loginInfo.nickname }}</span>
                    </a>
                    <!-- 分割线 -->
                    <span style="display: inline-block;
                                  height: 24px;
                                  width: 2px;
                                  margin: 0 1px;
                                  vertical-align: middle;
                                  background-color: #737373;" >
                    </span>
                    <a href="javascript:void(0);" title="退出" @click="logout()" class="ml5">退出</a>
                </li>
                <!-- /未登录显示第1 li；登录后显示第2，3 li -->
            </ul>

          </div>
          <aside class="mw-nav-btn">
            <div class="mw-nav-icon"></div>
          </aside>
          <div class="clear"></div>
        </section>
      </header>
      <!-- /公共头引入 -->

      <nuxt/>

      <!-- 公共底引入 -->
      <footer id="footer">
        <section class="container">
          <div class>
            <h4 class="hLh30">
              <span class="fsize18 f-fM c-999">鸣谢</span>
            </h4>
            <div class="b-foot">
              <section class="fl col-7">
                <section class="mr20">
                  <section class="b-f-link">
                    <a href="http://www.hgnc.net" title="黄冈师范学院" target="_blank">黄冈师范学院</a>|
                    <a href="http://www.atguigu.com" title="尚硅谷" target="_blank">尚硅谷</a>|
                    <a href="https://gulixueyuan.com" title="谷粒学院" target="_blank">谷粒学院</a>|
                    <a href="#" title="源码地址" target="_blank">源码地址</a>|
                    <span>Email:1766630051@qq.com</span>
                  </section>
                </section>
              </section>
              <div class="clear"></div>
            </div>
          </div>
         <!-- <div class="b-foot">
            <section class="fl col-7">
              <section class="mr20">
                <section class="b-f-link">
                  <a href="#" title="关于我们" target="_blank">关于我们</a>|
                  <a href="#" title="联系我们" target="_blank">联系我们</a>|
                  <a href="#" title="帮助中心" target="_blank">帮助中心</a>|
                  <a href="#" title="资源下载" target="_blank">资源下载</a>|
                  <span>服务热线：010-56253825(北京) 0755-85293825(深圳)</span>
                  <span>Email：1766630051@qq.com</span>
                </section>
                <section class="b-f-link mt10">
                  <span>©2018课程版权均归谷粒学院所有 京ICP备17055252号</span>
                </section>
              </section>
            </section>
            <aside class="fl col-3 tac mt15">
              <section class="gf-tx">
                <span>
                  <img src="~/assets/img/wx-icon.png" alt>
                </span>
              </section>
              <section class="gf-tx">
                <span>
                  <img src="~/assets/img/wb-icon.png" alt>
                </span>
              </section>
            </aside>
            <div class="clear"></div>
          </div> -->
        </section>
      </footer>
      <!-- /公共底引入 -->
    </div>
  </client-only>
</template>
<script>
import '~/assets/css/reset.css'
import '~/assets/css/theme.css'
import '~/assets/css/global.css'
import '~/assets/css/web.css'
import '~/assets/css/base.css'
import '~/assets/css/activity_tab.css'
import '~/assets/css/bottom_rec.css'
import '~/assets/css/nice_select.css'
import '~/assets/css/order.css'
import '~/assets/css/swiper-3.3.1.min.css'
import "~/assets/css/pages-weixinpay.css"


import loginApi from '@/api/login'
import cookie from 'js-cookie'
import indexApi from '@/api/index.js'
export default {
  data(){
    return {
      token: '',
      loginInfo: {},
      isSignIn: false
    }
  },
  created(){
    this.init()
  },
  methods:{
    init(){
      // 如果请求路径中带有token 则为微扫码登录
      this.token = this.$route.query.token
      if (this.token) {
        this.wxLogin()
      }
      this.showInfo()
    },
    showInfo(){
      var jsonStr = cookie.get('user_info')
      console.log(jsonStr)
      if (jsonStr) {
        this.getLoginInfo()
        this.getSignInStatus()
      }
    },
    getSignInStatus(){
      indexApi.isUserSignIn()
        .then(response => {
          this.isSignIn = response.data.data.isSignIn
        })
    },
    getLoginInfo(){
      loginApi.getUserInfo()
        .then(response => {
            this.loginInfo = response.data.data.user
            // 把用户信息 放到cookie中
            cookie.set("user_info", JSON.stringify(this.loginInfo), {domain:'localhost'})
      })
    },
    logout(){
      //debugger
      cookie.set('user_token', "", {domain: 'localhost'})
      cookie.set('user_info', "", {domain: 'localhost'})
      this.$message({
        type: 'info',
        message: '您已登出，即将前往首页'
      })
      setTimeout(() => {
        //跳转页面
        window.location.href = "/"
      },2000)
    },
    wxLogin(){
      if (this.token == '') return
      cookie.set('user_token', this.token, {domain: 'localhost'})
      cookie.set('user_info', "", {domain: 'localhost'})
      loginApi.getUserInfo()
        .then(response => {
            this.loginInfo = response.data.data.user
            // 把用户信息 放到cookie中
            cookie.set("user_info", JSON.stringify(this.loginInfo), {domain:'localhost'})
            this.$message({
              type: 'success',
              message: "登录成功"
            })
        })

    },
    // 签到
    signIn(){
      indexApi.userSignIn(this.loginInfo.id)
        .then(response => {
          this.$message({
            type: 'success',
            message: '签到成功！获得'+response.data.data.point+'积分'
          })
          this.isSignIn = true
          // 刷新cookie中的信息
          this.loginInfo.point = response.data.data.point
          cookie.set("user_info", JSON.stringify(this.loginInfo), {domain:'localhost'})
        })
    }
  }

};
</script>
<style>
.is-login-three {
  height: 50px;
  width: 50px;
  text-align: center;
  background-color: #ff557f;
  border-radius: 10px;
}
.is-login-three span{
  color: white;
}
.is-login-three span:hover{
  color: #000000;
}
</style>
