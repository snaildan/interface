package cn.gmw.api.meiyou.entity;

public class WorkItem {

    private String id;
    private String title;
    private String url;
    private long pubTime;
    private boolean isSuccess;
    private String comment;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public boolean isSuccess() {
        return isSuccess;
    }
    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public long getPubTime() {
        return pubTime;
    }
    public void setPubTime(long pubTime) {
        this.pubTime = pubTime;
    }

}