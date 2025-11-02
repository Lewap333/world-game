package game.organisms.plants;

import game.World;
import game.organisms.Organism;
import game.organisms.Plant;

import java.awt.Color;

public class Guarani extends Plant {
    public Guarani(World world, int x, int y) {
        super(world, x, y);
        setStr(0);
        setColor(Color.CYAN);
    }

    @Override
    public void collision(Organism other) {
        String event = getClass().getSimpleName() + "(" + getX() + "," + getY() + ") zjada " + getClass().getSimpleName();
        getWorld().addLog(event);
        other.setStr(other.getStr() * 3);
    }
}