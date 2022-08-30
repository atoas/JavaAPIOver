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

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
public class MyController {

    @GetMapping("/test")
    public void test(@RequestParam(value = "name", required = false) String name,
                     @RequestParam(value = "age", required = false) String age,
                     HttpServletRequest request) {
        HttpSession session = request.getSession();
        System.out.println("session = " + session.getId());
//        for (Cookie cookie : request.getCookies()) {
//            log.info("cookie is :{}", cookie);
//        }

        String authType = request.getAuthType();
        System.out.println("authType = " + authType);
        String contextPath = request.getContextPath();
        System.out.println("contextPath = " + contextPath);
        String method = request.getMethod();
        System.out.println("method = " + method);
        Enumeration<String> headerNames = request.getHeaderNames();
        System.out.println("headerNames = " + headerNames);
        HttpServletMapping httpServletMapping = request.getHttpServletMapping();
        System.out.println("httpServletMapping = " + httpServletMapping.getMatchValue());
        try {
            for (Part part : request.getParts()) {
                System.out.println("part = " + part);
            }
        } catch (IOException | ServletException e) {
            e.printStackTrace();
        }

        String pathInfo = request.getPathInfo();
        System.out.println("pathInfo = " + pathInfo);
        String pathTranslated = request.getPathTranslated();
        System.out.println("pathTranslated = " + pathTranslated);
        String queryString = request.getQueryString();
        System.out.println("queryString = " + queryString);
        String remoteUser = request.getRemoteUser();
        System.out.println("remoteUser = " + remoteUser);
        String requestedSessionId = request.getRequestedSessionId();
        System.out.println("requestedSessionId = " + requestedSessionId);
        for (Cookie requestCookie : request.getCookies()) {
            System.out.println("requestCookie = " + requestCookie.getValue());
            System.out.println("requestCookie = " + requestCookie.getComment());
            System.out.println("requestCookie = " + requestCookie.getDomain());
            System.out.println("requestCookie = " + requestCookie.getName());
            System.out.println("requestCookie = " + requestCookie.getPath());
            System.out.println("requestCookie = " + requestCookie.getMaxAge());
            System.out.println("requestCookie = " + requestCookie.getSecure());
            System.out.println("requestCookie = " + requestCookie.getVersion());
            System.out.println("requestCookie = " + requestCookie.getClass());
        }
        StringBuffer requestURL = request.getRequestURL();
        System.out.println("requestURL = " + requestURL);
        String requestURI = request.getRequestURI();
        System.out.println("requestURI = " + requestURI);
        String servletPath = request.getServletPath();
        System.out.println("servletPath = " + servletPath);
        Map<String, String> trailerFields = request.getTrailerFields();
        System.out.println("trailerFields = " + trailerFields);
        Principal userPrincipal = request.getUserPrincipal();
        System.out.println("userPrincipal = " + userPrincipal);
//        AsyncContext asyncContext = request.getAsyncContext();
//        System.out.println("asyncContext = " + asyncContext);
        Enumeration<String> attributeNames = request.getAttributeNames();
        System.out.println("attributeNames = " + attributeNames.toString());
        String characterEncoding = request.getCharacterEncoding();
        System.out.println("characterEncoding = " + characterEncoding);
        String contentType = request.getContentType();
        System.out.println("contentType = " + contentType);
        Enumeration<String> parameterNames = request.getParameterNames();
        System.out.println("parameterNames = " + parameterNames.toString());
        String remoteAddr = request.getRemoteAddr();
        System.out.println("remoteAddr = " + remoteAddr);
        String remoteHost = request.getRemoteHost();
        System.out.println("remoteHost = " + remoteHost);
        int remotePort = request.getRemotePort();
        System.out.println("remotePort = " + remotePort);


        log.error("this is error");
        log.info("this is error");
        log.debug("this is error");
        log.trace("this is error");
        log.warn("this is error");

        throw new RuntimeException("test error log");
    }

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
        click.click(request);
        return getMsg();
    }


    @Autowired
    private Install install;

    @GetMapping("/install")
    public Msg install(HttpServletRequest request) {
        String uuid = getUUID();

//        Jedis jedis = new Jedis("localhost", 3697);
//        String preview_url = jedis.get("preview_url");
        String installUrl = this.install.install(request);

        return new Msg(installUrl, uuid);
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
        return new Msg("Ok, Thanks for clicking! ", str);
    }
}
