package game.controller;
import game.World;
import java.io.IOException;

public interface AppController {
    void onAbility();
    void onShowInfo();
    void onArrow(World.Direction dir);
    void onSave() throws IOException;
    void onLoad(String filePath);
    void onTileClick(int x, int y);
    void onOrganismChosen(String name, int x, int y);
    void onNextTurn();
}