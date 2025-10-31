package game.organisms.animals;

import game.World;
import game.organisms.Animal;
import game.organisms.Organism;

import java.awt.Color;

public class Turtle extends Animal {

    public Turtle(World world, int x, int y) {
        super(world, x, y);
        setSila(2);
        setInicjatywa(1);
        Color darkGreen = new Color(0, 100, 0);
        setColor(darkGreen);
    }

    @Override
    public void akcja() {
        int moveChance = (int) (Math.random() * 4); // szansa na wykonanie ruchu: 0 - 25%

        if (moveChance == 1) {
            super.akcja();
        } else {
            String event = getClass().getSimpleName() + "(" + getX() + "," + getY() + ")" + " nie ruszyl sie";
            getSwiat().addLog(event);
        }
    }

    @Override
    public boolean czyOdbilAtak(Organism other) {
        if (other.getSila() < 5) {  // odbicie ataku zwierząt o sile < 5
            String event = getClass().getSimpleName() + "(" + getX() + "," + getY() + ")" + " odbil atak " + other.getClass().getSimpleName();
            getSwiat().addLog(event);
            return true;
        } else {
            return false;
        }
    }


}