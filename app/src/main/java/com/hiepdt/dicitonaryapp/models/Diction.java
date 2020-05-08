package com.hiepdt.dicitonaryapp.models;

public class Diction {
    private int id;
    private String key;
    private String meaning;
    private String langFrom;
    private String langTo;

    public Diction(int id, String key, String meaning, String langFrom, String langTo) {
        this.id = id;
        this.key = key;
        this.meaning = meaning;
        this.langFrom = langFrom;
        this.langTo = langTo;
    }

    public Diction(String key, String meaning, String langFrom, String langTo) {
        this.key = key;
        this.meaning = meaning;
        this.langFrom = langFrom;
        this.langTo = langTo;
    }

    public Diction(String key, String meaning) {
        this.key = key;
        this.meaning = meaning;
    }

    public Diction() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getLangFrom() {
        return langFrom;
    }

    public void setLangFrom(String langFrom) {
        this.langFrom = langFrom;
    }

    public String getLangTo() {
        return langTo;
    }

    public void setLangTo(String langTo) {
        this.langTo = langTo;
    }
}
