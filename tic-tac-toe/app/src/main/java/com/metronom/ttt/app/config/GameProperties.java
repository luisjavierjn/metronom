package com.metronom.ttt.app.config;

import com.metronom.ttt.app.exceptions.GameException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import static com.metronom.ttt.app.config.Constants.COMMA;
import static com.metronom.ttt.app.config.Constants.EMPTY;
import static com.metronom.ttt.app.config.Constants.MAX_CELLS;
import static com.metronom.ttt.app.config.Constants.MIN_CELLS;

public class GameProperties {
    String result = EMPTY;
    Properties prop;
    InputStream inputStream;

    private int numcells;
    private List<String> humanPlayers;
    private List<String> robotPlayers;
    private List<String> allPlayers;

    public int getNumcells() {
        return numcells;
    }

    public List<String> getHumanPlayers() {
        return humanPlayers;
    }

    public List<String> getRobotPlayers() {
        return robotPlayers;
    }

    public String getPropValues(String propertyFile) throws IOException, NullPointerException, GameException {
        prop = new Properties();
        inputStream = getClass().getClassLoader().getResourceAsStream(propertyFile);

        if (inputStream != null) {
            prop.load(inputStream);
        } else {
            throw new FileNotFoundException("property file '" + propertyFile + "' not found in the classpath");
        }

        Date time = new Date(System.currentTimeMillis());

        // get the property value and print it out
        String ncells = prop.getProperty("board.numcells").trim();
        String humans = prop.getProperty("players.humans").trim().replaceAll("^,|,$", EMPTY).toUpperCase();
        String robots = prop.getProperty("players.robots").trim().replaceAll("^,|,$", EMPTY).toUpperCase();
        String letter = (humans + "," + robots).trim().replaceAll("^,|,$", EMPTY).toUpperCase();

        try {
            numcells = Integer.parseInt(ncells);
            humanPlayers = !humans.equals(EMPTY) ? Arrays.asList(humans.split(COMMA)) : new ArrayList<>();
            robotPlayers = !robots.equals(EMPTY) ? Arrays.asList(robots.split(COMMA)) : new ArrayList<>();
            allPlayers = !letter.equals(EMPTY) ? Arrays.asList(letter.split(COMMA)) : new ArrayList<>();
        } catch (Exception e) {
            System.out.println("Exception: " + e);
            throw new GameException("Please verify the values in config.properties");
        }

        if (numcells < MIN_CELLS || numcells > MAX_CELLS) {
            throw new GameException("The property board.numcells should be between " + MIN_CELLS + " and " + MAX_CELLS);
        }

        if(allPlayers.size() == 0) {
            throw new GameException("This game has not sense if don't put any player, even if you want to play alone :)");
        }

        if (humanPlayers.stream().anyMatch(s -> !s.matches("^[A-Z]?"))) {
            throw new GameException("The name of any human player must be machina letter");
        }

        if (robotPlayers.stream().anyMatch(s -> !s.matches("^[A-Z]?"))) {
            throw new GameException("The name of any robot player must be machina letter");
        }

        List<String> repeated = allPlayers.stream().distinct().filter(entry -> Collections.frequency(allPlayers, entry) > 1).collect(Collectors.toList());
        if (repeated.size() > 0) {
            throw new GameException("You cannot repeat letters for the players " + repeated);
        }

        inputStream.close();

        result = "Human players = " + humans + " Robot players = " + robots;
        System.out.println(result + "\nProgram Ran on " + time + " with Board = " + ncells + "x" + ncells);
        return result;
    }
}
