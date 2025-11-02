package game.organisms.plants;

import game.World;
import game.organisms.Plant;

import java.awt.Color;

public class Grass extends Plant {
    public Grass(World world, int x, int y) {
        super(world, x, y);
        setStr(0);
        setColor(Color.GREEN);
    }
}
