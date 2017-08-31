/*
 * Copyright (C) 2014 hu
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
package com.heping.webcollector.util;

/**
 * 全局配置
 *
 * @author hu
 */
public class Config {

    public static String DEFAULT_USER_AGENT = "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Mobile Safari/537.36";
    public static int MAX_RECEIVE_SIZE = 1000 * 1000;
    public static long THREAD_KILLER = 1000 * 60 * 2;
    public static long WAIT_THREAD_END_TIME = 1000 * 60;
    /*最大连续重定向次数*/
    public static int MAX_REDIRECT = 3;

    public static int TIMEOUT_CONNECT = 3000;
    public static int TIMEOUT_READ = 10000;
    public static int MAX_EXECUTE_COUNT = 10;
    public static String DEFAULT_HTTP_METHOD = "GET";
    public static boolean IF_PROXY=false;

    //发布到服务器需要更改路径
    //下载压缩后的路径
    public static String IMG_DOWNLOAD_HN="d:/download/hn/";//河南慈善图片路径
    public static String IMG_DOWNLOAD_IF="d:/download/if/";//凤凰公益图片路径
    public static String IMG_DOWNLOAD_WY="d:/download/wy/";//网易新闻图片路径

    //图片压缩
    public static int IMG_MAXLENGTH=500;//设置普通图片压缩后的最大长度
    public static int IMG_HW=3;//原图长宽比
    public static int LENGTHIMG_H_W=1500;//图片长度或者宽度大于LENGTHIMG_H_W并且长宽比大于IMG_HW定为长图
    public static int M_LENGTHIMG_H_W=4000;//图片长度或者宽度大于M_LENGTHIMG_H_W并且长宽比大于IMG_HW定为超长图

    //爬取页数设置
    public static int PAGES=2;

    //数据库配置
    //数据库ip地址
    public static String HOST="123.56.30.88";
    //数据库端口
    public static int PORT=27017;
    //数据库名称
    public static String DATABASENAME="local";
    //登陆用户名
    public static String USERNAME="";
    //登陆密码
    public static String PASSWORD="";
}
