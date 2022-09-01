package com.example.wanderoute;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class RouteWithPoi {
    @Embedded
    public Route route;

    @Relation(parentColumn = "id", entityColumn = "poiId", entity = Poi.class)
    public List<RouteWithPoi> route_with_pois;
}
