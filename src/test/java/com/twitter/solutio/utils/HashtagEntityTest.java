package com.twitter.solutio.utils;

import twitter4j.HashtagEntity;

public class HashtagEntityTest implements HashtagEntity {

    private String text;

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public int getStart() {
        return 0;
    }

    @Override
    public int getEnd() {
        return 0;
    }
}
