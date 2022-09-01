package com.example.wanderoute;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

// Hier wird der Data Access Object der Entität, mit ihren CRUD-Operationen beschrieben.
@Dao
public interface PoiDao {
    @Insert
    void insert(Poi poi);

    @Update
    void update(Poi poi);

    @Query("DELETE FROM poi_table")
    void deleteAll();

    @Delete
    void delete(Poi poi);

    @Query("SELECT * FROM poi_table ORDER BY ort ASC")
    LiveData<List<Poi>> getAlphabetizedPoi();

    @Query("SELECT * FROM poi_table WHERE routeOwnerid = :routeId")
    LiveData<List<Poi>> getFilteredPois(int routeId);
}
