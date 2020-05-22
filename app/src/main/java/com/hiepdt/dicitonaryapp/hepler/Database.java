package com.hiepdt.dicitonaryapp.hepler;

import android.content.Context;

import com.hiepdt.dicitonaryapp.models.Diction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Database {

    public void readDBFromZip(Context context, ArrayList<String> mListWord, ArrayList<Diction>mListDiction,
                              String fileName, String langFrom, String langTo) {
        try {
            InputStream is = context.getAssets().open(fileName);
            ZipInputStream zipStream = new ZipInputStream(is);
            ZipEntry entry = zipStream.getNextEntry();

            BufferedReader reader = new BufferedReader(new InputStreamReader(zipStream, "utf-8"));

            String line, word, def;
            long count = 0;
            while ((line = reader.readLine()) != null) {
                //System.out.printf("%s\n----------------------\n", line);
                int index = line.indexOf("<html>");

                Diction diction;
                if (index != -1) {
                    word = line.substring(0, index);

                    word = word.trim();

                    //word = word.toLowerCase();
                    def = line.substring(index);
                    //def = "<html>" + def + "</html>";

                    diction = new Diction(word, def, langFrom, langTo);

                    mListWord.add(word);
                    mListDiction.add(diction);
                    count++;
                }
            }
            System.out.println("So tu: "+ count);
        } catch (IOException e) {
            System.out.println("Loi" + e.getMessage());
        }
    }

}
