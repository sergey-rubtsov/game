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

    public static char[][] translate(int size, char[] field) {
        char[][] board = new char[size][size];
        for (int i = 0; i < field.length; i++) {
            board[i / size][i % size] = field[i];
        }
        return board;
    }

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
