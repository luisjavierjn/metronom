package com.metronom.ttt.app.models;

public class Coord {
    private int row;
    private int col;

    public Coord(double row, double col) {
        this.row = (int)row;
        this.col = (int)col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
