<template>
  <div>
    <div class="head-table">我的评论</div>
    <div class="show">
      <!-- /无数据提示 开始-->
      <section class="no-data-wrap" v-if="commentList.length === 0">
        <em class="icon30 no-data-ico">&nbsp;</em>
        <span class="c-666 fsize14 ml10 vam">您还没有发表任何评论</span>
      </section>
      <!-- /无数据提示 结束-->
      <div v-if="commentList.length > 0">
        <el-table
          :data="commentList"
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

          <el-table-column prop="gmtCreate" label="内容" align="center" width="250px">
            <template slot-scope="scope">
              {{ scope.row.content }}
            </template>
          </el-table-column>


          <el-table-column prop="gmtCreate" label="评论时间" align="center">
            <template slot-scope="scope">
              {{ scope.row.gmtCreate.substr(0,10) }}
            </template>
          </el-table-column>

          <el-table-column label="操作" align="center">
            <template slot-scope="scope">
              <router-link :to="'/course/'+scope.row.courseId">
                <el-button type="text" icon="el-icon-edit">去学习</el-button>
              </router-link>&emsp;
              <el-button type="text" icon="el-icon-delete" @click="removeComment(scope.row.id)">删除评论</el-button>
            </template>
          </el-table-column>

        </el-table>
      </div>
    </div>
  </div>
</template>

<script>
import commentApi from '@/api/comment'
export default {
  data() {
    return {
      commentList: []
    }
  },
  created() {
    this.init()
  },
  methods:{
    init(){
      this.getCommentList()
    },
    getCommentList(){
      commentApi.getUserCommentList()
        .then(response => {
          this.commentList = response.data.data.commentList
        })
    },
    removeComment(id){
      this.$confirm('是否确定删除该条评论?', '提示', {
             confirmButtonText: '确定',
             cancelButtonText: '取消',
             type: 'warning'
         }).then(() => {
             commentApi.remoceUserComment(id)
              .then(() => {
                this.getCommentList()
                this.$message({
                  type: 'success',
                  message: '成功删除评论'
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
