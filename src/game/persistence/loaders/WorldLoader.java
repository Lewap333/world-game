package game.persistence.loaders;

import game.persistence.WorldState;

import java.io.IOException;

public interface WorldLoader
{
    WorldState load(String filePath) throws IOException;
}
