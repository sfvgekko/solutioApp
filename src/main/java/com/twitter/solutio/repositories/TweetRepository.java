package com.twitter.solutio.repositories;

import com.twitter.solutio.models.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {

    List<Tweet> findByValidatorUserAndValidatedTrue(String validatorUser);

}
