package game.persistence;

import java.util.ArrayList;
import java.util.List;

public final class WorldState
{
    public int width;
    public int height;
    public int turn;

    public List<OrganismRecord> organisms = new ArrayList<>();

    public static final class OrganismRecord
    {
        public String type;
        public int x, y;
        public int strength, cooldown, ability;
    }
}
