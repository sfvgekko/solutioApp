package com.twitter.solutio.application;

import com.twitter.solutio.domain.Tweet;
import com.twitter.solutio.stubs.HashtagStub;
import com.twitter.solutio.stubs.StatusStub;
import com.twitter.solutio.stubs.UserStatusStub;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.ConversionService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.ArrayMatching.arrayContaining;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ConvertStatusToTweetTest {

    @Autowired
    ConversionService conversionService;

    @Test
    void canConvertWithHashtag() {
        //Given
        final String userTest = "userTest";
        final String tweetText = "tweetText";
        final String lang = "es";
        final String hashtag1 = "Hashtag1";
        final String hashtag2 = "Hashtag2";
        final int followers = 1500;

        UserStatusStub userStatusStub = new UserStatusStub();
        userStatusStub.setName(userTest);
        userStatusStub.setFollowersCount(followers);

        HashtagStub hashtagStub1 = new HashtagStub();
        hashtagStub1.setText(hashtag1);

        HashtagStub hashtagStub2 = new HashtagStub();
        hashtagStub2.setText(hashtag2);

        StatusStub statusStub = new StatusStub();
        statusStub.setText(tweetText);
        statusStub.setLang(lang);
        statusStub.setHashtagEntities(new HashtagStub[] {hashtagStub1, hashtagStub2});
        statusStub.setUser(userStatusStub);

        //When
        Tweet response = conversionService.convert(statusStub, Tweet.class);

        //Then
        assertThat(response.getUser(), is(userTest));
        assertThat(response.getText(), is(tweetText));
        assertThat(response.getLang(), is(lang));
        assertThat(response.getValidated(), is(false));
        assertThat(response.getFollowers(), is(followers));
        assertThat(response.getHashtagsList(), arrayContaining(hashtag1, hashtag2));
    }

    @Test
    void canConvertWithoutHashtag() {
        //Given
        final String userTest = "userTest";
        final String tweetText = "tweetText";
        final String lang = "es";
        final int followers = 1500;

        UserStatusStub userStatusStub = new UserStatusStub();
        userStatusStub.setName(userTest);
        userStatusStub.setFollowersCount(followers);

        StatusStub statusStub = new StatusStub();
        statusStub.setText(tweetText);
        statusStub.setLang(lang);
        statusStub.setHashtagEntities(new HashtagStub[0]);
        statusStub.setUser(userStatusStub);

        //When
        Tweet response = conversionService.convert(statusStub, Tweet.class);

        //Then
        assertThat(response.getUser(), is(userTest));
        assertThat(response.getText(), is(tweetText));
        assertThat(response.getLang(), is(lang));
        assertThat(response.getValidated(), is(false));
        assertThat(response.getFollowers(), is(followers));
        assertThat(response.getHashtagsList(), is(emptyArray()));
    }
}