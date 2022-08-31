package com.example.log.service.impl;


import com.example.log.service.Install;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Slf4j
public class InstallImpl implements Install {
    @Override
    public void install(HttpServletRequest request) {


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        String data = format.format(new Date());
        StringBuffer url = request.getRequestURL();
        String queryString = request.getQueryString();
        String ipaddress = request.getRemoteAddr();
        int port = request.getRemotePort();
        String sessionId = request.getRequestedSessionId();

        log.info("time:{}, url:{}, queryString:{}, ipaddress:{}, port:{}, sessionId:{},",
                data, url, queryString, ipaddress, port, sessionId);
        log.info("The app was installed at{}", data);
    }
}
