package com.twitter.solutio.infraestructure;

import com.twitter.solutio.domain.Tweet;
import com.twitter.solutio.application.TweetsApiService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tweets")
public class TweetsApiController {

    @Autowired
    TweetsApiService tweetsApiService;

    @GetMapping("/all")
    @ApiOperation(value="Get list with all the stored tweets")
    public List<Tweet> getAllTweets(){
        return tweetsApiService.getAllTweets();
    }

    @PutMapping("/validate/{id}")
    @ApiOperation(value="Mark a tweet as validated")
    @ApiParam(value="Tweet Id", required=true)
    public Tweet validateTweet(@PathVariable Long id) {
        return tweetsApiService.validateTweet(id);
    }

    @GetMapping("/validated/{user}")
    @ApiOperation(value="Get a list with the validated tweets for a specific user")
    @ApiParam(value="user name", required=true)
    public List<Tweet> getValidatedTweetsByUser(@PathVariable String user){
        return tweetsApiService.getValidatedTweetsByUser(user);
    }

    @GetMapping("/hashtagRank")
    @ApiOperation(value="Get a list with the most used Hashtags")
    public List<String> getHashtagRank(){
        return tweetsApiService.getHashtagRank();
    }

}
