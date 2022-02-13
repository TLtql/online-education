<template>
  <div class="app-container">
        <!--查询表单-->
        <el-form :inline="true" class="demo-form-inline">
          <el-form-item>
            <el-input v-model="userQuery.id" placeholder="用户id"/>
          </el-form-item>


          <el-form-item>
            <el-input v-model="userQuery.nickname" placeholder="用户昵称"/>
          </el-form-item>


          <el-form-item>
            <el-input v-model="userQuery.mobile" placeholder="用户手机号"/>
          </el-form-item>

          <el-form-item>
            <el-select v-model="userQuery.disabled" clearable placeholder="状态">
              <el-option :value="0" label="未被禁用"/>
              <el-option :value="1" label="已被禁用"/>
            </el-select>
          </el-form-item>

          <el-button type="primary" icon="el-icon-search" @click="init()">查询</el-button>
          <el-button type="default" @click="resetData()">清空</el-button>
        </el-form>
        <!-- 表格 -->
        <el-table
          v-loading="listLoading"
          :data="userList"
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

          <el-table-column prop="nickname" label="名称" width="150" />


          <el-table-column prop="mobile" label="手机"  width="130" />


          <el-table-column prop="point" label="积分" width="60" />

          <el-table-column prop="sign" label="签名"/>

          <el-table-column label="状态" width="100" align="center">
            <template slot-scope="scope">
              <el-tag type="success" v-if="scope.row.disabled === 0">
                正常
              </el-tag>
              <el-tag type="warning" v-if="scope.row.disabled === 1">
                禁用状态
              </el-tag>
              <el-tag type="danger" v-if="scope.row.disabled === -1">
                永久禁用
              </el-tag>
            </template>
          </el-table-column>



          <el-table-column prop="gmtCreate" label="创建时间" align="center">
            <template slot-scope="scope">
              {{ scope.row.gmtCreate.substr(0,10) }}
            </template>
          </el-table-column>

          <el-table-column label="操作" width="200" align="center">
            <template slot-scope="scope">
              <div v-if="scope.row.disabled === 0">
                <el-button type="warning" size="mini" icon="el-icon-edit" @click="setBanTime(scope.row.id)">禁用</el-button>
                <el-button type="danger" size="mini" icon="el-icon-delete" @click="removeUser(scope.row.id)">删除</el-button>
              </div>
              <div v-if="scope.row.disabled != 0">
                <el-button type="success" size="mini" icon="el-icon-edit" @click="relieve(scope.row.id,scope.row.disabled)">解封</el-button>
                <el-button type="danger" size="mini" icon="el-icon-delete" @click="removeUser(scope.row.id)">删除</el-button>
              </div>

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


        <!-- 禁用用户表单 -->
        <el-dialog :visible.sync="disabled" title="选择封禁时间" width="40%">
              <el-radio-group v-model="disabledTime">
                <el-radio :label="60">1分钟</el-radio>
                <el-radio :label="24*60*60">1天</el-radio>
                <el-radio :label="3*24*60*60">3天</el-radio>
                <el-radio :label="7*24*60*60">7天</el-radio>
                <el-radio :label="30*24*60*60">一个月</el-radio>
                <el-radio :label="-1">永久</el-radio>
              </el-radio-group>
            <div slot="footer" class="dialog-footer">
                <el-button @click="disabled = false, disabledTime = ''">取 消</el-button>
                <el-button type="primary" @click="banUser()">确 定</el-button>
            </div>
        </el-dialog>
  </div>
</template>

<script>
import userApi from '@/api/edu/user'
export default {
  data(){
    return {
      listLoading: false,
      userList: [],
      page: 1,
      limit: 5,
      total: 0,
      userQuery: {},
      disabled: false,
      disabledTime: 0,
      banUserId: '',
    }
  },
  created() {
    this.init()
  },
  methods:{
    init(){
      this.getList()
      setTimeout(() => {
        this.$message({
          type: 'success',
          message: '查询到' +this.total+ '条数据'
        })
      },500)
    },
    getList(page = 1){
      this.page = page
      this.listLoading = true
      userApi.getUserListPage(this.page, this.limit, this.userQuery)
        .then(response => {
          this.userList = response.data.item.records
          this.total = response.data.item.total
          this.listLoading = false
        })

    },
    resetData(){
      //清空表单数据
      this.userQuery={}
      //重新查询用户数据
      this.getList()
      setTimeout(() => {
        this.$message({
          type: 'success',
          message: '查询到' +this.total+ '条数据'
        })
      },500)
    },
    setBanTime(id){
      this.disabled = true
      this.banUserId = id
    },
    banUser(){
      console.log(this.banUserId)
      console.log(this.disabledTime)
      userApi.banUserById(this.banUserId,this.disabledTime)
        .then(response => {
          var time = response.data.returnTime
          if(time == -1){
            this.$message({
              type: 'success',
              message: '用户已被永久封禁'
            })
          }else {
            this.$message({
              type: 'success',
              message: '用户已被封禁，剩余解封时间:' + time
            })
          }
          // 刷新页面
          this.disabled = false
          this.getList(this.page)
        })
    },
    relieve(id,status){
      var message = ''
      if(status === -1){
        this.$confirm('该用户为永久封禁状态,是否确认解封?', '提示', {
               confirmButtonText: '确定',
               cancelButtonText: '取消',
               type: 'warning'
           }).then(() => {
               return userApi.relieveUser(id)
           }).then(() => {
               this.getList(this.page)
               this.$message({
                   type: 'success',
                   message: '解封成功!'
               })
           }).catch((response) => { // 失败
               if (response === 'cancel') {
                   this.$message({
                       type: 'info',
                       message: '已取消操作'
                   })
               }
           })
      } else if(status === 1){
        userApi.getBlockStatus(id)
          .then(response => {
            var datatime = response.data.time
            this.$confirm('该用户 '+datatime+' 后解封,是否立即解封?', '提示', {
                   confirmButtonText: '确定',
                   cancelButtonText: '取消',
                   type: 'warning'
               }).then(() => {
                   return userApi.relieveUser(id)
               }).then(() => {
                   this.getList(this.page)
                   this.$message({
                       type: 'success',
                       message: '解封成功!'
                   })
               }).catch((response) => { // 失败
                   if (response === 'cancel') {
                       this.$message({
                           type: 'info',
                           message: '已取消操作'
                       })
                   }
               })
          })
      }

    },
    // 删除用户
    removeUser(id){
      this.$confirm('此操作将会移除用户的所有信息(包括订单，评论等),是否确认?', '提示', {
             confirmButtonText: '确定',
             cancelButtonText: '取消',
             type: 'warning'
         }).then(() => {
             return userApi.removeUserById(id)
         }).then(() => {
             this.getList(this.page)
             this.$message({
                 type: 'success',
                 message: '删除成功!'
             })
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

<style>
</style>
