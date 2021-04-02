package com.twitter.solutio;

import com.twitter.solutio.listeners.StreamTweetsListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

import javax.annotation.PostConstruct;


@SpringBootApplication
public class SolutioApplication {

	@Autowired
	StreamTweetsListener streamTweetsListener;

	public static void main(String[] args) {
		SpringApplication.run(SolutioApplication.class, args);
	}

	@PostConstruct
	public void execute(){
		TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
		twitterStream.addListener(streamTweetsListener);
		// sample() method internally creates a thread which manipulates TwitterStream and calls these adequate listener methods continuously.
		twitterStream.sample();
	}


}
