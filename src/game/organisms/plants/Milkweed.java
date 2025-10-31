package game.organisms.plants;

import game.World;
import game.organisms.Plant;

import java.awt.Color;

public class Milkweed extends Plant {
    public Milkweed(World world, int x, int y) {
        super(world, x, y);
        setSila(0);
        setColor(Color.YELLOW);
    }

    public void akcja() {
        for (int i = 0; i < 3; i++) {
            super.akcja();
        }
    }
}