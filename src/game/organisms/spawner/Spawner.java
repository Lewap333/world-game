package game.organisms.spawner;

import game.World;
import game.organisms.Organism;

import java.util.Optional;

public interface Spawner {
    /** Spawn initial organisms */
    void spawnInitial(World world);
    /** Try spawn at (x,y) return empty if taken. */
    <T extends Organism> Optional<T> trySpawnAt(World world, Class<T> cls, int x, int y);
}
