package com.metronom.ttt.app.config;

import static org.junit.Assert.assertEquals;

import com.metronom.ttt.app.exceptions.GameException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class GamePropertiesTest {
    private static final String NONE_FILE = "none_file";
    private static final String HAPPY_PATH = "happy_path.properties";
    private static final String NO_CELLS = "no_cells.properties";
    private static final String NO_HUMANS = "no_humans.properties";
    private static final String NO_ROBOTS = "no_robots.properties";
    private static final String NO_PLAYERS_AT_ALL = "no_players_at_all.properties";
    private static final String INVALID_NUMCELLS = "invalid_numcells.properties";
    private static final String INVALID_HUMANS_LENGTH = "invalid_humans_length.properties";
    private static final String INVALID_HUMANS_NUMBER = "invalid_humans_number.properties";
    private static final String INVALID_ROBOTS_LENGTH = "invalid_robots_length.properties";
    private static final String INVALID_ROBOTS_NUMBER = "invalid_robots_number.properties";
    private static final String REPEATED_LETTERS = "repeated_letters.properties";

    GameProperties props;

    @Before
    public void setUp() {
        props = new GameProperties();
    }

    @Test
    public void checkNormalParametersForHappyPath() throws IOException, GameException {
        props.getPropValues(HAPPY_PATH);
        assertEquals(props.getNumcells(), 3);
        assertEquals(props.getHumanPlayers().size(), 2);
        assertEquals(props.getRobotPlayers().size(), 1);
    }

    @Test(expected = IOException.class)
    public void checkNoneFileException() throws IOException, GameException {
        props.getPropValues(NONE_FILE);
    }

    @Test(expected = GameException.class)
    public void checkNoCellsSpecifiedException() throws IOException, GameException {
        props.getPropValues(NO_CELLS);
    }

    @Test
    public void checkNoHumansSpecifiedException() throws IOException, GameException {
        props.getPropValues(NO_HUMANS);
        assertEquals(props.getNumcells(), 3);
        assertEquals(props.getHumanPlayers().size(), 0);
        assertEquals(props.getRobotPlayers().size(), 2);
    }

    @Test
    public void checkNoRobotsSpecifiedException() throws IOException, GameException {
        props.getPropValues(NO_ROBOTS);
        assertEquals(props.getNumcells(), 3);
        assertEquals(props.getHumanPlayers().size(), 2);
        assertEquals(props.getRobotPlayers().size(), 0);
    }

    @Test(expected = GameException.class)
    public void checkNoPlayersAtAllException() throws IOException, GameException {
        props.getPropValues(NO_PLAYERS_AT_ALL);
    }

    @Test(expected = GameException.class)
    public void checkInvalidNumCellsException() throws IOException, GameException {
        props.getPropValues(INVALID_NUMCELLS);
    }

    @Test(expected = GameException.class)
    public void checkInvalidHumansNameLengthException() throws IOException, GameException {
        props.getPropValues(INVALID_HUMANS_LENGTH);
    }

    @Test(expected = GameException.class)
    public void checkInvalidHumansNameNumberException() throws IOException, GameException {
        props.getPropValues(INVALID_HUMANS_NUMBER);
    }

    @Test(expected = GameException.class)
    public void checkInvalidRobotsNameLengthException() throws IOException, GameException {
        props.getPropValues(INVALID_ROBOTS_LENGTH);
    }

    @Test(expected = GameException.class)
    public void checkInvalidRobotsNameNumberException() throws IOException, GameException {
        props.getPropValues(INVALID_ROBOTS_NUMBER);
    }

    @Test(expected = GameException.class)
    public void checkRepeatedLettersException() throws IOException, GameException {
        props.getPropValues(REPEATED_LETTERS);
    }
}
