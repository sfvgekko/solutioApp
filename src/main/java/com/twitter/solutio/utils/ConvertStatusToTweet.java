package com.twitter.solutio.utils;

import com.twitter.solutio.models.Tweet;
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
        tweet.setUser(status.getUser().getName());
        tweet.setText(status.getText());
        tweet.setLang(status.getLang());
        tweet.setValidated(false);
        tweet.setHashtagsList(extractHashtags(status));
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
