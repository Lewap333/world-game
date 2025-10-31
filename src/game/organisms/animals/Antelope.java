package game.organisms.animals;

import game.World;
import game.organisms.Animal;
import game.organisms.Organism;

import java.awt.Color;

public class Antelope extends Animal {
    public Antelope(World world, int x, int y) {
        super(world, x, y);
        setSila(4);
        setInicjatywa(4);
        Color brown = new Color(139, 69, 19);
        setColor(brown);
    }

    @Override
    public void akcja() {
        int direction = (int) (Math.random() * 4); // wylosuj kierunek: 0 - góra, 1 - dół, 2 - lewo, 3 - prawo
        int newX = getX();
        int newY = getY();

        switch (direction) {
            case 0:
                newY -= 2;
                break;
            case 1:
                newY += 2;
                break;
            case 2:
                newX -= 2;
                break;
            case 3:
                newX += 2;
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
            }
            // w przeciwnym razie wykonaj kolizję
            else {
                kolizja(getSwiat().getOrganism(newX, newY));
            }
        }
    }


    @Override
    public void kolizja(Organism other) {
        if (getClass() == other.getClass()) {
            super.kolizja(other);
        } else {
            int ucieczka = (int) (Math.random() * 2);
            // normalna kolizja
            if (ucieczka == 1) {
                super.kolizja(other);
            }
            // Ucieczka
            else {
                int newX = getX();
                int newY = getY();

                if (getSwiat().getOrganism(other.getX() - 1, other.getY()) == null) {
                    newX = other.getX() - 1;
                } else if (getSwiat().getOrganism(other.getX() + 1, other.getY()) == null) {
                    newX = other.getX() + 1;
                } else if (getSwiat().getOrganism(other.getX(), other.getY() - 1) == null) {
                    newY = other.getY() - 1;
                } else if (getSwiat().getOrganism(other.getX(), other.getY() + 1) == null) {
                    newY = other.getY() + 1;
                }

                getSwiat().setOrganism(getX(), getY(), null);
                getSwiat().setOrganism(newX, newY, this);
                setX(newX);
                setY(newY);
                String event = getClass().getSimpleName() + " ucieka przed walką na pole (" + getX() + "," + getY() + ")";
                getSwiat().addLog(event);
            }
        }
    }

}
