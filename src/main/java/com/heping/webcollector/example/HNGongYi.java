/*
 * Copyright (C) 2015 hu
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.heping.webcollector.example;

import com.heping.webcollector.model.CrawlDatum;
import com.heping.webcollector.model.CrawlDatums;
import com.heping.webcollector.model.Page;
import com.heping.webcollector.net.HttpRequest;
import com.heping.webcollector.net.HttpResponse;
import com.heping.webcollector.net.Proxys;
import com.heping.webcollector.plugin.berkeley.BreadthCrawler;
import com.heping.webcollector.util.Config;
import com.heping.webcollector.util.DownloadImage;
import com.heping.webcollector.util.MongoDBUtils;
import com.heping.webcollector.util.PictureChangeSize;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import org.apache.commons.lang.StringUtils;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HNGongYi extends BreadthCrawler {
    /**
     * 构造一个基于伯克利DB的爬虫
     * 伯克利DB文件夹为crawlPath，crawlPath中维护了历史URL等信息
     * 不同任务不要使用相同的crawlPath
     * 两个使用相同crawlPath的爬虫并行爬取会产生错误
     *
     * @param crawlPath 伯克利DB使用的文件夹
     * @param autoParse 是否根据设置的正则自动探测新URL
     */
    public HNGongYi(String crawlPath, boolean autoParse) {
        super(crawlPath, autoParse);
    }
    @Override
    public HttpResponse getResponse(CrawlDatum crawlDatum) throws Exception {
        HttpRequest request = new HttpRequest(crawlDatum.url());
        //代理ip
        if (Config.IF_PROXY){
            Proxys proxys=new Proxys();
            Properties pps = new Properties();
            pps.load(getClass().getResourceAsStream("/IpList.properties"));
            Enumeration enum1 = pps.propertyNames();//得到配置文件的名字
            while(enum1.hasMoreElements()) {
                String strKey = (String) enum1.nextElement();
                String strValue = pps.getProperty(strKey);
                proxys.add(strKey,Integer.parseInt(strValue));
            }
            request.setProxy(proxys.nextRandom());
        }
        request.setMethod("GET");
        //设置UserAgent
        request.setUserAgent(Config.DEFAULT_USER_AGENT);
        String outputData = crawlDatum.meta("outputData");
        if (outputData != null) {
            request.setOutputData(outputData.getBytes("utf-8"));
        }
        return request.response();
    }

    @Override
    public void visit(Page page, CrawlDatums next) {
        /*
        创建数据库链接
         */
        DBCollection dbCollection = null;
        try {
            dbCollection = MongoDBUtils.connMongoDB("hngongyi");
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
        使用数据库
         */
        BasicDBObject obj = new BasicDBObject();
        //标题
        String title=null;
        //发布时间
        String time=null;
        //来源
        String from=null;
        //内容
        String content=null;
        //图片
        String img=null;



        String type=page.meta("type");
        String type2 = page.key();
        /*
        截取入口url获取资讯类型
         */
        String type3 = StringUtils.substringBetween(type2,"/NewsShow/","/");
        if(type.equals("list")){
            //如果是列表页，抽取内容页链接
            //将内容页链接的type设置为content，并添加到后续任务中
            if (page.links("div.news_ltu1>a").isEmpty()){
                next.add(page.links("li>h2>a")).meta("type", "content");
            }else {
                next.add(page.links("div.news_ltu1>a")).meta("type", "content");
            }
        }else if(type.equals("content")){
            //处理内容页，抽取标题，发布时间，来源，内容并存储到数据库
            title=page.select("div.tit>h2").first().text();
            time=page.select("span.time").first().text();
            from=page.select("span.from").first().text();
            content=page.select("div#meta").first().text();
            //获取文章内容的全部信息，含标签
            String html = page.select("div#meta").html();
            //抽取img标签内容
            Matcher matcher = Pattern.compile("<img.*src=(.*?)[^>]*?>").matcher(html);
            //list用于保存图片下载并压缩后的路径
            ArrayList<String> list = new ArrayList<String>();
            while (matcher.find()){
                //截取图片URL
                img = StringUtils.substringBetween(matcher.group(),"src=\"","\"");
                if (!img.contains("http")){
                    //添加图片前缀
                    img="http://www.henancishan.org"+img;
                }
                //保存图片名字：时间戳+10000之内的随机数
                String imgName=System.currentTimeMillis()+"_"+((int)(1+Math.random()*10000))+".jpg";
                if (img!=null){
                    list.add(TimerManager.COM_HN_PATH+imgName);
                    try {
                        //图片下载
                        DownloadImage.download(img,imgName,TimerManager.COM_HN_PATH);
                        //图片压缩并替换原来的图片
                        PictureChangeSize.compressImage(TimerManager.COM_HN_PATH+imgName,TimerManager.COM_HN_PATH+imgName,Config.IMG_MAXLENGTH);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            obj.put("title",title);
            obj.put("type",type3);
            obj.put("time",time);
            obj.put("from",from);
            obj.put("content",content);
            //循环list保存图片路径到数据库
            for (int i=0;i<list.size();i++){
                obj.put("img"+i,list.get(i));
            }
            dbCollection.insert(obj);
        }
    }
}
