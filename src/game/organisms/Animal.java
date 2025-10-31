package game.organisms;

import game.*;
import game.organisms.animals.*;

public abstract class Animal extends Organism {
    @Override
    public void akcja() {
        int direction = (int) (Math.random() * 4); // wylosuj kierunek: 0 - góra, 1 - dół, 2 - lewo, 3 - prawo
        int newX = getX();
        int newY = getY();

        switch (direction) {
            case 0:
                newY--;
                break;
            case 1:
                newY++;
                break;
            case 2:
                newX--;
                break;
            case 3:
                newX++;
                break;
        }

        // upewnij się, że nowe pozycje są w granicach planszy
        if (newX >= 0 && newX < getSwiat().getWidth() && newY >= 0 && newY < getSwiat().getHeight()) {
            // jeśli pole jest wolne, przemieść organizm na nową pozycję
            if (getSwiat().getOrganism(newX, newY) == null) {
                getSwiat().setOrganism(getX(), getY(), null);
                getSwiat().setOrganism(newX, newY, this);
                setX(newX);
                setY(newY);
                String event = getClass().getSimpleName() + " ruszyl sie na pole (" + newX + ", " + newY + ")";
                getSwiat().addLog(event);
            }
            // w przeciwnym razie wykonaj kolizję
            else {
                kolizja(getSwiat().getOrganism(newX, newY));
            }
        }
    }
    @Override
    public void kolizja(Organism other) {
        if (other == null) {
            return;
        }

        // Sprawdzenie czy zwierzęta są tego samego gatunku
        if (this.getClass() == other.getClass()) {
            // Spróbuj się rozmnożyć
            rozmnazanie(other);
            return;
        }

        if (other.czyOdbilAtak(this)) {
            return;
        }

        int myStrength = getSila();
        int otherStrength = other.getSila();

        if (other instanceof Plant) {
            // Zwierze zjadło roślinę
            other.kolizja(this);
        }

        // Sprawdzenie, który organizm wygra walkę
        if (myStrength >= otherStrength) {
            // Zjedz organizm broniący pole jeśli jesteś silniejszy
            other.zjedz();
            getSwiat().setOrganism(getX(), getY(), null);
            getSwiat().setOrganism(other.getX(), other.getY(), this);
            if (!this.getCzyZyje()) {
                getSwiat().setOrganism(other.getX(), other.getY(), null);
            }
            setX(other.getX());
            setY(other.getY());
            String event = getClass().getSimpleName() + " pokonal " + other.getClass().getSimpleName() + " i zajal pole (" + other.getX() + "," + other.getY() + ")";
            getSwiat().addLog(event);
        } else {
            // Jeśli organizm broniący pola jest silniejszy, zostań zjedzony
            String event = getClass().getSimpleName() + " probowal pokonac " + other.getClass().getSimpleName() + "(" + other.getX() + "," + other.getY() + ") i zgnial";
            getSwiat().addLog(event);
            zjedz();
        }
    }

    public void rozmnazanie(Organism other) {
        // szukanie wolnego pola wokół this
        if (getSwiat().getOrganism(getX() - 1, getY()) == null) {
            stworzNowe(getX() - 1, getY());
        } else if (getSwiat().getOrganism(getX() + 1, getY()) == null) {
            stworzNowe(getX() + 1, getY());
        } else if (getSwiat().getOrganism(getX(), getY() - 1) == null) {
            stworzNowe(getX(), getY() - 1);
        } else if (getSwiat().getOrganism(getX(), getY() + 1) == null) {
            stworzNowe(getX(), getY() + 1);
        }
        // szukanie wolnego pola wokół other
        else if (getSwiat().getOrganism(other.getX() - 1, other.getY()) == null) {
            stworzNowe(other.getX() - 1, other.getY());
        } else if (getSwiat().getOrganism(other.getX() + 1, other.getY()) == null) {
            stworzNowe(other.getX() + 1, other.getY());
        } else if (getSwiat().getOrganism(other.getX(), other.getY() - 1) == null) {
            stworzNowe(other.getX(), other.getY() - 1);
        } else if (getSwiat().getOrganism(other.getX(), other.getY() + 1) == null) {
            stworzNowe(other.getX(), other.getY() + 1);
        }
    }


    public void stworzNowe(int x, int y) {
        if (x < 0 || y < 0) {
            return;
        }
        Organism nowyOrganism = null;

        if (this instanceof Wolf) {
            nowyOrganism = new Wolf(getSwiat(), x, y);
        } else if (this instanceof Sheep) {
            nowyOrganism = new Sheep(getSwiat(), x, y);
        } else if (this instanceof Fox) {
            nowyOrganism = new Fox(getSwiat(), x, y);
        } else if (this instanceof Turtle) {
            nowyOrganism = new Turtle(getSwiat(), x, y);
        } else if (this instanceof Antelope) {
            nowyOrganism = new Antelope(getSwiat(), x, y);
        }

        getSwiat().setOrganism(x, y, nowyOrganism);
        String event = getClass().getSimpleName() + " rozmnozyl sie na pole (" + x + ", " + y + ")";
        getSwiat().addLog(event);
    }


    public Animal(World world, int x, int y) {
        super(world, x, y);
    }
}
