package com.example.log.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.example.log.service.Install;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.Document;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Slf4j
public class InstallImpl implements Install {
    @Override
    public String install(HttpServletRequest request) {
        MongoClient client = new MongoClient("localhost", 9999);
        MongoCollection<Document> collection = client.getDatabase("pymongo").getCollection("test");

        FindIterable<Document> limit = collection.find().
                projection(new BsonDocument("install_url", new BsonInt32(1)).
                        append("_id", new BsonInt32(0))).sort(new Document("payout", -1)).limit(1);

        String installUrl = "";
        for (Document document : limit) {
            JSONObject obj = JSONObject.parseObject(document.toJson());
            installUrl = (String) obj.get("install_url");
        }


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        String data = format.format(new Date());
        StringBuffer url = request.getRequestURL();
        String queryString = request.getQueryString();
        String ipaddress = request.getRemoteAddr();
        int port = request.getRemotePort();
        String sessionId = request.getRequestedSessionId();

        log.info("time:{}, url:{}, queryString:{}, ipaddress:{}, port:{}, sessionId:{},",
                data, url, queryString, ipaddress, port, sessionId);

        log.info("The app {} is installed", installUrl);

        return installUrl;

    }
}
