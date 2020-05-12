package com.hiepdt.dicitonaryapp.search;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hiepdt.dicitonaryapp.R;
import com.hiepdt.dicitonaryapp.hepler.DBHelper;
import com.hiepdt.dicitonaryapp.models.APP;
import com.hiepdt.dicitonaryapp.models.Word;
import com.hiepdt.dicitonaryapp.search.result.ResultActivity;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private AutoCompleteTextView edSearch;
    private ImageView btnBack;
    private ImageView btnVoice, btnSearch;
    private RecyclerView mRecyclerView;
    private LinearLayout empty;

    private SearchAdapter mAdapter;

    private DBHelper helper;

    private boolean isDeletable;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().hide();

        init();
        action();
    }

    private void init() {
        helper = new DBHelper(this);


        btnBack = findViewById(R.id.btnBack);
        edSearch = findViewById(R.id.edSearch);
        btnVoice = findViewById(R.id.btnVoice);
        btnSearch = findViewById(R.id.btnSearch);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        empty = findViewById(R.id.empty);

        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager1);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        mAdapter = new SearchAdapter(this, APP.mListHis);
        mRecyclerView.setAdapter(mAdapter);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.item_suggest, APP.mListWord);
        edSearch.setAdapter(arrayAdapter);
        edSearch.setThreshold(1);
    }

    private void action() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        edSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String word = edSearch.getText().toString().trim();
                edSearch.setText("");
                Intent intent = new Intent(SearchActivity.this, ResultActivity.class);
                intent.putExtra("word", word);
                int pos = APP.mListWord.indexOf(word);

                intent.putExtra("meaning", APP.mListDiction.get(pos).getMeaning());
                helper.insertWord(new Word(word, System.currentTimeMillis(), "history"));
                APP.mListHis = helper.getWordWithType("history");
                mAdapter = new SearchAdapter(SearchActivity.this, APP.mListHis);
                mAdapter.notifyDataSetChanged();
                startActivity(intent);
            }
        });
        edSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    edSearch.setBackgroundResource(R.drawable.corner_search_edittext_select);
                } else {
                    edSearch.setBackgroundResource(R.drawable.corner_search_edittext_unselect);
                }
            }
        });
        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = edSearch.getText().toString().trim().length();
                if (length == 0){
                    isDeletable = false;
                    btnVoice.setImageResource(R.mipmap.micro);
                } else {
                    isDeletable = true;
                    btnVoice.setImageResource(R.mipmap.delete);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDeletable){
                    edSearch.setText("");
                }
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
            }
        });
    }

}
