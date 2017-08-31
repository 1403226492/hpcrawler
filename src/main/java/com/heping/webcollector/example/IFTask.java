package com.heping.webcollector.example;

import com.heping.webcollector.model.CrawlDatum;
import com.heping.webcollector.util.Config;

import java.util.TimerTask;

/**
 * Created by Spider on 2017/8/8.
 */
public class IFTask extends TimerTask{
    @Override
    public void run() {
        IFGongYi crawler = new IFGongYi("json_crawler", true);
        /*益关注：3124
        * 益创新：27451
        * 益行动：1473
        * 益调查：26417
        * 益言堂：1494
        * 益影像：26418
        * 一星一益：26416
        */
        CrawlDatum seed=null;
        String[] type={"3124","27451","1473","26417","1494","26418","26416"};
         /*
        * 循环网站分类
         */
        for (int j=0;j<type.length;j++){
            /*
             每一类公益咨询要爬取得页数
             */
            for (int i = 1; i<= Config.PAGES; i++) {
                /*
                * 入口url
                */
                seed = new CrawlDatum("http://gongyi.ifeng.com/listpage/"+type[j]+"/"+i+"/list.shtml").meta("type", "list").meta("type2","ttt");
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
