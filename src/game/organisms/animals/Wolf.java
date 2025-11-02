package game.organisms.animals;

import game.World;
import game.organisms.Animal;

import java.awt.Color;

public class Wolf extends Animal {
    public Wolf(World world, int x, int y) {
        super(world, x, y);
        setStr(9);
        setInitiative(7);
        setColor(Color.DARK_GRAY);
    }
}