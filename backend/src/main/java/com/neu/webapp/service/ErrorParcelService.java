package com.neu.webapp.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.neu.webapp.entity.ErrorParcel;

public interface ErrorParcelService extends IService<ErrorParcel> {

    ErrorParcel registerError(String trackingNumber, String errorType, String description, String reportedBy);

    void handleError(Long id, String handlerName, String handleResult);

    IPage<ErrorParcel> search(String status, int page, int size);

    IPage<ErrorParcel> myErrors(String username, int page, int size);
}
