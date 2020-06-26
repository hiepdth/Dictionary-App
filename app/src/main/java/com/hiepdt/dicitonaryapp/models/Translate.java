package com.hiepdt.dicitonaryapp.models;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Translate {

    public String translate(String langFrom, String langTo, String text) {
        StringBuilder response = new StringBuilder();
        try {
            String urlStr = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
//                urlStr = "https://script.google.com/macros/s/AKfycbzuQOYIH6zleqkigqLDemQCsnaNk0xJUVeFMSzHReRUumtvpk37/exec" +
//                        "?q=" + URLEncoder.encode(text, StandardCharsets.UTF_8.toString()) +
//                        "&target=" + langTo +
//                        "&source=" + langFrom;

                urlStr = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=" +
                        langFrom + "&tl=" + langTo + "&dt=t&q=" + URLEncoder.encode(text);
            }
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            InputStreamReader inputStreamReader = new InputStreamReader(con.getInputStream(), "utf-8");
            BufferedReader in = new BufferedReader(inputStreamReader);
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();
        } catch (Exception e) {
            Log.d("HTTP: ", e.getMessage());
        }
        int pos = response.indexOf(",");
        return response.substring(4, pos - 1);
    }
}
