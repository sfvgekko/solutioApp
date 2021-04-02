package com.twitter.solutio.services;

import com.twitter.solutio.models.Tweet;
import com.twitter.solutio.repositories.TweetRepository;
import com.twitter.solutio.utils.TweetStoreRules;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TweetService {

    @Autowired
    TweetRepository tweetRepository;

    @Autowired
    TweetStoreRules tweetStoreRules;


    public Tweet saveTweet(Tweet tweet){
        log.info("Adding tweet {}", tweet);
        tweet.setText(tweetStoreRules.trimTextToMaxLength(tweet.getText()));
        return tweetRepository.save(tweet);
    }


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
                                                .limit(tweetStoreRules.getItemsInHashtagRank())
                                                .collect(Collectors.toList());
    }

}
