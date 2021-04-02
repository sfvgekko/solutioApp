package com.twitter.solutio.listeners;

import com.twitter.solutio.models.Tweet;
import com.twitter.solutio.utils.TweetStoreRules;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import twitter4j.*;

@Slf4j
@Component
public class StreamTweetsListener implements StatusListener {

    @Autowired
    TweetStoreRules tweetStoreRules;

    @Autowired
    ConversionService conversionService;

    @Override
    public void onStatus(Status status) {
        tweetStoreRules.processTweet(conversionService.convert(status, Tweet.class), status.getUser().getFollowersCount());
    }

    @Override
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
        log.debug("onDeletionNotice {} ", statusDeletionNotice);
    }

    @Override
    public void onTrackLimitationNotice(int i) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onScrubGeo(long l, long l1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onStallWarning(StallWarning stallWarning) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onException(Exception e) {
        e.printStackTrace();
    }
}
