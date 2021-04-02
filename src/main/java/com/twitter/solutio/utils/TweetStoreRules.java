package com.twitter.solutio.utils;

import com.twitter.solutio.models.Tweet;
import com.twitter.solutio.services.TweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Locale;

@Component
public class TweetStoreRules {

    @Value("${rules.allowedLanguages}")
    private List<String> allowedLanguages;

    @Value("${rules.minFollowers}")
    private int minFollowers;

    @Value("${rules.maxTextLength}")
    private int maxTextLength;

    @Value("${rules.itemsInHashtagRank}")
    private int itemsInHashtagRank;

    @Autowired
    TweetService tweetService;


    public void processTweet(Tweet tweet, int followers){
        if (followers >= minFollowers && allowedLanguages.contains(tweet.getLang())){
            tweetService.saveTweet(tweet);
        }
    }

    public String trimTextToMaxLength(String text){
        return text.length() <= maxTextLength ? text : text.substring(0, maxTextLength);
    }

    public int getItemsInHashtagRank(){
        return itemsInHashtagRank;
    }

}
