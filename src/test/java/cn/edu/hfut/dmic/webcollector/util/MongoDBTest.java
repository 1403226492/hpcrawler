package cn.edu.hfut.dmic.webcollector.util;

import com.heping.webcollector.util.MongoDBUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

/**
 * Created by Spider on 2017/8/9.
 */
public class MongoDBTest {
    public static void main(String[] args){
        //String str = "http://www.henancishan.org/NewsShow/2193/2918.html";
        //System.out.println(StringUtils.substringBetween(str,"/NewsShow/","/"));
        DBCollection dbCollection = null;
        try {
            dbCollection = MongoDBUtils.connMongoDB("hngongyi");
        } catch (Exception e) {
            e.printStackTrace();
        }

        BasicDBObject searchEmployee = new BasicDBObject();
        searchEmployee.put("type","2193");
        DBCursor cursor = dbCollection.find(searchEmployee).limit(5).sort(new BasicDBObject("time",-1));
        while(cursor.hasNext()){
            System.out.println(cursor.next());
        }
    }
}
