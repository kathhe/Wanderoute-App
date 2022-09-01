package com.example.wanderoute;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "poi_table")
public class Poi {
    @PrimaryKey( autoGenerate = true)
    private int poiId;

    //Fremdschlüssel, zu welcher Route gehört der Poi
    @ColumnInfo(name="routeOwnerid")
    protected long mRouteOwnerId;

    @ColumnInfo(name = "ort")
    private String mOrt;

    @ColumnInfo(name = "koordinaten")
    private String mKoord;

    @ColumnInfo(name = "beschreibung")
    private String mBesch;

    @ColumnInfo(name = "foto")
    private String mFoto;

    public Poi( String ort, String koord, String besch, String foto) {
        this.mOrt = ort;
        this.mKoord = koord;
        this.mBesch = besch;
        this.mFoto = foto;
    }

    public int getPoiId() {
        return poiId;
    }
    public void setPoiId( int poiId) {
        this.poiId = poiId;
    }


    public long getRouteOwnerId() {
        return mRouteOwnerId;
    }
    public void setRouteOwnerId( long routeOwnerId) {
        this.mRouteOwnerId = routeOwnerId;
    }


    public String getOrt() {
        return this.mOrt;
    }
    public void setOrt( String ort) {
        this.mOrt = ort;
    }


    public String getKoord() {
        return this.mKoord;
    }
    public void setKoord( String koord) {
        this.mKoord = koord;
    }


    public String getBesch() {
        return this.mBesch;
    }
    public void setBesch( String besch) {
        this.mBesch = besch;
    }


    public String getFoto() {
        return this.mFoto;
    }
    public void setFoto( String foto) {
        this.mFoto = foto;
    }


    public String getAllValues() {
        StringBuilder s = new StringBuilder();

        s.append(String.format("Poi id: %s\r\n", this.poiId));
        s.append(String.format("Route id: %s\r\n", this.mRouteOwnerId));
        s.append(String.format("Route Ort: %s\r\n", this.mOrt));
        s.append(String.format("Route Koord: %s\r\n", this.mKoord));
        s.append(String.format("Route Beschreibung: %s\r\n", this.mBesch));
        s.append(String.format("Route Foto URI: %s\r\n", this.mFoto));

        return s.toString();
    }


}
