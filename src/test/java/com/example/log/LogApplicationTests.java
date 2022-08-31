package com.example.log;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonString;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SpringBootTest
class ApImongoApplicationTests {

    @Test
    void contextLoads() {
        for (int i = 0; i < 20; i++) {
            int v = (int) (Math.random() * 5);
            System.out.println("v = " + v);
        }
    }

    //插入app_id
    @Test
    public void insetAppId() {
        MongoClient client = new MongoClient("localhost", 9999);
        MongoCollection<Document> collection = client.getDatabase("pymongo").getCollection("py_coll");

        long len = collection.countDocuments();

        for (int i = 0; i < len; i++) {
            String s = UUID.randomUUID().toString();
            String[] split = s.split("-");
            StringBuilder s2 = new StringBuilder();
            for (String s1 : split) {
                s2.append(s1);
            }
            Bson gender = Filters.exists("app_id", false);

            Document document = new Document("$set", new Document("app_id", s2.toString()));
            collection.updateOne(gender, document);
        }
    }

    //插入ad_type
    @Test
    public void insertAdType() {
        MongoClient client = new MongoClient("localhost", 9999);
        MongoCollection<Document> collection = client.getDatabase("pymongo").getCollection("py_coll");

        long len = collection.countDocuments();

        String[] adType = {"picture", "video"};

        for (int i = 0; i < len; i++) {
            int v = (int) (Math.random() * 2);
            Bson gender = Filters.exists("ad_type", false);

            Document document = new Document("$set", new Document("ad_type", adType[v]));
            collection.updateOne(gender, document);
        }
    }

    //插入device
    @Test
    public void insertDevice() {
        MongoClient client = new MongoClient("localhost", 9999);
        MongoCollection<Document> collection = client.getDatabase("pymongo").getCollection("py_coll");

        long len = collection.countDocuments();

        String[] device = {"computer", "phone", "tablet", "Set Top Box", "TV"};

        for (int i = 0; i < len; i++) {
            int v = (int) (Math.random() * 5);
            Bson gender = Filters.exists("device", false);

            Document document = new Document("$set", new Document("device", device[v]));
            collection.updateOne(gender, document);
        }
    }

    @Test
    public void test1(){
        MongoClient client = new MongoClient("localhost", 9999);
        MongoCollection<Document> collection = client.getDatabase("pymongo").getCollection("py_coll");
        collection.updateMany(Filters.exists("impression_url"),
                new Document("$set",new Document("impression_url","http://localhost:8888/impression")));
    }

    @Test
    public void test5(){
        MongoClient client = new MongoClient("localhost", 9999);
        MongoCollection<Document> collection = client.getDatabase("pymongo").getCollection("py_coll");
        collection.updateMany(Filters.exists("impression_url"),
                new Document("$set",new Document("preview_url","https://www.ximalaya.com/down/lite?client=mac")));
    }

    @Test
    public void test() {
        MongoClient client = new MongoClient("localhost", 9999);
        MongoCollection<Document> collection = client.getDatabase("pymongo").getCollection("py_coll");
        long len = collection.countDocuments();

        for (int i = 0; i < len; i++) {

            Document document = new Document("$unset", new Document("click_url:", ""));
            collection.updateOne(Filters.exists("click_url:", true), document);

        }

    }

    @Test
    public void test2() {
        MongoClient client = new MongoClient("localhost", 9999);
        MongoCollection<Document> collection = client.getDatabase("pymongo").getCollection("py_coll");

        BsonDocument bsonDocument = new BsonDocument();
        bsonDocument.append("platform", new BsonString("IOS"));
        bsonDocument.append("device", new BsonString("TV"));
        FindIterable<Document> documents = collection.find(bsonDocument).sort(new Document("payout", -1)).limit(1);

        for (Document document : documents) {
            JSONObject obj = JSONObject.parseObject(document.toJson());

            Jedis jedis = new Jedis("localhost", 3697);

            jedis.set("offer_id", obj.getString("offer_id"));
            String offer_id = jedis.get(obj.getString("offer_id"));
            String offer_id1 = obj.getString("offer_id");
            if (offer_id == null || !offer_id.equals(offer_id1)) {
                Map<String, String> map = new HashMap<>(7);
                map.put("offer_name", obj.getString("offer_name"));
                map.put("platform", obj.getString("platform"));
                map.put("country_code", obj.getString("country_code"));
                map.put("payout", obj.getString("payout"));
                map.put("app_id", obj.getString("app_id"));
                map.put("ad_type", obj.getString("ad_type"));
                map.put("device", obj.getString("device"));
                jedis.hset(obj.getString("offer_id"), map);
            }

        }
    }

    @Test
    public void test4(){
        Jedis jedis = new Jedis("localhost", 3697);
        String offerId = jedis.hget("o_f21d0ae79a","preview_url");
        System.out.println("offerId = " + offerId);
    }

    @Test
    public void test3(){
        Jedis jedis = new Jedis("localhost", 3697);
        long hset = jedis.hset("o_f21d0ae79a", "preview_url", "https://www.ximalaya.com/down/lite?client=mac");
        System.out.println("hset = " + hset);
    }


    @Test
    public void test6(){
        MongoClient client = new MongoClient("localhost", 9999);
        MongoCollection<Document> collection = client.getDatabase("pymongo").getCollection("test");

        FindIterable<Document> limit = collection.find().
                projection(new BsonDocument("install_url", new BsonInt32(1)).
                        append("_id", new BsonInt32(0))).sort(new Document("payout", -1)).limit(1);
        for (Document document : limit) {
            System.out.println("document = " + document);
        }
    }

}
