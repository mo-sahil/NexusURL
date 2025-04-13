package com.example.urlshortener.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.*;

@Document(collection = "urls")
public class Url {
    @Setter
    @Getter
    @Id
    private String id;
    @Setter
    @Getter
    private String originalUrl;
    @Setter
    @Getter
    private String shortUrl;
    @Setter
    @Getter
    private LocalDateTime creationDate;
    @Setter
    @Getter
    private LocalDateTime expirationDate;
    @Setter
    @Getter
    private int clickCount;
    private List<Click> clicks = new ArrayList<>();

    public static class Click {
        private LocalDateTime clickTime;
        private String referrer;
        private String userAgent;
        private String ipAddress;

        public Click() {}

        public Click(LocalDateTime clickTime, String referrer, String userAgent, String ipAddress) {
            this.clickTime = clickTime;
            this.referrer = referrer;
            this.userAgent = userAgent;
            this.ipAddress = ipAddress;
        }

        public LocalDateTime getClickTime() {
            return clickTime;
        }

        public void setClickTime(LocalDateTime clickTime) {
            this.clickTime = clickTime;
        }

        public String getReferrer() {
            return referrer;
        }

        public void setReferrer(String referrer) {
            this.referrer = referrer;
        }

        public String getUserAgent() {
            return userAgent;
        }

        public void setUserAgent(String userAgent) {
            this.userAgent = userAgent;
        }

        public String getIpAddress() {
            return ipAddress;
        }

        public void setIpAddress(String ipAddress) {
            this.ipAddress = ipAddress;
        }
    }

    public void addClick(String referrer, String userAgent, String ipAddress) {
        this.clicks.add(new Click(LocalDateTime.now(), referrer, userAgent, ipAddress));
        this.clickCount++;
    }

    public Url() {
    }

    public Url(String originalUrl, String shortUrl, LocalDateTime creationDate, LocalDateTime expirationDate) {
        this.originalUrl = originalUrl;
        this.shortUrl = shortUrl;
        this.creationDate = creationDate;
        this.expirationDate = expirationDate;
        this.clickCount = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public int getClickCount() {
        return clickCount;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    public List<Click> getClicks() {
        return clicks;
    }

    public void setClicks(List<Click> clicks) {
        this.clicks = clicks;
    }
}