package game.organisms.plants;

import game.World;
import game.organisms.Organism;
import game.organisms.Plant;

import java.awt.Color;

public class Guarani extends Plant {
    public Guarani(World world, int x, int y) {
        super(world, x, y);
        setSila(0);
        setColor(Color.CYAN);
    }

    @Override
    public void kolizja(Organism other) {
        String event = getClass().getSimpleName() + "(" + getX() + "," + getY() + ") zjada " + getClass().getSimpleName();
        getSwiat().addLog(event);
        other.setSila(other.getSila() * 3);
    }
}