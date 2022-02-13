<template>
  <div>
    <div class="head-table">我的收藏</div>
    <div class="show">
      <!-- /无数据提示 开始-->
      <section class="no-data-wrap" v-if="collectList.length === 0">
        <em class="icon30 no-data-ico">&nbsp;</em>
        <span class="c-666 fsize14 ml10 vam">您还没有任何收藏呢,去课程列表看看吧</span>
      </section>
      <!-- /无数据提示 结束-->
      <div v-if="collectList.length > 0">
        <el-table
          :data="collectList"
          height="600"
          border
          style="width: 100%">
          <el-table-column label="课程信息" align="center">
            <template slot-scope="scope">
              <div class="pic">
                <img :src="scope.row.cover" :alt="scope.row.title" width="100px">
              </div>
              <div class="title">
                <a :href="'/course/'+scope.row.courseId">{{scope.row.title}}</a>
              </div>
            </template>
          </el-table-column>

          <el-table-column label="讲师名称" width="100px" align="center">
            <template slot-scope="scope">
              {{scope.row.teacherName}}
            </template>
          </el-table-column>

          <el-table-column label="价格" width="100px" align="center">
            <template slot-scope="scope">
              {{ scope.row.price > 0 ? scope.row.price + ' 积分' : '免费'}}
            </template>
          </el-table-column>


          <el-table-column prop="gmtCreate" label="收藏时间" align="center">
            <template slot-scope="scope">
              {{ scope.row.gmtCreate.substr(0,10) }}
            </template>
          </el-table-column>

          <el-table-column label="操作" align="center">
            <template slot-scope="scope">
              <router-link :to="'/course/'+scope.row.courseId">
                <el-button type="text" icon="el-icon-edit">去学习</el-button>
              </router-link>&emsp;
              <el-button type="text" icon="el-icon-delete" @click="removeCollect(scope.row.id)">取消收藏</el-button>
            </template>
          </el-table-column>

        </el-table>
      </div>
    </div>
  </div>
</template>

<script>
import collectApi from '@/api/collect'
export default {
  data() {
    return {
      collectList: []
    }
  },
  created() {
    this.init()
  },
  methods:{
    init(){
      this.getCollectList()
    },
    getCollectList(){
      collectApi.getUserCollectList()
        .then(response => {
          this.collectList = response.data.data.collectList
        })
    },
    removeCollect(id){
      this.$confirm('是否确定取消收藏?', '提示', {
             confirmButtonText: '确定',
             cancelButtonText: '取消',
             type: 'warning'
         }).then(() => {
             collectApi.remoceUserCollect(id)
              .then(() => {
                this.getCollectList()
                this.$message({
                  type: 'success',
                  message: '取消收藏成功'
                })
              })
         }).catch((response) => { // 失败
             if (response === 'cancel') {
                 this.$message({
                     type: 'info',
                     message: '已取消'
                 })
             }
          })

    }
  }
}
</script>

<style>
.show{
  background-color: white;
  margin: 10px auto;
  width: 80%;
  height: 600px;
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
</style>
