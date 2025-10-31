package game.organisms.animals;

import game.World;
import game.organisms.Animal;

import java.awt.Color;

public class Sheep extends Animal {
    public Sheep(World world, int x, int y) {
        super(world, x, y);
        setSila(4);
        setInicjatywa(4);
        setColor(Color.GRAY);
    }
}