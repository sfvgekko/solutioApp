package com.twitter.solutio.application;

import com.twitter.solutio.domain.Tweet;
import com.twitter.solutio.domain.TweetRepository;
import com.twitter.solutio.domain.TweetStoreRules;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class ProcessTweet {

    @Value("${rules.maxTextLength}")
    private int maxTextLength;

    @Autowired
    TweetRepository tweetRepository;

    @Autowired
    TweetStoreRules tweetStoreRules;

    public void process(Tweet tweet){
        if (Boolean.TRUE.equals(tweetStoreRules.checkStoreRules(tweet.getLang(), tweet.getFollowers()))){
            tweet.setText(trimTextToMaxLength(tweet.getText()));
            log.info("Adding tweet {}", tweet);
            tweetRepository.save(tweet);
        }
    }

    private String trimTextToMaxLength(String text){
        return text.length() <= maxTextLength ? text : text.substring(0, maxTextLength);
    }

}