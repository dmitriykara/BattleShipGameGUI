package main.java.gameEngine;

import java.io.Serializable;
import java.util.Random;

import static main.java.Main.SIZE;

/**
 * Ocean - class that contains game field and works with Ship classes
 *
 * @author Kara Dmitry
 */
public class Ocean implements Serializable {
    public static final int BATTLESHIP_COUNT = 1;
    public static final int CRUISER_COUNT = 2;
    public static final int DESTROYER_COUNT = 3;
    public static final int SUBMARINE_COUNT = 4;
    private static final Random random = new Random();
    public Ship[][] ships = new Ship[SIZE][SIZE];
    public int shotsFired;
    public int hitCount;
    public int shipsSunk;

    /**
     * Boolean table which sells are already hit
     */
    boolean[][] visible = new boolean[SIZE][SIZE];

    Ocean() {
        shotsFired = 0;
        hitCount = 0;
        shipsSunk = 0;

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                ships[i][j] = new EmptySea();
            }
        }

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                visible[i][j] = false;
            }
        }
    }

    public static Ocean createGame() {
        Ocean ocean = new Ocean();
        return ocean;
    }

    /**
     * Place one ship on the Ocean table
     */
    private void placeOneShipRandomly(Ship ship) {
        int row, column;
        boolean horizontal;

        while (true) {
            row = random.nextInt(9);
            column = random.nextInt(9);
            horizontal = random.nextBoolean();

            if (ship.okToPlaceShipAt(row, column, horizontal, this)) {
                ship.placeShipAt(row, column, horizontal, this);
                return;
            }
        }
    }

    /**
     * Fill the Ocean table with ships
     */
    public void placeAllShipsRandomly() {
        for (int i = 0; i < BATTLESHIP_COUNT; i++) {
            placeOneShipRandomly(new Battleship());
        }
        for (int i = 0; i < CRUISER_COUNT; i++) {
            placeOneShipRandomly(new Cruiser());
        }
        for (int i = 0; i < DESTROYER_COUNT; i++) {
            placeOneShipRandomly(new Destroyer());
        }
        for (int i = 0; i < SUBMARINE_COUNT; i++) {
            placeOneShipRandomly(new Submarine());
        }
    }

    public boolean isSunk(int row, int column) {
        return ships[row][column].isSunk();
    }

    public boolean isVisible(int row, int column) {
        return visible[row][column];
    }

    /**
     * @param row    row of Ocean table
     * @param column column of Ocean table
     * @return -1 if cell was empty
     * 0 if shoot twice
     * 1 if shoot was successful
     */
    public int shootAt(int row, int column) {
        if (visible[row][column]) {
            ++shotsFired;
            return 0;
        }

        visible[row][column] = true;
        if (!ships[row][column].shootAt(row, column)) {
            ++shotsFired;
            return -1;
        }
        ++shotsFired;
        if (!ships[row][column].isEmptySea())
            ++hitCount;
        if (ships[row][column].isSunk())
            ++shipsSunk;
        return 1;
    }

    public Ship getShip(int x, int y) {
        return ships[x][y];
    }

    public int getShotsFired() {
        return shotsFired;
    }

    public int getHitCount() {
        return hitCount;
    }

    public int getShipsSunk() {
        return shipsSunk;
    }

    public boolean isGameOver() {
        return shipsSunk == BATTLESHIP_COUNT + CRUISER_COUNT + DESTROYER_COUNT + SUBMARINE_COUNT;
    }

    public Ship[][] getShipArray() {
        return ships;
    }
}
