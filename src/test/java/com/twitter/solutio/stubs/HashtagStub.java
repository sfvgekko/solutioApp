package com.twitter.solutio.stubs;

import twitter4j.HashtagEntity;

public class HashtagStub implements HashtagEntity {

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
