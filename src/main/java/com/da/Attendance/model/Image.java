package com.da.Attendance.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Blob;

@Document
public class Image {
    @Id
    private String id;
    private String fileName;
    private String contentType;
    private byte[] image;
    private String url;

    public Image() {
    }

    public Image(String id, String imageName, String imageType, byte[] image, String url) {
        this.id = id;
        this.fileName = imageName;
        this.contentType = imageType;
        this.image = image;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
