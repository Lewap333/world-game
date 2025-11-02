package game.persistence.loaders;

import game.persistence.WorldState;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class TxtLoader implements WorldLoader
{
    @Override
    public WorldState load(String fileName) throws IOException
    {

        try (Scanner scanner = new Scanner(new File(fileName)))
        {
            WorldState state = new WorldState();
            // Czytanie wymiarow planszy
            state.width = scanner.nextInt();
            state.height = scanner.nextInt();
            state.turn = scanner.nextInt();
            scanner.nextLine();

            while(scanner.hasNextLine())
            {
                WorldState.OrganismRecord record = new WorldState.OrganismRecord();
                record.type = scanner.nextLine();
                record.x = scanner.nextInt();
                record.y = scanner.nextInt();
                record.strength = scanner.nextInt();
                record.cooldown = scanner.nextInt();
                record.ability = scanner.nextInt();
                if(scanner.hasNextLine()) scanner.nextLine();
                state.organisms.add(record);
            }
            return state;

        } catch (IOException e) {
            System.err.println("Błąd podczas wczytywania z pliku: " + fileName);
            e.printStackTrace();
        }
        return null;
    }
}
