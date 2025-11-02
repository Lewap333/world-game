package game.controller;

public interface SpawnController {
    void onTileClick(int x, int y);
    void onOrganismChosen(String name, int x, int y);
}
