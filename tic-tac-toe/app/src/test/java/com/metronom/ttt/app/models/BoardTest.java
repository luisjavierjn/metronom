package com.metronom.ttt.app.models;

import org.junit.Before;
import org.junit.Test;

import static com.metronom.ttt.app.config.Constants.MIN_CELLS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BoardTest {
    public static final String X = "X";
    public static final String O = "O";
    public static final String C = "C";
    public static final int X_IDX = 1;
    public static final int O_IDX = 2;
    public static final int C_IDX = 3;
    public static final int NUM_PLAYERS = 3;

    private Board board;
    private int numCells;
    private Player player;
    private int row, col, cell;

    @Before
    public void setUp() {
        numCells = MIN_CELLS;
        row = numCells - 1;
        col = numCells - 1;
        board = new Board(numCells);
        board.addHumanPlayer(X);
        board.addHumanPlayer(O);
        board.addRobotPlayer(C);
        board.Draw();
    }

    @Test
    public void checkNumCellsFromBoard() {
        assertEquals((int) Math.pow(numCells, 2), board.getMap().length);
        assertEquals(board.getNumCells(), numCells);
    }

    @Test
    public void checkNumPlayersFromBoard() {
        assertEquals(board.getAddedPlayers(), NUM_PLAYERS);
    }

    @Test
    public void checkChangingPlayersOnBoard() {
        player = board.getCurrentPlayer();
        assertEquals(player.getIndex(), X_IDX);
        assertEquals(player.getLetter(), X);
        board.nextPlayer();
        player = board.getCurrentPlayer();
        assertEquals(player.getIndex(), O_IDX);
        assertEquals(player.getLetter(), O);
        board.nextPlayer();
        player = board.getCurrentPlayer();
        assertEquals(player.getIndex(), C_IDX);
        assertEquals(player.getLetter(), C);
        board.nextPlayer();
        player = board.getCurrentPlayer();
        assertEquals(player.getIndex(), X_IDX);
        assertEquals(player.getLetter(), X);
    }

    @Test
    public void checkValidCellsOnBoard() {
        for (int i = 0; i < numCells; i++) {
            for (int j = 0; j < numCells; j++) {
                cell = board.getCellByCoords(i, j);
                assertTrue(board.isAValidCell(cell));
            }
        }
    }

    @Test
    public void checkInvalidCellOnBoard() {
        cell = board.getCellByCoords(numCells, numCells);
        assertFalse(board.isAValidCell(cell));
    }

    @Test
    public void checkCoordsTransformation() {
        cell = board.getCellByCoords(row, col);
        Coord coord = board.getCellCoords(cell);
        assertEquals(coord.getRow(), row);
        assertEquals(coord.getCol(), col);
    }

    @Test
    public void checkMovementFromPlayer() {
        board.resetMap();
        cell = board.getCellByCoords(row, col);
        player = board.getCurrentPlayer();
        board.setMoveToCell(cell, player.getIndex());
        assertEquals(board.getPlayerLetter(cell), player.getLetter());
    }

    @Test
    public void checkTypeOfPlayersOnBoard() {
        player = board.getCurrentPlayer();
        assertTrue(player.isHuman()); // X player
        board.nextPlayer();
        player = board.getCurrentPlayer();
        assertTrue(player.isHuman()); // O player
        board.nextPlayer();
        player = board.getCurrentPlayer();
        assertFalse(player.isHuman()); // C player
        board.nextPlayer();
        player = board.getCurrentPlayer();
        assertTrue(player.isHuman()); // X player again
    }

    @Test
    public void checkValidRobotPosition() {
        player = board.getCurrentPlayer();
        assertEquals(player.getLetter(), X);
        board.nextPlayer(); // O player
        board.nextPlayer(); // C player
        player = board.getCurrentPlayer();
        assertFalse(player.isHuman());
        cell = ((Robot) player).calculateMove(board);
        assertTrue(board.isAValidCell(cell));
        board.nextPlayer(); // X player again
    }

    //    X | O | X
    //   -----------
    //    X | O | O
    //   -----------
    //    O | X | X
    @Test
    public void checkThatGameResultIsATie() {
        board.resetMap();
        cell = board.getCellByCoords(0, 0);
        board.setMoveToCell(cell, X_IDX);
        cell = board.getCellByCoords(0, 1);
        board.setMoveToCell(cell, O_IDX);
        cell = board.getCellByCoords(0, 2);
        board.setMoveToCell(cell, X_IDX);
        cell = board.getCellByCoords(1, 0);
        board.setMoveToCell(cell, X_IDX);
        cell = board.getCellByCoords(1, 1);
        board.setMoveToCell(cell, O_IDX);
        cell = board.getCellByCoords(1, 2);
        board.setMoveToCell(cell, O_IDX);
        cell = board.getCellByCoords(2, 0);
        board.setMoveToCell(cell, O_IDX);
        cell = board.getCellByCoords(2, 1);
        board.setMoveToCell(cell, X_IDX);
        cell = board.getCellByCoords(2, 2);
        board.setMoveToCell(cell, X_IDX);
        assertTrue(board.isItATie());
    }

    @Test
    public void checkVerticalWinning() {
        for (int i = 0; i < numCells; i++) { // row
            board.resetMap();
            for (int j = 0; j < numCells; j++) { // col
                cell = board.getCellByCoords(i, j);
                board.setMoveToCell(cell, X_IDX);
            }
            assertTrue(board.isThereAWinner(cell, X_IDX));
        }
    }

    @Test
    public void checkHorizontalWinning() {
        for (int i = 0; i < numCells; i++) { // col
            board.resetMap();
            for (int j = 0; j < numCells; j++) { // row
                cell = board.getCellByCoords(j, i);
                board.setMoveToCell(cell, X_IDX);
            }
            assertTrue(board.isThereAWinner(cell, X_IDX));
        }
    }

    @Test
    public void checkDiagonalWinning() {
        board.resetMap();
        for (int i = 0; i < numCells; i++) {
            cell = board.getCellByCoords(i, i);
            board.setMoveToCell(cell, X_IDX);
        }
        assertTrue(board.isThereAWinner(cell, X_IDX));
    }

    @Test
    public void checkInverseDiagonalWinning() {
        board.resetMap();
        for (int i = 0, j = numCells - 1; i < numCells; i++, j--) {
            cell = board.getCellByCoords(i, j);
            board.setMoveToCell(cell, X_IDX);
        }
        assertTrue(board.isThereAWinner(cell, X_IDX));
    }
}
