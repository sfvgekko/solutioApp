package com.twitter.solutio.application;

import com.twitter.solutio.domain.Tweet;
import com.twitter.solutio.domain.TweetRepository;
import com.twitter.solutio.domain.TweetStoreRules;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.ArrayMatching.arrayContaining;
import static org.mockito.Mockito.*;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ProcessTweetTest {

    @Value("${rules.maxTextLength}")
    private int maxTextLength;

    @Autowired
    ProcessTweet processTweet;

    @MockBean
    TweetRepository tweetRepository;

    @MockBean
    TweetStoreRules tweetStoreRules;

    @Captor
    private ArgumentCaptor<Tweet> captor;

    @Test
    void processTest() {
        //Given
        final String userTest = "userTest";
        final String tweetText = "tweetTest";
        final String lang = "es";
        final String hashtag1 = "Hashtag1";
        final String hashtag2 = "Hashtag2";
        final int followers = 1500;

        Tweet tweet = new Tweet();
        tweet.setUser(userTest);
        tweet.setText(tweetText);
        tweet.setLang(lang);
        tweet.setValidated(false);
        tweet.setHashtagsList(new String[] {hashtag1, hashtag2});
        tweet.setFollowers(followers);

        //When
        when(tweetStoreRules.checkStoreRules(lang, followers)).thenReturn(true);
        processTweet.process(tweet);

        //Then
        verify(tweetRepository).save(captor.capture());
        assertThat(captor.getValue().getUser(), is(userTest));
        assertThat(captor.getValue().getText(), is(tweetText));
        assertThat(captor.getValue().getLang(), is(lang));
        assertThat(captor.getValue().getValidated(), is(false));
        assertThat(captor.getValue().getValidatorUser(), nullValue());
        assertThat(captor.getValue().getHashtagsList(), arrayContaining(hashtag1, hashtag2));

    }

    @Test
    void processTestWithTextTrimmed() {
        //Given
        final String userTest = "userTest";
        final String tweetText = StringUtils.repeat("*", maxTextLength+1);
        final String lang = "es";
        final String hashtag1 = "Hashtag1";
        final String hashtag2 = "Hashtag2";
        final int followers = 1500;

        Tweet tweet = new Tweet();
        tweet.setUser(userTest);
        tweet.setText(tweetText);
        tweet.setLang(lang);
        tweet.setValidated(false);
        tweet.setHashtagsList(new String[] {hashtag1, hashtag2});
        tweet.setFollowers(followers);

        //When
        when(tweetStoreRules.checkStoreRules(lang, followers)).thenReturn(true);
        processTweet.process(tweet);

        //Then
        verify(tweetRepository).save(captor.capture());
        assertThat(captor.getValue().getUser(), is(userTest));
        assertThat(captor.getValue().getText(), is(tweetText.substring(0, tweetText.length()-1)));
        assertThat(captor.getValue().getLang(), is(lang));
        assertThat(captor.getValue().getValidated(), is(false));
        assertThat(captor.getValue().getValidatorUser(), nullValue());
        assertThat(captor.getValue().getHashtagsList(), arrayContaining(hashtag1, hashtag2));

    }
}