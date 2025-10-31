package game.organisms.animals;

import game.World;
import game.organisms.Animal;
import game.organisms.Organism;

import java.awt.Color;

public class Fox extends Animal {
    public Fox(World world, int x, int y) {
        super(world, x, y);
        setSila(3);
        setInicjatywa(9);
        setColor(Color.ORANGE);
    }

    @Override
    public void kolizja(Organism other) {
        if (other == null) {
            return;
        }

        if (this.getClass() == other.getClass()) {
            rozmnazanie(other);
            return;
        }

        if (other.czyOdbilAtak(this)) {
            return;
        }

        int myStrength = getSila();
        int otherStrength = other.getSila();

        if (myStrength >= otherStrength) {
            other.zjedz();
            getSwiat().setOrganism(getX(), getY(), null);
            getSwiat().setOrganism(other.getX(), other.getY(), this);
            setX(other.getX());
            setY(other.getY());
            String event = getClass().getSimpleName() + " pokonal " + other.getClass().getSimpleName() + " i zajal pole (" + other.getX() + "," + other.getY() + ")";
            getSwiat().addLog(event);
        } else {
            String event = getClass().getSimpleName() + "(" + getX() + ", " + getY() + ")" + " wykorzystal dobry wech";
            getSwiat().addLog(event);
            return;
        }
    }

}