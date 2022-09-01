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
public interface RouteDao {

    @Insert
    void insert(Route route);

    @Update
    void update(Route route);

    @Query("DELETE FROM route_table")
    void deleteAll();

    @Delete
    void delete(Route route);

    @Query("SELECT * FROM route_table ORDER BY bez ASC")
    LiveData<List<Route>> getAlphabetizedRoutes();
}
