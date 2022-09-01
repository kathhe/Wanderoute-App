package com.example.wanderoute;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

// Hier wird die ModelView der Entität, mit ihren CRUD-Operationen beschrieben.
public class RouteViewModel extends AndroidViewModel {

    private RouteRepository mRepository;
    private final LiveData<List<Route>> mAllRoutes;

    public RouteViewModel(Application application) {
        super(application);

        // Neue Repository wird instanziert
        mRepository = new RouteRepository(application);

        // Auf Repository-Methoden zugreifen
        mAllRoutes = mRepository.getallRoutes();
    }

    // Methode, die, mit Hilfe der Repository, alle Route aus der Datenbank ausliest
    LiveData<List<Route>> getAllRoutes() {
        return mAllRoutes;
    }

    // Schreibt mit Hilfe des Repositoy, das Objekts in die Datenbank
    public void insert(Route route) {
        mRepository.insert(route);
    }

    // Aktualisiert mit Hilfe des Repository, das Objekts in der Datenbank
    public void update(Route route){
        mRepository.update(route);

    }

    // Löscht mit Hilfe der Repositoy, das Objekts aus der Datenbank
    public void delete(Route route){
        mRepository.delete(route);

    }
}
