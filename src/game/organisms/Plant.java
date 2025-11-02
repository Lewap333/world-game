package game.organisms;

import game.*;
import game.organisms.plants.*;

public abstract class Plant extends Organism {
    public Plant(World world, int x, int y) {
        super(world, x, y);
    }

    private static final int PROCENT_ROZROST = 25;
    @Override
    public void action() {
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
            if (newX >= 0 && newX < getWorld().getWidth() && newY >= 0 && newY < getWorld().getHeight()) {
                if (getWorld().getOrganism(newX, newY) == null) {
                    stworzNowe(newX, newY);
                }
            }
        }
    }



    @Override
    public void collision(Organism other) {
        String event = other.getClass().getSimpleName() + "(" + getX() + "," + getY() + ") zjada " + getClass().getSimpleName();
        getWorld().addLog(event);
        return;
    }

    protected void stworzNowe(int x, int y) {
        if (x < 0 || y < 0) {
            return;
        }
        Organism nowyOrganism = null;

        // Rozrastanie roslin
        if (this instanceof Grass) {
            nowyOrganism = new Grass(getWorld(), x, y);
        } else if (this instanceof Milkweed) {
            nowyOrganism = new Milkweed(getWorld(), x, y);
        } else if (this instanceof Guarani) {
            nowyOrganism = new Guarani(getWorld(), x, y);
        } else if (this instanceof Berries) {
            nowyOrganism = new Berries(getWorld(), x, y);
        } else if (this instanceof Borsch) {
            nowyOrganism = new Borsch(getWorld(), x, y);
        }

        getWorld().setOrganism(x, y, nowyOrganism);
        String event = getClass().getSimpleName() + " rozmnozyl sie na pole (" + x + ", " + y + ")";
        getWorld().addLog(event);
    }
}