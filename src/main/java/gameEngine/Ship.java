package main.java.gameEngine;

import java.io.Serializable;

/**
 * Ship class
 *
 * @author Kara Dmitry
 */
public abstract class Ship implements Serializable {
    protected int bowRow;
    protected int bowColumn;
    protected int length;

    /**
     * Counts the number of successful hits
     */
    protected int hitsCounter = 0;

    protected boolean horizontal;
    protected boolean[] hit = new boolean[4];

    public abstract int getLength();

    public int getBowRow() {
        return bowRow;
    }

    public void setBowRow(int row) {
        bowRow = row;
    }

    public int getBowColumn() {
        return bowColumn;
    }

    public void setBowColumn(int column) {
        bowColumn = column;
    }

    public boolean isHorizontal() {
        return horizontal;
    }

    public void setHorizontal(boolean horizontal) {
        this.horizontal = horizontal;
    }

    /**
     * @return is ship type is EmptySea
     */
    public boolean isEmptySea() {
        return false;
    }

    /**
     * @param row    row of Ocean table
     * @param column column of Ocean table
     * @return if this part of hip already hit
     */
    public boolean alreadyHit(int row, int column) {
        if (horizontal) {
            return hit[column - bowColumn];
        }
        return hit[row - bowRow];
    }

    /**
     * @return if the ship was hit more than 1 time
     */
    public boolean isHitTwice() {
        return hitsCounter > 1;
    }

    /**
     * @return is ship was sunk
     */
    public boolean isSunk() {
        return hitsCounter == length;
    }

    /**
     * @param row        row position to place ship
     * @param column     column position to place ship
     * @param horizontal is position horizontal
     * @param ocean      Ocean object
     * @return if available to put whole ship to this position
     */
    public boolean okToPlaceShipAt(int row, int column, boolean horizontal, Ocean ocean) {
        if (horizontal) {
            for (int i = 0; i < length; i++) {
                if (!okToPlaceBlockAt(row, column + i, ocean))
                    return false;
            }
            return true;
        }
        for (int i = 0; i < length; i++) {
            if (!okToPlaceBlockAt(row + i, column, ocean))
                return false;
        }
        return true;
    }

    /**
     * @param row    row position to place ship
     * @param column column position to place ship
     * @param ocean  Ocean object
     * @return if available to put only block of ship to this position
     */
    protected boolean okToPlaceBlockAt(int row, int column, Ocean ocean) {
        if (row > 9 || row < 0 || column > 9 || column < 0) {
            return false;
        }
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                try {
                    if (!ocean.getShipArray()[row + i][column + j].isEmptySea())
                        return false;
                } catch (IndexOutOfBoundsException skip) {
                }
            }
        }
        return true;
    }

    /**
     * @param row        row position to place ship
     * @param column     column position to place ship
     * @param horizontal is position horizontal
     * @param ocean      Ocean object
     */
    public void placeShipAt(int row, int column, boolean horizontal, Ocean ocean) {
        setBowRow(row);
        setBowColumn(column);
        setHorizontal(horizontal);
        if (horizontal) {
            for (int i = 0; i < length; i++) {
                ocean.getShipArray()[row][column + i] = this;
            }
            return;
        }
        for (int i = 0; i < length; i++) {
            ocean.getShipArray()[row + i][column] = this;
        }
    }

    /**
     * @param row    row of Ocean table
     * @param column column of Ocean table
     * @return if there is a hit
     */
    public boolean shootAt(int row, int column) {
        if (isSunk() || isEmptySea()) return false;

        if (!alreadyHit(row, column)) {
            if (horizontal) {
                hit[column - bowColumn] = true;
            } else {
                hit[row - bowRow] = true;
            }
            ++hitsCounter;
        }
        return true;
    }
}
