package game.persistence.savers;

import game.ReadOnlyWorld;

import java.io.IOException;

public interface WorldSaver
{
    void save(ReadOnlyWorld world, String filePath) throws IOException;
}
