package com.example;

public class Location {
    // protected variables can be accessed 
    // anywhere in the package I believe
    private int x;
    private int y;
    private String direction;
    
    public Location(int x, int y) {
        this.x = x;
        this.y = y;
        this.direction = "none";
    }

    public Location(int x, int y, String direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    // below are getter and setter methods
    // since x and y are of type protected,
    // these aren't actually needed

    // x getter and setter methods
    public int getX() {return x;}
    public void setX(int x) {this.x = x;}

    // y getter and setter methods
    public int getY() {return y;}
    public void setY(int y) {this.y = y;}

    // direction getter and setter methods
    public String getDirection() {return direction;}
    public void setDirection(String direction) {this.direction = direction;}
}
