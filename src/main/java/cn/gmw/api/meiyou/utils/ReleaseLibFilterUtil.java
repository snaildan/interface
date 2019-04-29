package cn.gmw.api.meiyou.utils;

import cn.gmw.api.meiyou.entity.APIWorkReport;
import cn.gmw.api.meiyou.entity.ReleaseLib;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


/**
 * ReleaseLib过滤工具类
 *
 * @author Administrator
 */
@Slf4j
@Component
public class ReleaseLibFilterUtil {

    @Value("${channel.valid.minimum.timestamp}")
    private long validMinimumTimestampInjected;
    private static long VALID_MIN_TIMESTAMP;

    @Value("${channel.exclude.source}")
    private String excludeSourceInjected;
    private static String EXCLUDE_SOURCE;

    @Value("${channel.caption.number}")
    private int captionNumberInjected;
    private static int CAPTION_NUMBER;

    @PostConstruct
    public void init(){
        //采用间接注入方式
        EXCLUDE_SOURCE = excludeSourceInjected;
        CAPTION_NUMBER = captionNumberInjected;
        VALID_MIN_TIMESTAMP = validMinimumTimestampInjected;
    }

    /**
     * 过滤
     *
     * @param releaseLibList
     * @param workReport
     * @return
     */
    public static List<ReleaseLib> filter(List<ReleaseLib> releaseLibList, APIWorkReport workReport) {
        List<ReleaseLib> list = new ArrayList<>();
        for (ReleaseLib releaseLib : releaseLibList) {
            Boolean needFilter = needFilter(releaseLib, workReport);
            if (!needFilter) {
                list.add(releaseLib);
            }
        }
        return list;
    }

    /**
     * 对不合格的ReleaseLib进行过滤
     *
     * @param lib
     * @param workReport
     * @return
     */
    public static Boolean needFilter(ReleaseLib lib, APIWorkReport workReport) {
        // 如果是多媒体稿件，则不输出
        if (lib.getMultiAttach() != null && !"".equals(lib.getMultiAttach().trim())) {
            log.warn("**** 视频稿件，已过滤！ID:" + lib.getArticleId());
            if (workReport != null) {
                workReport.report(false, "视频稿件", lib);
            }
            return true;
        }

        // 检测来源名称
        String sourceName = lib.getSourceName();
        if (sourceName != null && sourceName.length() > 0 && EXCLUDE_SOURCE != null && EXCLUDE_SOURCE.length() > 0) {
            String[] blockSource = EXCLUDE_SOURCE.split(",");
            boolean isLegal = true;
            for (String str : blockSource) {
                if (sourceName.equals(str) || sourceName.contains(str)) {
                    log.warn("**** 该稿件来源被屏蔽，稿件已过滤! **** |来源：" + sourceName + "|ID:" + lib.getArticleId());
                    if (workReport != null) {
                        workReport.report(false, "屏蔽此来源：" + sourceName, lib);
                    }
                    isLegal = false;
                    break;
                }
            }
            if (!isLegal) {
                return true;
            }
        } else {
            log.warn("**** 该稿件来源字段错误，稿件已过滤! **** |来源字段为空 |ID:" + lib.getArticleId());
            if (workReport != null) {
                workReport.report(false, "来源名称错误:来源字段为空", lib);
            }
            return true;
        }

        // 标题为空则不输出
        if (lib.getTitle() == null) {
            log.warn("**** 该稿件标题字段错误，稿件已过滤!ID:" + lib.getArticleId());
            if (workReport != null) {
                workReport.report(false, "标题错误", lib);
            }
            return true;
        }
        // 检测文章内容中图片，并设置 artUrl 属性。
        if (!checkImage(lib.getContent())) {
            log.info("**** 文章图片访问失败，ID：" + lib.getArticleId());
            if (workReport != null) {
                workReport.report(false, "图片错误", lib);
            }
            return true;
        }
        //检测日期
        if (lib.getPubTime() < VALID_MIN_TIMESTAMP) {
            log.info("**** 文章已过期，ID：" + lib.getArticleId());
            if (workReport != null) {
                workReport.report(false, "文章已过期", lib);
            }
            return true;
        }
        //artUrl
        String artUrl = lib.getArtUrl();
        if (artUrl == null) {
            log.info("**** 稿件ArtUrl为空！ 【" + lib.getArticleId() + "】");
            if (workReport != null) {
                workReport.report(false, "稿件ArtUrl为空", lib);
            }
            return true;
        }
        //图集
        Elements imgEles = Jsoup.parse(lib.getContent()).select("body").select("img");
        if (imgEles == null || imgEles.size() < 3) {
            log.info("**** 内容中没有图片或图片少于三张！ 【" + lib.getArticleId() + "】");
            if (workReport != null) {
                workReport.report(false, "内容中没有图片或图片少于三张", lib);
            }
            return true; // 不是图集稿件
        } else {
            String content = formatContent(lib);
            if (checkContent(content)) {
                log.info("**** 图片与图说不匹配！此稿件由今日头条图文接口输出 【" + lib.getArticleId() + "】");
                if (workReport != null) {
                    workReport.report(false, "图片与图说不匹配,此稿件由今日头条图文接口输出", lib);
//                    XmlUtils.import2XY(lib);
                }
                return true;

            }
//            if (checklength(content)) {
//                log.info("**** 图说长度大于200！ 【" + lib.getArticleId() + "】");
//                if (workReport != null) {
//                    workReport.report(false, "图说长度大于200", lib);
//                }
//                return true;
//            }
//            StringBuffer figureSB = isRightImgs(lib);
//            if (figureSB == null) {
//                log.info(lib.getArticleId() + "不是符合规范的图集");
//                if (workReport != null) {
//                    workReport.report(false, "不是符合规范的图集", lib);
//                }
//                return true;
//            }
        }


        return false;
    }

    private static boolean checklength(String content) {
        Elements pEles = Jsoup.parse(content).select("body").select("p");
        int i;
        for (i = 0; i < pEles.size(); i++) {
            if (pEles.get(i).text().length() > CAPTION_NUMBER)
                return true;
        }
        return false;
    }

    /**
     * 是否正确的图集
     * 不正确返回null
     *
     * @param lib
     * @return
     */
    public static StringBuffer isRightImgs(ReleaseLib lib) {
        String content = formatContent(lib);
        StringBuffer figureSB = new StringBuffer();
        if (content.indexOf("<hr/>") >= 0) {//有分页标签
            String imgAndDes[] = content.split("<hr/>");
            for (int i = 0; i < imgAndDes.length; i++) {
                String img = imgAndDes[i];
                StringBuilder oneFigure = new StringBuilder();
                oneFigure.append("<figure>");
                Document oneDoc = Jsoup.parse(img);
                String figCaption = oneDoc.select("p").text();
                oneFigure.append("<figcaption>");
                oneFigure.append(figCaption);
                oneFigure.append("</figcaption>");
                String altSrc = "";
                if (oneDoc.select("img") != null && oneDoc.select("img").size() > 0) {
                    altSrc = oneDoc.select("img").get(0).attr("src");
                }
                if (!altSrc.startsWith("http://")) {
                    log.info("**** 图片url不是以'http://'开头。【" + lib.getArticleId() + "】的图片【" + altSrc + "】...****");
                    continue;
                }
                log.debug("**** 正在读取读取对象【" + lib.getArticleId() + "】的图片【" + altSrc + "】...****");
                BufferedImage bfdImg = ImageUtil.getImageFromUrl(altSrc);
                /*
                 * 升级为 https
                 * Date ：2018-02-05
                 * Author ： Sugar、
                 */
                altSrc = altSrc.replaceAll("http://([\\w]+)\\.gmw\\.cn/([\\w]+)", "https://imgs.gmw.cn/$1/$2");
                log.debug("**** 读取读取对象【" + lib.getArticleId() + "】的图片【" + altSrc + "】成功！****");
                String widthAndHeight = "";
                if (bfdImg != null) {
                    widthAndHeight = " width=\"" + bfdImg.getWidth() + "\" height=\"" + bfdImg.getHeight() + "\"";
                }
                oneFigure.append("<img alt-src=\"" + altSrc + "\"" + widthAndHeight + "/>");
                oneFigure.append("</figure>");
                figureSB.append(oneFigure.toString());
            }
        } else {//p 否则以p标签进行分割
            Elements pEles = Jsoup.parse(content).select("body").select("p");
            if (pEles.get(0).html().contains("img")) {
                for (int i = 0; i < pEles.size(); i++) {
                    Elements pImgEles = pEles.get(i).select("img");//图开头
                    if (pImgEles != null && pImgEles.size() > 0) {
                        StringBuilder oneFigure = new StringBuilder();
                        oneFigure.append("<figure>");
                        String figCaption = i + 1 == pEles.size() ? "" : pEles.get(i + 1).text();
//                        if (figCaption.length() > capNum) {
//                            figCaption = figCaption.substring(0, capNum);
//                        }
                        oneFigure.append("<figcaption>");
                        oneFigure.append(figCaption);
                        oneFigure.append("</figcaption>");
                        String altSrc = pImgEles.get(0).attr("src");
                        log.debug("**** 正在读取对象【" + lib.getArticleId() + "】的图片【" + altSrc + "】...****");
                        BufferedImage bfdImg = ImageUtil.getImageFromUrl(altSrc);
                        /*
                         * 升级为 https
                         * Date ：2018-02-05
                         * Author ： Sugar、
                         */
//                        altSrc = altSrc.replace("http://", "https://");
                        altSrc = altSrc.replaceAll("http://([\\w]+)\\.gmw\\.cn/([\\w]+)", "https://imgs.gmw.cn/$1/$2");
                        log.debug("**** 读取读取对象【" + lib.getArticleId() + "】的图片【" + altSrc + "】成功！****");
                        String widthAndHeight = "";
                        if (bfdImg != null) {
                            widthAndHeight = " width=\"" + bfdImg.getWidth() + "\" height=\"" + bfdImg.getHeight() + "\"";
                        }
                        oneFigure.append("<img alt-src=\"" + altSrc + "\"" + widthAndHeight + "/>");
                        oneFigure.append("</figure>");
                        figureSB.append(oneFigure.toString());
                    }

                }
            } else {
                for (int i = 0; i < pEles.size(); i++) {
                    Elements pImgEles = pEles.get(i).select("img");//图开头
                    if (pImgEles != null && pImgEles.size() > 0) {
                        StringBuilder oneFigure = new StringBuilder();
                        oneFigure.append("<figure>");
                        String figCaption = i - 1 == pEles.size() ? "" : pEles.get(i - 1).text();
                        // log.info("==========文"+figCaption);
//                        if (figCaption.length() > capNum) {
//                            figCaption = figCaption.substring(0, capNum);
//                        }
                        oneFigure.append("<figcaption>");
                        oneFigure.append(figCaption);
                        oneFigure.append("</figcaption>");
                        String altSrc = pImgEles.get(0).attr("src");
                        log.debug("**** 正在读取对象【" + lib.getArticleId() + "】的图片【" + altSrc + "】...****");
                        BufferedImage bfdImg = ImageUtil.getImageFromUrl(altSrc);
                        /*
                         * 升级为 https
                         * Date ：2018-02-05
                         * Author ： Sugar、
                         */
//                        altSrc = altSrc.replace("http://", "https://");
                        altSrc = altSrc.replace("http://([\\w]+)\\.gmw\\.cn/([\\w]+)", "https://imgs.gmw.cn/$1/$2");
                        log.debug("**** 读取读取对象【" + lib.getArticleId() + "】的图片【" + altSrc + "】成功！****");

                        String widthAndHeight = "";
                        if (bfdImg != null) {
                            widthAndHeight = " width=\"" + bfdImg.getWidth() + "\" height=\"" + bfdImg.getHeight() + "\"";
                        }
                        oneFigure.append("<img alt-src=\"" + altSrc + "\"" + widthAndHeight + "/>");
                        oneFigure.append("</figure>");
                        figureSB.append(oneFigure.toString());
                    }
                }

            }
            //     }
        }
        return figureSB;
    }

    /**
     * 对文章内容进行优化，方便后续对图集进行判断
     *
     * @param lib
     * @return
     */
    private static String formatContent(ReleaseLib lib) {
        String content = lib.getContent();
        content = content.replaceAll("<p></p>", "");
        content = content.replaceAll("<p>\n</p>", "");
        content = content.trim();
        Elements pEles = Jsoup.parse(content).select("body").select("p");
        int flag = 0;
        for (int i = 0; i < pEles.size(); i++) {
            Elements pImgEles = pEles.get(i).select("img");
            if (pImgEles != null && pImgEles.size() > 0) {//如果是图片就进行下一次循环
                flag = 0;
                continue;
            } else {
                flag++;
                String newStr = "";
                if (flag > 1) {//记录文字的段落数，大于1时开始拼接
                    newStr += pEles.get(i - 1).html() + pEles.get(i).html();
                    pEles.get(i - 1).html("");
                    newStr = newStr.trim();
                    newStr = newStr.replace("\n", "");
                    newStr = newStr.replace("[\\s\\p{Zs}]*", "");
                 //   log.info("=================" + newStr);
                    pEles.get(i).html(newStr);
                    continue;
                }
                if (i == pEles.size() - 1) {//最后只剩版权说明时，将版权说明合并到最后一张图片的上方文字段落
                    String text1 = pEles.get(0).html();
                    String text2 = pEles.get(i).html();
                    if (!text1.contains("img") && text2.contains("版权声明")) {
                        if (pEles.get(i - 1).html().contains("img") && !pEles.get(i - 2).html().contains("img")) {
                            String str = pEles.get(i - 2).html().trim();
                            String str1 = pEles.get(i).text().trim();
                            pEles.get(i - 2).html(str + str1);
                            pEles.get(i).html("");
                        } else {
                            String str = pEles.get(i - 1).html().trim();
                            pEles.get(i - 1).html(pEles.get(i).html().trim());
                            pEles.get(i).html(str);
                        }
                    }
//                    else {
//                        String str = pEles.get(i).text();
//                        pEles.get(i).html(str);
//                    }
                }

            }

        }
        content = pEles.toString().replaceAll("<p></p>", "");
        content = content.replaceAll("(\r?\n(\\s*\r?\n)+)", "");
        Elements pEles2 = Jsoup.parse(content).select("body").select("p");
        int index = pEles2.size() - 1;
        if (!pEles2.get(index).html().contains("img")) {
            pEles2.get(index).html(pEles2.get(index).text() + "(" + "  文章来源：" + lib.getSourceName() + ")");
        } else {
            pEles2.get(index - 1).html(pEles2.get(index - 1).text() + "(" + "  文章来源：" + lib.getSourceName() + ")");
        }

        return pEles2.toString();
    }


    /**
     * 判断图片与文字是否对应
     *
     * @param content
     * @return
     */
    private static boolean checkContent(String content) {
        Elements pEles = Jsoup.parse(content).select("body").select("p");
        String img1 = pEles.get(0).html();
        String img2 = pEles.get(pEles.size() - 1).html();
//        if (img1.contains("img") && img2.contains("img"))
//            return true;
        if (!img1.contains("img") && !img2.contains("img"))
            return true;
        else
            return false;
    }

    /**
     * 对不合格稿件进行过滤
     *
     * @return
     * @Params content
     */

    private static boolean checkImage(String content) {
        if (content == null || content.length() <= 50) {
            return false;
        }
        Elements imgEles = Jsoup.parse(content).select("img");
        for (org.jsoup.nodes.Element e : imgEles) {
            String imgUrl = e.attr("src").toLowerCase();
            if (imgUrl == null || imgUrl.length() == 0
                    || !imgUrl.startsWith("http://")
                    || !imgUrl.contains("gmw.cn")) {
                log.info("**** 图片非法！ ****" + imgUrl);
                return false;
            }
        }
        return true;
    }
}
