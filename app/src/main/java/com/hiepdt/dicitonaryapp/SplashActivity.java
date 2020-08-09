package com.hiepdt.dicitonaryapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hiepdt.dicitonaryapp.hepler.Database;
import com.hiepdt.dicitonaryapp.main.MainActivity;
import com.hiepdt.dicitonaryapp.models.APP;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class SplashActivity extends AppCompatActivity {

    private Database db = new Database();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        new ReadDB().execute();
    }

    class ReadDB extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            publishProgress();
            APP.mListWordEng = new ArrayList<>();
            APP.mListWordVie = new ArrayList<>();
            APP.mListDictionEng = new ArrayList<>();
            APP.mListDictionVie = new ArrayList<>();
            db.readDBFromZip(SplashActivity.this, APP.mListWordEng, APP.mListDictionEng, "e_v.zip", "en", "vi");
            db.readDBFromZip(SplashActivity.this, APP.mListWordVie, APP.mListDictionVie, "v_e.zip", "vi", "en");

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String response) {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}
