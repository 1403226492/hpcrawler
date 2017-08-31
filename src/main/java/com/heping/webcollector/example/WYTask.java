package com.heping.webcollector.example;

import com.heping.webcollector.model.CrawlDatum;
import com.heping.webcollector.util.Config;
import com.heping.webcollector.util.MongoDBUtils;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

import java.util.TimerTask;

/**
 * Created by Spider on 2017/8/22.
 */
public class WYTask extends TimerTask {
    @Override
    public void run() {
        getIds();
        getContent();
    }

    private void getContent() {
        WYNewsContent wyNewsContent = new WYNewsContent("json_crawler", true);
        String url = "http://3g.163.com/news/article/";
        DBCollection dbCollection = null;
        //创建数据库链接
        try {
            dbCollection = MongoDBUtils.connMongoDB("wangyiids");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //使用数据库
        DBCursor cursor = dbCollection.find();
        while(cursor.hasNext()){
            wyNewsContent.addSeed(new CrawlDatum(url+cursor.next().get("docid")+".html").meta("type","content"));
        }
        wyNewsContent.setThreads(30);

        try {
            wyNewsContent.start(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getIds() {
        WYNewsIds crawler=new WYNewsIds("depth_crawler", true);
        String[] type={"BBM54PGAwangning","BCR1UC1Qwangning","BD29LPUBwangning","BD29MJTVwangning","C275ML7Gwangning"};
        String url = "http://3g.163.com/touch/reconstruct/article/list/";
        for (int i=0;i<type.length;i++){
            for(int j = 0; j<= Config.PAGES*10; j+=10){
                crawler.addSeed(new CrawlDatum(url+type[i]+"/"+j+"-10.html")
                        .meta("depth", "1"));
            }
        }
        crawler.setThreads(30);

        try {
            crawler.start(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
