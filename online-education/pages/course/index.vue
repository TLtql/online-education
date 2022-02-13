<template>
  <div id="aCoursesList" class="bg-fa of">
    <!-- /课程列表 开始 -->
    <section class="container">
      <header class="comm-title">
        <h2 class="fl tac">
          <span class="c-333">全部课程</span>
        </h2>
        <span style="text-align: right;">
            <label class="h-r-s-box">
              <input type="text" placeholder="搜索课程" v-model="courseQuery.title">
              <button @click="queryTitle()" class="s-btn">
                <em class="icon18">&nbsp;</em>
              </button>
            </label>
        </span>
      </header>
      <section class="c-sort-box">
        <section class="c-s-dl">
          <dl>
            <dt>
              <span class="c-999 fsize14">课程类别</span>
            </dt>
            <dd class="c-s-dl-li">
              <ul class="clearfix">
                <li>
                  <a title="全部" href="javascript:void(0);" @click="searchOne('')">全部</a>
                </li>
                <li v-for="(oneSubject,index) in oneSubjectList" :key="index" :class="{active:oneIndex==index}">
                  <a :title="oneSubject.title"
                      href="javascript:void(0);"
                      @click="searchOne(oneSubject.id,index)"
                      >{{oneSubject.title}}</a>
                </li>

              </ul>
            </dd>
          </dl>
          <dl>
            <dt>
              <span class="c-999 fsize14"></span>
            </dt>
            <dd class="c-s-dl-li">
              <ul class="clearfix">
                <li v-for="(twoSubject,index) in twoSubjectList" :key="index" :class="{active:twoIndex==index}">
                  <a :title="twoSubject.title"
                  href="javascript:void(0);"
                  @click="searchTwo(twoSubject.id,index)"
                  >{{twoSubject.title}}</a>
                </li>
              </ul>
            </dd>
          </dl>
          <div class="clear"></div>
        </section>
        <div class="js-wrap">
          <section class="fr">
            <span class="c-ccc">
              <i class="c-master f-fM">1</i>/
              <i class="c-666 f-fM">1</i>
            </span>
          </section>
          <section class="fl">
            <ol class="js-tap clearfix">
              <li :class="{'current bg-orange':buyCountSort!=''}">
                <a title="销量" href="javascript:void(0);" @click="searchBuyCount()">销量
                <span :class="{hide:buyCountSort==''}">↓</span>
                </a>
              </li>
              <li :class="{'current bg-orange':gmtCreateSort!=''}">
                <a title="最新" href="javascript:void(0);" @click="searchGmtCreate()">最新
                <span :class="{hide:gmtCreateSort==''}">↓</span>
                </a>
              </li>
              <li :class="{'current bg-orange':priceSort!=''}">
                <a title="价格" href="javascript:void(0);" @click="searchPrice()">价格&nbsp;
                  <span :class="{hide:priceSort==''}">↓</span>
                </a>
              </li>
            </ol>
          </section>
        </div>
        <div class="mt40">
          <!-- /无数据提示 开始-->
          <section class="no-data-wrap" v-if="data.total == 0">
            <em class="icon30 no-data-ico">&nbsp;</em>
            <span class="c-666 fsize14 ml10 vam">没有相关数据，小编正在努力整理中...</span>
          </section>
          <!-- /无数据提示 结束-->
          <article class="comm-course-list" v-if="data.total > 0">
            <ul class="of" id="bna">
              <li v-for="course in data.items" :key="course.id">
                <div class="cc-l-wrap">
                  <section class="course-img">
                        <img
                          :src="course.cover"
                          class="img-responsive"
                          :alt="course.title"
                        >
                    <div class="cc-mask">
                      <a :href="'/course/'+course.id" title="开始学习" class="comm-btn c-btn-1">开始学习</a>
                    </div>
                  </section>
                  <h3 class="hLh30 txtOf mt10">
                    <a :href="'/course/'+course.id" :title="course.title" class="course-title fsize18 c-333">{{course.title}}</a>
                  </h3>
                  <section class="mt10 hLh20 of">
                    <span class="fr jgTag bg-green">
                      <i class="c-fff fsize12 f-fA" v-if="course.price === 0">免费</i>
                      <i class="c-fff fsize12 f-fA" v-if="course.price > 0">￥{{course.price}}</i>
                    </span>
                    <span class="fl jgAttr c-ccc f-fA">
                      <i class="c-999 f-fA">{{course.viewCount}}人学习</i>
                      |
                      <i class="c-999 f-fA">{{course.buyCount}}评论</i>
                    </span>
                  </section>
                </div>
              </li>
            </ul>
            <div class="clear"></div>
          </article>
        </div>
       <!-- 公共分页 开始 -->
       <div>
         <div class="paging">
           <!-- undisable这个class是否存在，取决于数据属性hasPrevious -->
           <a
             :class="{undisable: !data.hasPrevious}"
             href="#"
             title="首页"
             @click.prevent="getCourseData(1)">首</a>
           <a
             :class="{undisable: !data.hasPrevious}"
             href="#"
             title="前一页"
             @click.prevent="getCourseData(data.current-1)">&lt;</a>
           <a
             v-for="page in data.pages"
             :key="page"
             :class="{current: data.current == page, undisable: data.current == page}"
             :title="'第'+page+'页'"
             href="#"
             @click.prevent="getCourseData(page)">{{ page }}</a>
           <a
             :class="{undisable: !data.hasNext}"
             href="#"
             title="后一页"
             @click.prevent="getCourseData(data.current+1)">&gt;</a>
           <a
             :class="{undisable: !data.hasNext}"
             href="#"
             title="末页"
             @click.prevent="getCourseData(data.pages)">末</a>
           <div class="clear"/>
         </div>
       </div>
       <!-- 公共分页 结束 -->
      </section>
    </section>
    <!-- /课程列表 结束 -->
  </div>
</template>
<script>
import courseApi from '@/api/course'
export default {
  data() {
    return {
      oneSubjectList: [],
      twoSubjectList: [],
      teacherId: null,
      data: {
        items: []
      },
      courseQuery: {},
      page: 1,
      limit: 8,

      oneIndex: -1,
      twoIndex: -1,
      buyCountSort: "",
      gmtCreateSort: "",
      priceSort: ""
    }
  },
  created() {
    this.init()
  },
  methods:{
    init(){
      this.getSubject()
      this.getCourseData()
    },
    getSubject(){
      courseApi.getSubjectList()
        .then(response => {
          this.oneSubjectList = response.data.data.subjectList
        })
    },
    getCourseData(page = 1){
      if(page > this.data.pages){
        page = this.data.pages
      }
      if(page < 1){
        page = 1
      }
      this.page = page
      courseApi.getCoursePage(this.page, this.limit, this.courseQuery)
        .then(response => {
          this.data = response.data.data
        })
    },
    searchOne(subjectParentId,index){
      this.courseQuery.title = ''
      this.oneIndex = index
      this.twoIndex = -1
      this.courseQuery.subjectId = ''
      this.twoSubjectList = []
      this.courseQuery.subjectParentId = subjectParentId
      // 重新查询数据刷新页面
      this.getCourseData()
      //刷新二级分类
      for (let i = 0; i < this.oneSubjectList.length; i++) {
        if (this.oneSubjectList[i].id === subjectParentId) {
          this.twoSubjectList = this.oneSubjectList[i].children
        }
      }
    },
    searchTwo(twoSubjectId,index){
      this.courseQuery.title = ''
      this.twoIndex = index
      this.courseQuery.subjectId = twoSubjectId
      this.getCourseData()
    },
    searchBuyCount(){
      this.buyCountSort = "1"
      this.gmtCreateSort = ""
      this.priceSort = ""
      this.courseQuery.buyCountSort = this.buyCountSort
      this.courseQuery.gmtCreateSort = this.gmtCreateSort
      this.courseQuery.priceSort = this.priceSort
      this.getCourseData()
    },
    searchGmtCreate(){
      this.buyCountSort = ""
      this.gmtCreateSort = "1"
      this.priceSort = ""
      this.courseQuery.buyCountSort = this.buyCountSort
      this.courseQuery.gmtCreateSort = this.gmtCreateSort
      this.courseQuery.priceSort = this.priceSort
      this.getCourseData()
    },
    searchPrice(){
      this.buyCountSort = ""
      this.gmtCreateSort = ""
      this.priceSort = "1"
      this.courseQuery.buyCountSort = this.buyCountSort
      this.courseQuery.gmtCreateSort = this.gmtCreateSort
      this.courseQuery.priceSort = this.priceSort
      this.getCourseData()
    },
    queryTitle(){
      this.getCourseData()
    }
  }
};
</script>
<style>
  .course-img img{
    height: 180px;
    margin-left: auto;
    margin-right:auto;
    display:block;
  }
  .cc-l-wrap {
    text-align: center;
  }
  .active {
    background: #bdbdbd;
  }
  .hide {
    display: none;
  }
  .show {
    display: block;
  }
</style>
