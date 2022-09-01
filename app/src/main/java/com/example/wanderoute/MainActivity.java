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

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Request codes für die Intents
    public static final int NEW_ROUTE_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_ROUTE_ACTIVITY_REQUEST_CODE = 2;

    private RouteViewModel mRouteViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // RecyclerView für die Routen wird instanziert und dem Layout hinzugefügt.
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // RecyclerAdapter für die Routen.
        RouteRecyclerAdapter adapter = new RouteRecyclerAdapter();
        recyclerView.setAdapter(adapter);

        // ViewModel wird instanziert, alle Routen werden dargestellt und beobachtet.
        mRouteViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(RouteViewModel.class);
        mRouteViewModel.getAllRoutes().observe(this, routes -> {

            // Kopie aller Routen werden im Adapter aktualisiert.
            adapter.setRoutes(routes);
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Button zum Hinzufügen einer neuen Route. Ein Intent mit dem dazugehören Requestcode wird abgefeurt.
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener( view -> {
            Intent intent = new Intent(MainActivity.this, NewRouteActivity.class);
            startActivityForResult(intent, NEW_ROUTE_ACTIVITY_REQUEST_CODE);
        });


        // Klickt man auf eine Route im Recycler, werden dessen Daten mit Hilfe eines Intents zum Bearbeiten dargestellt.
        adapter.setOnRouteClickListener( new RouteRecyclerAdapter.OnRouteClickListener() {
            @Override
            public void onRouteClick(Route route) {

                Intent intent = new Intent(MainActivity.this, NewRouteActivity.class);
                intent.putExtra(NewRouteActivity.EXTRA_ID, route.getId());
                intent.putExtra(NewRouteActivity.EXTRA_NAME, route.getBez());
                intent.putExtra(NewRouteActivity.EXTRA_BEG, route.getBeg());
                intent.putExtra(NewRouteActivity.EXTRA_END, route.getEnd());
                intent.putExtra(NewRouteActivity.EXTRA_GPX, route.getGpx());
                intent.putExtra(NewRouteActivity.EXTRA_DAU, route.getDau());

                startActivityForResult(intent, EDIT_ROUTE_ACTIVITY_REQUEST_CODE);

            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Nach dem bearbeiten einer Route, werden die Daten an den ViewModel weitergeleitet, je nach dem welche CRUD-Operation ausgeführt wird.
        if (requestCode == NEW_ROUTE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            String bez = data.getStringExtra(NewRouteActivity.EXTRA_NAME);
            String beg = data.getStringExtra(NewRouteActivity.EXTRA_BEG);
            String end = data.getStringExtra(NewRouteActivity.EXTRA_END);
            String gpx = data.getStringExtra(NewRouteActivity.EXTRA_GPX);
            String dau = data.getStringExtra(NewRouteActivity.EXTRA_DAU);

            // Ein neues Objekt, mit den Werten aus dem NewRoute Intent wird instanziert und dem ViewModel übergeben.
            Route route = new Route(bez, beg, end, gpx, dau);
            mRouteViewModel.insert(route);

            Toast.makeText(
                    getApplicationContext(),
                    R.string.route_saved,
                    Toast.LENGTH_LONG).show();

        } else if(requestCode == EDIT_ROUTE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){

            if(data.getBooleanExtra(NewRouteActivity.EXTRA_DELETE_ROUTE, false)) {
                int id = data.getIntExtra(NewRouteActivity.EXTRA_ID, -1);

                // Ein neues, leeres Objekt wird instanziert und mit einer ID an das Model übergeben.
                Route route = new Route("", "", "", "", "");

                route.setId(id);
                mRouteViewModel.delete(route);

                Toast.makeText(
                        getApplicationContext(),
                        R.string.route_deleted,
                        Toast.LENGTH_LONG).show();
            } else {
                int id = data.getIntExtra(NewRouteActivity.EXTRA_ID, -1);

                if(id == -1){
                    // Falls beim Aktualsieren etwas falsch läuft.
                    Toast.makeText(this,R.string.route_edit_error, Toast.LENGTH_SHORT).show();
                    return;
                }

                String bez = data.getStringExtra(NewRouteActivity.EXTRA_NAME);
                String beg = data.getStringExtra(NewRouteActivity.EXTRA_BEG);
                String ende = data.getStringExtra(NewRouteActivity.EXTRA_END);
                String gpx = data.getStringExtra(NewRouteActivity.EXTRA_GPX);
                String dau = data.getStringExtra(NewRouteActivity.EXTRA_DAU);

                // Ein neues Object mit aktualierten Werten aus dem NewRoute Intent wird instanziert und dem ViewModel übergeben.
                Route route = new Route(bez, beg, ende, gpx, dau);

                // *** Id wird hier gesetzt um update (Dao macht vergleich der Ids) zu ermöglichen.
                route.setId(id);
                mRouteViewModel.update(route);

                Toast.makeText(
                        getApplicationContext(),
                        R.string.route_updated,
                        Toast.LENGTH_LONG).show();
            }
        } else {

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}