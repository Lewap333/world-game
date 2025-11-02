package game.controller;

import game.World;

public interface MovementController {
    void onArrow(World.Direction dir);
}