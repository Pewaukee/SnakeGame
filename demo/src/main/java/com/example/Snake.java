package com.example;

import java.util.ArrayList;

// here define the snake, with length, direction,
// head coordinates, and also collision checker?

public class Snake  {
    
    private int length;
    //private String direction;
    private int[] headCoordinates;
    private int[] tailCoordinates;
    private boolean collision;
    private ArrayList<Location> bodyParts;

    public Snake() {
        /*
         * direction used to be set here, but i think
         * it makes more sense to put it in the Location 
         * class so that each snakeBody part has a seperate 
         * direction with pivoting and such
         * direction = "up";
         */
        length = 5; // initial snake length is 5
        
        // set the initial coordinates
        headCoordinates = new int[2]; 
        tailCoordinates = new int[2];

        // starts at about the middle of board
        headCoordinates[0] = 25+21*10;
        headCoordinates[1] = 25+21*11;
         
        tailCoordinates[0] = 25+21*10;
        tailCoordinates[1] = 25+21*15;
        
        //declare the bodyParts, then add based on location
        bodyParts = new ArrayList<Location>();
        bodyParts.add(new Location(25+21*10, 25+21*11, "up"));
        bodyParts.add(new Location(25+21*10, 25+21*12, "up"));
        bodyParts.add(new Location(25+21*10, 25+21*13, "up"));
        bodyParts.add(new Location(25+21*10, 25+21*14, "up"));
        bodyParts.add(new Location(25+21*10, 25+21*15, "up"));   
        
    }
    //length getter and setter methods
    public int getLength() {return length;}
    public void setLength(int length) {this.length = length;}

    //direction getter and setter methods
    //public String getDirection() {return direction;}
    //public void setDirection(String direction) {this.direction = direction;}

    //headCoodinate getter and setter methods
    public int[] getHeadCoordinates() {return headCoordinates;}
    public void setHeadCoordinates(int x, int y) {
        headCoordinates[0] = x;
        headCoordinates[1] = y;
    }

    public int[] getTailCoordinates() {return tailCoordinates;}
    public void setTailCoordinates(int x, int y) {
        tailCoordinates[0] = x;
        tailCoordinates[1] = y;
    }

    //collision getter and setter methods
    public boolean isCollision() {return collision;}
    public void setCollision(boolean collision) {this.collision = collision;}

    //bodyParts getter and setter methods
    public ArrayList<Location> getBodyParts() {return bodyParts;}
    public void addBodyPart(Location location) {this.bodyParts.add(location);}
    public void changeBodyPart(int i, Location location) {bodyParts.set(i, location);}
    
}
