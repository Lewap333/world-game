package game.organisms.plants;

import game.World;
import game.organisms.Animal;
import game.organisms.Organism;
import game.organisms.Plant;

import java.awt.Color;

public class Borsch extends Plant {
    public Borsch(World world, int x, int y) {
        super(world, x, y);
        setSila(10);
        setColor(Color.RED);
    }

    @Override
    public void kolizja(Organism other) {
        String event = other.getClass().getSimpleName() + "(" + getX() + "," + getY() + ") zjada " + getClass().getSimpleName();
        getSwiat().addLog(event);
        other.setCzyZyje(false);
    }

    @Override
    public void akcja() {
        super.akcja();

        int x = getX();
        int y = getY();

        // Sprawdza czy wokół barszczu są jakieś zwierzęta
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                // Pomiń własne pole
                if (i == 0 && j == 0) {
                    continue;
                }

                // Sprawdza czy pole nie jest poza plansza
                if (x + i < 0 || x + i >= getSwiat().getWidth() || y + j < 0 || y + j >= getSwiat().getHeight()) {
                    continue;
                }

                // Organizm z pola obok barszczu
                Organism neighbor = getSwiat().getOrganism(x + i, y + j);

                // Jeśli organizm jest zwierzę, usuń go
                if (neighbor instanceof Animal) {
                    String event = getClass().getSimpleName() + "(" + getX() + "," + getY() + ") zabija " + neighbor.getClass().getSimpleName();
                    getSwiat().addLog(event);
                    neighbor.setCzyZyje(false);
                    getSwiat().setOrganism(neighbor.getX(), neighbor.getY(), null);
                }
            }
        }
        world.updateBoard();
    }
}