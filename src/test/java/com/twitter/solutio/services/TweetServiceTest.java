package com.twitter.solutio.services;

import com.twitter.solutio.listeners.StreamTweetsListener;
import com.twitter.solutio.models.Tweet;
import com.twitter.solutio.repositories.TweetRepository;
import com.twitter.solutio.utils.TweetStoreRules;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.ArrayMatching.arrayContaining;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.*;


@WebMvcTest(TweetService.class)
@WithMockUser(username = "userTest")
@ExtendWith(MockitoExtension.class)
class TweetServiceTest {

    @InjectMocks
    TweetService tweetService;

    @MockBean
    TweetRepository tweetRepository;

    @MockBean
    TweetStoreRules tweetStoreRules;

    @MockBean
    StreamTweetsListener streamTweetsListener;


    @Test
    void saveTweetTest() {
        //Given
        Tweet tweet = new Tweet();
        tweet.setId(1L);
        tweet.setUser("userTest");
        tweet.setText("Text to test");
        tweet.setLang("es");
        tweet.setValidated(false);
        tweet.setHashtagsList(new String[] {"Hashtag1", "Hashtag2"});

        //When
        when(tweetRepository.save(any(Tweet.class))).thenReturn(tweet);
        Tweet response = tweetService.saveTweet(mock(Tweet.class));

        //Then
        assertThat(response, instanceOf(Tweet.class));
        assertThat(response.getId(), is(1L));
        assertThat(response.getUser(), is("userTest"));
        assertThat(response.getText(), is("Text to test"));
        assertThat(response.getLang(), is("es"));
        assertThat(response.getValidated(), is(false));
        assertThat(response.getHashtagsList(), arrayContaining("Hashtag1", "Hashtag2"));
        assertThat(response.getValidatorUser(), nullValue());
    }

    @Test
    void getAllTweetsTest() {
        //Given
        Tweet tweet1 = new Tweet();
        tweet1.setId(1L);

        Tweet tweet2 = new Tweet();
        tweet2.setId(2L);

        List<Tweet> tweetList = new ArrayList<>();
        tweetList.add(tweet1);
        tweetList.add(tweet2);

        //When
        when(tweetRepository.findAll()).thenReturn(tweetList);
        List<Tweet> response = tweetService.getAllTweets();

        //Then
        assertThat(response.size(), is(2));
    }

    @Test
    void validateTweetOkTest() {
        //Given
        Tweet tweet = new Tweet();
        tweet.setId(1L);
        tweet.setValidated(false);

        //When
        ArgumentCaptor<Tweet> tweetCaptured = ArgumentCaptor.forClass(Tweet.class);
        when(tweetRepository.save(tweetCaptured.capture())).thenReturn(mock(Tweet.class));
        when(tweetRepository.findById(anyLong())).thenReturn(java.util.Optional.of(tweet));
        tweetService.validateTweet(1L);

        //Then
        verify(tweetRepository, times(1)).save(any(Tweet.class));
        assertThat(tweetCaptured.getValue().getValidated(), is(true));
        assertThat(tweetCaptured.getValue().getValidatorUser(), is("userTest"));
    }

    @Test
    void validateTweetAlreadyValidatedTest() {
        //Given
        Tweet tweet = new Tweet();
        tweet.setId(1L);
        tweet.setUser("userTest");
        tweet.setValidated(true);

        //When
        when(tweetRepository.findById(anyLong())).thenReturn(java.util.Optional.of(tweet));

        //Then
        Assertions.assertThrows(ResponseStatusException.class, () -> tweetService.validateTweet(1L));
        verify(tweetRepository, never()).save(any(Tweet.class));
    }

    @Test
    void validateTweetWithWrongIdTest() {
        //Given

        //When
        when(tweetRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());

        //Then
        Assertions.assertThrows(ResponseStatusException.class, () -> tweetService.validateTweet(1L));
        verify(tweetRepository, never()).save(any(Tweet.class));
    }

    @Test
    void getValidatedTweetsByUserTest() {
        //Given

        //When
        tweetService.getValidatedTweetsByUser("");
        //Then
        verify(tweetRepository, times(1)).findByValidatorUserAndValidatedTrue(anyString());

    }

    @Test
    void getHashtagRankTest() {
        //Given

        //Times of Hashtags in dummy data
        //Hashtag1 = 5 - Hashtag2 = 4 - Hashtag3 to Hashtag9 = 3 ; Hashtag10 = 2 ; Hashtag11 and Hashtag12 = 1
        Tweet tweet1 = new Tweet();
        tweet1.setHashtagsList(new String[] {"Hashtag1", "Hashtag2", "Hashtag3", "Hashtag4", "Hashtag5", "Hashtag6", "Hashtag7", "Hashtag8", "Hashtag9"});
        Tweet tweet2 = new Tweet();
        tweet2.setHashtagsList(new String[] {"Hashtag1", "Hashtag2", "Hashtag3", "Hashtag4", "Hashtag5", "Hashtag6", "Hashtag7", "Hashtag8", "Hashtag9"});
        Tweet tweet3 = new Tweet();
        tweet3.setHashtagsList(new String[] {"Hashtag1", "Hashtag2", "Hashtag3", "Hashtag4", "Hashtag5", "Hashtag6", "Hashtag7", "Hashtag8", "Hashtag9"});
        Tweet tweet4 = new Tweet();
        tweet4.setHashtagsList(new String[] {"Hashtag1", "Hashtag2", "Hashtag10", "Hashtag11", "Hashtag12"});
        Tweet tweet5 = new Tweet();
        tweet5.setHashtagsList(new String[] {"Hashtag1", "Hashtag10"});

        List<Tweet> tweetList = new ArrayList<>();
        tweetList.add(tweet1);
        tweetList.add(tweet2);
        tweetList.add(tweet3);
        tweetList.add(tweet4);
        tweetList.add(tweet5);

        //When
        when(tweetRepository.findAll()).thenReturn(tweetList);
        when(tweetStoreRules.getItemsInHashtagRank()).thenReturn(10);
        List<String> response =  tweetService.getHashtagRank();

        //Then
        assertThat(response.size(), is(10));
        assertThat(response.get(0), is("Hashtag1"));
        assertThat(response.get(1), is("Hashtag2"));
        assertThat(response.get(9), is("Hashtag10"));
    }
}