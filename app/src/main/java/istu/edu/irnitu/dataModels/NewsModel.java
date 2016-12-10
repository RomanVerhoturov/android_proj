package istu.edu.irnitu.dataModels;

/**
 * NewFitGid
 * Created by Александр on 04.06.2016.
 * Contact on luck.alex13@gmail.com
 * © Aleksandr Novikov 2016
 */
public class NewsModel {
    public static final int COMMON = 0, PROGRESS = 1;
    private String id;
    private String theme;
    private int itemType;
    private String publishDate;
    private String newsTitle;
    private String newsText;
    private String newsLink;
    private String imagesUrls;
    private String headerImageUrl;

    public NewsModel(int itemType ) {
        this.itemType = itemType;
    }

    public NewsModel(String id, int itemType, String theme, String headerImageUrl,String publishDate, String newsTitle, String newsText, String newsLink, String imagesUrls) {
        this.id = id;
        this.theme = theme;
        this.itemType = itemType;
        this.publishDate = publishDate;
        this.newsTitle = newsTitle;
        this.newsText = newsText;
        this.newsLink = newsLink;
        this.imagesUrls = imagesUrls;
        this.headerImageUrl = headerImageUrl;
    }

    public NewsModel(String theme, int itemType, String publishDate, String newsTitle, String newsLink) {
        this.theme = theme;
        this.itemType = itemType;
        this.publishDate = publishDate;
        this.newsTitle = newsTitle;
        this.newsLink = newsLink;
    }

    public int getItemType() {
        return itemType;
    }

    public String getHeaderImageUrl() {
        return headerImageUrl;
    }

    public void setHeaderImageUrl(String headerImageUrl) {
        this.headerImageUrl = headerImageUrl;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getNewsLink() {
        return newsLink;
    }

    public void setNewsLink(String newsLink) {
        this.newsLink = newsLink;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsText() {
        return newsText;
    }

    public void setNewsText(String newsText) {
        this.newsText = newsText;
    }

    public String getImagesUrls() {
        return imagesUrls;
    }

    public void setImagesUrls(String imagesUrls) {
        this.imagesUrls = imagesUrls;
    }
}
