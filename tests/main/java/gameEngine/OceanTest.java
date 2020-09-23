package main.java.gameEngine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static main.java.Main.SIZE;

import static org.junit.jupiter.api.Assertions.*;

class OceanTest {
    Ocean ocean;

    @BeforeEach
    void setUp() {
        ocean = new Ocean();
    }

    @Test
    void createGame() {
        ocean = Ocean.createGame();
        assertNotEquals(ocean, null);
    }

    @Test
    void placeAllShipsRandomly() {
        int counter = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (ocean.getShip(i, j).isEmptySea())
                    ++counter;
            }
        }
        assertEquals(100, counter);

        ocean.placeAllShipsRandomly();

        counter = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (ocean.getShip(i, j).isEmptySea())
                    ++counter;
            }
        }
        assertEquals(80, counter);

        int battleship = 0;
        int cruiser = 0;
        int destroyer = 0;
        int submarine = 0;

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (!ocean.getShip(i,j).isEmptySea())
                    switch (ocean.getShip(i, j).getLength()) {
                        case 1:
                            ++submarine;
                            break;
                        case 2:
                            ++destroyer;
                            break;
                        case 3:
                            ++cruiser;
                            break;
                        case 4:
                            ++battleship;
                            break;
                    }
            }
        }

        assertEquals(4 * 1, battleship); // length of ship type * amount of ship with that type
        assertEquals(3 * 2, cruiser);
        assertEquals(2 * 3, destroyer);
        assertEquals(1 * 4, submarine);
    }

    @Test
    void isSunk() {
        int counter = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (ocean.isSunk(i, j))
                    ++counter;
            }
        }
        assertEquals(0, counter);

        ocean.placeAllShipsRandomly();

        counter = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (ocean.isSunk(i, j))
                    ++counter;
            }
        }
        assertEquals(0, counter);

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                ocean.shootAt(i, j);
            }
        }
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (ocean.isSunk(i, j))
                    ++counter;
            }
        }
        assertEquals(20, counter);
    }

    @Test
    void isVisible() {
        ocean.placeAllShipsRandomly();
        boolean ok = true;
        for (int i = 0; i < SIZE; i++) {
            ocean.shootAt(i, i);
        }
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (i == j)
                    ok &= ocean.isVisible(i, j);
                else
                    ok &= !ocean.isVisible(i, j);
            }
        }
        assertTrue(ok);
    }

    @Test
    void shootAt() {
        Destroyer destroyer = new Destroyer();
        destroyer.placeShipAt(3, 4, true, ocean);
        assertEquals(1, ocean.shootAt(3, 4));
        assertEquals(0, ocean.shootAt(3, 4));
        assertEquals(1, ocean.shootAt(3, 5));
        assertEquals(0, ocean.shootAt(3, 5));
        assertEquals(-1, ocean.shootAt(3, 6));
    }

    @Test
    void getShip() {
        Destroyer destroyer = new Destroyer();
        destroyer.placeShipAt(3, 4, true, ocean);
        assertEquals(destroyer, ocean.getShip(3, 4));
        assertEquals(destroyer, ocean.getShip(3, 5));
        assertTrue(ocean.getShip(3, 6).isEmptySea());
    }

    @Test
    void getShotsFired() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                ocean.shootAt(i, j);
            }
        }
        assertEquals(100, ocean.getShotsFired());
    }

    @Test
    void getHitCount() {
        ocean = new Ocean();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                ocean.shootAt(i, j);
            }
        }
        assertEquals(0, ocean.getHitCount());

        ocean.placeAllShipsRandomly();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                ocean.visible[i][j] = false;
            }
        }

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                ocean.shootAt(i, j);
            }
        }
        assertEquals(20, ocean.getHitCount());
    }

    @Test
    void getShipsSunk() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                ocean.shootAt(i, j);
            }
        }
        assertEquals(0, ocean.getShipsSunk());

        ocean.placeAllShipsRandomly();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                ocean.visible[i][j] = false;
            }
        }

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                ocean.shootAt(i, j);
            }
        }
        assertEquals(SIZE, ocean.getShipsSunk());
    }

    @Test
    void isGameOver() {
        ocean.placeAllShipsRandomly();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                ocean.shootAt(i, j);
            }
        }
        assertTrue(ocean.isGameOver());
    }

    @Test
    void getShipArray() {
        Ship[][] ships = ocean.getShipArray();
        int counter = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (ships[i][j].isEmptySea())
                    ++counter;
            }
        }
        assertEquals(100, counter);

        ocean.placeAllShipsRandomly();

        ships = ocean.getShipArray();
        counter = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (ships[i][j].isEmptySea())
                    ++counter;
            }
        }
        assertEquals(80, counter);
    }
}