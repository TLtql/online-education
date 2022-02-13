<template>
  <div>
    <!--查询表单-->
    <el-form :inline="true" class="demo-form-inline" style="margin-top: 10px;">

      <!-- 所属分类：级联下拉列表 -->
      <!-- 一级分类 -->
      <el-form-item label="课程类别">
        <el-select
          v-model="searchObj.subjectParentId"
          placeholder="请选择"
          @change="subjectLevelOneChanged">
          <el-option
            v-for="subject in subjectOne"
            :key="subject.id"
            :label="subject.title"
            :value="subject.id"/>
        </el-select>

        <!-- 二级分类 -->
        <el-select v-model="searchObj.subjectId" placeholder="请选择">
          <el-option
            v-for="subject in subjectTwo"
            :key="subject.id"
            :label="subject.title"
            :value="subject.id"/>
        </el-select>
      </el-form-item>

      <!-- 标题 -->
      <el-form-item>
        <el-input v-model="searchObj.title" placeholder="课程标题"/>
      </el-form-item>
      <!-- 讲师 -->
      <el-form-item>
        <el-select
          v-model="searchObj.teacherId"
          placeholder="请选择讲师">
          <el-option
            v-for="teacher in teacherList"
            :key="teacher.id"
            :label="teacher.name"
            :value="teacher.id"/>
        </el-select>
      </el-form-item>

      <el-button type="primary" icon="el-icon-search" @click="fetchData()">查询</el-button>
      <el-button type="default" @click="resetData()">清空</el-button>
    </el-form>

    <!-- 表格 -->
    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="数据加载中"
      border
      fit
      highlight-current-row
      row-class-name="myClassList">

      <el-table-column
        label="序号"
        width="70"
        align="center">
        <template slot-scope="scope">
          {{ (page - 1) * limit + scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column label="课程信息" width="470" align="center">
        <template slot-scope="scope">
          <div class="info">
            <div class="pic">
              <img :src="scope.row.cover" alt="scope.row.title" >
            </div>
            <div class="title">
              <a href="">{{ scope.row.title }}</a>
              <p>{{ scope.row.lessonNum }}课时</p>
            </div>
          </div>

        </template>
      </el-table-column>

      <el-table-column label="创建时间" align="center">
        <template slot-scope="scope">
          {{ scope.row.gmtCreate.substr(0, 10) }}
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === 'Normal' ? 'success' : 'warning'">
            {{ scope.row.status === 'Normal' ? "已发布" : "未发布"}}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="价格" width="100" align="center" >
        <template slot-scope="scope">
          {{ Number(scope.row.price) === 0 ? '免费' :
            scope.row.price + '积分' }}
        </template>
      </el-table-column>
      <el-table-column prop="buyCount" label="付费学员" width="100" align="center" >
        <template slot-scope="scope">
          {{ scope.row.buyCount }}人
        </template>
      </el-table-column>

      <el-table-column label="操作" width="150" align="center">
        <template slot-scope="scope">
          <div v-if="scope.row.status === 'Normal'">
            <el-button type="text" icon="el-icon-edit" @click="soldOutCourse(scope.row.id)">下架</el-button>
          </div>
          <div v-if="scope.row.status === 'Draft'">
            <router-link :to="'/edu/course/info/'+scope.row.id">
              <el-button type="text" size="mini" icon="el-icon-edit">编辑课程信息</el-button>
            </router-link>
            <router-link :to="'/edu/course/chapter/'+scope.row.id">
              <el-button type="text" size="mini" icon="el-icon-edit">编辑课程大纲</el-button>
            </router-link>
            <span>
              <router-link :to="'/edu/course/publish/'+scope.row.id">
                <el-button type="text" size="mini" icon="el-icon-edit">去发布</el-button>
              </router-link>&nbsp;
              <el-button type="text" size="mini" icon="el-icon-delete" @click="deleteCourse(scope.row.id)">删除</el-button>
            </span>

          </div>

        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      :current-page="page"
      :page-size="limit"
      :total="total"
      style="padding: 30px 0; text-align: center;"
      layout="total, prev, pager, next, jumper"
      @current-change="getCourseList"
    />
  </div>
</template>

<script>
  import course from '@/api/edu/course'
  import teacher from '@/api/edu/teacher'
  import subject from '@/api/edu/subject'
  export default{
    data(){
      return {
        listLoading: false,
        searchObj: {
          subjectParentId: '',
          subjectId: ''
        },
        list: [],
        page: 1,
        limit: 5,
        total: 0,
        teacherList: [],
        subjectOne: [],
        subjectTwo: []
      }
    },
    created() {
      console.log("list Created")
      this.init()
    },
    methods:{
      init(){
        this.getCourseList()
        this.initTeacherList()
        setTimeout(() => {
          this.$message({
            type: 'success',
            message: '查询到' +this.total+ '条数据'
          })
        },500)
        this.initSubject()
      },
      fetchData(){
        this.getCourseList()
        setTimeout(() => {
          this.$message({
            type: 'success',
            message: '查询到' +this.total+ '条数据'
          })
        },500)
      },
      resetData(){
        //清空表单数据
        this.searchObj={}
        //重新查询课程数据
        this.getCourseList()
        setTimeout(() => {
          this.$message({
            type: 'success',
            message: '查询到' +this.total+ '条数据'
          })
        },500)
      },
      getCourseList(page = 1){
        this.page = page
        this.listLoading = true
        course.getCourseListPage(this.page, this.limit, this.searchObj)
          .then(response => {
            this.list = response.data.page.records
            this.total = response.data.page.total
          })
        this.listLoading = false
      },
      initTeacherList(){
        teacher.getTacherList()
          .then(response => {
            this.teacherList = response.data.items
          })
      },
      initSubject(){
        subject.getSubjectList()
          .then(response => {
            this.subjectOne = response.data.subjectList
          })
      },
      subjectLevelOneChanged(value){
        for(let i=0; i < this.subjectOne.length; i++){
          if(this.subjectOne[i].id === value){
            this.subjectTwo = this.subjectOne[i].children
            this.searchObj.subjectId = ''
          }
        }
      },
      deleteCourse(courseId){
        this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
               confirmButtonText: '确定',
               cancelButtonText: '取消',
               type: 'warning'
           }).then(() => {
               return course.deleteCourse(courseId)
              }).then(() => {

                this.$message({
                  type: 'success',
                  message: '删除课程成功'
                })
            }).then(() => {
            this.getCourseList(this.page)
          }).catch((response) => { // 失败
              if (response === 'cancel') {
                  this.$message({
                      type: 'info',
                      message: '已取消删除'
                  })
              }
          })

      },
      soldOutCourse(id){
        this.$confirm('此操作将下架该课程, 是否继续?', '提示', {
               confirmButtonText: '确定',
               cancelButtonText: '取消',
               type: 'warning'
           }).then(() => {
               return course.soldOutCourseById(id)
              }).then(() => {
        
                this.$message({
                  type: 'success',
                  message: '课程下架成功'
                })
            }).then(() => {
            this.getCourseList(this.page)
          }).catch((response) => { // 失败
              if (response === 'cancel') {
                  this.$message({
                      type: 'info',
                      message: '已取消删除'
                  })
              }
          })
        
      }

    }
  }
</script>

<style scoped>
.myClassList .info {
    width: 450px;
    overflow: hidden;
}
.myClassList .info .pic {
    width: 150px;
    height: 90px;
    overflow: hidden;
    float: left;
}
.myClassList .info .pic a {
    display: block;
    width: 100%;
    height: 100%;
    margin: 0;
    padding: 0;
}
.myClassList .info .pic img {
    margin-left: 20px;
    display: block;
    height: 100%;
}
.myClassList td .info .title {
    width: 280px;
    left: 20px;
    top: 0px;
    height: 90px;
    position: relative;
}
.myClassList td .info .title a {
    display: block;
    height: 48px;
    line-height: 24px;
    overflow: hidden;
    color: #00baf2;
    margin-bottom: 12px;
}
.myClassList td .info .title p {
    line-height: 20px;
    margin-top: 5px;
    color: #818181;
}
</style>
