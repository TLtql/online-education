<template>
<div class="bg-fa of">

  <el-container style="height: 800px; border: 1px solid #eee;">

    <el-aside width="200px" style="margin-top: 1%" >
      <div style="width: 190px;padding: 5px;margin: auto">
        <img :src="loginInfo.avatar" width="150px" height="150px" style="border-radius: 300px;margin-left: 8%">
        <br>
        <br>
        <p style="margin:0 auto;text-align: center" >{{loginInfo.nickname}}</p>
        <p style="margin:0 auto;text-align: center" >我的积分 {{loginInfo.point}}
            <el-button type="text" icon="el-icon-edit" @click="goPay()">充值</el-button>
        </p>
      </div>
      <br>
      <el-menu >
        <el-submenu index="1">
         <template slot="title"><i class="el-icon-message"></i>用户中心</template>
          <el-menu-item-group>

            <nuxt-link class="alink" to="/user-center/ucenter"><el-menu-item index="1-1">个人信息</el-menu-item></nuxt-link>
            <nuxt-link class="alink" to="/user-center/ucenter/order"><el-menu-item index="1-2">我的订单</el-menu-item></nuxt-link>
            <nuxt-link class="alink" to="/user-center/ucenter/collect"><el-menu-item index="1-3">我的收藏</el-menu-item></nuxt-link>
            <nuxt-link class="alink" to="/user-center/ucenter/comment"><el-menu-item index="1-4">我的评论</el-menu-item></nuxt-link>
            <nuxt-link class="alink" to="/user-center/ucenter/points"><el-menu-item index="1-5">积分充值</el-menu-item></nuxt-link>
            <a @click.prevent="removeUser()" style="text-decoration: none;" title="用户注销" ><el-menu-item index="1-6">用户注销</el-menu-item></a>
          </el-menu-item-group>

        </el-submenu>
        <el-submenu index="2">
          <template slot="title"><i class="el-icon-menu"></i>博客管理</template>
          <el-menu-item-group>
            <nuxt-link class="alink" to="/ucenter/bokelist"><el-menu-item index="2-1">博客列表</el-menu-item></nuxt-link>
            <nuxt-link class="alink" to="/ucenter/wirterboke"><el-menu-item index="2-2">书写文章</el-menu-item></nuxt-link>
          </el-menu-item-group>

        </el-submenu>


      </el-menu>
    </el-aside>


    <el-container >
      <el-main>
        <nuxt/>
      </el-main>
    </el-container>



  </el-container>

</div>
</template>

<script>
import cookie from 'js-cookie'
import loginApi from '@/api/login'
import userApi from '@/api/user'
export default {

  data() {
    return {
      loginInfo:{}
    }
  },
  created() {
    this.init()
  },
  methods:{
    init(){
      var jsonStr = cookie.get('user_info')
      if(jsonStr){
        this.getLoginInfo()
      } else {
        this.$router.push({path: '/sign/login'})
        this.$message({
          type: 'warning',
          message: '请先登录'
        })
      }
    },
    getLoginInfo(){
      loginApi.getUserInfo()
        .then(response => {
            this.loginInfo = response.data.data.user
            // 把用户信息 放到cookie中
            cookie.set("user_info", JSON.stringify(this.loginInfo), {domain:'localhost'})
      })
    },
    goPay(){
      this.$router.push({path: '/user-center/ucenter/points'})
      this.$message({
        type: 'info',
        message: '请选择充值金额'
      })
    },
    removeUser(){
      this.$confirm('此操作将会移除您的所有信息(包括订单，评论等),无法恢复,是否继续?', '提示', {
             confirmButtonText: '确定',
             cancelButtonText: '取消',
             type: 'warning'
         }).then(() => {
             return userApi.deleteUser()
         }).then(() => {
            cookie.set('user_token', "", {domain: 'localhost'})
            cookie.set('user_info', "", {domain: 'localhost'})
            this.$message({
              type: 'success',
              message: '用户注销成功'
            })
            setTimeout(() => {
              //跳转页面
              window.location.href = "/"
            },1000)
         }).catch((response) => { // 失败
             if (response === 'cancel') {
                 this.$message({
                     type: 'info',
                     message: '已取消操作'
                 })
             }
         })
    }
  }

}
</script>

<style scoped>
.el-header {
  background-color: #B3C0D1;
  color: #333;
  line-height: 60px;
}

.el-aside {
  color: #333;
}
.alink{

  text-decoration: none;
}
</style>
