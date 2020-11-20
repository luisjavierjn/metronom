package com.metronom.ttt.app.models;

import java.util.Random;

public class Robot extends Player implements RobotMove {
    Random random;

    public Robot(int index, String letter) {
        super(index, letter);
        random = new Random();
    }

    @Override
    public boolean isHuman() {
        return false;
    }

    @Override
    public int calculateMove(Board board) {
        int idx;
        boolean found;
        int[] map = board.getMap();

        do {
            idx = random.nextInt(map.length);
            found = map[idx] == 0;
        } while(!found);

        return idx;
    }
}
