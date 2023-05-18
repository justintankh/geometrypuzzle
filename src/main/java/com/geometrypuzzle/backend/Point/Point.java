package com.geometrypuzzle.backend.Point;

import lombok.Data;

@Data
public class Point {
    private int x, y;

    public Point(int x, int y){
        this.x = x;
        this.y = y;
    }
}
