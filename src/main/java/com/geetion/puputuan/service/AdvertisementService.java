package com.geetion.puputuan.service;

import com.geetion.puputuan.common.mybatis.PageEntity;
import com.geetion.puputuan.common.mybatis.PagingResult;
import com.geetion.puputuan.model.Advertisement;

import java.util.List;
import java.util.Map;

public interface AdvertisementService {

    Advertisement getAdvertisementById(Long id);

    Advertisement getAdvertisement(Map param);

    List<Advertisement> getAdvertisementList(Map param);

    PagingResult<Advertisement> getAdvertisementPage(PageEntity pageEntity);

    boolean addAdvertisement(Advertisement object);

    boolean updateAdvertisement(Advertisement object);

    boolean removeAdvertisement(Long id);
}
