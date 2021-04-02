package com.twitter.solutio.utils;

import com.twitter.solutio.SolutioApplication;
import com.twitter.solutio.models.Tweet;
import com.twitter.solutio.services.TweetService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;


@SpringBootTest(classes= SolutioApplication.class)
@ExtendWith(MockitoExtension.class)
class TweetStoreRulesTest {

    @Autowired
    TweetStoreRules tweetStoreRules;

    @MockBean
    TweetService tweetService;

    @Value("${rules.maxTextLength}")
    private int maxTextLength;

    @Value("${rules.allowedLanguages}")
    private List<String> allowedLanguages;

    @Value("${rules.itemsInHashtagRank}")
    private int itemsInHashtagRank;

    @Value("${rules.minFollowers}")
    private int minFollowers;


    @Test
    void processTweetMatchRulesTest(){
        //Given
        Tweet tweet = new Tweet();
        tweet.setId(1L);
        tweet.setUser("userTest");
        tweet.setText("Text to test");
        tweet.setLang("es");
        tweet.setValidated(false);
        tweet.setHashtagsList(new String[] {"Hashtag1", "Hashtag2"});

        //When
        tweetStoreRules.processTweet(tweet, minFollowers);

        //Then
        verify(tweetService, times(1)).saveTweet(any(Tweet.class));

    }


    @Test
    void processTweetWithoutMinFollowersTest(){
        //Given
        Tweet tweet = new Tweet();
        tweet.setId(1L);
        tweet.setUser("userTest");
        tweet.setText("Text to test");
        tweet.setLang("es");
        tweet.setValidated(false);
        tweet.setHashtagsList(new String[] {"Hashtag1", "Hashtag2"});

        //When
        tweetStoreRules.processTweet(tweet, minFollowers-1);

        //Then
        verify(tweetService, never()).saveTweet(any(Tweet.class));
    }


    @Test
    void processTweetWithNoAllowedLanguageTest(){
        //Given
        Tweet tweet = new Tweet();
        tweet.setId(1L);
        tweet.setUser("userTest");
        tweet.setText("Text to test");
        tweet.setLang("xx");
        tweet.setValidated(false);
        tweet.setHashtagsList(new String[] {"Hashtag1", "Hashtag2"});

        //When
        tweetStoreRules.processTweet(tweet, minFollowers);

        //Then
        verify(tweetService, never()).saveTweet(any(Tweet.class));

    }

    @Test
    void trimTextToMaxLengthTest() {
        //Given
        String text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry." +
                " Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, " +
                "when an unknown printer took a galley of type and scrambled it to make a type specimen book." +
                "It has survived not only five centuries, but also the leap into electronic typesetting, remaining " +
                "essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing " +
                "Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker " +
                "including versions of Lorem Ipsum";

        //When
        String response = tweetStoreRules.trimTextToMaxLength(text);

        //Then
        assertThat(response.length(), is(maxTextLength));
    }


    @Test
    void getItemsInHashtagRankTest() {
        //Given
        //When
        int response = tweetStoreRules.getItemsInHashtagRank();

        //Then
        assertThat(response, is(itemsInHashtagRank));
    }

}