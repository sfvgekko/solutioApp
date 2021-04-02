package com.twitter.solutio.utils;

import com.twitter.solutio.models.Tweet;
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
        UserTest userTest = new UserTest();
        userTest.setName("userTest");

        HashtagEntityTest hashtagEntityTest1 = new HashtagEntityTest();
        hashtagEntityTest1.setText("Hashtag1");

        HashtagEntityTest hashtagEntityTest2 = new HashtagEntityTest();
        hashtagEntityTest2.setText("Hashtag2");

        StatusTest statusTest = new StatusTest();
        statusTest.setText("textTest");
        statusTest.setLang("es");
        statusTest.setHashtagEntities(new HashtagEntityTest[] {hashtagEntityTest1, hashtagEntityTest2});
        statusTest.setUser(userTest);

        //When
        Tweet response = conversionService.convert(statusTest, Tweet.class);

        //Then
        assertThat(response.getUser(), is("userTest"));
        assertThat(response.getText(), is("textTest"));
        assertThat(response.getLang(), is("es"));
        assertThat(response.getValidated(), is(false));
        assertThat(response.getLang(), is("es"));
        assertThat(response.getHashtagsList(), arrayContaining("Hashtag1", "Hashtag2"));
    }

    @Test
    void canConvertWithoutHashtag() {
        //Given
        UserTest userTest = new UserTest();
        userTest.setName("userTest");

        StatusTest statusTest = new StatusTest();
        statusTest.setText("textTest");
        statusTest.setLang("es");
        statusTest.setHashtagEntities(new HashtagEntityTest[0]);
        statusTest.setUser(userTest);

        //When
        Tweet response = conversionService.convert(statusTest, Tweet.class);

        //Then
        assertThat(response.getUser(), is("userTest"));
        assertThat(response.getText(), is("textTest"));
        assertThat(response.getLang(), is("es"));
        assertThat(response.getValidated(), is(false));
        assertThat(response.getLang(), is("es"));
        assertThat(response.getHashtagsList(), is(emptyArray()));
    }
}