package com.tianle.eduservice.controller.front;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tianle.commonutils.JwtUtils;
import com.tianle.commonutils.R;
import com.tianle.eduservice.entity.EduComment;
import com.tianle.eduservice.entity.vo.front.FrontCommentVo;
import com.tianle.eduservice.service.EduCommentService;
import com.tianle.servicebase.handler.CustomException;
import io.netty.util.internal.UnstableApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: Tianle
 * @Date: 2022/1/11 18:45
 * @Description: 评论前台接口
 */
@RestController
@CrossOrigin
@RequestMapping("/eduservice/front-comment")
@Api(tags = "评论前台接口")
public class FrontCommentController {
    @Autowired
    private EduCommentService commentService;

    @GetMapping("{page}/{limit}/{courseId}")
    public R getComment(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,
            @PathVariable String courseId){
        Page<EduComment> pageParam = new Page<>(page,limit);
        Map<String,Object> map = commentService.pageList(pageParam,courseId);
        return R.ok().data(map);
    }
    @PostMapping("saveComment")
    public R addComment(@RequestBody EduComment comment){
        boolean save = commentService.save(comment);
        if (!save){
            throw new CustomException(20001,"评论失败！");
        }
        return R.ok();
    }

    @DeleteMapping("deleteComment/{id}")
    public R deleteComment(@PathVariable String id){
        boolean remove = commentService.removeById(id);
        if (remove){
            return R.ok();
        }else {
            return R.error().message("删除评论失败");
        }
    }
    @GetMapping("getUserComment")
    public R getUserComment(HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if (!StringUtils.hasLength(userId)){
            throw new CustomException(20001, "用户未登录");
        }
        List<FrontCommentVo> commentList = commentService.getUserComment(userId);
        return R.ok().data("commentList",commentList);
    }

    @DeleteMapping("deleteCommentByUser/{userId}")
    @ApiOperation("根据用户Id删除评论")
    public R deleteCommentByUser(@PathVariable String userId){
        commentService.deleteCommentByUserId(userId);
        return R.ok();
    }
}
