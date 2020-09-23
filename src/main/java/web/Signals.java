package main.java.web;

import static main.java.Main.ROLE;

public class Signals {
    public static void shoot(int row, int column) {
        if (ROLE.equals("SERVER")) {
            Server.write(String.format("shoot %d %d", row, column));
        } else {
            Client.write(String.format("shoot %d %d", row, column));
        }
    }

    public static void leave() {
        if (ROLE.equals("SERVER")) {
            Server.write("leave");
        } else {
            Client.write("leave");
        }
    }
}
