package game.persistence.savers;

import game.World;
import game.organisms.Organism;

import java.io.IOException;
import java.io.PrintWriter;

public class TxtSaver implements WorldSaver
{
    @Override
    public void save(World world, String filePath) throws IOException
    {
        try (PrintWriter writer = new PrintWriter(filePath)) {
            // Zapisanie wymiarow swiata
            writer.println(world.getWidth());
            writer.println(world.getHeight());
            writer.println(world.getTurn());

            // Zapisanie wszystkich nie null organizmow
            for (int i = 0; i < world.getHeight(); i++) {
                for (int j = 0; j < world.getWidth(); j++)
                {
                    Organism org = world.getOrganism(i,j);
                    if (org != null) {
                        // Zapisanie info organizmow
                        writer.println(org.getClass().getSimpleName());
                        writer.println(org.getX());
                        writer.println(org.getY());
                        writer.println(org.getStr());
                        writer.println(org.getCooldown());
                        writer.println(org.getAbility());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Błąd podczas zapisywania do pliku: " + filePath);
            e.printStackTrace();
        }
    }
}
