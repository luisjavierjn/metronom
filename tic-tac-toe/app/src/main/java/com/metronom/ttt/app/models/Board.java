package com.metronom.ttt.app.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.metronom.ttt.app.config.Constants.DASH;
import static com.metronom.ttt.app.config.Constants.EMPTY_CELL_VALUE;
import static com.metronom.ttt.app.config.Constants.PIPE;
import static com.metronom.ttt.app.config.Constants.SPACE;

public class Board {

    private int numCells;
    private int totalCells;
    int[] map;

    private int currentPlayerIndex = 0;
    private int occupiedCells = 0;
    private int addedPlayers = 0;

    private List<Integer> horz = new ArrayList<>();
    private List<Integer> vert = new ArrayList<>();
    private List<Integer> izqu = new ArrayList<>();
    private List<Integer> dere = new ArrayList<>();

    HashMap<Integer, Player> players;

    public Board(int numCells) {
        this.numCells = numCells;
        totalCells = (int) Math.pow(numCells, 2);
        map = new int[totalCells];
        players = new HashMap<>();
    }

    public void addHumanPlayer(String letter) {
        addedPlayers++;
        players.put(addedPlayers, new Human(addedPlayers, letter));
    }

    public void addRobotPlayer(String letter) {
        addedPlayers++;
        players.put(addedPlayers, new Robot(addedPlayers, letter));
    }

    public int getAddedPlayers() {
        return addedPlayers;
    }

    public int getNumCells() {
        return numCells;
    }

    public int[] getMap() {
        return map;
    }

    public void setMoveToCell(int cell, int playerIndex) {
        this.map[cell] = playerIndex;
        occupiedCells++;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex % addedPlayers + 1);
    }

    public void nextPlayer() {
        currentPlayerIndex++;
    }

    private int calculateDashes(int numCells) {
        return numCells * 4 - 1;
    }

    public boolean isAValidCell(int cell) {
        return cell >= 0 && cell < totalCells && map[cell] == 0;
    }

    public boolean isThereAWinner(int cell, int playerIndex) {
        boolean winnerFound = false;
        Coord c = getCellCoords(cell);

        for (int i = 0; i < numCells; i++) {
            horz.add(map[c.getRow() * numCells + i]);
        }

        for (int j = 0; j < numCells; j++) {
            vert.add(map[j * numCells + c.getCol()]);
        }

        for (int k = 0; k < numCells; k++) {
            izqu.add(map[k * numCells + k]);
        }

        for (int l = 0, m = numCells - 1; l < numCells; l++, m--) {
            dere.add(map[l * numCells + m]);
        }

        if (horz.stream().allMatch(v -> v == playerIndex) ||
                vert.stream().allMatch(v -> v == playerIndex) ||
                izqu.stream().allMatch(v -> v == playerIndex) ||
                dere.stream().allMatch(v -> v == playerIndex)) {
            winnerFound = true;
        }

        horz.clear();
        vert.clear();
        izqu.clear();
        dere.clear();

        return winnerFound;
    }

    public boolean isItATie() {
        return occupiedCells == totalCells;
    }

    public Coord getCellCoords(int cell) {
        return new Coord(Math.floor(cell / numCells), cell - (Math.floor(cell / numCells) * numCells));
    }

    public int getCellByCoords(int row, int column) {
        if (row >= 0 && row < numCells && column >= 0 && column < numCells) {
            return row * numCells + column;
        } else {
            return -1;
        }
    }

    public String getPlayerLetter(int cell) {
        String letter = SPACE;
        Player p = players.get(map[cell]);
        if (Optional.ofNullable(p).isPresent()) {
            letter = p.getLetter();
        }
        return letter;
    }

    public void resetMap() {
        for (int i = 0; i < map.length; i++) {
            map[i] = EMPTY_CELL_VALUE;
        }
    }

    public void Draw() {
        System.out.println("___________________________________________");
        System.out.println();
        for (int j = 0; j < numCells; j++) { // rows
            int i = 0;
            while (i < numCells - 1) { // columns
                StringBuilder builder = new StringBuilder();
                builder.append(SPACE).append(getPlayerLetter(getCellByCoords(j, i))).append(SPACE.concat(PIPE));
                System.out.print(builder.toString());
                i++;
            }
            StringBuilder builder = new StringBuilder();
            builder.append(SPACE).append(getPlayerLetter(getCellByCoords(j, i))).append(SPACE);
            System.out.println(builder.toString());

            if (j < numCells - 1) {
                System.out.println(IntStream.range(0, calculateDashes(numCells)).mapToObj(k -> DASH).collect(Collectors.joining("")));
            }
        }
    }
}
