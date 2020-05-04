package com.hiepdt.dicitonaryapp.models;

public class Word {
    private String key;
    private String meaning;

    public Word(String key, String meaning) {
        this.key = key;
        this.meaning = meaning;
    }

    public Word() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }
}
