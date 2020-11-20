package com.metronom.ttt.app.machina;

import com.metronom.ttt.app.config.GameProperties;
import com.metronom.ttt.app.exceptions.GameException;
import com.metronom.ttt.app.models.Board;
import com.metronom.ttt.app.models.Player;
import com.metronom.ttt.app.models.RobotMove;

import java.util.Queue;
import java.util.Scanner;

import static com.metronom.ttt.app.config.Constants.COMMA;
import static com.metronom.ttt.app.config.Constants.EMPTY;
import static com.metronom.ttt.app.config.Constants.SPACE;
import static com.metronom.ttt.app.machina.Events.EVENT_BOARD_LOADED;
import static com.metronom.ttt.app.machina.Events.EVENT_EXIT_GAME;
import static com.metronom.ttt.app.machina.Events.EVENT_FIND_PLAYER;
import static com.metronom.ttt.app.machina.Events.EVENT_FULL_BOARD;
import static com.metronom.ttt.app.machina.Events.EVENT_INVALID_INPUT;
import static com.metronom.ttt.app.machina.Events.EVENT_NEUTRAL;
import static com.metronom.ttt.app.machina.Events.EVENT_RESET_GAME;
import static com.metronom.ttt.app.machina.Events.EVENT_VALID_INPUT;
import static com.metronom.ttt.app.machina.Events.EVENT_WINNER_FOUND;
import static com.metronom.ttt.app.machina.States.STATE_GAME_OVER;
import static com.metronom.ttt.app.machina.States.STATE_INITIALIZE;
import static com.metronom.ttt.app.machina.States.STATE_LOAD_BOARD;
import static com.metronom.ttt.app.machina.States.STATE_NEUTRAL;
import static com.metronom.ttt.app.machina.States.STATE_NEXT_PLAYER;
import static com.metronom.ttt.app.machina.States.STATE_PLAYER_MOVE;
import static com.metronom.ttt.app.machina.States.STATE_RESET_GAME;

public class Game {

    protected States currentState;
    protected States previousState;
    protected Scanner in;
    protected Queue<Events> queue;
    protected Board board;
    private int row, col, cell;
    private String propertyFile;

    public States getCurrentState() {
        return currentState;
    }

    public Board getBoard() {
        return board;
    }

    public void setIn(Scanner in) {
        this.in = in;
    }

    public Game(Scanner scanner, Queue<Events> queue, String propertyFile) {
        this.in = scanner;
        this.queue = queue;
        this.queue.add(EVENT_NEUTRAL);
        this.propertyFile = propertyFile;
        currentState = STATE_INITIALIZE;
    }

    public void check(Events evt) {
        execute(transfer(currentState, evt));
    }

    private States transfer(States initialState, Events evt) {
        switch (initialState) {
            case STATE_INITIALIZE:
                if (evt == EVENT_NEUTRAL) return STATE_LOAD_BOARD;
                break;
            case STATE_LOAD_BOARD:
                if (evt == EVENT_BOARD_LOADED) return STATE_NEXT_PLAYER;
                if (evt == EVENT_EXIT_GAME) return STATE_GAME_OVER;
                break;
            case STATE_NEXT_PLAYER:
                if (evt == EVENT_INVALID_INPUT) return STATE_NEXT_PLAYER;
                if (evt == EVENT_VALID_INPUT) return STATE_PLAYER_MOVE;
                break;
            case STATE_PLAYER_MOVE:
                if (evt == EVENT_WINNER_FOUND) return STATE_RESET_GAME;
                if (evt == EVENT_FULL_BOARD) return STATE_RESET_GAME;
                if (evt == EVENT_FIND_PLAYER) return STATE_NEXT_PLAYER;
                break;
            case STATE_RESET_GAME:
                if (evt == EVENT_RESET_GAME) return STATE_LOAD_BOARD;
                if (evt == EVENT_EXIT_GAME) return STATE_GAME_OVER;
                break;
        }

        return STATE_NEUTRAL;
    }

    private void execute(States state) {
        if (state != STATE_NEUTRAL) {
            previousState = currentState;
            currentState = state;
        }

        switch (state) {
            case STATE_LOAD_BOARD:
                sLoadBoard();
                break;
            case STATE_NEXT_PLAYER:
                sNextPlayer();
                break;
            case STATE_PLAYER_MOVE:
                sPlayerMove();
                break;
            case STATE_RESET_GAME:
                sResetGame();
                break;
            case STATE_GAME_OVER:
                sGameOver();
        }
    }

    private void sLoadBoard() {
        GameProperties props = new GameProperties();
        try {
            props.getPropValues(propertyFile);
            board = new Board(props.getNumcells());
            props.getHumanPlayers().forEach((h) -> {
                if (!h.equals(EMPTY)) {
                    board.addHumanPlayer(h);
                }
            });
            props.getRobotPlayers().forEach((r) -> {
                if (!r.equals(EMPTY)) {
                    board.addRobotPlayer(r);
                }
            });
            board.Draw();
            queue.add(EVENT_BOARD_LOADED);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
            queue.add(EVENT_EXIT_GAME);
        }
    }

    private void sNextPlayer() {
        Player player = board.getCurrentPlayer();
        if (player.isHuman()) {
            System.out.println("Player " + player.getLetter() + " turn");
            System.out.println("Enter row,column places and press Enter ->");
            try {
                String data = in.nextLine().trim().replace(SPACE, EMPTY);
                if (data.length() - data.replace(COMMA, EMPTY).length() > 1) {
                    System.out.println("Too many commas");
                    throw new GameException("Too many commas");
                }
                row = Integer.parseInt(data.split(COMMA)[0]) - 1;
                col = Integer.parseInt(data.split(COMMA)[1]) - 1;
                cell = board.getCellByCoords(row, col);

                if (!board.isAValidCell(cell)) {
                    board.Draw();
                    System.out.println("Invalid row " + (row + 1) + " and column " + (col + 1) + ". Out of range or occupied");
                    System.out.println("The range for row and column should be [1-" + board.getNumCells() + "]");
                    queue.add(EVENT_INVALID_INPUT);
                } else {
                    queue.add(EVENT_VALID_INPUT);
                }
            } catch (Exception e) {
                board.Draw();
                System.out.println("Please provide the position like row,column. e.g -> 3,2");
                queue.add(EVENT_INVALID_INPUT);
            }
        } else {
            try {
                System.out.println("Now is coming the computer move...");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cell = ((RobotMove) player).calculateMove(board);
            queue.add(EVENT_VALID_INPUT);
        }
    }

    private void sPlayerMove() {
        Player player = board.getCurrentPlayer();
        board.setMoveToCell(cell, player.getIndex());
        board.Draw();

        if (board.isThereAWinner(cell, player.getIndex())) {
            System.out.println("Player " + player.getLetter() + " wins!!!!!!");
            queue.add(EVENT_WINNER_FOUND);
        } else if (board.isItATie()) {
            System.out.println("It's machina tie");
            queue.add(EVENT_FULL_BOARD);
        } else {
            board.nextPlayer();
            queue.add(EVENT_FIND_PLAYER);
        }
    }

    private void sResetGame() {
        System.out.println("Press 0 and Enter to exit or just Enter to reset");

        String exit = in.nextLine().trim();
        if (exit.equals("0")) {
            queue.add(EVENT_EXIT_GAME);
        } else {
            queue.add(EVENT_RESET_GAME);
        }
    }

    private void sGameOver() {
        System.out.println("Game Over");
    }
}
