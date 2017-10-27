package com.crawler;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
*实现图片下载功能
 **/
public class ImageDownload {
    private static final String ENCODING= "UTF-8";
    // 获取img标签正则
    private static final String IMGURL_REG= "<(img|IMG)(.*?)(/>|></img>|>)";//"<img.*src=(.*?)[^>]*?>";
    // 获取src路径的正则
    private static final String IMGSRC_REG= "http:\"?(.*?)(\"|>|\\s+)";

    public static void downloadImg(String url){
        String html = null;
        try{
            html = ImageDownload.getHTML(url);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(html != null && !html.equals("")){
            List<String> imgUrl = ImageDownload.getImageUrl(html);
            List<String> imgSrc = ImageDownload.getImageSrc(imgUrl);
            ImageDownload.download(imgSrc);
        }
    }
    // 获取HTML内容
    private static String getHTML(String url){
        URLConnection conn = null;
        URL uri = null;
        InputStream in = null;
        BufferedReader reader = null;
        StringBuffer sb = null;
        try{
            uri = new URL(url);
            conn = uri.openConnection();
            conn.connect();
            in = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in));
            sb = new StringBuffer();
            String line = null;
            while((line = reader.readLine()) != null){
                sb.append(line);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                in.close();
                reader.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    //获取ImageUrl地址
    private static List<String> getImageUrl(String html){
        List<String> listImgUrl = new ArrayList<>();
        Matcher matcher = Pattern.compile(IMGURL_REG).matcher(html);
        while(matcher.find()){
            listImgUrl.add(matcher.group());
        }
        return listImgUrl;
    }

    //获取ImageSrc地址
    private static List<String> getImageSrc(List<String> listImageUrl){
        List<String> listImgSrc = new ArrayList<>();
        for(String image : listImageUrl){
            Matcher matcher = Pattern.compile(IMGSRC_REG).matcher(image);
            while(matcher.find()){
                listImgSrc.add(matcher.group().substring(0,matcher.group().length() - 1));
            }
        }
        return listImgSrc;
    }
    //下载图片
    private static void download(List<String> listImgSrc){
        for(String url : listImgSrc){
            try {
                String imageName = url.substring(url.lastIndexOf("/") + 1,url.length());
                URL uri = new URL(url);
                InputStream input = uri.openStream();
                //BufferedInputStream input = new BufferedInputStream(in);
                File directory = new File("C:\\Users\\iwct\\Desktop\\Image");
                if(!directory.exists()){
                    directory.mkdir();
                }
                FileOutputStream out = new FileOutputStream(new File(directory,imageName));
                //BufferedOutputStream out = new BufferedOutputStream(ou);
                int length = 0;
                byte[] buf = new byte[1024];
                while((length = input.read(buf,0,buf.length)) != -1){
                    out.write(buf,0,buf.length);
                }
                input.close();
                out.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
