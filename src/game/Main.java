package game;

import game.UI.SwingGameView;
import game.controller.GameController;
import game.organisms.animals.*;
import game.organisms.plants.*;
import game.organisms.spawner.PopulationPlan;
import game.organisms.spawner.SimpleSpawner;
import game.organisms.spawner.Spawner;
import game.persistence.loaders.TxtLoader;
import game.persistence.loaders.WorldLoader;
import game.persistence.savers.TxtSaver;
import game.persistence.savers.WorldSaver;

public class Main {
    public static void main(String[] args) {

        PopulationPlan plan = new PopulationPlan.Builder()
                .human(Human.class)
                .percent(Wolf.class,      2)
                .percent(Sheep.class,     4)
                .percent(Fox.class,       1)
                .percent(Turtle.class,    1)
                .percent(Antelope.class,  1)
                .percent(Grass.class,     2)
                .percent(Milkweed.class,  2)
                .percent(Guarani.class,   2)
                .percent(Berries.class,   2)
                .percent(Borsch.class,    2)
                .build();

        Spawner spawner = new SimpleSpawner(plan);

        WorldSaver saver = new TxtSaver();
        WorldLoader loader = new TxtLoader();
        World world = new World(20, 20, plan, spawner, saver, loader);
        SwingGameView view = new SwingGameView();

        new GameController(world, view);
    }
}