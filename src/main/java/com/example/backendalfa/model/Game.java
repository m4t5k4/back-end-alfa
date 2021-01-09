package com.example.backendalfa.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "games")
public class Game {
    @Id
    private String id;
    private Integer appId;
    private String gameTitle;
    private String developer;
    private Date releaseDate;

    public Game(){

    }

    public Game(String gameTitle, String developer, Date releaseDate, Integer appId){
        this.gameTitle = gameTitle;
        this.developer = developer;
        this.releaseDate = releaseDate;
        this.appId = appId;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public Integer getAppId() {
        return appId;
    }
    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getGameTitle() {
        return gameTitle;
    }
    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }

    public String getDeveloper() {
        return developer;
    }
    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }
    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }
}

