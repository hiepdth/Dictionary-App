package com.hiepdt.dicitonaryapp.hepler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hiepdt.dicitonaryapp.models.Diction;
import com.hiepdt.dicitonaryapp.models.Word;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DBHelper extends SQLiteOpenHelper {


    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "DictionDB";
    // Table Names
    private static final String TABLE_DICTION = "diction";
    private static final String TABLE_WORDS = "words";

    //Todo: Diction table create statement
    private static final String CREATE_DICTION = "CREATE TABLE " + TABLE_DICTION +
            "( id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
            "                word VARCHAR(20) NOT NULL ," +
            "                meaning TEXT NOT NULL," +
            "                langFrom VARCHAR(5) NOT NULL," +
            "                langTo VARCHAR(5) NOT NULL)";

    //Todo: Word table create statement
    private static final String CREATE_WORDS = "CREATE TABLE " + TABLE_WORDS +
            "( id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
            "                word VARCHAR(20) NOT NULL ," +
            "                timestamp LONG NOT NULL, " +
            "                type VARCHAR(8) NOT NULL)";

    private SQLiteDatabase sqLiteDatabase;
    private ContentValues contentValues;
    private Cursor cursor;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DICTION);
        db.execSQL(CREATE_WORDS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_DICTION);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORDS);

            onCreate(db);
        }
    }

    //--------- Functions for Diction TABLE--------------------//
    public void readDBFromZip(Context context, String fileName, String langFrom, String langTo){
        try {
            InputStream is = context.getAssets().open(fileName);
            ZipInputStream zipStream = new ZipInputStream(is);
            ZipEntry entry = zipStream.getNextEntry();

            BufferedReader reader = new BufferedReader(new InputStreamReader(zipStream, "utf-8"));

            String line, word, def;
            while ((line = reader.readLine()) != null) {
                //System.out.printf("%s\n----------------------\n", line);
                int index = line.indexOf("<html>");
                int index2 = line.indexOf("<ul>");

                Diction diction;
                if (index != -1) {
                    word = line.substring(0, index);

                    word = word.trim();

                    //word = word.toLowerCase();
                    def = line.substring(index);
                    //def = "<html>" + def + "</html>";
                    System.out.println(word);
                    diction = new Diction(word, def, langFrom, langTo);
                    insertDiction(diction);
                }
            }

        } catch (IOException e) {
            System.out.println("Loi" + e.getMessage());
        }
    }

    //Todo: CRUD for diction TABLE
    public void insertDiction(Diction diction) {
        sqLiteDatabase = getWritableDatabase();
        contentValues = new ContentValues();
        contentValues.put("word", diction.getKey());
        contentValues.put("meaning", diction.getMeaning());
        contentValues.put("langFrom", diction.getLangFrom());
        contentValues.put("langTo", diction.getLangTo());
        sqLiteDatabase.insert(TABLE_DICTION, null, contentValues);
        closeDB();
    }

    public ArrayList<Diction> getAllDiction() {
        sqLiteDatabase = getReadableDatabase();
        cursor = sqLiteDatabase.query(true, TABLE_DICTION, null, null, null, null, null, null, null);
        ArrayList<Diction> mListDiction = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String key = cursor.getString(cursor.getColumnIndex("word"));
            String meaning = cursor.getString(cursor.getColumnIndex("meaning"));
            String langFrom = cursor.getString(cursor.getColumnIndex("langFrom"));
            String langTo = cursor.getString(cursor.getColumnIndex("langTo"));
            Diction diction = new Diction(id, key, meaning, langFrom, langTo);
            mListDiction.add(diction);
        }
        System.out.println("So luong: "+mListDiction.size());
        closeDB();
        return mListDiction;
    }

    public ArrayList<String> getAllWord() {
        sqLiteDatabase = getReadableDatabase();
        cursor = sqLiteDatabase.query(true, TABLE_DICTION, null, null, null, null, null, null, null);
        ArrayList<String> mListWord = new ArrayList<>();
        while (cursor.moveToNext()) {
            String key = cursor.getString(cursor.getColumnIndex("word"));
            mListWord.add(key);
        }
        closeDB();
        return mListWord;
    }

    public void deleteDiction() {
        sqLiteDatabase = getWritableDatabase();
        closeDB();
    }


    //--------- Functions for Word TABLE--------------------//

    public void insertWord(Word word){
        sqLiteDatabase = getWritableDatabase();
        contentValues = new ContentValues();
        contentValues.put("word", word.getKey());
        contentValues.put("timestamp", word.getTimestamp());
        contentValues.put("type", word.getType());
        sqLiteDatabase.insert(TABLE_WORDS,null, contentValues);
        closeDB();
    }
    public ArrayList<Word> getWordWithType(String type){
        sqLiteDatabase = getReadableDatabase();
        cursor = sqLiteDatabase.query(true, TABLE_WORDS, null, null, null, null, null, null, null);
        ArrayList<Word> mListWord = new ArrayList<>();
        while (cursor.moveToNext()) {
            String _type = cursor.getString(cursor.getColumnIndex("type"));
            if (_type.equalsIgnoreCase(type)) {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String key = cursor.getString(cursor.getColumnIndex("word"));
                long timestamp = cursor.getLong(cursor.getColumnIndex("timestamp"));
                Word word = new Word(id, key, timestamp, _type);
                mListWord.add(word);
            }
        }
        closeDB();
        return mListWord;
    }
    //Todo: Đóng Database
    private void closeDB() {
        if (sqLiteDatabase != null) sqLiteDatabase.close();
        if (contentValues != null) contentValues.clear();
        if (cursor != null) cursor.close();
    }

    public SQLiteDatabase getDatabase() {
        sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase;
    }
}
