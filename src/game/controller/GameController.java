package game.controller;
import game.ControllableWorld;
import game.UI.GameView;
import game.World.Direction;
import java.io.IOException;

public final class GameController implements AppController {
    private final ControllableWorld world;
    private final GameView view;

    public GameController(ControllableWorld world, GameView view) {
        this.world = world;
        this.view = view;
        this.view.setController(this);
        this.view.render(world);
    }

    @Override
    public void onTileClick(int x, int y) {
        view.promptAddOrganism(x, y, world.getSpawnableOrganisms());
    }

    @Override
    public void onOrganismChosen(String name, int x, int y) {
        world.spawn(name, x, y);
        view.render(world);
    }

    @Override
    public void onNextTurn() {
        world.simulationStep();
        view.render(world);
    }

    @Override
    public void onArrow(Direction dir) {
        world.queueHumanMove(dir);
        world.simulationStep();
        view.render(world);
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
