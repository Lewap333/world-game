package game.controller;

import game.UI.GameView;
import game.World;

import game.World.Direction;

import java.io.IOException;

public final class GameController implements AppController {
    private final World world;          // logika
    private final GameView view;        // UI

    public GameController(World world, GameView view) {
        this.world = world;
        this.view = view;
        this.view.setController(this);
        this.view.render(world);
    }

    @Override
    // klik kafelka -> UI pyta o organizm
    public void onTileClick(int x, int y) {
        view.promptAddOrganism(x, y, world.getSpawnableOrganisms());
    }

    @Override
    // użytkownik wybrał organizm z dialogu
    public void onOrganismChosen(String name, int x, int y) {
        world.spawn(name, x, y);       // czysta logika
        view.render(world);            // odśwież UI
    }
    @Override
    // następna tura
    public void onNextTurn() {
        world.simulationStep();
        view.render(world);
    }

    @Override
    public void onArrow(Direction dir) {
        world.queueHumanMove(dir);
        world.simulationStep();
        view.render(world);        // odśwież UI
    }

    @Override
    public void onAbility() {
        world.activateHumanAbility();
        view.render(world);
    }

    @Override
    public void onSave() throws IOException {
        world.save();
        view.render(world);
    }

    @Override
    public void onLoad(String filePath) {
        if (filePath == null) return;
        world.load(filePath);
        view.render(world);
    }

    @Override
    public void onShowInfo() {
        view.showInfo(world.getOrganismColors());
    }
}
