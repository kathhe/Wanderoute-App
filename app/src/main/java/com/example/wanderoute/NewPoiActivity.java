package com.example.wanderoute;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class NewPoiActivity extends AppCompatActivity {
    public static final String EXTRA_POI_ID = "com.example.wanderoute.POI_ID";
    public static final String EXTRA_ORT = "com.example.wanderoute.ORT";
    public static final String EXTRA_KOORD = "com.example.wanderoute.KOORD";
    public static final String EXTRA_BESCH = "com.example.wanderoute.BESCH";
    public static final String EXTRA_FOTO = "com.example.wanderoute.FOTO";
    public static final String EXTRA_DELETE_POI = "com.example.wanderoute.DELETE_POI";
    private static final int TAKE_PIC = 100;

    static final int REQUEST_IMAGE_CAPTURE = 99;

    public static final String TAG = "PoiLogs";

    private long poiId;
    private EditText mEditOrtView;
    private EditText mEditKoordView;
    private EditText mEditBeschView;
    private EditText mEditFotoView;

    private ImageView imageView;
    private ImageButton photo;
    private Uri photoFromDB;
    private ImageButton buttonSavePoi;
    private ImageButton buttonDeletePoi;
    private ImageButton buttonBack;

    // Aufgenommenes Image
    private String currentPhotoPath;
    private Uri currentPhotoUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_poi);

        // Neues Bild aufnehmen
        imageView = findViewById(R.id.image_view_poi);
        photo = findViewById(R.id.button_poi_photo_intent);

        // App Erlaubnis zum Fotografieren
        if(ContextCompat.checkSelfPermission(NewPoiActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(NewPoiActivity.this, new String[]{
                    Manifest.permission.CAMERA
            }, TAKE_PIC);
        }

        // Button zum Öffnen der Kamera.
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("PoiActivity", "neues bild machen");
                dispatchTakePictureIntent();
            }
        });


        mEditOrtView = findViewById(R.id.edit_text_ort);
        mEditKoordView = findViewById(R.id.edit_text_koord);
        mEditBeschView = findViewById(R.id.edit_text_besch);
        mEditFotoView = findViewById(R.id.edit_text_foto);

        Intent intent = getIntent();

        // Hat die Point of Interest bereits eine ID, dann sollen alle Daten aus dem Objekt ausgelesen werden.
        if(intent.hasExtra(EXTRA_POI_ID)){

            setTitle("Point of Interest bearbeiten");
            mEditOrtView.setText(intent.getStringExtra(EXTRA_ORT));
            mEditKoordView.setText(intent.getStringExtra(EXTRA_KOORD));
            mEditBeschView.setText(intent.getStringExtra(EXTRA_BESCH));
            mEditFotoView.setText(intent.getStringExtra(EXTRA_FOTO));

            photoFromDB = Uri.parse(intent.getStringExtra(EXTRA_FOTO));
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoFromDB);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            // Gibt es noch keine ID, dann soll eine neue Route erstellt werden.
            setTitle("Neue Point of Interest hinzufügen");
        }


        // Listener zum speichern der Daten
        buttonSavePoi = findViewById(R.id.button_save_poi);
        buttonSavePoi.setOnClickListener(view -> {
            Intent poiIntent = new Intent();

            String ort = mEditOrtView.getText().toString();
            String koord = mEditKoordView.getText().toString();
            String besch = mEditBeschView.getText().toString();
            String foto = mEditFotoView.getText().toString();

            poiIntent.putExtra(EXTRA_ORT, ort);
            poiIntent.putExtra(EXTRA_KOORD, koord);
            poiIntent.putExtra(EXTRA_BESCH, besch);
            poiIntent.putExtra(EXTRA_FOTO, foto);


            int id = getIntent().getIntExtra(EXTRA_POI_ID, -1);
            if(id != -1){
                poiIntent.putExtra(EXTRA_POI_ID, id);
            }

            // Intent zum Speichern wird mit Request code an PoiListActivity gesendet.
            setResult(RESULT_OK, poiIntent);

            finish();
        });

        // Button zum Löschen des Point of Interest
        buttonDeletePoi = findViewById(R.id.ibutton_poi_delete);
        buttonDeletePoi.setOnClickListener(view -> {
            int id = getIntent().getIntExtra(EXTRA_POI_ID, -1);
            Intent poiIntentDelete = new Intent();

            if(id != -1){
                poiIntentDelete.putExtra(EXTRA_POI_ID, id);
            }

            String ort = mEditOrtView.getText().toString();
            String koord = mEditKoordView.getText().toString();
            String besch = mEditBeschView.getText().toString();
            String foto = mEditFotoView.getText().toString();

            poiIntentDelete.putExtra(EXTRA_ORT, ort);
            poiIntentDelete.putExtra(EXTRA_KOORD, koord);
            poiIntentDelete.putExtra(EXTRA_BESCH, besch);
            poiIntentDelete.putExtra(EXTRA_FOTO, foto);
            poiIntentDelete.putExtra(EXTRA_DELETE_POI, true);

            // Intent zum Löschen wird mit Request code an PoiListActivity gesendet.
            setResult(RESULT_OK, poiIntentDelete);

            finish();
        });


        buttonBack = (ImageButton) findViewById(R.id.new_poi_back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    //Kamerafunktion
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                imageView.setImageURI(currentPhotoUri);
            }
        }
    }



    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                //...
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                String ello;
                Uri photoURI = FileProvider.getUriForFile(this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        photoFile);


                currentPhotoUri = photoURI;
                mEditFotoView.setText("" + currentPhotoUri);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        } else {
            Log.d(TAG, "Kamera Intent läuft nicht.");
        }
    }


}
