package org.testeditor.fixture.host;

public class Locator {

    private int row;
    private int col;

    public Locator(int col, int row) {
        this.setRow(row);
        this.setCol(col);
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

}
