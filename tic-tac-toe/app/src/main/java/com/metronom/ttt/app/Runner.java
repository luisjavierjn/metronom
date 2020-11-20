package com.metronom.ttt.app;

import com.metronom.ttt.app.machina.Events;
import com.metronom.ttt.app.machina.Game;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import static com.metronom.ttt.app.config.Constants.PROPERTY_FILE;
import static com.metronom.ttt.app.machina.States.STATE_GAME_OVER;

public class Runner {
    static String propertyFile;
    static Queue<Events> queue = new LinkedList<>();

    public static void main(String[] args) {
        propertyFile = args.length > 0 ? args[0] : PROPERTY_FILE;
        Game game = new Game(new Scanner(System.in), queue, propertyFile);
        do {
            while (queue.size() > 0) {
                game.check(queue.poll());
            }
        } while (game.getCurrentState() != STATE_GAME_OVER);
    }
}
