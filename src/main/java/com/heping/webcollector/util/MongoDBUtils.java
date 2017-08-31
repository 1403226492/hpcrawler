package com.heping.webcollector.util;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;

public class MongoDBUtils {
	private static final String host = Config.HOST;
	private static final int port = Config.PORT;
	private static final String userName = Config.USERNAME;
	private static final String password = Config.PASSWORD;
	private static final String dataBaseName = Config.DATABASENAME;
	//调用此方法时设置表名
	public static DBCollection connMongoDB(String tableName) throws Exception {
	    Mongo mongo = new Mongo(host, port);  
	    DB db = mongo.getDB(dataBaseName);
	    //如果数据库设置了密码需要打开注释进行判断

	    /*if (!StringUtils.isEmpty(userName) || !StringUtils.isEmpty(password)) {
	        db.authenticate(userName, password.toCharArray());  
	    }*/


	    DBCollection dbCollection = db.getCollection(tableName);  
	    return dbCollection;
	}
}
