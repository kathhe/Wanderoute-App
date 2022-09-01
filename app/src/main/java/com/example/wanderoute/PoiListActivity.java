package com.example.wanderoute;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PoiListActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "com.example.wanderoute.ID";
    public static final int NEW_POI_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_POI_ACTIVITY_REQUEST_CODE = 2;

    private PoiViewModel mPoiViewModel;
    private int ownerRouteId;
    ImageButton buttonBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_list);
        setTitle("Points of Interest");


        // Intent von der Route wird aufgegriffen, um die Route ID zu erfassen
        Intent routeIntent = getIntent();
        ownerRouteId = routeIntent.getIntExtra(EXTRA_ID, -1);

        // RecylerView wird instanziert und dem Layout übergeben.
        RecyclerView recyclerView = findViewById(R.id.recyclerview_poi);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        PoiRecyclerAdapter adapter = new PoiRecyclerAdapter();
        recyclerView.setAdapter(adapter);


        // Der neu hinzugefügte Point of Interest wird dem ViewModel übergeben.
        mPoiViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(PoiViewModel.class);
        mPoiViewModel.getAllFilteredPoi(ownerRouteId).observe(this, pois -> {
            // Kopie aller Points of Interest werden im Adapter aktualisiert.
            adapter.setPois(pois);
        });


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Button zum Hinzufügen eines neuen Point of Interest. Ein Intent mit dem dazugehören Requestcode wird abgefeurt.
        FloatingActionButton fab = findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PoiListActivity.this, NewPoiActivity.class);
                startActivityForResult(intent, NEW_POI_ACTIVITY_REQUEST_CODE);
            }
        });

        // Klickt man auf ein Point of Interest im Recycler, werden dessen Daten mit Hilfe eines Intents zum Bearbeiten dargestellt.
        adapter.setOnPoiClickListener( new PoiRecyclerAdapter.OnPoiClickListener() {
            @Override
            public void onPoiClick(Poi poi) {

                Intent intent = new Intent(PoiListActivity.this, NewPoiActivity.class);
                intent.putExtra(NewPoiActivity.EXTRA_POI_ID, poi.getPoiId());
                intent.putExtra(NewPoiActivity.EXTRA_ORT, poi.getOrt());
                intent.putExtra(NewPoiActivity.EXTRA_KOORD, poi.getKoord());
                intent.putExtra(NewPoiActivity.EXTRA_BESCH, poi.getBesch());
                intent.putExtra(NewPoiActivity.EXTRA_FOTO, poi.getFoto());

                startActivityForResult(intent, EDIT_POI_ACTIVITY_REQUEST_CODE);
           }
        });

        // Button zum Zurücknavigieren
        buttonBack = (ImageButton) findViewById(R.id.poi_list_back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Nach dem bearbeiten einer Route, werden die Daten an den ViewModel weitergeleitet, je nach dem welche CRUD-Operation ausgeführt wird.
        if (requestCode == NEW_POI_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            String ort = data.getStringExtra(NewPoiActivity.EXTRA_ORT);
            String koord = data.getStringExtra(NewPoiActivity.EXTRA_KOORD);
            String besch = data.getStringExtra(NewPoiActivity.EXTRA_BESCH);
            String foto = data.getStringExtra(NewPoiActivity.EXTRA_FOTO);

            // Ein neues Objekt, mit den Werten aus dem NewRoute Intent wird instanziert und dem ViewModel übergeben.
            Poi poi = new Poi( ort, koord, besch, foto);
            poi.setRouteOwnerId(ownerRouteId);
            mPoiViewModel.insert(poi);

        // Poi wird bearbeitet.
        } else if(requestCode == EDIT_POI_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            if(data.getBooleanExtra(NewPoiActivity.EXTRA_DELETE_POI, false)) {
                int id = data.getIntExtra(NewPoiActivity.EXTRA_POI_ID, -1);

                // Ein neues, leeres Objekt wird instanziert und mit einer ID an das Model übergeben.
                Poi poi = new Poi("", "", "", "");
                poi.setPoiId(id);
                mPoiViewModel.delete(poi);
                Toast.makeText(this,R.string.poi_deleted, Toast.LENGTH_SHORT).show();

            } else {
                int id = data.getIntExtra(NewPoiActivity.EXTRA_POI_ID, -1);

                if(id == -1){
                    // Falls beim Aktualsieren etwas falsch läuft.
                    Toast.makeText(this,R.string.poi_edit_error, Toast.LENGTH_SHORT).show();
                    return;
                }


                String ort = data.getStringExtra(NewPoiActivity.EXTRA_ORT);
                String koord = data.getStringExtra(NewPoiActivity.EXTRA_KOORD);
                String besch = data.getStringExtra(NewPoiActivity.EXTRA_BESCH);
                String foto = data.getStringExtra(NewPoiActivity.EXTRA_FOTO);

                // Ein neues Objekt mit aktualierten Werten aus dem NewRoute Intent wird instanziert und dann dem ViewModel übergeben.
                Poi poi = new Poi(ort, koord, besch, foto);


                // *** Id wird hier gesetzt um update zu ermöglichen (Dao macht vergleich der Ids).
                poi.setPoiId(id);
                poi.setRouteOwnerId(ownerRouteId);
                mPoiViewModel.update(poi);

                Toast.makeText(
                        getApplicationContext(),
                        R.string.poi_updated,
                        Toast.LENGTH_LONG).show();
            }
        } else {

        }
    }
}