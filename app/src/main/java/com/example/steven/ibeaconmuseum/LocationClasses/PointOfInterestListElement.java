package com.example.steven.ibeaconmuseum.LocationClasses;

public class PointOfInterestListElement {

    private String left;
    private String right;
    private String center;

    public PointOfInterestListElement(String left, String right, String center){
        this.left = left;
        this.right = right;
        this.center = center;
    }

    public String getLeft() {
        return left;
    }
    public String getRight() {
        return right;
    }
    public String getCenter() {
        return center;
    }

    public void setLeft(String left) {
        this.left = left;
    }
    public void setRight(String right) {
        this.right = right;
    }
    public void setCenter(String center) {
        this.center = center;
    }

}
