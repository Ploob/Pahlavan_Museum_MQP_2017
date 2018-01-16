package com.example.steven.ibeaconmuseum;

// TODO Remove class, depreciated
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

    public void setLeft(String s){
        this.left = s;
    }
    public void setRight(String s){
        this.right = s;
    }
    public void setCenter(String s){
        this.center = s;
    }

}
