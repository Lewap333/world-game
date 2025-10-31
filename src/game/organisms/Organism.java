package game.organisms;

import game.World;

import java.awt.Color;

public abstract class Organism {
    protected Color color;
    protected int sila;
    protected int inicjatywa;
    protected int wiek;
    protected int x, y;
    protected boolean czyZyje;
    protected World world;
    protected int cooldown;
    protected int abilityDuration;

    protected void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public World getSwiat() {
        return world;
    }

    public void zjedz() {
        // Ten organizm ma byc usuniety
        getSwiat().setOrganism(getX(), getY(), null);
        setCzyZyje(false);
    }

    public boolean czyOdbilAtak(Organism other) {
        return false;
    }

    public abstract void akcja();

    public abstract void kolizja(Organism other);

    public Organism(World world, int x, int y) {
        this.x = x;
        this.y = y;
        this.wiek = 0;
        this.inicjatywa = 0;
        this.sila = 0;
        this.abilityDuration = 0;
        this.cooldown = 0;
        this.czyZyje = true;
        this.world = world;
    }

    public int getSila() {
        return sila;
    }

    public int getInicjatywa() {
        return inicjatywa;
    }

    public void setSila(int sila) {
        this.sila = sila;
    }

    public void setInicjatywa(int inicjatywa) {
        this.inicjatywa = inicjatywa;
    }

    public int getWiek() {
        return wiek;
    }

    public void setWiek(int wiek) {
        this.wiek = wiek;
    }

    public boolean getCzyZyje() {
        return czyZyje;
    }

    public void setCzyZyje(boolean czyZyje) {
        this.czyZyje = czyZyje;
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
