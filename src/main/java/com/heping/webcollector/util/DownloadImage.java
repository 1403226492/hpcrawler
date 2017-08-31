package com.heping.webcollector.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;


public class DownloadImage {

    /**
     * @param
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub  
        download("http://p0.ifengimg.com/a/2017_34/c9037147f69dc83_size21_w600_h366.jpg", "51bi.jpg","f:/image/");
    }

    public static void download(String urlString, String filename,String savePath) throws Exception {
        // 构造URL  
        URL url = new URL(urlString);
        // 打开连接  
        URLConnection con = url.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        //设置请求超时为5s  
        con.setConnectTimeout(5*1000);
        // 输入流  
        InputStream is = con.getInputStream();

        // 1K的数据缓冲  
        byte[] bs = new byte[1024];
        // 读取到的数据长度  
        int len;
        // 输出的文件流  
        File sf=new File(savePath);
        if(!sf.exists()){
            sf.mkdirs();
        }
        OutputStream os = new FileOutputStream(sf.getPath()+"\\"+filename);
        // 开始读取  
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        // 完毕，关闭所有链接  
        os.close();
        is.close();
    }
    public static void resizeImage(String srcImgPath, String distImgPath,int width, int height) throws IOException {

        String subfix = "jpg";

        subfix = srcImgPath.substring(srcImgPath.lastIndexOf(".")+1,srcImgPath.length());



        File srcFile = new File(srcImgPath);

        Image srcImg = ImageIO.read(srcFile);



        BufferedImage buffImg = null;

        if(subfix.equals("png")){

            buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        }else{

            buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        }



        Graphics2D graphics = buffImg.createGraphics();

        graphics.setBackground(Color.WHITE);

        graphics.setColor(Color.WHITE);

        graphics.fillRect(0, 0, width, height);

        graphics.drawImage(srcImg.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);



        ImageIO.write(buffImg, subfix, new File(distImgPath));

    }

}  