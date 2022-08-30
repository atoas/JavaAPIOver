package com.example.log.service.impl;

import com.example.log.service.Click;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Slf4j
public class ClickImpl implements Click {
    @Override
    public void click(HttpServletRequest request) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        String data = format.format(new Date());
        StringBuffer url = request.getRequestURL();
        String queryString = request.getQueryString();
        String ipaddress = request.getRemoteAddr();
        int port = request.getRemotePort();
        String sessionId = request.getRequestedSessionId();

        log.info("time:{}, url:{}, queryString:{}, ipaddress:{}, port:{}, sessionId:{},",
                data, url, queryString, ipaddress, port, sessionId);
        log.info("The AD was hit once at {}", data);
    }
}
