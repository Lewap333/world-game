package game;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public interface ControllableWorld extends ReadOnlyWorld {
    List<String> getSpawnableOrganisms();
    void spawn(String name, int x, int y);
    void simulationStep();
    void queueHumanMove(World.Direction dir);
    void activateHumanAbility();
    void save() throws IOException;
    void load(String filePath);
    Map<String, Color> getOrganismColors();
}

