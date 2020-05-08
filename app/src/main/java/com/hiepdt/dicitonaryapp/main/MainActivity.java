package com.hiepdt.dicitonaryapp.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.hiepdt.dicitonaryapp.R;
import com.hiepdt.dicitonaryapp.bookmark.BookmarkActivity;
import com.hiepdt.dicitonaryapp.history.HistoryActivity;
import com.hiepdt.dicitonaryapp.search.SearchActivity;

public class MainActivity extends AppCompatActivity {

    private LinearLayout btnSearch;
    private EditText edSearch;
    private ImageView btnHis, btnMark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        init();
        action();

    }

    private void init() {
        btnSearch = findViewById(R.id.btnSearch);
        edSearch = findViewById(R.id.edSearch);
        btnHis = findViewById(R.id.btnHis);
        btnMark = findViewById(R.id.btnMark);
    }

    private void action() {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
        edSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
        btnHis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });
        btnMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BookmarkActivity.class);
                startActivity(intent);
            }
        });
    }
}
