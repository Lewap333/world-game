package game.controller;

import java.io.IOException;

public interface SaveLoadController {
    void onSave() throws IOException;
    void onLoad(String filePath);
}