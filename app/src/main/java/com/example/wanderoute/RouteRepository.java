package com.example.wanderoute;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

// Hier wird die Rpository der Entität, mit ihren CRUD-Operationen beschrieben.
public class RouteRepository {

    private RouteDao mRouteDao;
    private LiveData<List<Route>> mAllRoutes;

    RouteRepository(Application application) {
        // Datenbank instanzieren
        RouteRoomDatabase db = RouteRoomDatabase.getDatabase(application);

        // Auf routeDao zugreifen
        mRouteDao = db.routeDao();

        // Auf DAO-Methoden zugreifen
        mAllRoutes = mRouteDao.getAlphabetizedRoutes();
    }

    // Methode, die, mit Hilfe der DAO, alle Routen aus der Datenbank ausliest
    LiveData<List<Route>> getallRoutes(){
        return mAllRoutes;
    }

    // Schreibt mit Hilfe des DAO, das Objekts in die Datenbank
    void insert(Route route){
        RouteRoomDatabase.databaseWriteExecutor.execute(() -> {
            mRouteDao.insert(route);
        });
    }

    // Aktualisiert mit Hilfe des DAO, das Objekts in der Datenbank
    void update(Route route){
        RouteRoomDatabase.databaseWriteExecutor.execute(() -> {
            mRouteDao.update(route);
        });
    }

    // Löscht mit Hilfe der DAO, das Objekts aus der Datenbank
    void delete(Route route){
        RouteRoomDatabase.databaseWriteExecutor.execute(() -> {
            mRouteDao.delete(route);
        });
    }
}
