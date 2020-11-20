package com.metronom.ttt.app.machina;

import com.metronom.ttt.app.Runner;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

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
import static com.metronom.ttt.app.machina.States.STATE_NEXT_PLAYER;
import static com.metronom.ttt.app.machina.States.STATE_PLAYER_MOVE;
import static com.metronom.ttt.app.machina.States.STATE_RESET_GAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GameTest {
    private static final String NO_ROBOTS = "no_robots.properties";
    private static final String NO_CELLS = "no_cells.properties";
    private static final String NO_HUMANS = "no_humans.properties";

    //    X | O | X
    //   -----------
    //    X | O | O
    //   -----------
    //    O | X | X
    @Test
    public void checkItIsATieWithTwoHumansMixedEntries() {
        Queue<Events> queue = new LinkedList<>();
        System.setIn(new ByteArrayInputStream("1,1\r\n".getBytes()));
        Game game = new Game(new Scanner(System.in), queue, NO_ROBOTS);
        assertTrue(game.getCurrentState() == STATE_INITIALIZE);
        assertTrue(queue.peek() == EVENT_NEUTRAL);
        game.check(queue.poll()); // apply event to the current state
        assertTrue(game.getCurrentState() == STATE_LOAD_BOARD);
        assertTrue(queue.peek() == EVENT_BOARD_LOADED);
        assertEquals(game.getBoard().getCurrentPlayer().getLetter(), "X");
        game.check(queue.poll()); // apply event to the current state
        assertTrue(game.getCurrentState() == STATE_NEXT_PLAYER);
        assertTrue(queue.peek() == EVENT_VALID_INPUT);
        game.check(queue.poll()); // apply event to the current state
        assertTrue(game.getCurrentState() == STATE_PLAYER_MOVE);
        assertTrue(queue.peek() == EVENT_FIND_PLAYER);

        System.setIn(new ByteArrayInputStream("1,2\r\n".getBytes()));
        game.setIn(new Scanner(System.in)); // cannot mock Scanner because is final
        assertEquals(game.getBoard().getCurrentPlayer().getLetter(), "O");
        game.check(queue.poll()); // apply event to the current state
        assertTrue(game.getCurrentState() == STATE_NEXT_PLAYER);
        assertTrue(queue.peek() == EVENT_VALID_INPUT);
        game.check(queue.poll()); // apply event to the current state
        assertTrue(game.getCurrentState() == STATE_PLAYER_MOVE);
        assertTrue(queue.peek() == EVENT_FIND_PLAYER);

        System.setIn(new ByteArrayInputStream("1,3\r\n".getBytes()));
        game.setIn(new Scanner(System.in)); // cannot mock Scanner because is final
        assertEquals(game.getBoard().getCurrentPlayer().getLetter(), "X");
        game.check(queue.poll()); // apply event to the current state
        assertTrue(game.getCurrentState() == STATE_NEXT_PLAYER);
        assertTrue(queue.peek() == EVENT_VALID_INPUT);
        game.check(queue.poll()); // apply event to the current state
        assertTrue(game.getCurrentState() == STATE_PLAYER_MOVE);
        assertTrue(queue.peek() == EVENT_FIND_PLAYER);

        System.setIn(new ByteArrayInputStream("1,3\r\n".getBytes())); // cell occupied
        game.setIn(new Scanner(System.in)); // cannot mock Scanner because is final
        assertEquals(game.getBoard().getCurrentPlayer().getLetter(), "O");
        game.check(queue.poll()); // apply event to the current state
        assertTrue(game.getCurrentState() == STATE_NEXT_PLAYER);
        assertTrue(queue.peek() == EVENT_INVALID_INPUT);

        System.setIn(new ByteArrayInputStream("2,3,\r\n".getBytes())); // too many commas
        game.setIn(new Scanner(System.in)); // cannot mock Scanner because is final
        assertEquals(game.getBoard().getCurrentPlayer().getLetter(), "O");
        game.check(queue.poll()); // apply event to the current state
        assertTrue(game.getCurrentState() == STATE_NEXT_PLAYER);
        assertTrue(queue.peek() == EVENT_INVALID_INPUT);

        System.setIn(new ByteArrayInputStream("a,3,\r\n".getBytes())); // throw exception
        game.setIn(new Scanner(System.in)); // cannot mock Scanner because is final
        assertEquals(game.getBoard().getCurrentPlayer().getLetter(), "O");
        game.check(queue.poll()); // apply event to the current state
        assertTrue(game.getCurrentState() == STATE_NEXT_PLAYER);
        assertTrue(queue.peek() == EVENT_INVALID_INPUT);

        System.setIn(new ByteArrayInputStream("2,2\r\n".getBytes()));
        game.setIn(new Scanner(System.in)); // cannot mock Scanner because is final
        assertEquals(game.getBoard().getCurrentPlayer().getLetter(), "O");
        game.check(queue.poll()); // apply event to the current state
        assertTrue(game.getCurrentState() == STATE_NEXT_PLAYER);
        assertTrue(queue.peek() == EVENT_VALID_INPUT);
        game.check(queue.poll()); // apply event to the current state
        assertTrue(game.getCurrentState() == STATE_PLAYER_MOVE);
        assertTrue(queue.peek() == EVENT_FIND_PLAYER);

        System.setIn(new ByteArrayInputStream("3,2\r\n".getBytes()));
        game.setIn(new Scanner(System.in)); // cannot mock Scanner because is final
        assertEquals(game.getBoard().getCurrentPlayer().getLetter(), "X");
        game.check(queue.poll()); // apply event to the current state
        assertTrue(game.getCurrentState() == STATE_NEXT_PLAYER);
        assertTrue(queue.peek() == EVENT_VALID_INPUT);
        game.check(queue.poll()); // apply event to the current state
        assertTrue(game.getCurrentState() == STATE_PLAYER_MOVE);
        assertTrue(queue.peek() == EVENT_FIND_PLAYER);

        System.setIn(new ByteArrayInputStream("2,3\r\n".getBytes()));
        game.setIn(new Scanner(System.in)); // cannot mock Scanner because is final
        assertEquals(game.getBoard().getCurrentPlayer().getLetter(), "O");
        game.check(queue.poll()); // apply event to the current state
        assertTrue(game.getCurrentState() == STATE_NEXT_PLAYER);
        assertTrue(queue.peek() == EVENT_VALID_INPUT);
        game.check(queue.poll()); // apply event to the current state
        assertTrue(game.getCurrentState() == STATE_PLAYER_MOVE);
        assertTrue(queue.peek() == EVENT_FIND_PLAYER);

        System.setIn(new ByteArrayInputStream("3,3\r\n".getBytes()));
        game.setIn(new Scanner(System.in)); // cannot mock Scanner because is final
        assertEquals(game.getBoard().getCurrentPlayer().getLetter(), "X");
        game.check(queue.poll()); // apply event to the current state
        assertTrue(game.getCurrentState() == STATE_NEXT_PLAYER);
        assertTrue(queue.peek() == EVENT_VALID_INPUT);
        game.check(queue.poll()); // apply event to the current state
        assertTrue(game.getCurrentState() == STATE_PLAYER_MOVE);
        assertTrue(queue.peek() == EVENT_FIND_PLAYER);

        System.setIn(new ByteArrayInputStream("3,1\r\n".getBytes()));
        game.setIn(new Scanner(System.in)); // cannot mock Scanner because is final
        assertEquals(game.getBoard().getCurrentPlayer().getLetter(), "O");
        game.check(queue.poll()); // apply event to the current state
        assertTrue(game.getCurrentState() == STATE_NEXT_PLAYER);
        assertTrue(queue.peek() == EVENT_VALID_INPUT);
        game.check(queue.poll()); // apply event to the current state
        assertTrue(game.getCurrentState() == STATE_PLAYER_MOVE);
        assertTrue(queue.peek() == EVENT_FIND_PLAYER);

        System.setIn(new ByteArrayInputStream("2,1\r\n".getBytes()));
        game.setIn(new Scanner(System.in)); // cannot mock Scanner because is final
        assertEquals(game.getBoard().getCurrentPlayer().getLetter(), "X");
        game.check(queue.poll()); // apply event to the current state
        assertTrue(game.getCurrentState() == STATE_NEXT_PLAYER);
        assertTrue(queue.peek() == EVENT_VALID_INPUT);
        game.check(queue.poll()); // apply event to the current state
        assertTrue(game.getCurrentState() == STATE_PLAYER_MOVE);
        assertTrue(queue.peek() == EVENT_FULL_BOARD); // it's a tie

        System.setIn(new ByteArrayInputStream("0\r\n".getBytes()));
        game.setIn(new Scanner(System.in)); // cannot mock Scanner because is final
        game.check(queue.poll()); // apply event to the current state
        assertTrue(game.getCurrentState() == STATE_RESET_GAME);
        assertTrue(queue.peek() == EVENT_EXIT_GAME);
        game.check(queue.poll()); // apply event to the current state
        assertTrue(game.getCurrentState() == STATE_GAME_OVER);
        assertTrue(queue.size() == 0);
    }

    @Test
    public void checkWinningWithTwoHumansMixedEntries() {
        Queue<Events> queue = new LinkedList<>();
        System.setIn(new ByteArrayInputStream("1,1\r\n".getBytes()));
        Game game = new Game(new Scanner(System.in), queue, NO_ROBOTS);
        assertTrue(game.getCurrentState() == STATE_INITIALIZE);
        assertTrue(queue.peek() == EVENT_NEUTRAL);
        game.check(queue.poll()); // apply event to the current state
        assertTrue(game.getCurrentState() == STATE_LOAD_BOARD);
        assertTrue(queue.peek() == EVENT_BOARD_LOADED);
        assertEquals(game.getBoard().getCurrentPlayer().getLetter(), "X");
        game.check(queue.poll()); // apply event to the current state
        assertTrue(game.getCurrentState() == STATE_NEXT_PLAYER);
        assertTrue(queue.peek() == EVENT_VALID_INPUT);
        game.check(queue.poll()); // apply event to the current state
        assertTrue(game.getCurrentState() == STATE_PLAYER_MOVE);
        assertTrue(queue.peek() == EVENT_FIND_PLAYER);

        System.setIn(new ByteArrayInputStream("1,2\r\n".getBytes()));
        game.setIn(new Scanner(System.in)); // cannot mock Scanner because is final
        assertEquals(game.getBoard().getCurrentPlayer().getLetter(), "O");
        game.check(queue.poll()); // apply event to the current state
        assertTrue(game.getCurrentState() == STATE_NEXT_PLAYER);
        assertTrue(queue.peek() == EVENT_VALID_INPUT);
        game.check(queue.poll()); // apply event to the current state
        assertTrue(game.getCurrentState() == STATE_PLAYER_MOVE);
        assertTrue(queue.peek() == EVENT_FIND_PLAYER);

        System.setIn(new ByteArrayInputStream("1,3\r\n".getBytes()));
        game.setIn(new Scanner(System.in)); // cannot mock Scanner because is final
        assertEquals(game.getBoard().getCurrentPlayer().getLetter(), "X");
        game.check(queue.poll()); // apply event to the current state
        assertTrue(game.getCurrentState() == STATE_NEXT_PLAYER);
        assertTrue(queue.peek() == EVENT_VALID_INPUT);
        game.check(queue.poll()); // apply event to the current state
        assertTrue(game.getCurrentState() == STATE_PLAYER_MOVE);
        assertTrue(queue.peek() == EVENT_FIND_PLAYER);

        System.setIn(new ByteArrayInputStream("2,2\r\n".getBytes()));
        game.setIn(new Scanner(System.in)); // cannot mock Scanner because is final
        assertEquals(game.getBoard().getCurrentPlayer().getLetter(), "O");
        game.check(queue.poll()); // apply event to the current state
        assertTrue(game.getCurrentState() == STATE_NEXT_PLAYER);
        assertTrue(queue.peek() == EVENT_VALID_INPUT);
        game.check(queue.poll()); // apply event to the current state
        assertTrue(game.getCurrentState() == STATE_PLAYER_MOVE);
        assertTrue(queue.peek() == EVENT_FIND_PLAYER);

        System.setIn(new ByteArrayInputStream("3,2\r\n".getBytes()));
        game.setIn(new Scanner(System.in)); // cannot mock Scanner because is final
        assertEquals(game.getBoard().getCurrentPlayer().getLetter(), "X");
        game.check(queue.poll()); // apply event to the current state
        assertTrue(game.getCurrentState() == STATE_NEXT_PLAYER);
        assertTrue(queue.peek() == EVENT_VALID_INPUT);
        game.check(queue.poll()); // apply event to the current state
        assertTrue(game.getCurrentState() == STATE_PLAYER_MOVE);
        assertTrue(queue.peek() == EVENT_FIND_PLAYER);

        System.setIn(new ByteArrayInputStream("2,3\r\n".getBytes()));
        game.setIn(new Scanner(System.in)); // cannot mock Scanner because is final
        assertEquals(game.getBoard().getCurrentPlayer().getLetter(), "O");
        game.check(queue.poll()); // apply event to the current state
        assertTrue(game.getCurrentState() == STATE_NEXT_PLAYER);
        assertTrue(queue.peek() == EVENT_VALID_INPUT);
        game.check(queue.poll()); // apply event to the current state
        assertTrue(game.getCurrentState() == STATE_PLAYER_MOVE);
        assertTrue(queue.peek() == EVENT_FIND_PLAYER);

        System.setIn(new ByteArrayInputStream("3,3\r\n".getBytes()));
        game.setIn(new Scanner(System.in)); // cannot mock Scanner because is final
        assertEquals(game.getBoard().getCurrentPlayer().getLetter(), "X");
        game.check(queue.poll()); // apply event to the current state
        assertTrue(game.getCurrentState() == STATE_NEXT_PLAYER);
        assertTrue(queue.peek() == EVENT_VALID_INPUT);
        game.check(queue.poll()); // apply event to the current state
        assertTrue(game.getCurrentState() == STATE_PLAYER_MOVE);
        assertTrue(queue.peek() == EVENT_FIND_PLAYER);

        System.setIn(new ByteArrayInputStream("2,1\r\n".getBytes()));
        game.setIn(new Scanner(System.in)); // cannot mock Scanner because is final
        assertEquals(game.getBoard().getCurrentPlayer().getLetter(), "O");
        game.check(queue.poll()); // apply event to the current state
        assertTrue(game.getCurrentState() == STATE_NEXT_PLAYER);
        assertTrue(queue.peek() == EVENT_VALID_INPUT);
        game.check(queue.poll()); // apply event to the current state
        assertTrue(game.getCurrentState() == STATE_PLAYER_MOVE);
        assertTrue(queue.peek() == EVENT_WINNER_FOUND);

        System.setIn(new ByteArrayInputStream("\r\n".getBytes()));
        game.setIn(new Scanner(System.in)); // cannot mock Scanner because is final
        game.check(queue.poll()); // apply event to the current state
        assertTrue(game.getCurrentState() == STATE_RESET_GAME);
        assertTrue(queue.peek() == EVENT_RESET_GAME);
        game.check(queue.poll()); // apply event to the current state
        assertTrue(game.getCurrentState() == STATE_LOAD_BOARD);
        assertTrue(queue.peek() == EVENT_BOARD_LOADED);
    }

    @Test
    public void checkGameExitWithInvalidProperties() {
        Queue<Events> queue = new LinkedList<>();
        System.setIn(new ByteArrayInputStream("0\r\n".getBytes()));
        Game game = new Game(new Scanner(System.in), queue, NO_CELLS);
        assertTrue(game.getCurrentState() == STATE_INITIALIZE);
        assertTrue(queue.peek() == EVENT_NEUTRAL);
        game.check(queue.poll()); // apply event to the current state
        assertTrue(game.getCurrentState() == STATE_LOAD_BOARD);
        assertTrue(queue.peek() == EVENT_EXIT_GAME);
        queue.poll(); // we discard this message
        queue.add(EVENT_NEUTRAL); // this cause to throw STATE_NEUTRAL
        game.check(queue.poll()); // apply event to the current state
        assertTrue(game.getCurrentState() == STATE_LOAD_BOARD);
        assertTrue(queue.size() == 0);
        queue.add(EVENT_EXIT_GAME);
        game.check(queue.poll()); // apply event to the current state
        assertTrue(game.getCurrentState() == STATE_GAME_OVER);
        assertTrue(queue.size() == 0);
    }

    @Test
    public void checkMovementsWithTwoRobot() {
        System.setIn(new ByteArrayInputStream("0\r\n".getBytes()));
        String[] props = {NO_HUMANS};
        Runner.main(props);
    }
}
