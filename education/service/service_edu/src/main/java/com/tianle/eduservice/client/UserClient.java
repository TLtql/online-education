package com.tianle.eduservice.client;

import com.tianle.commonutils.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * @Author: Tianle
 * @Date: 2022/1/13 17:59
 * @Description: 远程调用 service-user 模块
 */
@Component
@FeignClient(name = "service-user")
public interface UserClient {

    @PutMapping("/userservice/user-member/signIn/{id}/{point}")
    R userSignIn(@PathVariable("id") String id, @PathVariable("point") Integer point);
}
