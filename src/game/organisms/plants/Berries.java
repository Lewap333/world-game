package game.organisms.plants;

import game.World;
import game.organisms.Organism;
import game.organisms.Plant;

import java.awt.Color;

public class Berries extends Plant {
    public Berries(World world, int x, int y) {
        super(world, x, y);
        setSila(99);
        setColor(Color.BLUE);
    }
    @Override
    public void kolizja(Organism other) {
        String event = other.getClass().getSimpleName() + "(" + getX() + "," + getY() + ") zjada " + getClass().getSimpleName();
        getSwiat().addLog(event);
        other.setCzyZyje(false);
    }
}
