package istu.edu.irnitu.dataModels;

/**
 * NewFitGid
 * Created by Александр on 26.07.2016.
 * Contact on luck.alex13@gmail.com
 * Copyright Aleksandr Novikov 2016
 */
public class ResourceModel {
    private String id;
    private String updTimeDate;
    private String resName;
    private String imageUrl;
    private String resLink;

    public ResourceModel(String id, String updTimeDate, String resName, String imageUrls, String resLink) {
        this.id = id;
        this.updTimeDate = updTimeDate;
        this.resName = resName;
        this.imageUrl = imageUrls;
        this.resLink = resLink;
    }

    public ResourceModel(String updTimeDate, String resName, String imageUrl, String resLink) {
        this.updTimeDate = updTimeDate;
        this.resName = resName;
        this.imageUrl = imageUrl;
        this.resLink = resLink;
    }

    public ResourceModel(String resName, String imageUrl, String resLink) {
        this.resName = resName;
        this.imageUrl = imageUrl;
        this.resLink = resLink;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUpdTimeDate() {
        return updTimeDate;
    }

    public void setUpdTimeDate(String updTimeDate) {
        this.updTimeDate = updTimeDate;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getResLink() {
        return resLink;
    }

    public void setResLink(String resLink) {
        this.resLink = resLink;
    }
}
