package com.heping.webcollector.example;

import com.heping.webcollector.model.CrawlDatum;
import com.heping.webcollector.net.HttpRequest;
import com.heping.webcollector.net.HttpResponse;
import com.heping.webcollector.net.Proxys;
import com.heping.webcollector.util.Config;

import java.util.TimerTask;

/**
 * Created by Spider on 2017/8/8.
 */
public class HNTask extends TimerTask{
    @Override
    public void run() {
        HNGongYi crawler = new HNGongYi("json_crawler", true);
        CrawlDatum seed=null;

        /*网站分类
        * 总会动态：2190
        * 慈善聚焦：2191
        * 政策法规：2192
        * 媒体关注：2193
        * 慈善文苑：2196
        * */
        String[] typeStr={"2190","2191","2192","2193","2196"};
        /*
        * 循环网站分类
         */
        for (int i=typeStr.length-1;i>=0;i--){
            /*
            * 每一类慈善咨询要爬取得页数
             */
            for (int j = Config.PAGES; j>0; j--){
                /*
                * 入口url
                */
                seed=new CrawlDatum("http://www.henancishan.org/NewsList/"+typeStr[i]+"/"+j+".html").meta("type", "list");
                crawler.addSeed(seed);
            }
        }
        crawler.setThreads(30);
        try {
            crawler.start(2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
