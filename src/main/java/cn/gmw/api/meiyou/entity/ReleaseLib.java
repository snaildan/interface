package cn.gmw.api.meiyou.entity;

public class ReleaseLib implements Comparable<ReleaseLib>{
    private long articleId;

    private long originalId;

    private String introTitle;

    private String title;

    private String subTitle;

    private String author;

    private String _abstract;

    private long createTime;

    private String sourceName;

    private String sourceUrl;

    private String sourceIcon;

    private Short attr;

    private String picLinks;

    private long wordCount;

    private String keyWord;

    private long importance;

    private String url;

    private long picCount;

    private String multiAttach;

    private Short isRemoteSend;

    private long importId;

    private long typeId;

    private String category;

    private String autoCategory;

    private String editor;

    private String liability;

    private long pageCount;

    private String commentFlag;

    private String tags;

    private long oriTypeId;

    private String selector;

    private String processor;

    private String subscriber;

    private Short displayCtl;

    private String briefAbstract;

    private String artiposition;

    private long pubTime;

    private String preHandler;

    private long masterId;

    private Short publishState;

    private long tplId;

    private long archiveFlag;

    private long intodatacenter;

    private String inputer;

    private long expirationTime;

    private Short isTop;

    private String specSuffixWord;

    private String specSuffixUrl;

    private String importMark;

    private String pageTitle;

    private String jabbarMark;

    private String firstPub;

    private String content;

    private String mime;

    private String trace;

    private String artUrl;


    private String num;
    private String name;
    private String pnum;

    private String picUrl;
    private String imgContent;

    private String noHtmlContent;

    private String isMaster;

    private long indexTime;

    private long isdeal; //0未处理，1处理


    public String getImgContent() {
        return imgContent;
    }

    public void setImgContent(String imgContent) {
        this.imgContent = imgContent;
    }

    public long getIndexTime() {
        return indexTime;
    }

    public void setIndexTime(long indexTime) {
        this.indexTime = indexTime;
    }

    public String getIsMaster() {
        return isMaster;
    }

    public void setIsMaster(String isMaster) {
        this.isMaster = isMaster;
    }

    public String getNoHtmlContent() {
        return noHtmlContent;
    }

    public void setNoHtmlContent(String noHtmlContent) {
        this.noHtmlContent = noHtmlContent;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPnum() {
        return pnum;
    }

    public void setPnum(String pnum) {
        this.pnum = pnum;
    }

    public String getArtUrl() {
        return artUrl;
    }

    public void setArtUrl(String artUrl) {
        this.artUrl = artUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public String getTrace() {
        return trace;
    }

    public void setTrace(String trace) {
        this.trace = trace;
    }

    public long getArticleId() {
        return articleId;
    }

    public void setArticleId(long articleId) {
        this.articleId = articleId;
    }

    public long getOriginalId() {
        return originalId;
    }

    public void setOriginalId(long originalId) {
        this.originalId = originalId;
    }

    public String getIntroTitle() {
        return introTitle;
    }

    public void setIntroTitle(String introTitle) {
        this.introTitle = introTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String get_abstract() {
        return _abstract;
    }

    public void set_abstract(String _abstract) {
        this._abstract = _abstract;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getSourceIcon() {
        return sourceIcon;
    }

    public void setSourceIcon(String sourceIcon) {
        this.sourceIcon = sourceIcon;
    }

    public Short getAttr() {
        return attr;
    }

    public void setAttr(Short attr) {
        this.attr = attr;
    }

    public String getPicLinks() {
        return picLinks;
    }

    public void setPicLinks(String picLinks) {
        this.picLinks = picLinks;
    }

    public long getWordCount() {
        return wordCount;
    }

    public void setWordCount(long wordCount) {
        this.wordCount = wordCount;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public long getImportance() {
        return importance;
    }

    public void setImportance(long importance) {
        this.importance = importance;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getPicCount() {
        return picCount;
    }

    public void setPicCount(long picCount) {
        this.picCount = picCount;
    }

    public String getMultiAttach() {
        return multiAttach;
    }

    public void setMultiAttach(String multiAttach) {
        this.multiAttach = multiAttach;
    }

    public Short getIsRemoteSend() {
        return isRemoteSend;
    }

    public void setIsRemoteSend(Short isRemoteSend) {
        this.isRemoteSend = isRemoteSend;
    }

    public long getImportId() {
        return importId;
    }

    public void setImportId(long importId) {
        this.importId = importId;
    }

    public long getTypeId() {
        return typeId;
    }

    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAutoCategory() {
        return autoCategory;
    }

    public void setAutoCategory(String autoCategory) {
        this.autoCategory = autoCategory;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getLiability() {
        return liability;
    }

    public void setLiability(String liability) {
        this.liability = liability;
    }

    public long getPageCount() {
        return pageCount;
    }

    public void setPageCount(long pageCount) {
        this.pageCount = pageCount;
    }

    public String getCommentFlag() {
        return commentFlag;
    }

    public void setCommentFlag(String commentFlag) {
        this.commentFlag = commentFlag;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public long getOriTypeId() {
        return oriTypeId;
    }

    public void setOriTypeId(long oriTypeId) {
        this.oriTypeId = oriTypeId;
    }

    public String getSelector() {
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public String getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(String subscriber) {
        this.subscriber = subscriber;
    }

    public Short getDisplayCtl() {
        return displayCtl;
    }

    public void setDisplayCtl(Short displayCtl) {
        this.displayCtl = displayCtl;
    }

    public String getBriefAbstract() {
        return briefAbstract;
    }

    public void setBriefAbstract(String briefAbstract) {
        this.briefAbstract = briefAbstract;
    }

    public String getArtiposition() {
        return artiposition;
    }

    public void setArtiposition(String artiposition) {
        this.artiposition = artiposition;
    }

    public long getPubTime() {
        return pubTime;
    }

    public void setPubTime(long pubTime) {
        this.pubTime = pubTime;
    }

    public String getPreHandler() {
        return preHandler;
    }

    public void setPreHandler(String preHandler) {
        this.preHandler = preHandler;
    }

    public long getMasterId() {
        return masterId;
    }

    public void setMasterId(long masterId) {
        this.masterId = masterId;
    }

    public Short getPublishState() {
        return publishState;
    }

    public void setPublishState(Short publishState) {
        this.publishState = publishState;
    }

    public long getTplId() {
        return tplId;
    }

    public void setTplId(long tplId) {
        this.tplId = tplId;
    }

    public long getArchiveFlag() {
        return archiveFlag;
    }

    public void setArchiveFlag(long archiveFlag) {
        this.archiveFlag = archiveFlag;
    }

    public long getIntodatacenter() {
        return intodatacenter;
    }

    public void setIntodatacenter(long intodatacenter) {
        this.intodatacenter = intodatacenter;
    }

    public String getInputer() {
        return inputer;
    }

    public void setInputer(String inputer) {
        this.inputer = inputer;
    }

    public long getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(long expirationTime) {
        this.expirationTime = expirationTime;
    }

    public Short getIsTop() {
        return isTop;
    }

    public void setIsTop(Short isTop) {
        this.isTop = isTop;
    }

    public String getSpecSuffixWord() {
        return specSuffixWord;
    }

    public void setSpecSuffixWord(String specSuffixWord) {
        this.specSuffixWord = specSuffixWord;
    }

    public String getSpecSuffixUrl() {
        return specSuffixUrl;
    }

    public void setSpecSuffixUrl(String specSuffixUrl) {
        this.specSuffixUrl = specSuffixUrl;
    }

    public String getImportMark() {
        return importMark;
    }

    public void setImportMark(String importMark) {
        this.importMark = importMark;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public String getJabbarMark() {
        return jabbarMark;
    }

    public void setJabbarMark(String jabbarMark) {
        this.jabbarMark = jabbarMark;
    }

    public String getFirstPub() {
        return firstPub;
    }

    public void setFirstPub(String firstPub) {
        this.firstPub = firstPub;
    }

    public long getIsdeal() {
        return isdeal;
    }

    public void setIsdeal(long isdeal) {
        this.isdeal = isdeal;
    }

    @Override
    public int compareTo(ReleaseLib o) {
        if(pubTime < o.pubTime){
            return 1;
        } else {
            return -1;
        }
    }
}