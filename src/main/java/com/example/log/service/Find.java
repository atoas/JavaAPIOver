package com.example.log.service;

import org.bson.Document;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface Find {
    /**
     * 根据条件进行查询
     *
     * @param appId appid or channel or source or publish
     * @param platform    平台
     * @param countryCode 国家代码
     * @param adType      广告类型
     * @param device      设备类型
     * @return 返回查询结果的价格最大的一条记录
     */
    List<Document> query(String appId, String platform, String countryCode, String adType, String device, HttpServletRequest request);
}
