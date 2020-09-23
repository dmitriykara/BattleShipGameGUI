package main.java.gameEngine;

import org.junit.jupiter.api.Test;

import static main.java.Main.SIZE;
import static org.junit.jupiter.api.Assertions.*;

class ShipTest {

    @Test
    void getLength() {
        assertEquals(1, new EmptySea().getLength());
        assertEquals(1, new Submarine().getLength());
        assertEquals(2, new Destroyer().getLength());
        assertEquals(3, new Cruiser().getLength());
        assertEquals(4, new Battleship().getLength());
    }

    @Test
    void testGettersAndSetters() {
        EmptySea emptySea = new EmptySea();
        Submarine submarine = new Submarine();
        Destroyer destroyer = new Destroyer();
        Cruiser cruiser = new Cruiser();
        Battleship battleship = new Battleship();

        assertEquals(0, emptySea.getBowRow());
        assertEquals(0, submarine.getBowRow());
        assertEquals(0, destroyer.getBowRow());
        assertEquals(0, cruiser.getBowRow());
        assertEquals(0, battleship.getBowRow());

        assertEquals(0, emptySea.getBowColumn());
        assertEquals(0, submarine.getBowColumn());
        assertEquals(0, destroyer.getBowColumn());
        assertEquals(0, cruiser.getBowColumn());
        assertEquals(0, battleship.getBowColumn());

        assertFalse(emptySea.isHorizontal());
        assertFalse(submarine.isHorizontal());
        assertFalse(destroyer.isHorizontal());
        assertFalse(cruiser.isHorizontal());
        assertFalse(battleship.isHorizontal());

        emptySea.setBowRow(1);
        submarine.setBowRow(3);
        destroyer.setBowRow(5);
        cruiser.setBowRow(7);
        battleship.setBowRow(9);

        emptySea.setBowColumn(0);
        submarine.setBowColumn(2);
        destroyer.setBowColumn(4);
        cruiser.setBowColumn(6);
        battleship.setBowColumn(8);

        emptySea.setHorizontal(true);
        destroyer.setHorizontal(true);
        battleship.setHorizontal(true);

        assertEquals(1, emptySea.getBowRow());
        assertEquals(3, submarine.getBowRow());
        assertEquals(5, destroyer.getBowRow());
        assertEquals(7, cruiser.getBowRow());
        assertEquals(9, battleship.getBowRow());

        assertEquals(0, emptySea.getBowColumn());
        assertEquals(2, submarine.getBowColumn());
        assertEquals(4, destroyer.getBowColumn());
        assertEquals(6, cruiser.getBowColumn());
        assertEquals(8, battleship.getBowColumn());

        assertTrue(emptySea.isHorizontal());
        assertFalse(submarine.isHorizontal());
        assertTrue(destroyer.isHorizontal());
        assertFalse(cruiser.isHorizontal());
        assertTrue(battleship.isHorizontal());
    }

    @Test
    void isEmptySea() {
        assertTrue(new EmptySea().isEmptySea());
        assertFalse(new Submarine().isEmptySea());
        assertFalse(new Destroyer().isEmptySea());
        assertFalse(new Cruiser().isEmptySea());
        assertFalse(new Battleship().isEmptySea());
    }

    @Test
    void alreadyHit() {
        Battleship battleship = new Battleship();
        battleship.shootAt(0, 0);
        assertTrue(battleship.alreadyHit(0, 0));
    }

    @Test
    void isHitTwice() {
        Battleship battleship = new Battleship();
        battleship.shootAt(0, 0);
        battleship.shootAt(1, 0);
        assertTrue(battleship.isHitTwice());
    }

    @Test
    void isSunk() {
        Battleship battleship = new Battleship();
        battleship.shootAt(0, 0);
        battleship.shootAt(1, 0);
        battleship.shootAt(2, 0);
        battleship.shootAt(3, 0);
        assertTrue(battleship.isSunk());
    }

    @Test
    void okToPlaceShipAt() {
        Ocean ocean = new Ocean();
        Battleship battleship = new Battleship();
        assertFalse(battleship.okToPlaceShipAt(9, 9, true, ocean));
        assertFalse(battleship.okToPlaceShipAt(9, 9, false, ocean));
        assertFalse(battleship.okToPlaceShipAt(9, 7, true, ocean));
        assertFalse(battleship.okToPlaceShipAt(7, 9, false, ocean));
        assertTrue(battleship.okToPlaceShipAt(9, 6, true, ocean));
        assertTrue(battleship.okToPlaceShipAt(6, 9, false, ocean));

        battleship.placeShipAt(4, 5, true, ocean);
        assertFalse(new Cruiser().okToPlaceShipAt(5, 9, false, ocean));
    }

    @Test
    void okToPlaceBlockAt() {
        Ocean ocean = new Ocean();
        Battleship battleship = new Battleship();

        boolean ok = true;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                ok &= battleship.okToPlaceBlockAt(i, j, ocean);
            }
        }

        assertTrue(ok);
    }

    @Test
    void placeShipAt() {
        Ocean ocean = new Ocean();
        Battleship battleship = new Battleship();
        battleship.placeShipAt(4, 5, true, ocean);
        assertEquals(4, battleship.getBowRow());
        assertEquals(5, battleship.getBowColumn());
        assertTrue(battleship.isHorizontal());
        assertEquals(battleship, ocean.getShip(4, 8));
    }

    @Test
    void shootAt() {
        Battleship battleship = new Battleship();
        battleship.shootAt(0, 0);
        assertTrue(battleship.hit[0]);
    }
}