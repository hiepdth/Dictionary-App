package com.hiepdt.dicitonaryapp.hepler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hiepdt.dicitonaryapp.models.Word;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {


    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "DictionDB";
    // Table Names
    private static final String TABLE_WORDS = "words";

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
        db.execSQL(CREATE_WORDS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORDS);

            onCreate(db);
        }
    }

    //--------- Functions for Word TABLE--------------------//

    public void insertWord(Word word) {
        sqLiteDatabase = getWritableDatabase();
        contentValues = new ContentValues();
        contentValues.put("word", word.getKey());
        contentValues.put("timestamp", word.getTimestamp());
        contentValues.put("type", word.getType());
        sqLiteDatabase.insert(TABLE_WORDS, null, contentValues);
        closeDB();
    }

    public ArrayList<Word> getWordWithType(String type) {
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

}
