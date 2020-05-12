package com.hiepdt.dicitonaryapp.models;

public class Word {
    private int id;
    private String key;
    private long timestamp;
    private String type;

    public Word(){}
    public Word(String key, long timestamp, String type) {
        this.key = key;
        this.timestamp = timestamp;
        this.type = type;
    }
    public Word(int id, String key, long timestamp, String type) {
        this.id = id;
        this.key = key;
        this.timestamp = timestamp;
        this.type = type;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
