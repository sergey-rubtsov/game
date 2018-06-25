package tic.tac.toe.server;

import tic.tac.toe.server.model.Cell;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static final Cell[][] LINES = {
            {new Cell(-2, -2), new Cell(-1, -1), new Cell(0, 0), new Cell(1, 1), new Cell(2, 2)},
            {new Cell(0, -2), new Cell(0, -1), new Cell(0, 0), new Cell(0, 1), new Cell(0, 2)},
            {new Cell(-2, 0), new Cell(-1, 0), new Cell(0, 0), new Cell(1, 0), new Cell(2, 0)},
            {new Cell(-2, 2), new Cell(-1, 1), new Cell(0, 0), new Cell(1, -1), new Cell(2, -2)}
    };

    /**
     * @param size the dimension of game board
     * @param field one-dimension array of marks stored in db
     *
     * @return 2-d array of marks
     */
    public static char[][] translate(int size, char[] field) {
        char[][] board = new char[size][size];
        for (int i = 0; i < field.length; i++) {
            board[i / size][i % size] = field[i];
        }
        return board;
    }


    /**
     * @param size the dimension of game board
     * @param cellIndex the number of char in one-dimension array of marks stored in db
     *
     * @return 2-d coordinate
     */
    public static Cell translate(int size, int cellIndex) {
        int x = cellIndex / size;
        int y = cellIndex % size;
        return new Cell(x, y);
    }

    /**
     * @param size the dimension of game board
     * @param x coordinate
     * @param y coordinate
     *
     * @return translates coordinate from 2-d array to one-dimension array index
     */
    public static int translate(int size, int x, int y) {
        return y + size * x;
    }

    /**
     * @param board 2-d array of marks
     * @param x coordinate
     * @param y coordinate
     * @param mark required character
     * @param length required length of continuous characters
     *
     * @return list of found cells
     */
    public static List<Cell> findRow(char[][] board, int x, int y, Character mark, int length) {
        List<Cell> found = new ArrayList<>();
        Cell[][] matrix = Utils.LINES;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                int checkX = x + matrix[i][j].getX();
                if (checkX > (board.length - 1) || checkX < 0) {
                    found.clear();
                    continue;
                }
                int checkY = y + matrix[i][j].getY();
                if (checkY > (board.length - 1) || checkY < 0) {
                    found.clear();
                    continue;
                }
                if (mark == board[checkX][checkY]) {
                    found.add(new Cell(checkX, checkY));
                } else {
                    found.clear();
                }
                if (found.size() == length) {
                    return found;
                }
            }
            found.clear();
        }
        return found;
    }

}
