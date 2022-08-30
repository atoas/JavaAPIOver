package com.example.log.service.impl;

import com.example.log.entity.Msg;
import com.example.log.service.Impression;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


@Service
@Slf4j
public class Impressionimpl implements Impression {

    @Override
    public Msg impression(String platform, String countryCode, String adType, String deviceId,
                          String appId, String offerId, String imageId, HttpServletRequest request) {

        String s = UUID.randomUUID().toString();
        String[] split = s.split("-");
        StringBuilder sb = new StringBuilder();
        for (String s1 : split) {
            sb.append(s1);
        }

        String str = sb.toString();
        Msg msg = new Msg("", str);

        if (!StringUtils.hasText(platform)) {
            msg.setMessage("platform Missing required parameters");
            return msg;
        }
        if (!StringUtils.hasText(countryCode)) {
            msg.setMessage("country_code Missing required parameters");
            return msg;
        }
        if (!StringUtils.hasText(adType)) {
            msg.setMessage("ad_type Missing required parameters");
            return msg;
        }
        if (!StringUtils.hasText(deviceId)) {
            msg.setMessage("device_id Missing required parameters");
            return msg;
        }
        if (!StringUtils.hasText(appId)) {
            msg.setMessage("app_id Missing required parameters");
            return msg;
        }
        if (!StringUtils.hasText(offerId)) {
            msg.setMessage("offer_id Missing required parameters");
            return msg;
        }
        if (!StringUtils.hasText(imageId)) {
            msg.setMessage("image_id Missing required parameters");
            return msg;
        }

//        ThreadPoolExecutor executor = new ThreadPoolExecutor(
//                1,
//                1,
//                20,
//                TimeUnit.SECONDS,
//                new LinkedBlockingDeque<>(1),
//                new DefaultManagedAwareThreadFactory(),
//                new ThreadPoolExecutor.CallerRunsPolicy()
//        );
//
//        executor.submit(() -> {
//            Jedis jedis = new Jedis("localhost", 3697);
//            String s12 = jedis.get(offerId);
//            if (s12 == null || s12.isBlank()) {
//                jedis.set(offerId, String.valueOf(1));
//            } else {
//                jedis.incr(offerId);
//            }
//        });
//
//        executor.shutdown();
//
//        Jedis jedis = new Jedis("localhost", 3697);
//        String previewUrl = jedis.hget("o_f21d0ae79a", "preview_url");
//        log.info("the impression_url is:{}", previewUrl);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        String data = format.format(new Date());
        StringBuffer url = request.getRequestURL();
        String queryString = request.getQueryString();
        String ipaddress = request.getRemoteAddr();
        int port = request.getRemotePort();
        String sessionId = request.getRequestedSessionId();

        log.info("time:{}, url:{}, queryString:{}, ipaddress:{}, port:{}, sessionId:{},",
                data, url, queryString, ipaddress, port, sessionId);

        log.info("AD ID: 123 was shown once at {}", data);

        return new Msg("This is my advertisement display page, hope you like it!", str);
    }
}
