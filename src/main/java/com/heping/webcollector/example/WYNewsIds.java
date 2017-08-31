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
import com.heping.webcollector.util.MongoDBUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Enumeration;
import java.util.Properties;

/**
 * 网易新闻爬取
 * 获取新闻id和新闻类型储存到数据库中
 * 
 * @author hu
 */
public class WYNewsIds extends BreadthCrawler {

    public WYNewsIds(String crawlPath, boolean autoParse) {
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
        request.setCookie("_ntes_nnid=fcee2394eba69d89272181580336652b,1502253606648; _ntes_nuid=fcee2394eba69d89272181580336652b; Province=0370; City=0371; __s_=1; vjuids=dd61334a.15dc5545203.0.21e0b91bbb39f; __gads=ID=d9d5cb7f782e98cd:T=1502254317:S=ALNI_MZSdO8-vppLRd71F4ShKPR9I1GdnA; usertrack=ZUcIhVmSoq8J4G8GmSFXAg==; vjlast=1502254224.1502849270.11; vinfo_n_f_l_n3=0660df9e2dc38128.1.1.1502782131865.1502782220147.1502850116655; NNSSPID=c7261d87afbf4da898a851ba7394ff11; Hm_lvt_b2d0b085a122275dd543c6d39d92bc62=1502935636,1502940881,1502948411; Hm_lpvt_b2d0b085a122275dd543c6d39d92bc62=1502949252");
        String outputData = crawlDatum.meta("outputData");
        if (outputData != null) {
            request.setOutputData(outputData.getBytes("utf-8"));
        }
        return request.response();
    }

    @Override
    public void visit(Page page, CrawlDatums next) {
        String str = page.html();
        //System.out.println(str);
        String str3 = StringUtils.substringAfter(str,"artiList({\"");
        //新闻类型
        String type = StringUtils.substringBefore(str3,"\":[{\"");
        String str1 = StringUtils.substringAfter(str,"\":");
        String str2 = StringUtils.substringBeforeLast(str1,"})");
        //System.out.println(str2);
        JSONArray json = new JSONArray(str2);

        for (int i=0;i<json.length();i++){
            JSONObject object = new JSONObject(json.get(i).toString());
            //新闻id
            String docid = (String) object.get("docid");
            DBCollection dbCollection = null;
            BasicDBObject obj = new BasicDBObject();
            //创建数据库链接
            try {
                dbCollection = MongoDBUtils.connMongoDB("wangyiids");
            } catch (Exception e) {
                e.printStackTrace();
            }
            //使用数据库
            obj.put("type",type);
            obj.put("docid",docid);
            dbCollection.insert(obj);
        }

    }

}
