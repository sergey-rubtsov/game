package tic.tac.toe.server;

import org.junit.Test;
import tic.tac.toe.server.model.Cell;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class UtilsTest {

    @Test
    public void translate() {
        char[] chars = {'X', 'Y', 'Z', 'O'};
        char[][] result = Utils.translate(2, chars);
        assertEquals('X', result[0][0]);
        assertEquals('Y', result[0][1]);
        assertEquals('Z', result[1][0]);
        assertEquals('O', result[1][1]);
    }

    @Test
    public void findRow() {
        char[][] t0 = {
                {'X', 'X', 'X', 'O'},
                {' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' '},
        };
        List<Cell> found = Utils.findRow(t0, 0, 0, 'X', 3);
        assertEquals(3, found.size());
        char[][] t1 = {
                {' ', ' ', ' ', ' '},
                {' ', ' ', 'X', ' '},
                {' ', ' ', 'X', ' '},
                {' ', ' ', 'X', ' '},
        };
        found = Utils.findRow(t1, 2, 2, 'X', 3);
        assertEquals(3, found.size());
        char[][] t2 = {
                {' ', ' ', ' ', 'O'},
                {' ', ' ', 'X', ' '},
                {' ', 'X', ' ', ' '},
                {'X', ' ', ' ', ' '},
        };
        found = Utils.findRow(t2, 1, 2, 'X', 3);
        assertEquals(3, found.size());
        char[][] t3 = {
                {'X', 'X', 'O', 'O'},
                {' ', 'X', ' ', ' '},
                {' ', ' ', 'X', ' '},
                {' ', ' ', ' ', ' '},
        };
        found = Utils.findRow(t3, 1, 1, 'X', 3);
        assertEquals(3, found.size());
        char[][] t4 = {
                {'X', 'X', 'X'},
                {' ', 'O', ' '},
                {' ', ' ', 'X'},
        };
        found = Utils.findRow(t4, 0, 1, 'X', 3);
        assertEquals(3, found.size());
    }
}