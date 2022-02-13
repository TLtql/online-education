<template>
  <div>
    <a href="javascript:void(0);" @click="toPay()" >去支付</a>
    <el-input v-model="price"></el-input>
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
export default {
  data() {
    return {
      isPaying: false,
      userId: '',
      payObj: {},
      price: '',
      timer1: ''
    }
  },
  created() {
    this.init()
  },
  methods:{
    init(){
      this.userId = this.$route.params.pid
    },
    toPay(){
      payApi.createNative(this.userId, this.price)
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
      payApi.queryPayStatus(this.userId, out_trade_no)
        .then(response => {
           if (response.data.success) {
              //如果支付成功，清除定时器
              clearInterval(this.timer1)
              var point = this.price*10000
              this.isPaying = false
              this.$message({
                type: 'success',
                message: '支付成功!获得'+point+'积分'
              })
              // 跳转至用户中心 TODO
              // 修改cookie 中的用户信息  TODO
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
    }
  }
}
</script>

<style>
.checkout-steps{
  width: 500px;
}
.tit-txt{
  text-align: center;
}

</style>
