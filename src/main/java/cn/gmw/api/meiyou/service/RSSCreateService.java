package cn.gmw.api.meiyou.service;

import cn.gmw.api.meiyou.entity.APIWorkReport;
import cn.gmw.api.meiyou.entity.ReleaseLib;
import cn.gmw.api.meiyou.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.jdom2.Attribute;
import org.jdom2.CDATA;
import org.jdom2.Document;
import org.jdom2.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class RSSCreateService {
    @Value("${channel.article.url}")
    private String articleUrl;

    @Value("${xml.channel.title}")
    private String channelTitle;

    @Value("${xml.channel.link}")
    private String channelLink;

    @Value("${xml.channel.image.url}")
    private String channelImageUrl;

    @Value("${xml.channel.image.link}")
    private String channelImageLink;

    @Value("${xml.channel.item.type}")
    private String channelItemType;
    /**
     * 生成XML保存稿件信息
     * @param list
     * @param workReport
     * @return
     */
    public Document createArticleRss(List<ReleaseLib> list, APIWorkReport workReport){
        /*创建根节点*/
        Element rss = new Element("rss");
        rss.setAttribute(new Attribute("version", "2.0"));
        Element channel = new Element("channel");
        channel.addContent(new Element("title").addContent(new CDATA(channelTitle)));
        channel.addContent(new Element("link").addContent(new CDATA(channelLink)));
        Element image = new Element("image");
        image.addContent(new Element("url").addContent(new CDATA(channelImageUrl)));
        image.addContent(new Element("title").addContent(new CDATA(channelTitle)));
        image.addContent(new Element("link").addContent(new CDATA(channelImageLink)));
        channel.addContent(image);
        rss.addContent(channel);  // 将根节点添加到文档中；

        Document doc = new Document(rss);
        for (int i = 0; i < list.size(); i++) {
            ReleaseLib releaseLib =  list.get(i);
            try {
                Element item = new Element("item");
                item.addContent(new Element("title").addContent(new CDATA(releaseLib.getTitle())));
                releaseLib.setArtUrl(articleUrl + DateUtil.format(releaseLib.getPubTime(), "yyyyMM/dd/") + releaseLib.getArticleId() + ".html");
                item.addContent(new Element("link").addContent(releaseLib.getArtUrl()));
                item.addContent(new Element("description").addContent(new CDATA(releaseLib.get_abstract() == null ? "" : releaseLib.get_abstract().trim())));
                item.addContent(new Element("pubDate").addContent(new CDATA(DateUtil.getFormatdate(releaseLib.getPubTime()))));
                item.addContent(new Element("source").addContent(new CDATA(releaseLib.getSourceName())));
                item.addContent(new Element("type").addContent(new CDATA(channelItemType)));
                channel.addContent(item);

                workReport.report(true, "正常处理", releaseLib);
            } catch (Exception e) {
                log.error("**** 处理本条稿件异常，ID:" + releaseLib.getArticleId(), e);
                workReport.report(false, "处理异常，联系管理员", releaseLib);
            }
        }
        log.info("**** 本次输出稿件【" + channel.getChildren("item").size() + "】条");
        return doc;
    }
}
