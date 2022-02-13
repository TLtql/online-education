<template>
  <div class="app-container">
    <!-- 表格 -->
    <el-table
      width = '100%'
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

      <el-table-column prop="title" label="标题" width="150" />


      <el-table-column label="展示图片" width="200" align="center">
        <template slot-scope="scope">
          <div class="pic">
            <img :src="scope.row.imageUrl" alt="scope.row.title" >
          </div>
        </template>
      </el-table-column>

      <el-table-column prop="linkUrl" label="链接地址" width="160"/>

      <el-table-column prop="gmtCreate" label="添加时间" width="200"/>

      <el-table-column prop="sort" label="排序" width="80" />

      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <router-link :to="'/edu/banner/edit/'+scope.row.id">
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
          @current-change="getList"
        />
  </div>

</template>

<script>
import banner from '@/api/edu/banner'
export default {
  data(){
    return {
      list: [],
      page: 1,
      limit: 5,
      total: 0,
      listLoading: false
    }
  },
  created(){
    console.log("list created")
    this.init()
  },
  methods:{
    init(){
      this.getList()
    },
    getList(page = 1){
      this.page = page
      this.listLoading = true
      banner.getBannerList(this.page, this.limit)
        .then(response => {
          this.list = response.data.items.records
          this.total = response.data.items.total
        })
      this.listLoading = false
    },
    removeDataById(id){
       this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
          }).then(() => {
              return banner.removeBannerById(id)
          }).then(() => {
              this.getList(this.page)
              this.$message({
                  type: 'success',
                  message: '删除banner成功!'
              })
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

<style>
  .pic img{
    margin-left: 5px;
    display: block;
    height: 100px;
  }
</style>
