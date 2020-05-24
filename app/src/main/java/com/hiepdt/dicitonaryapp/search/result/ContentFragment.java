package com.hiepdt.dicitonaryapp.search.result;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hiepdt.dicitonaryapp.R;
import com.hiepdt.dicitonaryapp.hepler.DBHelper;
import com.hiepdt.dicitonaryapp.models.APP;
import com.hiepdt.dicitonaryapp.models.Word;

import java.util.Locale;

public class ContentFragment extends Fragment {
    private String WORD = "";
    private String MEANING = "";

    private TextView tvWord, tvMean, tvLangFrom;
    private FloatingActionButton btnSound, btnBookmark;
    private TextToSpeech tts;
    private DBHelper helper;

    private boolean isBookmark;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_content, container, false);
        init(v);
        action();
        return v;
    }

    private void init(View v) {
        helper = new DBHelper(getContext());
        APP.mListMark = helper.getWordWithType("bookmark", APP.LANG_DICTION);
        tvWord = v.findViewById(R.id.tvWord);
        tvMean = v.findViewById(R.id.tvMean);
        tvLangFrom = v.findViewById(R.id.tvLangFrom);
        btnSound = v.findViewById(R.id.btnSound);
        btnBookmark = v.findViewById(R.id.btnBookmark);

        WORD = getActivity().getIntent().getExtras().getString("word", "Không tìm thấy");
        MEANING = getActivity().getIntent().getExtras().getString("meaning", "Chịu...");

        tts = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.US);
                }
            }
        });


    }

    private void action() {
        tvWord.setText(WORD);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvMean.setText(Html.fromHtml(MEANING, Html.FROM_HTML_MODE_COMPACT));
        } else {
            tvMean.setText(Html.fromHtml(MEANING));
        }

        if (isContain(WORD)) {
            isBookmark = true;
            btnBookmark.setColorFilter(Color.parseColor("#56ccf2"));
        }

        btnSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSound.setColorFilter(Color.parseColor("#56ccf2"));
                tts.speak(getActivity().getIntent().getExtras().getString("word", "Không tìm thấy"), TextToSpeech.QUEUE_FLUSH, null);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btnSound.setColorFilter(Color.parseColor("#000000"));
                    }
                }, 1000);
            }
        });
        btnBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isBookmark) {
                    Word word = new Word(WORD, System.currentTimeMillis(), APP.LANG_DICTION, "bookmark");
                    helper.insertWord(word);
                    isBookmark = true;
                    btnBookmark.setColorFilter(Color.parseColor("#56ccf2"));
                    Toast.makeText(getContext(), "Bookmark success!", Toast.LENGTH_SHORT).show();
                } else {
                    helper.deleteWord(WORD, "bookmark");
                    isBookmark = false;
                    btnBookmark.setColorFilter(Color.parseColor("#000000"));
                    Toast.makeText(getContext(), "Cancelled bookmark!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onPause() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onPause();
    }

    private boolean isContain(String string) {
        for (Word w : APP.mListMark) {
            if (w.getKey().equalsIgnoreCase(string)) {
                return true;
            }
        }
        return false;
    }

}
