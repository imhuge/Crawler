package com.crawler;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;

import java.util.HashSet;
import java.util.Set;

/**
 * 收集每一个url下的链接进行过滤产生新的链接
 */
public class ParseHttpUrl {
    public static Set<String> extractLinks(String url, Filter filter){
        Set<String> links = new HashSet<>();
        try {
            Parser parser = new Parser(url);
            // 过滤 <frame >标签的 filter，用来提取 frame 标签里的 src 属性所表示的链接
            NodeFilter frameFilter = new NodeFilter(){
                public boolean accept(Node node){
                    if(node.getText().startsWith("frame src=")){
                        return true;
                    }else{
                        return false;
                    }
                }
            };
            // OrFilter 来设置过滤 <a> 标签，和 <frame> 标签
            OrFilter linkFilter = new OrFilter(new NodeClassFilter(LinkTag.class),frameFilter);
            // 得到所有经过过滤的标签
            NodeList list = parser.extractAllNodesThatMatch(linkFilter);
            for(int i=0;i<list.size();i++){
                Node tag = list.elementAt(i);
                if(tag instanceof LinkTag) { //<a>标签
                    LinkTag link = (LinkTag) tag;
                    String linkUrl = link.getLink();
                    if(filter.accept(linkUrl)){
                        links.add(linkUrl);
                    }
                }else { //<frame>标签
                    String frame = tag.getText();
                    int start = frame.indexOf("src=");
                    frame = frame.substring(start);
                    int end = frame.indexOf(" ");
                    if(end == -1){
                        end = frame.indexOf(">");
                    }
                    String frameUrl = frame.substring(5,end - 1);
                    if(filter.accept(frameUrl)){
                        links.add(frameUrl);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return links;
    }
}
