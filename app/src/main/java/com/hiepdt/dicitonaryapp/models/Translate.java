package com.hiepdt.dicitonaryapp.models;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Translate {
    public String translate(Context mContext, String langFrom, String langTo, String text) {
        StringBuilder response = new StringBuilder();
        try {
            String urlStr = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                urlStr = "https://script.google.com/macros/s/AKfycbzuQOYIH6zleqkigqLDemQCsnaNk0xJUVeFMSzHReRUumtvpk37/exec" +
                        "?q=" + URLEncoder.encode(text) +
                        "&target=" + langTo +
                        "&source=" + langFrom;
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
            System.out.println("Tra li: "+ response.toString());
            in.close();
        } catch (Exception e) {
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return response.toString();
    }
}
