package com.tianle.eduservice.controller.front;

import com.tianle.commonutils.R;
import com.tianle.eduservice.entity.CrmBanner;
import com.tianle.eduservice.service.CrmBannerService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: Tianle
 * @Date: 2022/1/5 17:05
 * @Description: Banner前台控制器
 */
@RestController
@RequestMapping("/eduservice/front-banner")
@CrossOrigin
@Api(tags = "前台Banner显示")
public class FrontBannerController {
    @Autowired
    private CrmBannerService bannerService;

    @GetMapping("get-banner")
    public R getBanner(){
        List<CrmBanner> list = bannerService.getListBanner();
        return R.ok().data("list", list);
    }
}
