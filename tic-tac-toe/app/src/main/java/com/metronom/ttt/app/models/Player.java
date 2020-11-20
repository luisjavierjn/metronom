package com.metronom.ttt.app.models;

public abstract class Player implements Humanity {
    private int index;
    private String letter;

    public Player(int index, String letter) {
        this.index = index;
        this.letter = letter;
    }

    public String getLetter() {
        return letter;
    }

    public int getIndex() {
        return index;
    }
}
