package com.example.wanderoute;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

// Hier wird die ModelView der Entität, mit ihren CRUD-Operationen beschrieben.
public class PoiViewModel extends AndroidViewModel {

    private PoiRepository mRepository;
    private final LiveData<List<Poi>> mAllPoi;


    public PoiViewModel(@NonNull Application application) {
        super(application);

        // Neue Repository wird instanziert
        mRepository = new PoiRepository(application);

        // Auf Repository-Methoden zugreifen
        mAllPoi = mRepository.getAllPoi();
    }

    // Methode, die, mit Hilfe der Repository, alle Poi aus der Datenbank ausliest
    LiveData<List<Poi>> getAllPoi() {
        return mAllPoi;
    }

    // Methode, die, die mit Hilfe der Repository, Poi anhand ihrere jeweiligen routeId ausliest
    LiveData<List<Poi>> getAllFilteredPoi(int routeId) {
        LiveData<List<Poi>> filtered = mRepository.getAllFilteredPoi(routeId);
        return filtered;
    }

    // Schreibt mit Hilfe des Repositoy, das Objekts in die Datenbank
    public void insert(Poi poi) {
        mRepository.insert(poi);
    }

    // Aktualisiert mit Hilfe des Repository, das Objekts in der Datenbank
    public void update(Poi poi) {
        mRepository.update(poi);
    }

    // Löscht mit Hilfe der Repositoy, das Objekts aus der Datenbank
    public void delete(Poi poi) {
        mRepository.delete(poi);
    }
}
