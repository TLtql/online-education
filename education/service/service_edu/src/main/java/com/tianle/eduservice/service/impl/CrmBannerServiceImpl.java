package com.tianle.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tianle.eduservice.entity.CrmBanner;
import com.tianle.eduservice.mapper.CrmBannerMapper;
import com.tianle.eduservice.service.CrmBannerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务实现类
 * </p>
 *
 * @author Tianle
 * @since 2022-01-05
 */
@Service
public class CrmBannerServiceImpl extends ServiceImpl<CrmBannerMapper, CrmBanner> implements CrmBannerService {

    @Override
    public void pageBanner(Page<CrmBanner> pageParam) {
        baseMapper.selectPage(pageParam, null);
    }

    @Override
    @Cacheable(value = "banner", key = "'selectIndexList'")
    public List<CrmBanner> getListBanner() {
        QueryWrapper<CrmBanner> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("sort");
        wrapper.last("limit 4");
        List<CrmBanner> list = baseMapper.selectList(wrapper);
        return list;
    }
}
