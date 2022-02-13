package com.tianle.userservice.client;

import com.tianle.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * @Author: Tianle
 * @Date: 2022/1/13 16:23
 * @Description:
 */
@Component
@FeignClient(name = "service-edu")
public interface EduServiceClient {

    /**
     * 更新购买数
     * @param courseId
     * @return
     */
    @PutMapping("/eduservice/front-course/update-buyCount/{courseId}")
    R updateBuyCount(@PathVariable("courseId") String courseId);

    /**
     * 删除用户评论
     * @param userId
     * @return
     */
    @DeleteMapping("/eduservice/front-comment/deleteCommentByUser/{userId}")
    R deleteCommentByUser(@PathVariable("userId") String userId);
}
