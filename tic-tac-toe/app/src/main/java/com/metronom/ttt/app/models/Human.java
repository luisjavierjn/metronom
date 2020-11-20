package com.metronom.ttt.app.models;

public class Human extends Player {

    public Human(int index, String letter) {
        super(index, letter);
    }

    @Override
    public boolean isHuman() {
        return true;
    }
}
