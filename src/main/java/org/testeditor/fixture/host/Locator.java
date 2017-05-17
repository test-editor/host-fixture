package org.testeditor.fixture.host;

public class Locator {

  private int row;
  private int col;

  /**
   *
   * @param rol
   * @param row
   */
  public Locator(int col, int row) {

    this.setRow(row);
    this.setCol(col);
  }

  /**
   * @return the col
   */
  public int getCol() {
    return col;
  }

  /**
   * @param col
   *          the col to set
   */
  public void setCol(int col) {
    this.col = col;
  }

  /**
   * @return the row
   */
  public int getRow() {
    return row;
  }

  /**
   * @param row
   *          the row to set
   */
  public void setRow(int row) {
    this.row = row;
  }

}
