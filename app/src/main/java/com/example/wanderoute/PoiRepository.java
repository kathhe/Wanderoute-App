package com.example.wanderoute;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

// Hier wird die Rpository der Entität, mit ihren CRUD-Operationen beschrieben.
public class PoiRepository {

    private PoiDao mPoiDao;
    private LiveData<List<Poi>> mAllPoi;

    PoiRepository(Application application) {
        // Datenbank instanzieren
        RouteRoomDatabase db = RouteRoomDatabase.getDatabase(application);

        // Auf poiDao zugreifen
        mPoiDao = db.poiDao();

        // Auf DAO-Methoden zugreifen
        mAllPoi = mPoiDao.getAlphabetizedPoi();
    }

    // Methode, die, mit Hilfe der DAO, alle Poi aus der Datenbank ausliest
    LiveData<List<Poi>> getAllPoi() {
        return mAllPoi;
    }

    // Methode, die, die mit Hilfe der DAO, Poi anhand ihrere jeweiligen routeId ausliest
    LiveData<List<Poi>> getAllFilteredPoi(int routeId) {
        return mPoiDao.getFilteredPois(routeId);
    }

    // Schreibt mit Hilfe des DAO, das Objekts in die Datenbank
    void insert(Poi poi) {
        RouteRoomDatabase.databaseWriteExecutor.execute(() -> {
            mPoiDao.insert(poi);
        });
    }

    // Aktualisiert mit Hilfe des DAO, das Objekts in der Datenbank
    void update(Poi poi){
        RouteRoomDatabase.databaseWriteExecutor.execute(() -> {
            mPoiDao.update(poi);
        });
    }

    // Löscht mit Hilfe der DAO, das Objekts aus der Datenbank
    void delete(Poi poi){
        RouteRoomDatabase.databaseWriteExecutor.execute(() -> {
            mPoiDao.delete(poi);
        });
    }
}
