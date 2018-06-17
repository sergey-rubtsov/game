package tic.tac.toe.model;

public class Cell {

    private char status;

    private int x;

    private int y;

    public Cell(char status, int x, int y) {
        super();
        this.status = status;
        this.x = x;
        this.y = y;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "[" + x + ":" + y + ":" + status + "]";
    }

    public boolean isShip() {
        return status == 's' || status == 'x';
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cell)) {
            return false;
        }

        Cell cell = (Cell) o;

        if (getStatus() != cell.getStatus()) {
            return false;
        }
        if (getX() != cell.getX()) {
            return false;
        }
        return getY() == cell.getY();
    }

    public int hashCode() {
        int result = (int) getStatus();
        result = 31 * result + getX();
        result = 31 * result + getY();
        return result;
    }
}