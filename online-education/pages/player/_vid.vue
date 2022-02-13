<template>
  <div>
    <head>
      <meta charset="utf-8">
      <meta http-equiv="x-ua-compatible" content="IE=edge" >
      <meta name="viewport" content="width=device-width, height=device-height, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no"/>
      <link rel="stylesheet" href="https://g.alicdn.com/de/prismplayer/2.9.17/skins/default/aliplayer-min.css" />
      <script type="text/javascript" charset="utf-8" src="https://g.alicdn.com/de/prismplayer/2.9.17/aliplayer-min.js"></script>
      <script src="./CommentCoreLibrary.js"></script>
      <!-- 请下载之后使用 -->
      <script type="text/javascript" charset="utf-8" src="/aliplayercomponents-1.0.8.min.js"></script>
    </head>
    
    <div id="J_prismPlayer" class="prism-player" />
  </div>
</template>

<script>
import vod from '@/api/vod'
export default {


  layout: 'video',//应用video布局
  asyncData({ params, error }) {
    return vod.getPlayAuth(params.vid).then(response => {
      // console.log(response.data.data)
      return {
        vid: params.vid,
        playAuth: response.data.data.playAuth
      }
    })
  },
  mounted() {
      var danmukuList = []
      new Aliplayer({
          id: 'J_prismPlayer',
          vid: this.vid, // 视频id
          playauth: this.playAuth, // 播放凭证
          encryptType: '1', // 如果播放加密视频，则需设置encryptType=1，非加密视频无需设置此项
          width: '800px',
          height: '500px',


          // 以下可选设置
          cover: 'http://guli.shop/photo/banner/1525939573202.jpg', // 封面
          qualitySort: 'asc', // 清晰度排序

          mediaType: 'video', // 返回音频还是视频
          autoplay: false, // 自动播放
          isLive: false, // 直播
          rePlay: false, // 循环播放
          preload: true,
          controlBarVisibility: 'hover', // 控制条的显示方式：鼠标悬停
          useH5Prism: true, // 播放器类型：html5
          components: [{
            name: 'AliplayerDanmuComponent',
            type: AliPlayerComponent.AliplayerDanmuComponent,
            args: [danmukuList]
          }]
      }, function(player) {
          console.log('播放器创建成功')
      })
  }
}
</script>

<style>
  .prism-player{
    margin-left: auto;
    margin-right:auto;
    display:block;
  }
</style>
