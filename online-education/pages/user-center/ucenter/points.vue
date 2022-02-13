<template>
  <div>
    <div class="head-table">积分充值</div>
    <div class="show">
      <div >
        <dt class="now-point">
          <span >当前积分：{{loginInfo.point}}</span>
        </dt>
        <table width="100%">
            <tr>
              <th>价格</th>
              <th>积分</th>
              <th>操作</th>
            </tr>
        		<tr>
        			<td>￥0.01</td>
        			<td>100积分</td>
              <td>
                <el-button type="text" icon="el-icon-edit" @click="toPay(0.01)">去支付</el-button>
              </td>
        		</tr>
            <tr>
              <td>￥0.02</td>
              <td>200积分</td>
              <td>
                <el-button type="text" icon="el-icon-edit" @click="toPay(0.02)">去支付</el-button>
              </td>
            </tr>
            <tr>
              <td>￥0.05</td>
              <td>500积分</td>
              <td>
                <el-button type="text" icon="el-icon-edit" @click="toPay(0.05)">去支付</el-button>
              </td>
            </tr>
            <tr>
              <td>￥0.1</td>
              <td>1000积分</td>
              <td>
                <el-button type="text" icon="el-icon-edit" @click="toPay(0.1)">去支付</el-button>
              </td>
            </tr>
            <tr>
              <td>其他数量:</td>
              <td>
                <form action="#" method="post">
                  <label class="h-r-s-box" >
                    <input type="text" placeholder="请输入价格" v-model="price" value>
                    <span v-if="price">{{price*10000}} 积分</span>
                  </label>

                </form>
              </td>
              <td>
                <el-button type="text" icon="el-icon-edit" @click="toPay(price)">去支付</el-button>
              </td>
            </tr>
        </table>

      </div>
    </div>
    <el-dialog title="微信扫码支付" :visible.sync="isPaying" :before-close="cancelPay">
      <div class="cart py-container">
          <!--主内容-->
          <div class="checkout py-container  pay">
            <div class="checkout-tit">
              <h4 class="fl tit-txt"><span class="success-info">
              您正在购买{{price*10000}}积分，请您及时付款！订单号：{{payObj.out_trade_no}}</span>
              </h4>
              <span class="fr"><em class="sui-lead">应付金额：</em><em class="orange money">￥{{payObj.total_fee}}</em></span>
              <div class="clearfix"></div>
            </div>
            <div class="checkout-steps">
              <div class="fl weixin">微信支付</div>
              <div class="fl sao">
                <p class="red">请使用微信扫一扫。</p>
                <div class="fl code">
                  <!-- <img id="qrious" src="~/assets/img/erweima.png" alt=""> -->
                  <!-- <qriously value="weixin://wxpay/bizpayurl?pr=R7tnDpZ" :size="338"/> -->
                  <qriously :value="payObj.code_url" :size="338"/>
                </div>
                <div>
                  <p>请使用微信扫一扫</p>
                  <p>扫描二维码支付</p>
                </div>
              </div>
              <div class="clearfix"></div>
              <!-- <p><a href="pay.html" target="_blank">> 其他支付方式</a></p> -->

            </div>
          </div>
        </div>
    </el-dialog>

  </div>
</template>

<script>
import payApi from '@/api/pay'
import cookie from 'js-cookie'
export default {
  data() {
    return {
      isPaying: false,
      payObj: {},
      price: '',
      timer1: '',
      loginInfo: {}
    }
  },
  created() {
    this.init()
  },
  methods:{
    init(){
      var jsonStr = cookie.get('user_info')
      if(!jsonStr){
        this.$router.push({path: '/sign/login'})
        this.$message({
          type: 'warning',
          message: '请先登录'
        })
      }else {
        this.loginInfo = JSON.parse(jsonStr)
      }
    },
    toPay(price){
      this.price = price
      payApi.createNative(price)
        .then(response => {
          this.payObj = response.data.data
        }).then(() => {
          // this.isPaying = true
          this.paying()
        })
    },
    paying(){
      this.isPaying = true
      this.timer1 = setInterval(() => {
        this.getPayStatus(this.payObj.out_trade_no)
      }, 3000);
    },
    getPayStatus(out_trade_no){
      payApi.queryPayStatus(out_trade_no)
        .then(response => {
           if (response.data.success) {
              //如果支付成功，清除定时器
              clearInterval(this.timer1)
              var addPoint = this.price*10000
              var newPoint = this.loginInfo.point + addPoint
              this.isPaying = false
              this.$message({
                type: 'success',
                message: '支付成功!获得'+addPoint+'积分,您当前积分:' + newPoint
              })
              // 修改cookie 中的用户信息  TODO
              this.loginInfo.point = newPoint
              cookie.set("user_info", JSON.stringify(this.loginInfo), {domain:'localhost'})
              // 跳转至用户中心 TODO
              location.reload()

          }
        })
    },
    // 取消支付
    cancelPay(done){
      clearInterval(this.timer1)
      this.$message({
        type: 'info',
        message: '已取消支付'
      })
      return done()
    },
    testPay(price){
      var addPoint = price*10000
      var newPoint = this.loginInfo.point + addPoint
      this.$message({
        type: 'success',
        message: '支付成功!获得'+addPoint+'积分,您当前积分:' + newPoint
      })
      // 修改cookie 中的用户信息  TODO
      this.loginInfo.point = newPoint
      cookie.set("user_info", JSON.stringify(this.loginInfo), {domain:'localhost'})
    }
  }
}
</script>

<style scoped>
.checkout-steps{
  width: 500px;
}
.tit-txt{
  text-align: center;
}
.show{
  background-color: white;
  margin: 10px auto;
  width: 80%;
  height: 400px;
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
.now-point {
  margin-top: 10px;
  font-family: 楷体;
  text-align: center;
  font-size: 20px;
}
table {
  margin-top: 20px;
  text-align: center;
  font-family: 楷体;
  font-size: 20px;
  border-spacing: 20px;
}
td, th {
  border-bottom: 1px solid #5555ff;
}
</style>
