package com.example.steven.ibeaconmuseum;

/**
 * Created by Steven on 11/15/2017.
 */

public class DataObject {

    private String left;
    private String right;
    private String center;

    public DataObject(String left, String right, String center){
        this.left = left;
        this.right = right;
        this.center = center;
    }

    public String getLeft(){
        return this.left;
    }
    public String getRight(){
        return this.right;
    }
    public String getCenter(){
        return this.center;
    }

}
