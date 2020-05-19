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
    private ImageView copyTo, btnRun, btnBack;
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
        copyTo = findViewById(R.id.copyTo);
        tvFrom = findViewById(R.id.tvFrom);
        tvTo = findViewById(R.id.tvTo);
        btnVoice = findViewById(R.id.btnVoice);
        btnSwap = findViewById(R.id.btnSwap);
        btnRun = findViewById(R.id.btnRun);
        btnBack = findViewById(R.id.btnBack);
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
        spinFrom.setText("English");
        spinTo.setText("Vietnamese");

        if (getIntent().getExtras() != null) {
            TEXT_FROM = getIntent().getExtras().getString("text", "");
            edFrom.setText(TEXT_FROM);

            spinFrom.setText(getIntent().getExtras().getString("lang", ""));
            LANG_FROM = getIntent().getExtras().getString("acronym", "");

            delete.setVisibility(View.VISIBLE);
        }
    }

    private void action() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        spinFrom.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                LANG_FROM = language.acronym.get(spinFrom.getText().toString());
                translate();
            }
        });
        spinTo.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                LANG_TO = language.acronym.get(spinTo.getText().toString());
                translate();
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
                } else {
                    delete.setVisibility(View.INVISIBLE);
                }
            }
        });
        btnRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translate();
            }
        });
        btnSwap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swapLanguage();
                translate();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TEXT_FROM = "";
                TEXT_TO = "";
                edFrom.setText("");
                tvResult.setText("");
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
        if (TEXT_FROM.length() != 0) {
            new TranHttp().execute(TEXT_FROM);
        }
    }

    public void swapLanguage() {
        //Swap acronym
        String temp1 = LANG_FROM;
        LANG_FROM = LANG_TO;
        LANG_TO = temp1;

        //Swap text
        TEXT_FROM = TEXT_TO;
        TEXT_TO = "";
        edFrom.setText(TEXT_FROM);

        //Swap language
        String temp = spinTo.getText().toString();
        spinTo.setText(spinFrom.getText().toString());
        spinFrom.setText(temp);

    }

    class TranHttp extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            if (LANG_FROM.equalsIgnoreCase(LANG_TO)) {
                return strings[0];
            }
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
