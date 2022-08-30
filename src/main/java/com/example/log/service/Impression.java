package com.example.log.service;


import com.example.log.entity.Msg;

import javax.servlet.http.HttpServletRequest;

public interface Impression {

    Msg impression(String platform, String countryCode, String adType, String deviceId, String appId, String offerId, String imageId, HttpServletRequest request);
}
