package com.tianle.eduservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tianle.eduservice.entity.CrmBanner;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务类
 * </p>
 *
 * @author Tianle
 * @since 2022-01-05
 */
public interface CrmBannerService extends IService<CrmBanner> {

    void pageBanner(Page<CrmBanner> pageParam);

    List<CrmBanner> getListBanner();
}
