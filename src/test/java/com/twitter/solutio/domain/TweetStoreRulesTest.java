package com.twitter.solutio.domain;

import com.twitter.solutio.SolutioApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


@SpringBootTest(classes= SolutioApplication.class)
@ExtendWith(MockitoExtension.class)
class TweetStoreRulesTest {

    @Autowired
    TweetStoreRules tweetStoreRules;

    @Value("${rules.allowedLanguages}")
    private List<String> allowedLanguages;

    @Value("${rules.minFollowers}")
    private int minFollowers;


    @Test
    void checkStoreRulesMatchAllTest(){
        //Given
        final String lang = allowedLanguages.get(0);
        final int followers = minFollowers;

        //When
        Boolean result = tweetStoreRules.checkStoreRules(lang, followers);

        //Then
        assertThat(result, is(true));
    }


    @Test
    void checkStoreRulesWithoutMinFollowersTest(){
        //Given
        final String lang = allowedLanguages.get(0);
        final int followers = minFollowers - 1;

        //When
        Boolean result = tweetStoreRules.checkStoreRules(lang, followers);

        //Then
        assertThat(result, is(false));
    }


    @Test
    void checkStoreRulesWithNoAllowedLanguageTest(){
        //Given
        final String lang = "xx";
        final int followers = minFollowers;

        //When
        Boolean result = tweetStoreRules.checkStoreRules(lang, followers);

        //Then
        assertThat(result, is(false));
    }


}