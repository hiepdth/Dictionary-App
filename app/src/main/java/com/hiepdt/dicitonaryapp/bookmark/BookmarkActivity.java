package com.hiepdt.dicitonaryapp.bookmark;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hiepdt.dicitonaryapp.R;
import com.hiepdt.dicitonaryapp.hepler.DBHelper;
import com.hiepdt.dicitonaryapp.models.Word;

import java.util.ArrayList;

public class BookmarkActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<Word> mListWord;
    private ImageView btnBack;
    private BookmarkAdapter mAdapter;

    private LinearLayout linearEmpty;
    private DBHelper helper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        getSupportActionBar().hide();
        init();
        action();
    }

    private void init() {
        helper = new DBHelper(this);
        btnBack = findViewById(R.id.btnBack);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        linearEmpty = findViewById(R.id.linearEmpty);
        mListWord = helper.getWordWithType("bookmark");

        if (mListWord.size() == 0){
            linearEmpty.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
            return;
        } else {
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(BookmarkActivity.this, RecyclerView.VERTICAL, false);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mAdapter = new BookmarkAdapter(BookmarkActivity.this, mListWord);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    private void action() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}
