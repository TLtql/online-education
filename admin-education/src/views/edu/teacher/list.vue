<template>
  <div class="app-container">
    讲师列表
        <!--查询表单-->
        <el-form :inline="true" class="demo-form-inline">
          <el-form-item>
            <el-input v-model="teacherQuery.name" placeholder="讲师名"/>
          </el-form-item>

          <el-form-item>
            <el-select v-model="teacherQuery.level" clearable placeholder="讲师头衔">
              <el-option :value="1" label="高级讲师"/>
              <el-option :value="2" label="首席讲师"/>
            </el-select>
          </el-form-item>

          <el-form-item label="添加时间">
            <el-date-picker
              v-model="teacherQuery.begin"
              type="datetime"
              placeholder="选择开始时间"
              value-format="yyyy-MM-dd HH:mm:ss"
              default-time="00:00:00"
            />
          </el-form-item>
          <el-form-item>
            <el-date-picker
              v-model="teacherQuery.end"
              type="datetime"
              placeholder="选择截止时间"
              value-format="yyyy-MM-dd HH:mm:ss"
              default-time="00:00:00"
            />
          </el-form-item>

          <el-button type="primary" icon="el-icon-search" @click="init()">查询</el-button>
          <el-button type="default" @click="resetData()">清空</el-button>
        </el-form>
        <!-- 表格 -->
        <el-table
          v-loading="listLoading"
          :data="list"
          element-loading-text="数据加载中"
          border
          fit
          highlight-current-row>

          <el-table-column
            label="序号"
            width="70"
            align="center">
            <template slot-scope="scope">
              {{ (page - 1) * limit + scope.$index + 1 }}
            </template>
          </el-table-column>

          <el-table-column prop="name" label="名称" width="80" />

          <el-table-column label="头衔" width="80">
            <template slot-scope="scope">
              {{ scope.row.level===1?'高级讲师':'首席讲师' }}
            </template>
          </el-table-column>

          <el-table-column prop="intro" label="简介" />

          <el-table-column prop="gmtCreate" label="添加时间" width="160"/>

          <el-table-column prop="sort" label="排序" width="60" />

          <el-table-column label="操作" width="200" align="center">
            <template slot-scope="scope">
              <router-link :to="'/edu/teacher/edit/'+scope.row.id">
                <el-button type="primary" size="mini" icon="el-icon-edit">修改</el-button>
              </router-link>
              <el-button type="danger" size="mini" icon="el-icon-delete" @click="removeDataById(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
              :current-page="page"
              :page-size="limit"
              :total="total"
              style="padding: 30px 0; text-align: center;"
              layout="total, prev, pager, next, jumper"
              @current-change="getlist"
            />
  </div>
</template>

<script>
import teacher from '@/api/edu/teacher'

export default {
  name: 'list',
  data(){
    return {
      listLoading: false,
      page: 1,
      limit: 5,
      list: [],
      total: 0,
      teacherQuery: {}
    }
  },
  created(){
    this.init()
  },
  methods:{
    init(){
      this.getlist()
      setTimeout(() => {
        this.$message({
          type: 'success',
          message: '查询到' +this.total+ '条数据'
        })
      },500)
    },
    getlist(page = 1){
      this.page = page
      this.listLoading = true
      teacher.getTeacherList(this.page, this.limit, this.teacherQuery)
                .then(response => {
                  this.list = response.data.page.records
                  this.total = response.data.page.total
                })
      this.listLoading = false
    },
    resetData(){
      //清空表单数据
      this.teacherQuery={}
      //重新查询讲师数据
      this.getlist()
      setTimeout(() => {
        this.$message({
          type: 'success',
          message: '查询到' +this.total+ '条数据'
        })
      },500)
    },
    removeDataById(id){
       this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
          }).then(() => {
              return teacher.removeTeacherbyId(id)
          }).then(() => {
              this.getlist(this.page)
              this.$message({
                  type: 'success',
                  message: '删除成功!'
              })
          }).catch((response) => { // 失败
              if (response === 'cancel') {
                  this.$message({
                      type: 'info',
                      message: '已取消删除'
                  })
              } else {
                  this.$message({
                      type: 'error',
                      message: '删除失败'
                  })
              }
          })
    }
  }
}

</script>

<style>
</style>
