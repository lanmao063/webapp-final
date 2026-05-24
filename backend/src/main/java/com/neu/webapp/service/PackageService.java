package com.neu.webapp.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.neu.webapp.entity.Package;

import java.util.Map;

public interface PackageService extends IService<Package> {

    Map<String, Object> track(String trackingNumber);

    IPage<Map<String, Object>> search(String keyword, int page, int size);
}
