package com.twitter.solutio.infraestructure;

import com.twitter.solutio.application.ProcessTweet;
import com.twitter.solutio.domain.Tweet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import twitter4j.*;

import java.util.Objects;

@Slf4j
@Component
public class TweetsStreamListener implements StatusListener {

    @Autowired
    ProcessTweet processTweet;

    @Autowired
    ConversionService conversionService;

    @Override
    public void onStatus(Status status) {
        processTweet.process(Objects.requireNonNull(conversionService.convert(status, Tweet.class)));
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
