package com.twitter.solutio.application;

import com.twitter.solutio.domain.Tweet;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import twitter4j.HashtagEntity;
import twitter4j.Status;

import java.util.Arrays;

@Component
public class ConvertStatusToTweet implements Converter<Status, Tweet> {

    @Override
    public Tweet convert(Status status) {
        Tweet tweet = new Tweet();
        tweet.setUser(status.getUser() != null && status.getUser().getName() != null ? status.getUser().getName() : "");
        tweet.setText(status.getText() != null ? status.getText() : "");
        tweet.setLang(status.getLang() != null ? status.getLang() : "");
        tweet.setValidated(false);
        tweet.setHashtagsList(status.getHashtagEntities() != null ? extractHashtags(status) : new String[] {});
        tweet.setFollowers(status.getUser().getFollowersCount());
        return tweet;
    }


    private String[] extractHashtags(Status status){
        if (status.getHashtagEntities() != null && status.getHashtagEntities().length > 0){
            return Arrays.stream(status.getHashtagEntities()).map(HashtagEntity::getText).toArray(String[]::new);
        } else {
            return new String[] {};
        }
    }

}
