package com.heping.webcollector.example;

import com.heping.webcollector.model.CrawlDatums;
import com.heping.webcollector.plugin.berkeley.BreadthCrawler;
import com.heping.webcollector.model.Page;
import com.heping.webcollector.util.FileUtils;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/**
 * 用WebCollector爬虫爬取整站图片 
 */
public class PicCrawler extends BreadthCrawler{

    /*用一个整数，不断自增，来作为下载的图片的文件名*/
    AtomicInteger id=new AtomicInteger(0);

    /**
     * 构造一个基于伯克利DB的爬虫
     * 伯克利DB文件夹为crawlPath，crawlPath中维护了历史URL等信息
     * 不同任务不要使用相同的crawlPath
     * 两个使用相同crawlPath的爬虫并行爬取会产生错误
     *
     * @param crawlPath 伯克利DB使用的文件夹
     * @param autoParse 是否根据设置的正则自动探测新URL
     */
    public PicCrawler(String crawlPath, boolean autoParse) {
        super(crawlPath, autoParse);
    }

    @Override
    public void visit(Page page, CrawlDatums next) {
        /*不处理非jpg的网页/文件*/
        if(!Pattern.matches(".*jpg$",page.getUrl())){
            return;
        }
        /*将图片内容保存到文件，page.getContent()获取的是文件的byte数组*/
        try {
            FileUtils.writeFileWithParent("download/"+id.incrementAndGet()+".jpg",page.getContent());
            System.out.println("download:"+page.getUrl());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        PicCrawler crawler=new PicCrawler("json_crawler", true);
        crawler.addSeed("http://www.meishij.net/");
        crawler.addRegex("http://.*meishij.net/.*");
        crawler.setThreads(50);
        crawler.start(10);
    }
}