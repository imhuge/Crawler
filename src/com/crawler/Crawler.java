package com.crawler;

import java.util.Set;

public class Crawler {
    public static void main(String[] args){
        Crawler crawler = new Crawler();
        crawler.crawling("http://www.imooc.com");
    }

    // 定义过滤器
    public void crawling(String url){
        Filter filter = new Filter() {
            @Override
            public boolean accept(String url) {
                if(url.indexOf("imooc.com/") != -1 || url.indexOf("imooc.com/search/") != -1){
                    return true;
                }else {
                    return false;
                }
            }
        };
        // 初始化 URL 队列
        LinkQueue.addUnvisitedUrl(url);
        // 循环条件，待抓取的链接不空
        while(!LinkQueue.isEmptyUnvisitedUrl()){
            // 队头URL出队列
            String visitUrl = (String) LinkQueue.getUnvisitedUrl();
            if(visitUrl == null){
                continue;
            }
            ImageDownload.downloadImg(visitUrl);
            // 提取出下载网页中的 URL
            Set<String> links = ParseHttpUrl.extractLinks(url,filter);
            // 新的未访问的 URL 入队
            /*for(String link : links){
                LinkQueue.addUnvisitedUrl(link);
            }*/
        }
    }
}
