package com.example.steven.ibeaconmuseum.LocationClasses;

/**
 * Created by Steven on 11/28/2017.
 */

public class FakeNearbyPoi {

    private String paintingName;
    private String paintingInfo;
    private String paintingArtist;

    FakeNearbyPoi(String name, String info, String artist){
        this.paintingArtist = artist;
        this.paintingInfo = info;
        this.paintingName = name;
    }



    // Getters/Setters
    public String getPaintingName() {
        return paintingName;
    }
    public String getPaintingInfo() {
        return paintingInfo;
    }
    public String getPaintingArtist() {
        return paintingArtist;
    }
    public void setPaintingInfo(String paintingInfo) {
        this.paintingInfo = paintingInfo;
    }
    public void setPaintingArtist(String paintingArtist) {
        this.paintingArtist = paintingArtist;
    }
    public void setPaintingName(String paintingName) {
        this.paintingName = paintingName;
    }
}
