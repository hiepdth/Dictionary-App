package com.hiepdt.dicitonaryapp.translate;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hiepdt.dicitonaryapp.R;
import com.hiepdt.dicitonaryapp.models.Language;
import com.hiepdt.dicitonaryapp.models.Translate;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.scwang.wave.MultiWaveHeader;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class TranslateActivity extends AppCompatActivity {
    private SpeechRecognizer mSpeechRecongizer;
    private Intent mSpeechRecognizerIntent;
    private MultiWaveHeader wave1, wave2;

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
            tvFrom.setText(getIntent().getExtras().getString("lang", ""));

            LANG_FROM = getIntent().getExtras().getString("acronym", "");

            delete.setVisibility(View.VISIBLE);
        }

        mSpeechRecongizer = SpeechRecognizer.createSpeechRecognizer(this);
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);


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
                tvFrom.setText(spinFrom.getText().toString());
                translate();
            }
        });
        spinTo.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                LANG_TO = language.acronym.get(spinTo.getText().toString());
                tvTo.setText(spinTo.getText().toString());
                translate();
            }
        });

        copyTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TEXT_TO.isEmpty()) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("result", TEXT_TO);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(TranslateActivity.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
                }
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
                TEXT_FROM = edFrom.getText().toString().trim();
                if (TEXT_FROM.length() != 0) {
                    delete.setVisibility(View.VISIBLE);
                } else {
                    delete.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

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

        btnVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechToText();
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
        spinner.setVisibility(View.GONE);


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
                                mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, LANG_FROM);
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
                        }
                    }, 1000);
                    edFrom.setText(matches.get(0));
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
        tvTo.setText(spinFrom.getText().toString());
        spinFrom.setText(temp);
        tvFrom.setText(temp);


    }

    class TranHttp extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            if (LANG_FROM.equalsIgnoreCase(LANG_TO)) {
                return strings[0];
            }
            String mean = tran.translate(LANG_FROM, LANG_TO, strings[0]);
            return mean.trim();
        }


        @Override
        protected void onPostExecute(String response) {
            TEXT_TO = response;
            tvResult.setText(TEXT_TO);
        }
    }
}
