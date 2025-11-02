package game.organisms.animals;

import game.World;
import game.organisms.Animal;
import game.organisms.Organism;

import java.awt.Color;

public class Human extends Animal {

    public int kierunek;

    public Human(World world, int x, int y) {
        super(world, x, y);
        setStr(5);
        setInitiative(5);
        setColor(Color.MAGENTA);
    }

    @Override
    public void rozmnazanie(Organism other) {
        return;
    }

    @Override
    public void action() {

        if (abilityDuration > 0) {
            specialnaUmiejetnosc();
            abilityDuration--;
        }
        int newX = getX();
        int newY = getY();


        switch(kierunek) {
            case 1: // arrow up
                newY--;
                break;
            case 2: // arrow down
                newY++;
                break;
            case 3: // arrow left
                newX--;
                break;
            case 4: // arrow right
                newX++;
                break;
        }

        // zmniejszenie cooldownu
        if (cooldown > 0 && abilityDuration == 0) {
            cooldown--;
        }

        if (newX >= 0 && newX < world.getWidth() && newY >= 0 && newY < world.getHeight()) {
            // jak jest puste to tam idz
            if (world.getOrganism(newX, newY) == null) {
                world.setOrganism(getX(), getY(), null);
                world.setOrganism(newX, newY, this);
                setX(newX);
                setY(newY);
            }
            // nie jest puste kolizja
            else {
                collision(world.getOrganism(newX, newY));
            }
        }
    }

    public void setKierunek(int kierunek) {
        this.kierunek = kierunek;
    }

    public void specialnaUmiejetnosc() {
        // Specjalna zdolnosc - calopalenie

        int x = getX();
        int y = getY();

        // Sprawdza czy wokół człowieka są jakieś nie-null organizmy
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                // Pomiń własne pole
                if (i == 0 && j == 0) {
                    continue;
                }

                // Sprawdza czy pole nie jest poza planszą
                if (x + i < 0 || x + i >= world.getWidth() || y + j < 0 || y + j >= world.getHeight()) {
                    continue;
                }

                Organism neighbor = world.getOrganism(x + i, y + j);

                // Usuwa wszystkie organizmy wokół, które nie są null
                if (neighbor != null) {
                    neighbor.setAlive(false);
                    getWorld().setOrganism(neighbor.getX(), neighbor.getY(), null);
                }
            }
        }
    }
}