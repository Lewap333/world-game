package game.organisms.animals;

import game.World;
import game.organisms.Animal;
import game.organisms.Organism;

import java.awt.Color;

public class Fox extends Animal {
    public Fox(World world, int x, int y) {
        super(world, x, y);
        setStr(3);
        setInitiative(9);
        setColor(Color.ORANGE);
    }

    @Override
    public void collision(Organism other) {
        if (other == null) {
            return;
        }

        if (this.getClass() == other.getClass()) {
            rozmnazanie(other);
            return;
        }

        if (other.didParryAttack(this)) {
            return;
        }

        int myStrength = getStr();
        int otherStrength = other.getStr();

        if (myStrength >= otherStrength) {
            other.eat();
            getWorld().setOrganism(getX(), getY(), null);
            getWorld().setOrganism(other.getX(), other.getY(), this);
            setX(other.getX());
            setY(other.getY());
            String event = getClass().getSimpleName() + " pokonal " + other.getClass().getSimpleName() + " i zajal pole (" + other.getX() + "," + other.getY() + ")";
            getWorld().addLog(event);
        } else {
            String event = getClass().getSimpleName() + "(" + getX() + ", " + getY() + ")" + " wykorzystal dobry wech";
            getWorld().addLog(event);
            return;
        }
    }

}