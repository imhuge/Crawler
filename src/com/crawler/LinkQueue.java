package com.crawler;

import java.util.*;

public class LinkQueue {
    //已访问的url集合
    private static Set<String> visitedUrl = Collections.synchronizedSet(new HashSet<String>());
    //未访问的url集合
    private static List<String> unvisitedUrl = Collections.synchronizedList(new ArrayList<String>());
    //未访问的url出队列
    public static String getUnvisitedUrl(){
        if(unvisitedUrl.size() > 0){
            String url = unvisitedUrl.get(0);
            unvisitedUrl.remove(0);
            return url;
        }
        return null;
    }
    //新的url添加时验证，保证只是添加一次
    public static void addUnvisitedUrl(String url){
        if(url != null && !url.trim().equals("") && !visitedUrl.contains(url) && !unvisitedUrl.contains(url)){
            unvisitedUrl.add(url);
        }
    }
    //判断未访问的url队列中是否为空
    public static boolean isEmptyUnvisitedUrl(){
        return unvisitedUrl.isEmpty();
    }
}
