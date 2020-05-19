package com.hiepdt.dicitonaryapp.models;

import java.util.HashMap;

public class Language {
    public HashMap<String, String> acronym;

    public Language() {
        init();
    }

    public void init() {
        acronym = new HashMap<>();
        acronym.put("English", "en");
        acronym.put("Vietnamese", "vi");
        acronym.put("Japan", "ja");
        acronym.put("Chinese", "zh");
        acronym.put("Korea", "ko");
        acronym.put("Thai", "th");
        acronym.put("Lao", "lo");
    }
}
