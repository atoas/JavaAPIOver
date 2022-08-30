package com.example.log.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.log.service.Find;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonString;
import org.bson.Document;
import org.springframework.scheduling.concurrent.DefaultManagedAwareThreadFactory;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class FindImpl implements Find {
    @Override
    public List<Document> query(String appId, String platform, String countryCode,
                                String adType, String device, HttpServletRequest request) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                1,
                1,
                20,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(1),
                new DefaultManagedAwareThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );

        MongoClient client = new MongoClient("localhost", 9999);
        MongoCollection<Document> collection = client.getDatabase("pymongo").getCollection("py_coll");

        BsonDocument bsonDocument = new BsonDocument();

        if (appId != null) {
            bsonDocument.append("app_id", new BsonString(appId));
        }
        if (platform != null) {
            bsonDocument.append("platform", new BsonString(platform));
        }
        if (countryCode != null) {
            bsonDocument.append("country_code", new BsonString(countryCode));
        }
        if (adType != null) {
            bsonDocument.append("ad_type", new BsonString(adType));
        }
        if (device != null) {
            bsonDocument.append("device", new BsonString(device));
        }

        FindIterable<Document> documents = collection.find(bsonDocument).
                projection(new BsonDocument("_id", new BsonInt32(0))).
                sort(new Document("payout", -1)).limit(1);

        List<Document> documentArrayList = new ArrayList<>(1);
        for (Document document : documents) {
            documentArrayList.add(document);

            executor.submit(() -> {
                        JSONObject obj = JSONObject.parseObject(document.toJson());

                        Jedis jedis = new Jedis("localhost", 3697);

                        String id1 = jedis.get(obj.getString("offer_id"));
                        String id2 = obj.getString("offer_id");
                        if (id1 == null || !id1.equals(id2)) {
                            Map<String, String> map = new HashMap<>(7);
                            map.put("offer_name", obj.getString("offer_name"));
                            map.put("platform", obj.getString("platform"));
                            map.put("country_code", obj.getString("country_code"));
                            map.put("payout", obj.getString("payout"));
                            map.put("app_id", obj.getString("app_id"));
                            map.put("ad_type", obj.getString("ad_type"));
                            map.put("device", obj.getString("device"));
                            map.put("preview_url", obj.getString("preview_url"));
                            map.put("click_url", obj.getString("click_url"));
                            map.put("impression_url", obj.getString("impression_url"));
                            jedis.hset(obj.getString("offer_id"), map);
                        }
                    }
            );

        }

        executor.shutdown();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        String data = format.format(new Date());
        StringBuffer url = request.getRequestURL();
        String queryString = request.getQueryString();
        String ipaddress = request.getRemoteAddr();
        int port = request.getRemotePort();
        String sessionId = request.getRequestedSessionId();

        log.info("time:{}, url:{}, queryString:{}, ipaddress:{}, port:{}, sessionId:{},",
                data, url, queryString, ipaddress, port, sessionId);
        log.info("/find?app_Id={}&platform={}&country_code={}&ad_type={}&device={}在{}被请求一次",
                appId, platform, countryCode, adType, device, data);

        return documentArrayList;
    }
}
