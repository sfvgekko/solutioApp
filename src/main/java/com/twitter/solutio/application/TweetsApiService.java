package com.twitter.solutio.application;

import com.twitter.solutio.domain.Tweet;
import com.twitter.solutio.domain.TweetRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TweetsApiService {

    @Value("${rules.itemsInHashtagRank}")
    private int itemsInHashtagRank;

    @Autowired
    TweetRepository tweetRepository;


    public List<Tweet> getAllTweets() {
        return tweetRepository.findAll();
    }


    public Tweet validateTweet(Long id) {
        Optional<Tweet> tweetOptional = tweetRepository.findById(id);
        if (tweetOptional.isPresent()){
            Tweet tweet = tweetOptional.get();
            if (Boolean.FALSE.equals(tweet.getValidated())){
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                tweet.setValidatorUser(authentication.getName());
                tweet.setValidated(true);
                log.info("Validating tweet {} by {}", tweet, tweet.getValidatorUser());
                return tweetRepository.save(tweet);

            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tweet Already Validated");
            }

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tweet Not Found");
        }
    }


    public List<Tweet> getValidatedTweetsByUser(String user) {
        return tweetRepository.findByValidatorUserAndValidatedTrue(user);
    }


    public List<String> getHashtagRank(){
        log.info("Getting Hashtag ranking");
        List<Tweet> tweetList = tweetRepository.findAll();
        Map<String, Long> hashTagRankMap = tweetList.stream()
                                        .filter(x->x.getHashtagsList().length>0)
                                        .flatMap(x-> Arrays.stream(x.getHashtagsList()))
                                        .collect(Collectors.groupingBy(x -> x, Collectors.counting()));

        return hashTagRankMap.entrySet()
                                                .stream()
                                                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                                                .map(Map.Entry::getKey)
                                                .limit(itemsInHashtagRank)
                                                .collect(Collectors.toList());
    }

}
