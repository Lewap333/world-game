package game.organisms;

import game.World;

import java.awt.Color;

public abstract class Organism {
    protected Color color;
    protected int str;
    protected int initiative;
    protected int age;
    protected int x, y;
    protected boolean isAlive;
    protected World world;
    protected int cooldown;
    protected int abilityDuration;

    protected void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public World getWorld() {
        return world;
    }

    public void eat() {
        // Ten organizm ma byc usuniety
        getWorld().setOrganism(getX(), getY(), null);
        setAlive(false);
    }

    public boolean didParryAttack(Organism other) {
        return false;
    }

    public abstract void action();

    public abstract void collision(Organism other);

    public Organism(World world, int x, int y) {
        this.x = x;
        this.y = y;
        this.age = 0;
        this.initiative = 0;
        this.str = 0;
        this.abilityDuration = 0;
        this.cooldown = 0;
        this.isAlive = true;
        this.world = world;
    }

    public int getStr() {
        return str;
    }

    public int getInitiative() {
        return initiative;
    }

    public void setStr(int str) {
        this.str = str;
    }

    public void setInitiative(int initiative) {
        this.initiative = initiative;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean getAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        this.isAlive = alive;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getAbility() {
        return abilityDuration;
    }

    public void setAbility(int ability) {
        this.abilityDuration = ability;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

}
