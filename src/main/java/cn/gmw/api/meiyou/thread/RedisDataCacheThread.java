package cn.gmw.api.meiyou.thread;


import cn.gmw.api.meiyou.entity.APIWorkReport;
import cn.gmw.api.meiyou.entity.WorkItem;
import cn.gmw.api.meiyou.service.RedisService;
import cn.gmw.api.meiyou.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RedisDataCacheThread implements Runnable {

    private APIWorkReport workReport;

    private RedisService redisService;

    public RedisDataCacheThread(APIWorkReport workReport, RedisService redisService) {
        this.workReport = workReport;
        this.redisService = redisService;
    }


    @Override
    public void run() {
        if (workReport != null) {
            if (workReport.getWorkItemList() != null && workReport.getWorkItemList().size() > 0) {
                if (workReport.getChannelInfo() == null) {
                    log.error("频道信息为空！****");
                }
                try {
                    String appId = workReport.getAppName().split("-")[0];
                    String channelId = workReport.getChannelInfo().split("_")[0];
                    for (WorkItem item : workReport.getWorkItemList()) {
                        if (item.isSuccess()) {
                            String key = "api_" + appId + DateUtil.format(item.getPubTime(), "_yyyyMMdd_") + channelId;
                            redisService.set(key, item.getId());
                        } else {
                            String failKey = "api_fail_" + appId + DateUtil.format(item.getPubTime(), "_yyyyMMdd_") + channelId;
                            String value = item.getId() + "####" + item.getComment() + "####" + item.getTitle() + "####" + item.getUrl();
                            redisService.set(failKey, value);
                        }
                        String esKey = "api_es_" + appId + DateUtil.format(item.getPubTime(), "_yyyyMMdd_") + channelId;
                        redisService.set(esKey, item.getId());
                    }
                } catch (Exception e) {
                    log.error("**** 缓存接口数据失败！****", e);
                } finally {
                    log.info("**** 缓存接口数据写入结束！****");
                }
            } else {
                log.error("workReport.getWorkItemList为空！****");
            }
        } else {
            log.error("workReport为空！****");
        }
    }
}