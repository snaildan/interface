package cn.gmw.api.meiyou.entity;

import java.util.ArrayList;
import java.util.List;

public class APIWorkReport {
    private String appName;
    private String channelInfo;
    private List<WorkItem> workItemList;

    public APIWorkReport(String appName, String channelInfo) {
        this.channelInfo = channelInfo;
        this.appName = appName;
        this.workItemList = new ArrayList<WorkItem>();
    }
    public String getAppName() {
        return appName;
    }
    public void setAppName(String appName) {
        this.appName = appName;
    }
    public String getChannelInfo() {
        return channelInfo;
    }
    public void setChannelInfo(String channelInfo) {
        this.channelInfo = channelInfo;
    }
    public List<WorkItem> getWorkItemList() {
        return workItemList;
    }
    public void setWorkItemList(List<WorkItem> workItemList) {
        this.workItemList = workItemList;
    }

    public void report(boolean isSuccess, String comment, ReleaseLib releaseLib) {
        WorkItem workItem = new WorkItem();
        workItem.setSuccess(isSuccess);
        workItem.setComment(comment);
        workItem.setId(String.valueOf(releaseLib.getArticleId()));
        workItem.setPubTime(releaseLib.getPubTime());
        workItem.setTitle(releaseLib.getTitle());
        workItem.setUrl(releaseLib.getArtUrl());
        this.workItemList.add(workItem);
    }
}
