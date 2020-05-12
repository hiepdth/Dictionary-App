package com.hiepdt.dicitonaryapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import com.hiepdt.dicitonaryapp.hepler.DBHelper;
import com.hiepdt.dicitonaryapp.main.MainActivity;
import com.hiepdt.dicitonaryapp.models.APP;

import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class SplashActivity extends AppCompatActivity {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private DBHelper helper;

    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        sp = getSharedPreferences("db", MODE_PRIVATE);
        editor = sp.edit();
        helper = new DBHelper(this);



//        deleteDatabase("DictionDB");
//        if (sp.getInt("exist", 0) == 1){
//            showProgressDialog();
//            helper.readDBFromZip(this, "e_v.zip", "EN", "VI");
//            helper.readDBFromZip(this, "v_e.zip", "VI", "EN");
//
//            editor.putInt("exist", 1);
//            editor.apply();
//        }
////        deleteDatabase("DictionDB");
        APP.mListDiction = helper.getAllDiction();
        APP.mListWord = helper.getAllWord();
        APP.mListHis = helper.getWordWithType("history");
        APP.mListMark = helper.getWordWithType("bookmark");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        }, new Random().nextInt(1000) + 1000);
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Đang tải dữ liệu...");
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
//        hideProgressDialog();
    }
}
