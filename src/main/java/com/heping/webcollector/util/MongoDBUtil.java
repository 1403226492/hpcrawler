package com.heping.webcollector.util;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;

public class MongoDBUtil {
	private static final String host = "123.56.30.88";  
	private static final int port = 27017;  
	private static final String userName = "";  
	private static final String password = "";  
	private static final String dataBaseName = "local";
	private static final String tableName = "wangyinews";
	public static DBCollection connMongoDB() throws Exception {  
	    Mongo mongo = new Mongo(host, port);  
	    DB db = mongo.getDB(dataBaseName);
	    DBCollection dbCollection = db.getCollection(tableName);  
	    return dbCollection;
	}
}
