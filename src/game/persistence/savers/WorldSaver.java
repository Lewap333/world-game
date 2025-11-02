package game.persistence.savers;

import game.World;

import java.io.IOException;

public interface WorldSaver
{
    void save(World world, String filePath) throws IOException;
}
