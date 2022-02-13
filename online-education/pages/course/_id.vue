<template>
  <div id="aCoursesList" class="bg-fa of">
    <!-- /课程详情 开始 -->
    <section id="courseHead" class="container">
      <section class="path-wrap txtOf hLh30">
        <a href="/" title class="c-999 fsize14">首页</a>
        \
        <a href="/course" title class="c-999 fsize14">课程列表</a>
        \
        <span class="c-333 fsize14">{{ courseInfo.subjectLevelOne }}</span>
        \
        <span class="c-333 fsize14">{{ courseInfo.subjectLevelTwo }}</span>
      </section>
      <div>
        <article class="c-v-pic-wrap" style="height: 357px;">
          <section class="p-h-video-box" id="videoPlay">
            <img :src="courseInfo.cover" :alt="courseInfo.title" class="dis c-v-pic">
          </section>
        </article>
        <aside class="c-attr-wrap">
          <section class="ml20 mr15">
            <h2 class="hLh30 txtOf mt15">
              <span class="c-fff fsize24">{{courseInfo.title}}</span>
            </h2>
            <section class="c-attr-jg">
              <span class="c-fff">价格：</span>
              <b class="c-yellow" style="font-size:24px;" v-if="courseInfo.price == 0">免费</b>
              <b class="c-yellow" style="font-size:24px;" v-if="courseInfo.price > 0">￥{{courseInfo.price}}</b>
            </section>
            <section class="c-attr-mt c-attr-undis">
              <span class="c-fff fsize14">主讲： {{courseInfo.teacherName}}&nbsp;&nbsp;&nbsp;</span>
            </section>
            <section class="c-attr-mt of">
              <span class="ml10 vam" v-if="!isCollect">
                <em class="icon18 scIcon"></em>
                <a class="c-fff vam" title="收藏" href="#" @click.prevent="addUserCollect()">收藏</a>
              </span>
              <span class="ml10 vam" v-if="isCollect">
                <em class="icon18 scIcon" style="color: red;"></em>
                <a class="c-fff vam" title="取消收藏" @click.prevent="removeCollect()" >已收藏</a>
              </span>
            </section>
            <section class="c-attr-mt" v-if="courseInfo.price == 0">
              <a href="#chapter" title="立即观看" class="comm-btn c-btn-3">立即观看</a>
            </section>
            <section class="c-attr-mt" v-if="courseInfo.price > 0 && isBuyCourse == false">
              <a href="#" @click.prevent="buyCourse()" title="立即购买" class="comm-btn c-btn-3">立即购买</a>
            </section>
            <section class="c-attr-mt" v-if="courseInfo.price > 0 && isBuyCourse == true">
              <a href="#chapter" title="立即观看" class="comm-btn c-btn-3">已购买,点击观看</a>
            </section>
          </section>
        </aside>
        <aside class="thr-attr-box">
          <ol class="thr-attr-ol">
            <li>
              <p>&nbsp;</p>
              <aside>
                <span class="c-fff f-fM">购买数</span>
                <br>
                <h6 class="c-fff f-fM mt10">{{courseInfo.buyCount}}</h6>
              </aside>
            </li>
            <li>
              <p>&nbsp;</p>
              <aside>
                <span class="c-fff f-fM">课时数</span>
                <br>
                <h6 class="c-fff f-fM mt10">{{courseInfo.lessonNum}}</h6>
              </aside>
            </li>
            <li>
              <p>&nbsp;</p>
              <aside>
                <span class="c-fff f-fM">浏览数</span>
                <br>
                <h6 class="c-fff f-fM mt10">{{courseInfo.viewCount}}</h6>
              </aside>
            </li>
          </ol>
        </aside>
        <div class="clear"></div>
      </div>
      <!-- /课程封面介绍 -->
      <div class="mt20 c-infor-box">
        <article class="fl col-7">
          <section class="mr30">
            <div id="chapter" class="i-box">
              <div>
                <section id="c-i-tabTitle" class="c-infor-tabTitle c-tab-title">
                  <a name="c-i" class="current" title="课程详情">课程详情</a>
                </section>
              </div>
              <article class="ml10 mr10 pt20">
                <div>
                  <h6 class="c-i-content c-infor-title">
                    <span>课程介绍</span>
                  </h6>
                  <div class="course-txt-body-wrap">
                    <section class="course-txt-body">
                      <p v-html="courseInfo.description">{{ courseInfo.description }}</p>
                    </section>
                  </div>
                </div>
                <!-- /课程介绍 -->
                <div  class="mt50">
                  <h6 class="c-g-content c-infor-title">
                    <span>课程大纲</span>
                  </h6>
                  <section class="mt20">
                    <div class="lh-menu-wrap">
                      <menu id="lh-menu" class="lh-menu mt10 mr10">
                        <ul>
                          <!-- 文件目录 -->
                          <li class="lh-menu-stair" v-for="subjectOne in subjectList" :key="subjectOne.id">
                            <a href="javascript: void(0)" :title="subjectOne.title" class="current-1">
                              <em class="lh-menu-i-1 icon18 mr10"></em>{{subjectOne.title}}
                            </a>
                            
                            <!-- 课程免费，所有视屏皆可观看-->
                            <ol class="lh-menu-ol" style="display: block;" v-if="courseInfo.price === 0">
                              <li class="lh-menu-second ml30" v-for="subjectTwo in subjectOne.children" :key="subjectTwo.id">
                                <a
                                 :href="'/player/'+subjectTwo.videoSourceId"
                                 :title="subjectTwo.title"
                                 target="_blank">
                                  <span class="fr">
                                    <i class="free-icon vam mr10">免费</i>
                                  </span>
                                  <em class="lh-menu-i-2 icon16 mr5">&nbsp;</em>{{subjectTwo.title}}
                                </a>
                              </li>
                            </ol>
                            <!-- 课程收费，部分视屏或者购买完成后可观看-->
                            <ol class="lh-menu-ol" style="display: block;" v-if="courseInfo.price > 0">
                              <li class="lh-menu-second ml30" v-for="subjectTwo in subjectOne.children" :key="subjectTwo.id">
                                <a
                                 :href="'/player/'+subjectTwo.videoSourceId"
                                 :title="subjectTwo.title"
                                 target="_blank"
                                 v-if="subjectTwo.free === true">
                                  <span class="fr">
                                    <i class="free-icon vam mr10" v-if="subjectTwo.free === true">免费试听</i>
                                  </span>
                                  <em class="lh-menu-i-2 icon16 mr5">&nbsp;</em>{{subjectTwo.title}}
                                </a>
                                <a
                                 href="#courseHead"
                                 @click="showBuyMessage()"
                                 v-if="subjectTwo.free === false && isBuyCourse === false">
                                  <em class="lh-menu-i-2 icon16 mr5">&nbsp;</em>{{subjectTwo.title}}
                                </a>
                                <a
                                 :href="'/player/'+subjectTwo.videoSourceId"
                                 :title="subjectTwo.title"
                                 target="_blank"
                                 v-if="isBuyCourse === true && subjectTwo.free === false">
                                  <span class="fr">
                                    <i class="free-icon vam mr10" v-if="subjectTwo.free === true">免费试听</i>
                                  </span>
                                  <em class="lh-menu-i-2 icon16 mr5">&nbsp;</em>{{subjectTwo.title}}
                                </a>
                              </li>
                            </ol>
                          </li>
                        </ul>
                      </menu>
                    </div>
                  </section>
                </div>
                <!-- /课程大纲 -->
              </article>
            </div>
          </section>
        </article>
        <aside class="fl col-3">
          <div class="i-box">
            <div>
              <section class="c-infor-tabTitle c-tab-title">
                <a title href="javascript:void(0)">主讲讲师</a>
              </section>
              <section class="stud-act-list">
                <ul style="height: auto;">
                  <li>
                    <div class="u-face">
                      <a :href="'/teacher/'+courseInfo.teacherId">
                        <img :src="courseInfo.avatar" width="50" height="50" :alt="courseInfo.teacherName">
                      </a>
                    </div>
                    <section class="hLh30 txtOf">
                      <a class="c-333 fsize16 fl" :href="'/teacher/'+courseInfo.teacherId">{{courseInfo.teacherName}}</a>
                    </section>
                    <section class="hLh20 txtOf">
                      <span class="c-999">{{courseInfo.career}}</span>
                    </section>
                  </li>
                </ul>
              </section>
            </div>
          </div>
        </aside>
        <div class="clear"></div>
      </div>
    </section>
    <!-- /课程详情 结束 -->
    <div class="mt50 commentHtml"><div>
          <h6 class="c-c-content c-infor-title" id="i-art-comment">
            <span class="commentTitle">课程评论</span>
          </h6>
          <section class="lh-bj-list pr mt20 replyhtml">
            <ul>
              <li class="unBr">
                <aside class="noter-pic" v-if="!loginInfo.id">
                  <img width="50" height="50" class="picImg" src="~/assets/img/avatar-boy.gif">
                  </aside>
                <aside class="noter-pic" v-if="loginInfo.id">
                  <img width="50" height="50" class="picImg" :src="loginInfo.avatar">
                  </aside>
                <div class="of">
                  <section class="n-reply-wrap">
                    <fieldset v-if="loginInfo.id">
                      <textarea name="" v-model="comment.content" placeholder="输入您要评论的文字" id="commentContent"></textarea>
                    </fieldset>
                    <fieldset v-if="!loginInfo.id">
                      <textarea name="" v-model="comment.content" placeholder="登录后可进行评论" id="commentContent"></textarea>
                    </fieldset>
                    <p class="of mt5 tar pl10 pr10">
                      <span class="fl "><tt class="c-red commentContentmeg" style="display: none;"></tt></span>
                      <input type="button" @click="submitComment()" value="回复" class="lh-reply-btn">
                    </p>
                  </section>
                </div>
              </li>
            </ul>
          </section>
          <!-- /无数据提示 开始-->
          <section class="no-data-wrap" v-if="data.total == 0">
            <em class="icon30 no-data-ico">&nbsp;</em>
            <span class="c-666 fsize14 ml10 vam">没有相关评论，小编正在努力整理中...</span>
          </section>
          <!-- /无数据提示 结束-->
          <section class="">
              <section class="question-list lh-bj-list pr">
                <ul class="pr10">
                  <li v-for="(comment,index) in data.items" v-bind:key="index">
                      <aside class="noter-pic">
                        <img width="50" height="50" class="picImg" :src="comment.avatar">
                        </aside>
                      <div class="of">
                        <span class="fl">
                        <font class="fsize12 c-blue">
                          {{comment.nickname}}</font>
                        <font class="fsize12 c-999 ml5">评论：</font></span>
                      </div>
                      <div class="noter-txt mt5">
                        <p>{{comment.content}}</p>
                      </div>
                      <div class="of mt5">
                        <span class="fr"><font class="fsize12 c-999 ml5">{{comment.gmtCreate}}</font></span>
                      </div>
                    </li>

                  </ul>
              </section>
            </section>

            <!-- 公共分页 开始 -->
            <div class="paging">
                <!-- undisable这个class是否存在，取决于数据属性hasPrevious -->
                <a
                :class="{undisable: !data.hasPrevious}"
                href="#"
                title="首页"
                @click.prevent="getCommentData(1)">首</a>
                <a
                :class="{undisable: !data.hasPrevious}"
                href="#"
                title="前一页"
                @click.prevent="getCommentData(data.current-1)">&lt;</a>
                <a
                v-for="page in data.pages"
                :key="page"
                :class="{current: data.current == page, undisable: data.current == page}"
                :title="'第'+page+'页'"
                href="#"
                @click.prevent="getCommentData(page)">{{ page }}</a>
                <a
                :class="{undisable: !data.hasNext}"
                href="#"
                title="后一页"
                @click.prevent="getCommentData(data.current+1)">&gt;</a>
                <a
                :class="{undisable: !data.hasNext}"
                href="#"
                title="末页"
                @click.prevent="getCommentData(data.pages)">末</a>
                <div class="clear"/>
            </div>
            <!-- 公共分页 结束 -->
          </div>
        </div>
  </div>
</template>

<script>
import courseApi from '@/api/course'
import cookie from 'js-cookie'
import commentApi from '@/api/comment'
import orderApi from '@/api/order'
import collectApi from '@/api/collect'
export default {
  asyncData({ params, error }) {
      return courseApi.getCourseInfoById(params.id)
        .then(response => {
          console.log(response);
          return {
            courseInfo: response.data.data.courseInfo,
            subjectList: response.data.data.chapterVideos
          }
        })
    },
    data() {
      return{
        data: {},
        comment: {
          content: ''
        },
        loginInfo: {},
        page: 1,
        limit: 5,
        total: 0,
        isBuyCourse: false,
        userOrder: {},
        isCollect: false,
        userCollect: {}
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
        this.getCommentData()
        if(this.loginInfo.id){
          this.getBuyStatus()
          this.getCollectStatus()
        }
      },
      getCommentData(page = 1){
        if(page > this.data.pages){
          page = this.data.pages
        }
        if(page < 1){
          page = 1
        }
        this.page = page
        commentApi.getComment(this.page, this.limit, this.courseInfo.id)
          .then(response => {
            this.data = response.data.data
          })
      },
      submitComment(){
        if(!this.comment.content){
          this.$message({
            type: 'info',
            message: '请输入评论内容'
          })
        }else if(this.loginInfo.id){
          this.comment.memberId = this.loginInfo.id
          this.comment.nickname = this.loginInfo.nickname
          this.comment.avatar = this.loginInfo.avatar
          this.comment.courseId = this.courseInfo.id
          this.comment.teacherId = this.courseInfo.teacherId
          //最终提交
          this.saveComment()
        }else{
          this.$confirm('您尚未登录，无法进行评论, 是否前往登录?', '提示', {
                 confirmButtonText: '确定',
                 cancelButtonText: '取消',
                 type: 'warning'
             }).then(() => {
                 window.location.href = "/sign/login"
             }).catch((response) => { // 失败
                 if (response === 'cancel') {
                     this.$message({
                         type: 'info',
                         message: '已取消'
                     })
                 }
              })
        }
      },
      saveComment(){
        commentApi.addComment(this.comment)
          .then(() => {
            this.$message({
              type: 'success',
              message: '评论成功'
            })
            this.comment = {}
            this.getCommentData()
          })
      },
      buyCourse(){
        // 判断是否登录
        if(this.loginInfo.id){
          // 判断积分是否足以购买
          if(this.loginInfo.point < this.courseInfo.price){
            this.$confirm('您的积分不足, 是否前往充值?', '提示', {
                   confirmButtonText: '确定',
                   cancelButtonText: '取消',
                   type: 'warning'
               }).then(() => {
                   window.location.href = "#"
               }).catch((response) => { // 失败
                   if (response === 'cancel') {
                       this.$message({
                           type: 'info',
                           message: '已取消'
                       })
                   }
                })
          } else { // 积分充足 可以购买
            this.$confirm('是否确定花费'+this.courseInfo.price+'积分购买'+this.courseInfo.title+'?', '提示', {
                   confirmButtonText: '确定',
                   cancelButtonText: '取消',
                   type: 'warning'
               }).then(() => {
                   this.endBuy()
               }).catch((response) => { // 失败
                   if (response === 'cancel') {
                       this.$message({
                           type: 'info',
                           message: '已取消'
                       })
                   }
                })
          }
        // 未登录，提示是否去登陆
        } else {
          this.$confirm('您尚未登录，无法进行购买, 是否前往登录?', '提示', {
                 confirmButtonText: '确定',
                 cancelButtonText: '取消',
                 type: 'warning'
             }).then(() => {
                 window.location.href = "/sign/login"
             }).catch((response) => { // 失败
                 if (response === 'cancel') {
                     this.$message({
                         type: 'info',
                         message: '已取消'
                     })
                 }
              })
        }
      },
      endBuy(){
        this.userOrder.courseId = this.courseInfo.id
        this.userOrder.userId = this.loginInfo.id
        this.userOrder.point = this.courseInfo.price
        orderApi.userBuyCourse(this.userOrder)
          .then(response => {
            this.isBuyCourse = response.data.data.isBuyCourse
            this.courseInfo.buyCount++
            var newPoint = this.loginInfo.point - this.courseInfo.price
            this.$message({
              type: 'success',
              message: '购买成功！您剩余积分：'+ newPoint
            })
            this.loginInfo.point = newPoint
            // 更新cookie 中的用户信息
            cookie.set("user_info", JSON.stringify(this.loginInfo), {domain:'localhost'})
          })
      },
      // 判断已登录用户是否购买过此课程
      getBuyStatus(){
        orderApi.getOrderStatus(this.loginInfo.id, this.courseInfo.id)
          .then(response => {
            this.isBuyCourse = response.data.data.isBuyCourse
          })
      },
      // 判断用户是否收藏了课程
      getCollectStatus(){
        collectApi.getUserCollectStatus(this.loginInfo.id, this.courseInfo.id)
          .then(response => {
            this.isCollect = response.data.data.isCollect
          })
      },
      // 收藏课程
      addUserCollect(){
        // 判断是否登录
        if(this.loginInfo.id){
          this.userCollect.userId = this.loginInfo.id
          this.userCollect.courseId = this.courseInfo.id
          collectApi.addUserCollect(this.userCollect)
            .then(() => {
              this.isCollect = true
              this.$message({
                type: 'success',
                message: '添加收藏成功'
              })

            })
        } else {
          this.$confirm('您尚未登录，无法进行购买, 是否前往登录?', '提示', {
                 confirmButtonText: '确定',
                 cancelButtonText: '取消',
                 type: 'warning'
             }).then(() => {
                 window.location.href = "/sign/login"
             }).catch((response) => { // 失败
                 if (response === 'cancel') {
                     this.$message({
                         type: 'info',
                         message: '已取消'
                     })
                 }
              })
        }
      },
      // 取消收藏
      removeCollect(){
        collectApi.cancelCollect(this.loginInfo.id, this.courseInfo.id)
          .then(() => {
            this.isCollect = false
            this.$message({
              type: 'success',
              message: '已取消收藏'
            })
          })
      },
      showBuyMessage(){
        this.$message({
          type: 'warning',
          message: '请先购买课程'
        })
      }
  }
}
</script>
<style>
  .p-h-video-box img {
    height: 357px;
    margin-left: auto;
    margin-right:auto;
    display:block;
  }
  html, body{ scroll-behavior:smooth; }
</style>
