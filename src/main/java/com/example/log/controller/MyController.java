package com.example.log.controller;

import com.example.log.entity.Msg;
import com.example.log.service.Click;
import com.example.log.service.Find;
import com.example.log.service.Impression;
import com.example.log.service.Install;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
public class MyController {

    @Autowired
    private Find find;

    @GetMapping("/find")
    public List<Document> query(
            @RequestParam(value = "app_Id", required = false) String appId,
            @RequestParam(value = "platform", required = false) String platform,
            @RequestParam(value = "country_code", required = false) String countryCode,
            @RequestParam(value = "ad_type", required = false) String adType,
            @RequestParam(value = "device", required = false) String device,
            HttpServletRequest request
    ) {
        return find.query(appId, platform, countryCode, adType, device, request);
    }

    @Autowired
    private Impression impression;

    @GetMapping("/impression")
    public Msg impression(
            @RequestParam(value = "platform", required = false) String platform,
            @RequestParam(value = "country_code", required = false) String countryCode,
            @RequestParam(value = "ad_type", required = false) String adType,
            @RequestParam(value = "device_id", required = false) String deviceId,
            @RequestParam(value = "app_id", required = false) String appId,
            @RequestParam(value = "offer_id", required = false) String offerId,
            @RequestParam(value = "image_id", required = false) String imageId,
            HttpServletRequest request) {
        return this.impression.impression(platform, countryCode, adType, deviceId, appId, offerId, imageId, request);
    }

    @Autowired
    private Click click;

    @GetMapping("/click")
    public Msg click(HttpServletRequest request) {
        String installUrl = this.click.click(request);
        return new Msg(installUrl, getUUID());
    }


    @Autowired
    private Install install;

    @GetMapping("/install")
    public Msg install(HttpServletRequest request) {

        install.install(request);

        return getMsg();
    }

    private String getUUID() {
        String s = UUID.randomUUID().toString();
        String[] split = s.split("-");
        StringBuilder sb = new StringBuilder();
        for (String s1 : split) {
            sb.append(s1);
        }

        return sb.toString();
    }

    private Msg getMsg() {
        String s = UUID.randomUUID().toString();
        String[] split = s.split("-");
        StringBuilder sb = new StringBuilder();
        for (String s1 : split) {
            sb.append(s1);
        }

        String str = sb.toString();
        return new Msg("Ok, Thanks for install! ", str);
    }
}
