package game.organisms;

import game.*;
import game.organisms.plants.*;

public abstract class Plant extends Organism {
    public Plant(World world, int x, int y) {
        super(world, x, y);
    }

    private static final int PROCENT_ROZROST = 25;
    @Override
    public void akcja() {
        int szansa = (int) (Math.random() * 100);

        if (szansa < PROCENT_ROZROST) {  // PROCENT_ROZROST na rozrośnięcie jeśli jest wolne pole
            int newX;
            int newY;

            int direction = (int) (Math.random() * 4);
            switch (direction) {
                case 0:
                    newX = getX();
                    newY = getY() - 1;
                    break;
                case 1:
                    newX = getX() + 1;
                    newY = getY();
                    break;
                case 2:
                    newX = getX();
                    newY = getY() + 1;
                    break;
                case 3:
                    newX = getX() - 1;
                    newY = getY();
                    break;
                default:
                    newX = getX();
                    newY = getY();
                    break;
            }

            // upewnij się, że nowe pozycje są w granicach planszy
            if (newX >= 0 && newX < getSwiat().getWidth() && newY >= 0 && newY < getSwiat().getHeight()) {
                if (getSwiat().getOrganism(newX, newY) == null) {
                    stworzNowe(newX, newY);
                }
            }
        }
    }



    @Override
    public void kolizja(Organism other) {
        String event = other.getClass().getSimpleName() + "(" + getX() + "," + getY() + ") zjada " + getClass().getSimpleName();
        getSwiat().addLog(event);
        return;
    }

    protected void stworzNowe(int x, int y) {
        if (x < 0 || y < 0) {
            return;
        }
        Organism nowyOrganism = null;

        // Rozrastanie roslin
        if (this instanceof Grass) {
            nowyOrganism = new Grass(getSwiat(), x, y);
        } else if (this instanceof Milkweed) {
            nowyOrganism = new Milkweed(getSwiat(), x, y);
        } else if (this instanceof Guarani) {
            nowyOrganism = new Guarani(getSwiat(), x, y);
        } else if (this instanceof Berries) {
            nowyOrganism = new Berries(getSwiat(), x, y);
        } else if (this instanceof Borsch) {
            nowyOrganism = new Borsch(getSwiat(), x, y);
        }

        getSwiat().setOrganism(x, y, nowyOrganism);
        String event = getClass().getSimpleName() + " rozmnozyl sie na pole (" + x + ", " + y + ")";
        getSwiat().addLog(event);
    }
}