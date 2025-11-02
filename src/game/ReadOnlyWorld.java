package game;

import game.organisms.Organism;

import java.util.List;

public interface ReadOnlyWorld {
    int getWidth();
    int getHeight();
    Organism getOrganism(int x, int y);
    int getTurn();
    List<String> getLogs();
}