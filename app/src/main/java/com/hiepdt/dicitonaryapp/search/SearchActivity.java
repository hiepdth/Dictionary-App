package com.hiepdt.dicitonaryapp.search;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hiepdt.dicitonaryapp.R;
import com.hiepdt.dicitonaryapp.models.Word;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private EditText edSearch;
    private ImageView btnSearch;
    private RecyclerView mRecyclerView;
    private LinearLayout empty;

    private SearchAdapter mAdapter;

    private ArrayList<Word>mListWord;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().hide();

        init();
        action();
    }

    private void init() {
        mListWord = new ArrayList<>();

        edSearch = findViewById(R.id.edSearch);
        btnSearch = findViewById(R.id.btnSearch);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        empty = findViewById(R.id.empty);

        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager1);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        mListWord.add(new Word("Hello", ""));
        mListWord.add(new Word("Goodbye", ""));
        mListWord.add(new Word("See", ""));
        mListWord.add(new Word("Later", ""));

        mAdapter = new SearchAdapter(this, mListWord);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void action() {

    }
}
