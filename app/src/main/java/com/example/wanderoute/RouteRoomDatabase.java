package com.example.wanderoute;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Route.class, Poi.class}, version = 3, exportSchema = false)
public abstract class RouteRoomDatabase extends RoomDatabase {

    public abstract RouteDao routeDao();
    public abstract PoiDao poiDao();

    private static volatile RouteRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static RouteRoomDatabase getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (RouteRoomDatabase.class) {
                if(INSTANCE == null) {
                    // Methode: fallbackToDestructiveMigration() hinzugefügt, da bei mir die Datenbank sonst abgestürzt ist
                    // https://stackoverflow.com/questions/44543608/room-cannot-verify-the-data-integrity-in-android
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), RouteRoomDatabase.class, "route_database").fallbackToDestructiveMigration().addCallback(sRoomDatabaseCallback).build();
                    //INSTANCE = Room.databaseBuilder(context.getApplicationContext(), RouteRoomDatabase.class, "route_database").addCallback(sRoomDatabaseCallback).build();
                }
            }
        }

        return INSTANCE;
    }


    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                RouteDao dao = INSTANCE.routeDao();
                dao.deleteAll();

                Route route = new Route("Victoria, London", "51.49, -0.14", "51.46, -0.30", "001.gpx", "02:03");
                dao.insert(route);
                route = new Route("Richmond, London", "51.46, -0.30", "51.49, -0.14", "002.gpx", "03:32");
                dao.insert(route);
            });
        }
    };

}
