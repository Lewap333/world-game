package game.organisms.plants;

import game.World;
import game.organisms.Organism;
import game.organisms.Plant;

import java.awt.Color;

public class Berries extends Plant {
    public Berries(World world, int x, int y) {
        super(world, x, y);
        setStr(99);
        setColor(Color.BLUE);
    }
    @Override
    public void collision(Organism other) {
        String event = other.getClass().getSimpleName() + "(" + getX() + "," + getY() + ") zjada " + getClass().getSimpleName();
        getWorld().addLog(event);
        other.setAlive(false);
    }
}
