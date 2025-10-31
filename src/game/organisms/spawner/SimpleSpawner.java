package game.organisms.spawner;

import game.organisms.animals.Human;
import game.World;
import game.organisms.Organism;

import java.lang.reflect.Constructor;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class SimpleSpawner implements Spawner {

    private final PopulationPlan plan;

     public SimpleSpawner(PopulationPlan plan) {
        this.plan = plan;
     }

    @Override
    public void spawnInitial(World world) {
        final int cells = world.getWidth() * world.getHeight();

        // Human - 1
        Human human = spawnRandomFree(world, (Class<? extends Human>) plan.humanType());
        world.setHuman(human);

        // Rest as in plan
        for (var entry : plan.percentByType().entrySet()) {
            Class<? extends Organism> type = entry.getKey();
            int count = countFor(type, cells);
            for (int i = 0; i < count; i++) {
                spawnRandomFree(world, type);
            }
        }
    }

    @Override
    public <T extends Organism> Optional<T> trySpawnAt(World world, Class<T> cls, int x, int y) {
        if (!inBounds(world, x, y)) return Optional.empty();
        if (world.getOrganism(x, y) != null) return Optional.empty();

        try {
            Constructor<T> ctor = cls.getConstructor(World.class, int.class, int.class);
            T org = ctor.newInstance(world, x, y);
            world.setOrganism(x, y, org);
            if (org instanceof Human h) {
                world.setHuman(h);
            }
            return Optional.of(org);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(cls.getName() + " must have (World,int,int) constructor", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to spawn " + cls.getName(), e);
        }
    }

    // ===== helpers =====

    private <T extends Organism> T spawnRandomFree(World world, Class<T> cls) {
        int[] cell = findFreeCell(world);
        return trySpawnAt(world, cls, cell[0], cell[1])
                .orElseThrow(() -> new IllegalStateException("Could not place " + cls.getSimpleName()));
    }

    private int[] findFreeCell(World world) {
        int w = world.getWidth(), h = world.getHeight();

        // szybkie próby losowe
        for (int tries = 0; tries < 64; tries++) {
            int x = ThreadLocalRandom.current().nextInt(w);
            int y = ThreadLocalRandom.current().nextInt(h);
            if (world.getOrganism(x, y) == null) return new int[]{x, y};
        }
        // fallback: skanowanie całej planszy
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if (world.getOrganism(x, y) == null) return new int[]{x, y};
            }
        }
        throw new IllegalStateException("Board is full");
    }

    private boolean inBounds(World world, int x, int y) {
        return x >= 0 && y >= 0 && x < world.getWidth() && y < world.getHeight();
    }

    /** Count cell amount */
    private int countFor(Class<? extends Organism> type, int boardCells) {
        Integer p = plan.percentByType().get(type);
        return (p == null) ? 0 : Math.max(0, p * boardCells / 100);
    }
}
