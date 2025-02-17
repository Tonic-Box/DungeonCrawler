package com.tonic.systems;

public class StaircaseLink {
    public int sourceFloor;
    public int sourceX, sourceY; // tile coordinates on source floor
    public int targetFloor;
    public int targetX, targetY; // tile coordinates on target floor

    public StaircaseLink(int sourceFloor, int sourceX, int sourceY, int targetFloor, int targetX, int targetY) {
        this.sourceFloor = sourceFloor;
        this.sourceX = sourceX;
        this.sourceY = sourceY;
        this.targetFloor = targetFloor;
        this.targetX = targetX;
        this.targetY = targetY;
    }
}
