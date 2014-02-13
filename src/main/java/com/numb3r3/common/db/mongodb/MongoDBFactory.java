package com.numb3r3.common.db.mongodb;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.*;

/**
 * A factory class of MongoDB database
 *
 * @author Feng Wang
 *
 */
public class MongoDBFactory {

    /**
     * The default database name
     */
    private final static String DEFAULT_DB_NAME = "MONGO-APP-DEFAULT";

    /**
     * The default database host url
     */
    private final static String DEFAULT_HOST = "localhost";

    /**
     * The default database port
     */
    private final static int DEFAULT_PORT = 27017;

    /**
     * The MongoDB DB class instance
     */
//    private static DB db;

    public static Map<String, DB> dbs = Maps.newHashMap();

    public static DB cur_db = null;

    public static void init(String dbName) {
        try {
            MongoClient mg = new MongoClient(DEFAULT_HOST, DEFAULT_PORT);
            DB db = mg.getDB(dbName);
            cur_db = db;
            dbs.put(dbName, db);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (MongoException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
    }


    /**
     * Get the DB instance.
     * @return
     */
    public static DB getDB() {
        return cur_db;
    }

    /**
     * Get the DB instance.
     * @return
     */
    public static DB getDB(String dbName) {
        if (dbs.containsKey(dbName)) {
            //cur_db = dbs.get(dbName);
            return dbs.get(dbName);
        } else {
            try {
                MongoClient mg = new MongoClient(DEFAULT_HOST, DEFAULT_PORT);
                DB db = mg.getDB(dbName);
                //cur_db = db;
                dbs.put(dbName, db);
                return db;
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            } catch (MongoException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
        }


    }

    public static void use(String dbName){
        cur_db = getDB(dbName);
    }

    public static DBCollection collection(String collectionName) {
        DBCollection collection = cur_db.getCollection(collectionName);
        return collection;
    }

    public static DBCollection collection(String dbName, String collectionName) {
        DB db = getDB(dbName);
        DBCollection collection = db.getCollection(collectionName);
        return collection;
    }


    public static DBCursor find(String collectionName, BasicDBObject searchQuery) {
        DBCollection collection = collection(collectionName);
        DBCursor cursor = collection.find(searchQuery);
        return cursor;
    }

    public static DBCursor find(String dbName, String collectionName, BasicDBObject searchQuery) {
        DBCollection collection = collection(dbName, collectionName);
        DBCursor cursor = collection.find(searchQuery);
        return cursor;
    }

    public static DBObject findOne(String collectionName, BasicDBObject searchQuery) {
        DBCollection collection = collection(collectionName);
        return collection.findOne(searchQuery);
    }

    public static DBObject findOne(String dbName, String collectionName, BasicDBObject searchQuery) {
        DBCollection collection = collection(dbName, collectionName);
        return collection.findOne(searchQuery);
    }

    public static void delete(String dbName, String collectionName, BasicDBObject searchQuery) {
        DBCollection collection = collection(dbName, collectionName);
        collection.remove(searchQuery);
    }
}
