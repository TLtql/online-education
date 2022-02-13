<template>
  <div class="app-container">

      <h2 style="text-align: center;">发布新课程</h2>

      <el-steps :active="2" process-status="wait" align-center style="margin-bottom: 40px;">
        <el-step title="填写课程基本信息"/>
        <el-step title="创建课程大纲"/>
        <el-step title="最终发布"/>
      </el-steps>


      <el-button type="text" @click="dialogChapterFormVisible = true; chapter = {}">添加章节</el-button>
      <!-- 章节 -->
      <ul class="chanpterList">
          <li
              v-for="chapter in chapterList"
              :key="chapter.id">
              <p>
                  {{ chapter.title }}
                  <span class="acts">
                        <el-button type="text"
                        @click="dialogVideoFormVisible = true;
                                video = {};
                                video.chapterId = chapter.id;
                                fileList = []" >添加课时</el-button>
                        <el-button type="text" @click="editChapter(chapter.id)">编辑</el-button>
                        <el-button type="text" @click="deleteChapter(chapter.id)">删除</el-button>
                  </span>
              </p>

              <!-- 视频 -->
              <ul class="chanpterList videoList">
                  <li
                      v-for="video in chapter.children"
                      :key="video.id">
                      <p>{{ video.title }}
                          <span class="acts">
                              <el-button type="text" @click="editVideo(video.id)">编辑</el-button>
                              <el-button type="text" @click="deleteVideo(video.id)">删除</el-button>
                          </span>
                      </p>
                  </li>
              </ul>
          </li>
      </ul>

      <div>
          <el-button @click="previous">上一步</el-button>
          <el-button :disabled="saveBtnDisabled" type="primary" @click="next">下一步</el-button>
      </div>

      <!-- 添加和修改章节表单 -->
      <el-dialog :visible.sync="dialogChapterFormVisible" title="添加章节">
          <el-form :model="chapter" label-width="120px">
              <el-form-item label="章节标题">
                  <el-input v-model="chapter.title"/>
              </el-form-item>
              <el-form-item label="章节排序">
                  <el-input-number v-model="chapter.sort" :min="0" controls-position="right"/>
              </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
              <el-button @click="dialogChapterFormVisible = false">取 消</el-button>
              <el-button type="primary" @click="saveOrUpdate">确 定</el-button>
          </div>
      </el-dialog>
      <!-- 添加和修改课时表单 -->
      <el-dialog :visible.sync="dialogVideoFormVisible" title="添加课时">
        <el-form :model="video" :rules="rules" ref="video" label-width="120px">
          <el-form-item label="课时标题" prop="title">
            <el-input v-model="video.title"/>
          </el-form-item>
          <el-form-item label="课时排序">
            <el-input-number v-model="video.sort" :min="0" controls-position="right"/>
          </el-form-item>
          <el-form-item label="是否免费">
            <el-radio-group v-model="video.free">
              <el-radio :label="true">免费</el-radio>
              <el-radio :label="false">默认</el-radio>
            </el-radio-group>
          </el-form-item>

          <el-form-item prop="videoSourceId" label="上传视频">
              <el-upload
                     :on-success="handleVodUploadSuccess"
                     :on-remove="handleVodRemove"
                     :before-remove="beforeVodRemove"
                     :on-exceed="handleUploadExceed"
                     :file-list="fileList"
                     :action="BASE_API+'/vodService/video/uploadVideo'"
                     :limit="1"
                     class="upload-demo">
              <el-button size="small" type="primary">上传视频</el-button>
              <el-tooltip placement="right-end">
                  <div slot="content">最大支持1G，<br>
                      支持3GP、ASF、AVI、DAT、DV、FLV、F4V、<br>
                      GIF、M2T、M4V、MJ2、MJPEG、MKV、MOV、MP4、<br>
                      MPE、MPG、MPEG、MTS、OGG、QT、RM、RMVB、<br>
                      SWF、TS、VOB、WMV、WEBM 等视频格式上传</div>
                  <i class="el-icon-question"/>
              </el-tooltip>
              </el-upload>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogVideoFormVisible = false">取 消</el-button>
          <el-button :disabled="saveVideoBtnDisabled" type="primary" @click="saveOrUpdateVideo('video')">确 定</el-button>
        </div>
      </el-dialog>
    </div>
</template>

<script>
  import chapter from '@/api/edu/chapter'
  import video from '@/api/edu/video'
  import vod from '@/api/edu/vod'
  export default {
    data() {
      return {
        saveBtnDisabled: false, // 保存按钮是否禁用
        courseId: null,
        chapterList: [],
        dialogChapterFormVisible: false,
        chapter: {// 章节对象
          title: '',
          sort: 0
        },
        saveVideoBtnDisabled: false, // 课时按钮是否禁用
        dialogVideoFormVisible: false, // 是否显示课时表单
        video: {// 课时对象
          chapterId: '', // 课时所在的章节id
          title: '',
          sort: 0,
          free: 0,
          videoSourceId: ''
        },
        fileList: [],//上传文件列表
        BASE_API: process.env.BASE_API, // 接口API地址
        rules: {
                videoSourceId: [
                  { required: true, message: '视屏未完成上传', trigger: 'blur' },
                ],
                title: [
                  { required: true, message: '请输入课时标题', trigger: 'blur' }
                ]
        }
      }
    },

    created() {
      console.log('chapter created')
      this.init()
      console.log(this.courseId)
    },

    methods: {
      previous() {
        console.log('previous')
        this.$router.push({ path: '/edu/course/info/' + this.courseId })
      },
      init(){
        if (this.$route.params && this.$route.params.id) {
            this.courseId = this.$route.params.id
            // 根据id获取课程基本信息
            this.getChapterListByCourseId()
          }
      },
      //=================== 章节操作  ========================================
      getChapterListByCourseId(){
        chapter.getChapterList(this.courseId)
          .then(response => {
            this.chapterList = response.data.list
          })
      },
      editChapter(chapterId){
        console.log(chapterId)
        this.dialogChapterFormVisible = true
        chapter.getOneChapter(chapterId)
          .then(response => {
            this.chapter = response.data.chapter
          })
      },
      saveOrUpdate(){
        if(this.chapter.id){
          this.updateChapter()
          this.dialogChapterFormVisible = false
        }else {
          this.saveChapter()
          this.dialogChapterFormVisible = false
        }
      },
      updateChapter(){
        console.log(this.chapter)
        chapter.updateChapterById(this.chapter)
          .then(response => {
            this.$message({
              type: 'success',
              message: '修改章节信息成功'
            })
          }).then(() => {
            this.getChapterListByCourseId()
          })

      },
      saveChapter(){
        console.log(this.chapter)
        chapter.addChapter(this.chapter, this.courseId)
          .then(() => {
            this.$message({
              type: 'success',
              message: '添加章节成功'
            })
          }).then(() => {
            this.getChapterListByCourseId()
          })
      },
      deleteChapter(chapterId){
        this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
               confirmButtonText: '确定',
               cancelButtonText: '取消',
               type: 'warning'
           }).then(() => {
               return chapter.deleteChapterById(chapterId)
              }).then(() => {
                this.$message({
                  type: 'success',
                  message: '删除章节成功'
                })
            }).then(() => {
            this.getChapterListByCourseId()
          }).catch((response) => { // 失败
              if (response === 'cancel') {
                  this.$message({
                      type: 'info',
                      message: '已取消删除'
                  })
              }
          })
      },
      //===================== 小节操作 =============================================================
      saveOrUpdateVideo(formName){
        this.$refs[formName].validate((valid) => {
          if (valid) {
            if(this.video.id){
              this.updateVideo()
              this.dialogVideoFormVisible = false
            }else {
              this.video.courseId=this.courseId
              this.saveVideo()
              this.dialogVideoFormVisible = false
            }
          } else {
              console.log('error submit!!');
              return false;
          }
        })
      },
      saveVideo(){
        video.saveVideoInfo(this.video)
          .then(response => {
            this.$message({
              type: 'success',
              message: '添加课时成功'
            })
          }).then(() => {
            this.getChapterListByCourseId()
          })
      },
      updateVideo(){
        video.updateVideoInfo(this.video)
          .then(response => {
            this.$message({
              type: 'success',
              message: '更新课时成功'
            })
          }).then(() => {
            this.getChapterListByCourseId()
          })
      },
      editVideo(videoId){
        this.dialogVideoFormVisible = true
        video.getOneVideoInfo(videoId)
          .then(response => {
            this.video = response.data.video
            if(this.video.videoOriginalName){
              this.fileList = [{'name': this.video.videoOriginalName}]
            } else {
              this.fileList = []
            }
          })
      },
      deleteVideo(videoId){
        this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
               confirmButtonText: '确定',
               cancelButtonText: '取消',
               type: 'warning'
           }).then(() => {
               return video.deleteVideoById(videoId)
              }).then(() => {
                this.$message({
                  type: 'success',
                  message: '删除章节成功'
                })
            }).then(() => {
            this.getChapterListByCourseId()
          }).catch((response) => { // 失败
              if (response === 'cancel') {
                  this.$message({
                      type: 'info',
                      message: '已取消删除'
                  })
              }
          })
      },
      //============================ 课程视屏上传 =======================
      handleVodUploadSuccess(response, file, fileList){
        this.video.videoSourceId = response.data.videoSourceId
        this.video.videoOriginalName = file.name
        this.$message({
          type: "success",
          message: "视屏上传成功"
        })
      },
      handleUploadExceed(files, fileList){
        this.$message.warning('想要重新上传视频，请先删除已上传的视频')
      },
      beforeVodRemove(file, fileList){
        return this.$confirm(`确定移除 ${file.name}？`)
      },
      handleVodRemove(file, fileList){
        vod.removeVideo(this.video.videoSourceId)
          .then(() => {
            this.video.videoSourceId = ''
            this.video.videoOriginalName = ''
            this.fileList = []
            this.$message({
                type: 'success',
                message: '删除视屏成功'
            })
          })
      },
      next() {
        console.log('next')
        this.$router.push({ path: '/edu/course/publish/' + this.courseId })
      }
    }
  }
</script>

<style scoped>
.chanpterList{
    position: relative;
    list-style: none;
    margin: 0;
    padding: 0;
}
.chanpterList li{
  position: relative;
}
.chanpterList p{
  float: left;
  font-size: 20px;
  margin: 10px 0;
  padding: 10px;
  height: 70px;
  line-height: 50px;
  width: 100%;
  border: 1px solid #DDD;
}
.chanpterList .acts {
    float: right;
    font-size: 14px;
}

.videoList{
  padding-left: 50px;
}
.videoList p{
  float: left;
  font-size: 14px;
  margin: 10px 0;
  padding: 10px;
  height: 50px;
  line-height: 30px;
  width: 100%;
  border: 1px dotted #DDD;
}

</style>
