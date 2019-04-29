package cn.gmw.api.meiyou.thread;

import cn.gmw.api.meiyou.entity.APIWorkReport;
import cn.gmw.api.meiyou.entity.ReleaseLib;
import cn.gmw.api.meiyou.service.ElasticSearchService;
import cn.gmw.api.meiyou.service.RSSCreateService;
import cn.gmw.api.meiyou.service.RedisService;
import cn.gmw.api.meiyou.utils.DeduplUtil;
import cn.gmw.api.meiyou.utils.ReleaseLibFilterUtil;
import cn.gmw.api.meiyou.utils.XMLOutUtil;
import lombok.extern.slf4j.Slf4j;
import org.jdom2.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *  Warning:java: 无法找到类型 'org.junit.jupiter.api.extension.ExtendWith' 的注释方法 'value()': 找不到org.junit.jupiter.api.extension.ExtendWith的类文件
 *  解决方案：右键-->pom.xml-->Maven-->Reimport即可解决此问题
 */
/*
*   log.info中log一直是红色，IntelliJ IDEA未安装lombok插件导致
*   解决方案：https://www.cnblogs.com/yr1126/p/10375325.html
*
* */
/*
 *   @Scope("prototype") 注解，该注解的作用是将该类变成多例模式
 *
 * */

@Slf4j
@Service
@Scope("prototype")
public class MeiyouThread implements Runnable{
    @Value("${channel.info}")
    private String channelInfo;

    @Value("${channel.es.index}")
    private String esIndex;

    @Value("${channel.es.type}")
    private String esType;

    @Value("${channel.es.search.from}")
    private int esFrom;

    @Value("${channel.es.search.size}")
    private int esSize;

    @Value("${app.name}")
    private String appName;

    @Value("${channel.out.path}")
    private String outPath;

    @Value("${app.job.cycle}")
    private int jobCycle;

    @Resource
    private RSSCreateService rssCreateService;

    @Resource
    private ElasticSearchService elasticSearchService;

    @Resource
    private RedisService redisService;

    @Override
    public void run(){
        while (true){
            try {
                String masterIds = channelInfo.split("_")[2];
                //按照节点搜索
                List<ReleaseLib> list = elasticSearchService.stringQueryByES(esIndex, esType, masterIds, esFrom, esSize);
                if(list != null && list.size() > 0) {
                    APIWorkReport workReport = new APIWorkReport(appName, channelInfo);
                    log.info("**** 检索到稿件【" + list.size() + "】条， 开始进行过滤... ****");
                    list = ReleaseLibFilterUtil.filter(list, workReport);
                    log.info("**** 过滤后稿件【" + list.size() + "】条， 开始排重... ****");
                    list = DeduplUtil.distinct(list, 0.5F, workReport);
                    log.info("**** 排重后稿件【" + list.size() + "】条， 开始生成XML Doc... ****");
                    Document doc = rssCreateService.createArticleRss(list, workReport);
                    log.info("**** 开始输出XML... ***");
                    XMLOutUtil.outputXML(outPath, channelInfo.split("_")[0] + ".xml", doc, "UTF-8");
                    log.info("**** 输出" + outPath + channelInfo.split("_")[0] + ".xml成功！***");
                    try {
                        log.info("**** 缓存更新结果到Redis... ****");
                        new Thread(new RedisDataCacheThread(workReport,redisService)).start();
                    } catch (Exception e) {
                        log.error("**** 缓存更新结果到Redis失败！****", e);
                    }
                }

                //log.info("开始执行代码_节点号："+masterIds);
                //Thread.sleep(50 * 1000);
            } catch (Exception e) {
                try{
                    log.info("**** 本次更新异常，30秒后重试... ****", e);
                    Thread.sleep(30 * 1000);
                }catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            try {
                log.info("**** 本次更新完成，休息【" +jobCycle + "】秒 ****");
                Thread.sleep(jobCycle* 1000);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }
}
