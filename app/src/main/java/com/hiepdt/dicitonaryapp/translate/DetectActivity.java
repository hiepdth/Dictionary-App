package com.hiepdt.dicitonaryapp.translate;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.hiepdt.dicitonaryapp.R;
import com.hiepdt.dicitonaryapp.models.Language;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DetectActivity extends AppCompatActivity implements View.OnClickListener {

    SweetAlertDialog pDialog;
    private TessBaseAPI tessBaseAPI;
    private LinearLayout btnCam, btnGal;
    private ImageView btnBack;
    private ImageView image;
    private Uri uri;
    private Button btnRun;
    private MaterialSpinner spinner;
    private Language language;
    private String LANG = "vie";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect);
        getSupportActionBar().hide();
        init();
        action();
    }

    private void init() {

        language = new Language();
        btnBack = findViewById(R.id.btnBack);
        btnCam = findViewById(R.id.btnCam);
        btnGal = findViewById(R.id.btnGal);
        image = findViewById(R.id.image);

        btnRun = findViewById(R.id.btnRun);
        spinner = findViewById(R.id.spinner);

        ArrayList<String> data = new ArrayList<>();
        data.add("English");
        data.add("Vietnamese");
        data.add("Japanese");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, data);
        spinner.setAdapter(adapter);
        spinner.setText("");
    }

    private void action() {
        btnBack.setOnClickListener(this);
        btnGal.setOnClickListener(this);
        btnCam.setOnClickListener(this);
        btnRun.setOnClickListener(this);

        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                LANG = convertToAcronym(spinner.getText().toString());
                System.out.println(LANG);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                onBackPressed();
                break;
            case R.id.btnCam:
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "NewPic");
                values.put(MediaStore.Images.Media.DESCRIPTION, "Image To Text");
                uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(cameraIntent, 2);
                break;
            case R.id.btnGal:
                CropImage.startPickImageActivity(DetectActivity.this);
                break;
            case R.id.btnRun:
                new doOCR().execute();
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE) {
                Uri uriImage = CropImage.getPickImageResultUri(this, data);
                if (CropImage.isReadExternalStoragePermissionsRequired(this, uriImage)) {
                    uri = uriImage;
                } else {
                    startCrop(uriImage);
                }
            }
            if (requestCode == 2) {
                startCrop(uri);
            }

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    uri = result.getUri();
                    image.setImageURI(result.getUri());
                }
            }
        }
    }

    //Todo: Get URI from Gallery
    private void startCrop(Uri uri) {
        CropImage.activity(uri).setGuidelines(CropImageView.Guidelines.ON).setMultiTouchEnabled(true).start(this);
    }

    private void initOCR(String lang) throws IOException {
        prepareLanguageDir(lang);
        tessBaseAPI = new TessBaseAPI();
        tessBaseAPI.init(String.valueOf(getFilesDir()), lang);
    }

    private void prepareLanguageDir(String lang) throws IOException {
        File dir = new File(getFilesDir() + "/tessdata");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File trainedData = new File(getFilesDir() + "/tessdata/" + lang + ".traineddata");
        if (!trainedData.exists()) {
            copyFile(lang);
        }
    }

    private void copyFile(String lang) throws IOException {
        AssetManager manager = getAssets();
        InputStream is = manager.open("/tessdata/" + lang + ".traineddata");
        OutputStream os = new FileOutputStream(getFilesDir() +
                "/tessdata/" + lang + ".traineddata");
        byte[] buffer = new byte[1024];
        int read;
        while ((read = is.read(buffer)) != -1) {
            os.write(buffer, 0, read);
        }

        is.close();
        os.flush();
        os.close();
    }

    public void doRecognize() {
        try {
            initOCR(LANG);
            tessBaseAPI.setImage(MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri));
            String result = tessBaseAPI.getUTF8Text();
            Intent intent = new Intent(this, TranslateActivity.class);
            intent.putExtra("text", result);
            intent.putExtra("lang", spinner.getText().toString());
            intent.putExtra("acronym", language.acronym.get(spinner.getText().toString()));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String convertToAcronym(String lang) {
        String l = null;
        switch (lang) {
            case "English":
                l = "eng";
                break;
            case "Vietnamese":
                l = "vie";
                break;
            case "Japanese":
                l = "jpn";
                break;
        }
        return l;
    }

    //---------------------------------------------//
    @Override
    protected void onResume() {
        super.onResume();
        if (uri != null && !LANG.isEmpty()) {
            btnRun.setEnabled(true);
            btnRun.setBackgroundResource(R.drawable.corner_button_select);
            btnRun.setTextColor(Color.parseColor("#ffffff"));
        } else {
            btnRun.setEnabled(false);
            btnRun.setBackgroundResource(R.drawable.corner_search_edittext_unselect);
            btnRun.setTextColor(Color.parseColor("#cccccc"));
        }
    }

    class doOCR extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            publishProgress();
            doRecognize();
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            pDialog = new SweetAlertDialog(DetectActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String response) {
            pDialog.dismiss();
        }
    }


}
