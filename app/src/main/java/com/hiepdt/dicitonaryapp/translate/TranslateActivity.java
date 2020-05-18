package com.hiepdt.dicitonaryapp.translate;

import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hiepdt.dicitonaryapp.R;
import com.hiepdt.dicitonaryapp.models.Language;
import com.hiepdt.dicitonaryapp.models.Translate;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class TranslateActivity extends AppCompatActivity {
    private MaterialSpinner spinFrom, spinTo;
    private ImageView speakFrom, speakTo;
    private EditText edFrom;
    private TextView tvResult;
    private ImageView copyFrom, copyTo;
    private TextView tvFrom, tvTo;
    private CircleImageView btnVoice;
    private FloatingActionButton btnSwap;
    private TextToSpeech tts;

    private Translate tran;

    private String LANG_FROM = "en";
    private String LANG_TO = "vi";
    private String TEXT_FROM = "";
    private String TEXT_TO = "";
    private Language language;

    private ImageView delete;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        getSupportActionBar().hide();

        init();
        action();
    }

    private void init() {
        language = new Language();
        spinFrom = findViewById(R.id.spinFrom);
        spinTo = findViewById(R.id.spinTo);
        speakFrom = findViewById(R.id.speakFrom);
        speakTo = findViewById(R.id.speakTo);
        edFrom = findViewById(R.id.edFrom);
        tvResult = findViewById(R.id.tvResult);
        copyFrom = findViewById(R.id.copyFrom);
        copyTo = findViewById(R.id.copyTo);
        tvFrom = findViewById(R.id.tvFrom);
        tvTo = findViewById(R.id.tvTo);
        btnVoice = findViewById(R.id.btnVoice);
        btnSwap = findViewById(R.id.btnSwap);

        delete = findViewById(R.id.delete);
        tran = new Translate();
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.US);
                }
            }
        });


        List<String> data = new ArrayList<>();
        data.addAll(language.acronym.keySet());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, data);

        spinFrom.setAdapter(adapter);

        spinTo.setAdapter(adapter);


    }

    private void action() {

        spinFrom.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                
            }
        });
        speakFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String from = edFrom.getText().toString().trim();
                tts.speak(from, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        speakTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String to = tvResult.getText().toString().trim();
                tts.speak(to, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        edFrom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                TEXT_FROM = edFrom.getText().toString().trim();
                if (TEXT_FROM.length() != 0) {
                    delete.setVisibility(View.VISIBLE);
                    translate();
                } else {
                    delete.setVisibility(View.INVISIBLE);
                }
            }
        });
//        btnSwap.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                edFrom.setText(TEXT_TO);
//                tvResult.setText("");
//            }
//        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edFrom.setText("");
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

    public void translate() {
        new TranHttp().execute(TEXT_FROM);
    }

    class TranHttp extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String mean = tran.translate(TranslateActivity.this, LANG_FROM, LANG_TO, strings[0]);
            return mean.trim();
        }

        @Override
        protected void onPostExecute(String response) {
            TEXT_TO = response;
            tvResult.setText(TEXT_TO);
        }
    }
}
