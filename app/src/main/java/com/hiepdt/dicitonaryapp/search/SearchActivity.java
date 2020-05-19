package com.hiepdt.dicitonaryapp.search;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.hiepdt.dicitonaryapp.R;
import com.hiepdt.dicitonaryapp.hepler.DBHelper;
import com.hiepdt.dicitonaryapp.models.APP;
import com.hiepdt.dicitonaryapp.models.Language;
import com.hiepdt.dicitonaryapp.models.Word;
import com.hiepdt.dicitonaryapp.search.result.ResultActivity;
import com.hiepdt.dicitonaryapp.translate.TranslateActivity;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.scwang.wave.MultiWaveHeader;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private SpeechRecognizer mSpeechRecongizer;
    private Intent mSpeechRecognizerIntent;

    private AutoCompleteTextView edSearch;
    private ImageView btnBack;
    private ImageView btnVoice, btnSearch;
    private RecyclerView mRecyclerView;
    private LinearLayout empty, root;

    private SearchAdapter mAdapter;

    private DBHelper helper;

    private boolean isDeletable;
    private MultiWaveHeader wave1, wave2;
    private Language language;

    private String LANG = "vi";

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
        language = new Language();

        btnBack = findViewById(R.id.btnBack);
        edSearch = findViewById(R.id.edSearch);
        btnVoice = findViewById(R.id.btnVoice);
        btnSearch = findViewById(R.id.btnSearch);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        empty = findViewById(R.id.empty);
        root = findViewById(R.id.root);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this, RecyclerView.VERTICAL, true);
        mRecyclerView.setLayoutManager(layoutManager1);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        mAdapter = new SearchAdapter(this, APP.mListHis);
        mRecyclerView.setAdapter(mAdapter);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.item_suggest, APP.mListWord);
        edSearch.setAdapter(arrayAdapter);
        edSearch.setThreshold(1);

        mSpeechRecongizer = SpeechRecognizer.createSpeechRecognizer(this);
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "vi");
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
                startActivity(intent);
            }
        });
        edSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
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
                if (length == 0) {
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
                if (isDeletable) {
                    edSearch.setText("");
                } else {
                    speechToText();
                }
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
            }
        });

    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                    == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                finish();
            }
        }
    }

    private void speechToText() {
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.dialog_speech);
        final ImageView btnVoice = dialog.findViewById(R.id.btnVoice);
        final TextView tvDetect = dialog.findViewById(R.id.tvDetect);
        final MaterialSpinner spinner = dialog.findViewById(R.id.spinner);
        List<String> data = new ArrayList<>();
        data.addAll(language.acronym.keySet());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, data);
        spinner.setAdapter(adapter);
        spinner.setText("Vietnamese");

        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                LANG = language.acronym.get(spinner.getText().toString());
            }
        });
        wave1 = dialog.findViewById(R.id.wave1);
        wave2 = dialog.findViewById(R.id.wave2);

        wave1.setStartColor(Color.parseColor("#ffffff"));
        wave1.setCloseColor(Color.parseColor("#ffffff"));
        wave1.setColorAlpha(.4f);
        wave1.setVelocity(2);
        wave1.setProgress(0.8f);
        wave1.stop();
        wave1.setGradientAngle(45);

        wave2.setStartColor(Color.parseColor("#ffffff"));
        wave2.setCloseColor(Color.parseColor("#ffffff"));
        wave2.setColorAlpha(.4f);
        wave2.setVelocity(2);
        wave2.setProgress(0.8f);
        wave2.stop();
        wave2.setGradientAngle(45);
        checkPermission();
        btnVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnVoice.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_UP:
                                wave1.setVisibility(View.INVISIBLE);
                                wave2.setVisibility(View.INVISIBLE);
                                wave1.stop();
                                wave2.stop();
                                mSpeechRecongizer.stopListening();
                                tvDetect.setHint("You will see the input here");
                                break;
                            case MotionEvent.ACTION_DOWN:
                                tvDetect.setText("");
                                tvDetect.setHint("Listening ... ");
                                wave1.setVisibility(View.VISIBLE);
                                wave2.setVisibility(View.VISIBLE);
                                wave1.start();
                                wave2.start();
                                mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, LANG);
                                mSpeechRecongizer.startListening(mSpeechRecognizerIntent);
                                break;
                        }
                        return false;
                    }
                });
            }
        });
        dialog.show();

        mSpeechRecongizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {
                final ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null) {
                    wave1.setVisibility(View.INVISIBLE);
                    wave2.setVisibility(View.INVISIBLE);

//                    System.out.println(matches.get(0));
                    tvDetect.setText(matches.get(0));
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            Intent intent = new Intent(SearchActivity.this, TranslateActivity.class);
                            intent.putExtra("text", matches.get(0));
                            intent.putExtra("lang", spinner.getText().toString());
                            intent.putExtra("acronym", LANG);
                            startActivity(intent);
                        }
                    }, 1000);

                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {
            }
        });

    }
}
