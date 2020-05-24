package com.hiepdt.dicitonaryapp;

import android.content.Intent;
import android.graphics.Color;
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
    private SweetAlertDialog pDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        APP.mListWordEng = new ArrayList<>();
        APP.mListWordVie = new ArrayList<>();
        APP.mListDictionEng = new ArrayList<>();
        APP.mListDictionVie = new ArrayList<>();
        new ReadDB().execute();
    }


    class ReadDB extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            publishProgress();
            db.readDBFromZip(SplashActivity.this, APP.mListWordEng, APP.mListDictionEng, "e_v.zip", "en", "vi");
            db.readDBFromZip(SplashActivity.this, APP.mListWordVie, APP.mListDictionVie, "v_e.zip", "vi", "en");

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            pDialog = new SweetAlertDialog(SplashActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String response) {
            pDialog.dismiss();
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

}
