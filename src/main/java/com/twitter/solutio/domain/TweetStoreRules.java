package com.twitter.solutio.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class TweetStoreRules {

    @Value("${rules.allowedLanguages}")
    private List<String> allowedLanguages;

    @Value("${rules.minFollowers}")
    private int minFollowers;

    public Boolean checkStoreRules(String lang, int followers){
        return (followers >= minFollowers && allowedLanguages.contains(lang));
    }

}
