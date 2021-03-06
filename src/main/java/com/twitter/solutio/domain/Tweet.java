package com.twitter.solutio.domain;

import javax.persistence.*;
import java.util.Arrays;

@Entity
public class Tweet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String user;

    @Column(length=280)
    private String text;

    private String lang;
    private Boolean validated;
    private String validatorUser;
    private String [] hashtagsList;
    private int followers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Boolean getValidated() {
        return validated;
    }

    public void setValidated(Boolean validated) {
        this.validated = validated;
    }

    public String getValidatorUser() {
        return validatorUser;
    }

    public void setValidatorUser(String validatorUser) {
        this.validatorUser = validatorUser;
    }

    public String[] getHashtagsList() {
        return hashtagsList;
    }

    public void setHashtagsList(String[] hashtagsList) {
        this.hashtagsList = hashtagsList;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }


    @Override
    public String toString() {
        return "Tweet{" +
                "id=" + id +
                ", user='" + user + '\'' +
                ", text='" + text + '\'' +
                ", lang='" + lang + '\'' +
                ", validated=" + validated +
                ", validatorUser='" + validatorUser + '\'' +
                ", hashtagsList=" + Arrays.toString(hashtagsList) +
                ", followers=" + followers +
                '}';
    }
}
