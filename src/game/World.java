package game;

import game.logs.GameLog;
import game.logs.MemoryLog;
import game.organisms.Organism;
import game.organisms.animals.*;
import game.organisms.spawner.PopulationPlan;
import game.organisms.spawner.Spawner;
import game.persistence.WorldState;
import game.persistence.loaders.WorldLoader;
import game.persistence.savers.WorldSaver;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class World implements ControllableWorld {
    public enum Direction { UP, DOWN, LEFT, RIGHT }
    private final GameLog log;
    private final Spawner spawner;
    private final WorldSaver worldSaver;
    private final WorldLoader worldLoader;

    private int width;
    private int height;
    private int turn = 0;
    private Organism[][] organisms;

    private final Map<String, Class<? extends Organism>> organismClasses = new LinkedHashMap<>();

    private Direction queuedHumanMove = null;

    private Human human;

    public World(int width, int height, PopulationPlan pP, Spawner sP, WorldSaver wS, WorldLoader wL) {
        this.worldSaver = wS;
        this.worldLoader = wL;
        this.log = new MemoryLog();
        this.width = width;
        this.height = height;
        this.organisms = new Organism[height][width];

        this.spawner = sP;
        sP.spawnInitial(this);
        registerFromPlan(pP);
    }

    private void registerFromPlan(PopulationPlan plan) {
        addType(plan.humanType());
        for (Class<? extends Organism> cls : plan.percentByType().keySet()) {
            addType(cls);
        }
    }

    /** Dodaj pojedynczy typ do rejestru, dbając o unikatową nazwę wyświetlaną. */
    private void addType(Class<? extends Organism> cls) {
        String base = cls.getSimpleName();        // np. "Wolf"
        String key  = uniqueName(base);           // zadba o unikaty, gdyby nazwy się powtarzały
        organismClasses.putIfAbsent(key, cls);
    }

    /** Jeśli nazwa już istnieje, dołóż sufiks (2), (3), ... */
    private String uniqueName(String base) {
        String key = base;
        int i = 2;
        while (organismClasses.containsKey(key)) {
            key = base + " (" + i++ + ")";
        }
        return key;
    }

    public int getTurn() {
        return turn;
    }

    @Override
    public List<String> getLogs() {
        return log.snapshot();
    }

    /** Jedna tura symulacji. */
    public void simulationStep() {
        clearLogs();

        applyQueuedHumanMoveIfAny();

        List<Organism> toAct = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Organism org = organisms[i][j];
                if (org != null) toAct.add(org);
            }
        }

        toAct.sort((a, b) -> {
            int byIni = Integer.compare(b.getInitiative(), a.getInitiative());
            if (byIni != 0) return byIni;
            return Integer.compare(b.getAge(), a.getAge());
        });

        // akcje
        for (Organism org : toAct) {
            if (org.getAlive()) org.action();
        }
        // starzenie
        for (Organism org : toAct) {
            if (org.getAlive()) org.setAge(org.getAge() + 1);
        }

        turn++;
        log.addLog("Tura " + turn + " zakończona.");

    }

    public void queueHumanMove(Direction dir) {
        this.queuedHumanMove = dir;
    }

    private void applyQueuedHumanMoveIfAny() {
        if (human == null || queuedHumanMove == null || !human.getAlive()) return;

        switch (queuedHumanMove) {
            case UP -> human.setKierunek(1);
            case DOWN -> human.setKierunek(2);
            case LEFT -> human.setKierunek(3);
            case RIGHT -> human.setKierunek(4);
        }
        queuedHumanMove = null;
    }

    public List<String> getSpawnableOrganisms() {
        return new ArrayList<>(organismClasses.keySet());
    }

    public void activateHumanAbility() {
        if (human != null && human.getAlive() && human.getCooldown() == 0) {
            human.setAbility(5);
            human.setCooldown(5);
            human.specialnaUmiejetnosc();
            addLog("Aktywowano umiejętność człowieka.");
        } else {
            addLog("Umiejętność niedostępna (brak człowieka lub cooldown).");
        }
    }

    public void spawn(String simpleName, int x, int y) {

        Class<? extends Organism> cls = organismClasses.get(simpleName);
        if (cls == null) {
            log.addLog("Nieznany organizm: " + simpleName);
            return;
        }

        var result = spawner.trySpawnAt(this, cls, x, y);
        if (result.isPresent()) {
            log.addLog("Dodano " + simpleName + " na (" + x + "," + y + ").");
        } else {
            log.addLog("Nie udało się dodać " + simpleName + " na (" + x + "," + y + ").");
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Organism getOrganism(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return organisms[y][x];
        } else {
            return null;
        }
    }

    public void setOrganism(int x, int y, Organism organism) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            organisms[y][x] = organism;
        }
    }

    public void addLog(String wydarzenie) {
        log.addLog(wydarzenie);
    }

    private void clearLogs() {
        log.clear();
    }

    public void setHuman(Human h)
    {
        this.human = h;
    }

    public Map<String, Color> getOrganismColors() {
        Map<String, Color> map = new LinkedHashMap<>();

        for (var entry : organismClasses.entrySet()) {
            String name = entry.getKey();
            Class<? extends Organism> cls = entry.getValue();

            try {
                // szukamy konstruktora (World, int, int)
                var ctor = cls.getConstructor(World.class, int.class, int.class);
                Organism org = ctor.newInstance(this, 0, 0); // instancja testowa
                Color color = org.getColor();
                map.put(name, color != null ? color : Color.BLACK);
            } catch (Exception e) {
                map.put(name, Color.BLACK);
            }
        }

        return map;
    }

    public void save() throws IOException {
        String folderName = "saves";
        String filePath = folderName + "/Zapis tury " + this.getTurn() + ".txt";
        worldSaver.save(this,filePath);
    }

    public void load(String fileName)
    {
        try
        {
            WorldState state = worldLoader.load(fileName);
            apply(state);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void apply(WorldState state) {
        this.width = state.width;
        this.height = state.height;
        this.turn = state.turn;
        this.organisms = new Organism[height][width];
        this.human = null;

        for (WorldState.OrganismRecord r : state.organisms) {

            Class<? extends Organism> cls = organismClasses.get(r.type);

            if (cls == null) {
                throw new IllegalArgumentException("Unknown organism type: " + r.type);
            }

            // spawner
            @SuppressWarnings("unchecked")
            Optional<? extends Organism> createdOpt =
                    (Optional<? extends Organism>) spawner.trySpawnAt(this, (Class) cls, r.x, r.y);

            if (createdOpt.isEmpty()) {
                System.err.println("Nie udało się wczytać organizmu " + r.type +
                        " na (" + r.x + "," + r.y + ")");
                continue;
            }

            Organism organism = createdOpt.get();

            // odtwórz atrybuty
            organism.setStr(r.strength);
            organism.setCooldown(r.cooldown);
            organism.setAbility(r.ability);

            organisms[r.y][r.x] = organism;
            if (organism instanceof Human h) this.human = h;
        }
    }
}
