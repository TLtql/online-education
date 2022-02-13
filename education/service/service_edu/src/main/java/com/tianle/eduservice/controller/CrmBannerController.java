package com.tianle.eduservice.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tianle.commonutils.R;
import com.tianle.eduservice.entity.CrmBanner;
import com.tianle.eduservice.service.CrmBannerService;
import com.tianle.servicebase.handler.CustomException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author Tianle
 * @since 2022-01-05
 */
@RestController
@RequestMapping("/eduservice/admin-banner")
@CrossOrigin
@Api(tags = "幻灯片后台管理")
public class CrmBannerController {

    @Autowired
    private CrmBannerService bannerService;

    @GetMapping("{page}/{limit}")
    public R getPageBanner(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit){
        Page<CrmBanner> pageParam = new Page<>(page, limit);
        bannerService.pageBanner(pageParam);
        return R.ok().data("items", pageParam);
    }

    @GetMapping("{id}")
    public R getBanner(@PathVariable String id){
        CrmBanner crmBanner = bannerService.getById(id);
        if (crmBanner == null){
            throw new CustomException(20001, "数据不存在");
        }
        return R.ok().data("banner", crmBanner);
    }

    @PostMapping("save-banner")
    public R saveBanner(@RequestBody CrmBanner banner){
        boolean save = bannerService.save(banner);
        if (!save){
            throw new CustomException(20001,"添加banner失败");
        }
        return R.ok();
    }
    @PutMapping("update")
    public R updateBanner(@RequestBody CrmBanner banner){
        boolean update = bannerService.updateById(banner);
        if (!update){
            throw new CustomException(20001,"更新banner失败");
        }
        return R.ok();
    }
    @DeleteMapping("{id}")
    public R deleteBanner(@PathVariable String id){
        boolean remove = bannerService.removeById(id);
        if (!remove){
            throw new CustomException(20001, "删除banner失败");
        }
        return R.ok();
    }

}

