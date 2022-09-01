package com.example.wanderoute;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "route_table")
public class Route {

    @PrimaryKey (autoGenerate = true)
    private int id;

    @ColumnInfo(name = "bez")
    private String mBez;

    @ColumnInfo(name = "beg")
    private String mBeg;

    @ColumnInfo(name = "end")
    private String mEnd;

    @ColumnInfo(name = "gpx")
    private String mGpx;

    @ColumnInfo(name = "dauer")
    private String mDau;



    public Route(@NonNull String bez, String beg, String end, String gpx, String dau){
        this.mBez = bez;
        this.mBeg = beg;
        this.mEnd = end;
        this.mGpx = gpx;
        this.mDau = dau;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getBez() {
        return this.mBez;
    }
    public void setBez(String bez) {
        this.mBez = bez;
    }

    public String getBeg() {
        return this.mBeg;
    }
    public void setBeg(String beg) {
        this.mBeg = beg;
    }

    public String getEnd() {
        return this.mEnd;
    }
    public void setEnd(String end) {
        this.mEnd = end;
    }

    public String getGpx() {
        return this.mGpx;
    }
    public void setGpx(String gpx) {
        this.mGpx = gpx;
    }

    public String getDau() {
        return this.mDau;
    }
    public void setDau(String dau) {
        this.mDau = dau;
    }

    public String getAllValues() {
        StringBuilder s = new StringBuilder();
        s.append(String.format("Route id: %s\r\n", this.id));
        s.append(String.format("Route bezeichnung: %s\r\n", this.mBez));
        s.append(String.format("Route beginn: %s\r\n", this.mBeg));
        s.append(String.format("Route ende: %s\r\n", this.mEnd));
        s.append(String.format("Route gpx: %s\r\n", this.mGpx));
        s.append(String.format("Route dauer: %s\r\n", this.mDau));

        return s.toString();
    }

}
