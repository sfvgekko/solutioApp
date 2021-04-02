package com.twitter.solutio.controllers;

import com.twitter.solutio.listeners.StreamTweetsListener;
import com.twitter.solutio.models.Tweet;
import com.twitter.solutio.services.TweetService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;


@WebMvcTest(TweetController.class)
@ExtendWith(MockitoExtension.class)
class TweetControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TweetService tweetService;

    @MockBean
    StreamTweetsListener streamTweetsListener;

    @Test
    @WithMockUser(username = "user1")
    void getAllTweetsTest() throws Exception {
        //Given
        Tweet tweet1 = new Tweet();
        tweet1.setId(1L);

        Tweet tweet2 = new Tweet();
        tweet2.setId(2L);

        List<Tweet> tweetList = new ArrayList<>();
        tweetList.add(tweet1);
        tweetList.add(tweet2);

        //When
        when(tweetService.getAllTweets()).thenReturn(tweetList);

        //Then
        mockMvc.perform(MockMvcRequestBuilders.get("/tweets/all"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)));
    };


    @Test
    void getAllTweetsUnauthorizedTest() throws Exception {
        //Given
        Tweet tweet1 = new Tweet();
        tweet1.setId(1L);

        Tweet tweet2 = new Tweet();
        tweet2.setId(2L);

        List<Tweet> tweetList = new ArrayList<>();
        tweetList.add(tweet1);
        tweetList.add(tweet2);

        //When
        when(tweetService.getAllTweets()).thenReturn(tweetList);

        //Then
        mockMvc.perform(MockMvcRequestBuilders.get("/tweets/all"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    };


    @Test
    @WithMockUser(username = "user1")
    void validateTweetTest() throws Exception {
        //Given
        Tweet tweet = new Tweet();
        tweet.setId(1L);
        tweet.setValidated(true);
        tweet.setValidatorUser("user1");

        //When
        when(tweetService.validateTweet(anyLong())).thenReturn(tweet);

        //Then
        mockMvc.perform(MockMvcRequestBuilders.put("/tweets/validate/1"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.validatorUser", is("user1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.validated", is(true)));
    }

    @Test
    @WithMockUser(username = "user1")
    void getValidatedTweetsByUserTest() throws Exception {
        //Given
        Tweet tweet1 = new Tweet();
        tweet1.setId(1L);
        tweet1.setValidated(true);
        tweet1.setValidatorUser("user1");

        Tweet tweet2 = new Tweet();
        tweet2.setId(2L);
        tweet2.setValidated(true);
        tweet2.setValidatorUser("user1");

        Tweet tweet3 = new Tweet();
        tweet3.setId(3L);
        tweet3.setValidated(true);
        tweet3.setValidatorUser("user1");

        List<Tweet> tweetList = new ArrayList<>();
        tweetList.add(tweet1);
        tweetList.add(tweet2);
        tweetList.add(tweet3);

        //When
        when(tweetService.getValidatedTweetsByUser(anyString())).thenReturn(tweetList);

        //Then
        mockMvc.perform(MockMvcRequestBuilders.get("/tweets/validated/user1"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].validatorUser", is("user1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].validated", is(true)));
    }

    @Test
    @WithMockUser(username = "user1")
    void getHashtagRankTest() throws Exception {
        //Given
        List<String> hashtagsRankList = Arrays.asList("Hashtag1", "Hashtag2", "Hashtag3", "Hashtag4", "Hashtag5",
                                                      "Hashtag6", "Hashtag7", "Hashtag8", "Hashtag9", "Hashtag10");
        //When
        when(tweetService.getHashtagRank()).thenReturn(hashtagsRankList);

        //Then
        mockMvc.perform(MockMvcRequestBuilders.get("/tweets/hashtagRank"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]", is("Hashtag1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[9]", is("Hashtag10")));

    }
}