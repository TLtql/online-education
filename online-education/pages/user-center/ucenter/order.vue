<template>
  <div>
    <div class="head-table">我的订单</div>
    <div class="show">
      <!-- /无数据提示 开始-->
      <section class="no-data-wrap" v-if="orderList.length === 0">
        <em class="icon30 no-data-ico">&nbsp;</em>
        <span class="c-666 fsize14 ml10 vam">您还没有任何订单呢,快去购买课程吧</span>
      </section>
      <!-- /无数据提示 结束-->
      <div v-if="orderList.length > 0">
        <el-table
          :data="orderList"
          height="600"
          border
          style="width: 100%">
          <el-table-column label="课程信息" align="center">
            <template slot-scope="scope">
              <div class="pic">
                <img :src="scope.row.cover" alt="scope.row.title" width="100px">
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
              {{scope.row.point}} 积分
            </template>
          </el-table-column>

          <el-table-column label="订单状态" align="center" width="100">
            <template slot-scope="scope">
              <el-tag :type="scope.row.status ? 'success' : 'warning'">
                {{ scope.row.status ? "已支付" : "未支付"}}
              </el-tag>
            </template>
          </el-table-column>

          <el-table-column prop="gmtCreate" label="创建时间" align="center">
            <template slot-scope="scope">
              {{ scope.row.gmtCreate.substr(0,10) }}
            </template>
          </el-table-column>

          <el-table-column label="操作" align="center">
            <template slot-scope="scope">
              <router-link :to="'/course/'+scope.row.courseId">
                <el-button type="text" icon="el-icon-edit">去学习</el-button>
              </router-link>&emsp;
              <!-- 删除订单 -->
              <!-- <el-button type="text" icon="el-icon-delete" @click="removeOrder(scope.row.id)">删除</el-button> -->
            </template>
          </el-table-column>

        </el-table>
      </div>
    </div>
  </div>
</template>

<script>
import orderApi from '@/api/order'
export default {
  data() {
    return {
      orderList:[]
    }
  },
  created() {
    this.init()
  },
  methods:{
    init(){
      this.getOrderList()
    },
    getOrderList(){
      orderApi.getUserOrder()
        .then(response => {
          this.orderList = response.data.data.orderList
        })
    },
    removeOrder(id){
      this.$confirm('您正在进行删除操作,是否确定删除?', '提示', {
             confirmButtonText: '确定',
             cancelButtonText: '取消',
             type: 'warning'
         }).then(() => {
             orderApi.removeUserOrder(id)
              .then(() => {
                this.getOrderList()
                this.$message({
                  type: 'success',
                  message: '删除订单成功'
                })
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
