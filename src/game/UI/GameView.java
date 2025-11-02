package game.UI;

import game.ReadOnlyWorld;
import game.controller.GameController;

import java.awt.*;
import java.util.List;
import java.util.Map;

public interface GameView {
    void setController(GameController controller);
    void render(ReadOnlyWorld world);
    void promptAddOrganism(int x, int y, List<String> allowed);
    void showInfo(Map<String, Color> legend);
}