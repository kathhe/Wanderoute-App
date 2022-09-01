package com.example.wanderoute;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.provider.SelfDestructiveThread;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class NewRouteActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "com.example.wanderoute.ID";
    public static final String EXTRA_NAME = "com.example.wanderoute.NAME";
    public static final String EXTRA_BEG = "com.example.wanderoute.BEG";
    public static final String EXTRA_END = "com.example.wanderoute.END";
    public static final String EXTRA_GPX = "com.example.wanderoute.GPX";
    public static final String EXTRA_DAU = "com.example.wanderoute.DAU";
    public static final String EXTRA_DELETE_ROUTE = "com.example.wanderoute.DELETE_ROUTE";
    public static final String TAG = "NewRouteLogs";

    private long rId;
    private EditText mEditRouteView;
    private EditText mEditBeginnView;
    private EditText mEditEndeView;
    private EditText mEditGpxView;
    private EditText mEditDauView;

    ImageButton buttonBack;
    private ImageButton buttonSaveRoute;
    private ImageButton buttonDeleteRoute;
    private ImageButton buttonAddPoi;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_route);

        mEditRouteView = findViewById(R.id.edit_word);
        mEditBeginnView = findViewById(R.id.beginn);
        mEditEndeView = findViewById(R.id.ende);
        mEditGpxView = findViewById(R.id.gpx);
        mEditDauView = findViewById(R.id.dauer);

        buttonSaveRoute = findViewById(R.id.ibutton_route_save);
        buttonDeleteRoute = findViewById(R.id.ibutton_route_delete);
        buttonAddPoi = findViewById(R.id.ibutton_route_add_poi);

        Intent routeIntent = getIntent();

        // Hat eine Route noch keine ID, kann man noch keine Points of Interest hinzufügen.
        if(! routeIntent.hasExtra(EXTRA_ID)) {

            buttonAddPoi.setActivated(false);

            buttonAddPoi.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(
                            getApplicationContext(),
                            "Bitte speichern sie die Route, bevor sie Poi hinzufügen.",
                            Toast.LENGTH_LONG).show();
                }
            });
        } else {
            buttonAddPoi.setActivated(true);
            // Dieser Button führt mit Hilfe eines Intents zur Liste aller Points of Interest.
            buttonAddPoi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Insantieren des Intent
                    Intent intent = new Intent(NewRouteActivity.this, PoiListActivity.class);

                    // Hat die Route eine ID, wird diese dem Intent als Extra hinzugefügt, damit alle Poi, diese als Owner ID bekommen.
                    if (routeIntent.hasExtra(EXTRA_ID)) {
                        int routeId = routeIntent.getIntExtra(EXTRA_ID, -1);
                        intent.putExtra(EXTRA_ID, routeId);
                    }
                    startActivity(intent);
                }
            });
        }


        Intent intent = getIntent();

        // Hat die Route bereits eine ID, dann sollen alle Daten aus dem Objekt ausgelesen werden.
        if(intent.hasExtra(EXTRA_ID)){
            setTitle("Route bearbeiten");
            mEditRouteView.setText(intent.getStringExtra(EXTRA_NAME));
            mEditBeginnView.setText(intent.getStringExtra(EXTRA_BEG));
            mEditEndeView.setText(intent.getStringExtra(EXTRA_END));
            mEditGpxView.setText(intent.getStringExtra(EXTRA_GPX));
            mEditDauView.setText(intent.getStringExtra(EXTRA_DAU));
        } else {
            // Gibt es noch keine ID, dann soll eine neue Route erstellt werden.
            setTitle("Neue Route hinzufügen");
        }

        // Listener zum speichern der Daten
        buttonSaveRoute.setOnClickListener(view -> {
            Intent replyIntent = new Intent();

            if (TextUtils.isEmpty(mEditRouteView.getText())) {
                setResult(RESULT_CANCELED, replyIntent);

            } else {
                String word = mEditRouteView.getText().toString();
                String beg = mEditBeginnView.getText().toString();
                String end = mEditEndeView.getText().toString();
                String gpx = mEditGpxView.getText().toString();
                String dau = mEditDauView.getText().toString();

                replyIntent.putExtra(EXTRA_NAME, word);
                replyIntent.putExtra(EXTRA_BEG, beg);
                replyIntent.putExtra(EXTRA_END, end);
                replyIntent.putExtra(EXTRA_GPX, gpx);
                replyIntent.putExtra(EXTRA_DAU, dau);

                int id = getIntent().getIntExtra(EXTRA_ID, -1);

                if(id != -1){
                    replyIntent.putExtra(EXTRA_ID, id);
                }

                // Intent zum Speichern wird mit Request code an MainActivity gesendet.
                setResult(RESULT_OK, replyIntent);

            }
            finish();
        });


        // Button zum Löschen der Route
        buttonDeleteRoute = findViewById(R.id.ibutton_route_delete);

        buttonDeleteRoute.setOnClickListener(view -> {

            int id = getIntent().getIntExtra(EXTRA_ID, -1);
            Intent routeIntentDelete = new Intent();

            // Rotue ID wird dem Intent hinzugefügt.
            if(id != -1){
                routeIntentDelete.putExtra(EXTRA_ID, id);
            }

            String word = mEditRouteView.getText().toString();
            String beg = mEditBeginnView.getText().toString();
            String end = mEditEndeView.getText().toString();
            String gpx = mEditGpxView.getText().toString();
            String dau = mEditDauView.getText().toString();

            routeIntentDelete.putExtra(EXTRA_NAME, word);
            routeIntentDelete.putExtra(EXTRA_BEG, beg);
            routeIntentDelete.putExtra(EXTRA_END, end);
            routeIntentDelete.putExtra(EXTRA_GPX, gpx);
            routeIntentDelete.putExtra(EXTRA_DAU, dau);
            routeIntentDelete.putExtra(EXTRA_DELETE_ROUTE, true);

            // Intent zum Löschen wird mit Request code an MainActivity gesendet.
            setResult(RESULT_OK, routeIntentDelete);

            finish();
        });


        // Button zum Zurücknavigieren
        buttonBack = (ImageButton) findViewById(R.id.new_route_back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

}