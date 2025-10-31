package game;

import game.logs.GameLog;
import game.logs.MemoryLog;
import game.organisms.Organism;
import game.organisms.animals.*;
import game.organisms.plants.*;
import game.organisms.spawner.PopulationPlan;
import game.organisms.spawner.SimpleSpawner;
import game.organisms.spawner.Spawner;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.Collections;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.io.File;


public class World {

    private final GameLog log;

    private int width;
    private int height;
    private int turn = 0;
    private Organism[][] organisms;
    private final String[] organismsNames = {"Sheep", "Wolf", "Fox", "Antelope", "Grass", "Milkweed", "Berries", "Borsch", "Guarani", "Turtle"};
    private Human human;
    private JFrame frame;

    public World(int width, int height) {
        this.log = new MemoryLog();
        this.width = width;
        this.height = height;
        this.organisms = new Organism[height][width];

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
        spawner.spawnInitial(this);
    }

    public void save() {
        String folderName = "saves";
        String fileName = folderName + "/Zapis tury " + turn + ".txt";
        try (PrintWriter writer = new PrintWriter(fileName)) {
            // Zapisanie wymiarow swiata
            writer.println(getWidth());
            writer.println(getHeight());
            writer.println(getTurn());
            // Zapisanie wszystkich nie null organizmow
            for (int i = 0; i < getHeight(); i++) {
                for (int j = 0; j < getWidth(); j++) {
                    Organism org = organisms[i][j];
                    if (org != null) {
                        // Zapisanie info organizmow
                        writer.println(org.getClass().getSimpleName());
                        writer.println(org.getX());
                        writer.println(org.getY());
                        writer.println(org.getSila());
                        writer.println(org.getCooldown());
                        writer.println(org.getAbility());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Błąd podczas zapisywania do pliku: " + fileName);
            e.printStackTrace();
        }
    }

    private int getTurn() {
        return turn;
    }

    public void run() {
        SwingUtilities.invokeLater(() -> {
            displayBoard();
        });
    }

    private File[] getSaveFiles() {
        File folder = new File("saves");
        if (folder.exists() && folder.isDirectory()) {
            File[] saveFiles = folder.listFiles();
            if (saveFiles != null && saveFiles.length > 0) {
                return saveFiles;
            }
        }
        return new File[0];
    }

    public void load(String fileName) {
        try (Scanner scanner = new Scanner(new File(fileName))) {
            // Czytanie wymiarow planszy
            int width = scanner.nextInt();
            int height = scanner.nextInt();
            int tura = scanner.nextInt();
            scanner.nextLine();


            Organism[][] organizmy = new Organism[height][width];
            Human human = new Human(this, 0, 0);
            human.setCzyZyje(false);
            // Czytanie organizmow i dodawanie ich do tablicy
            while (scanner.hasNextLine()) {
                String className = scanner.nextLine();
                int x = scanner.nextInt();
                int y = scanner.nextInt();
                int sila = scanner.nextInt();
                int cooldown = scanner.nextInt();
                int ability = scanner.nextInt();
                scanner.nextLine();

                Organism org;

                switch (className) {
                    case "Owca":
                        org = new Sheep(this, x, y);
                        break;
                    case "Wilk":
                        org = new Wolf(this, x, y);
                        break;
                    case "Lis":
                        org = new Fox(this, x, y);
                        break;
                    case "Antylopa":
                        org = new Antelope(this, x, y);
                        break;
                    case "Czlowiek":
                        org = new Human(this, x, y);
                        human = (Human) org; // Assign the read Czlowiek instance to the variable
                        break;
                    case "Trawa":
                        org = new Grass(this, x, y);
                        break;
                    case "Mlecz":
                        org = new Milkweed(this, x, y);
                        break;
                    case "WilczeJagody":
                        org = new Berries(this, x, y);
                        break;
                    case "BarszczSosnowskiego":
                        org = new Borsch(this, x, y);
                        break;
                    case "Guarana":
                        org = new Guarani(this, x, y);
                        break;
                    case "Zolw":
                        org = new Turtle(this, x, y);
                        break;

                    default:
                        throw new ClassNotFoundException("Unknown Organizm class: " + className);
                }

                org.setX(x);
                org.setY(y);
                org.setSila(sila);
                org.setCooldown(cooldown);
                org.setAbility(ability);

                organizmy[y][x] = org;
            }


            this.organisms = organizmy;
            this.width = width;
            this.height = height;
            this.human = human;
            this.turn = tura;

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Błąd podczas wczytywania z pliku: " + fileName);
            e.printStackTrace();
        }
    }
    
    public void displayBoard() {
        JFrame frame = new JFrame("Paweł Kusznierczuk 193394");
        this.frame = frame;
        frame.setLayout(new BorderLayout());

        // Lewy panel plansza
        JPanel gameBoardPanel = new JPanel(new GridLayout(height, width));
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                final int y = i;
                final int x = j;
                Organism org = organisms[i][j];
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(20, 20));
                if (org != null) {
                    button.setBackground(org.getColor());
                } else {
                    button.setBackground(Color.WHITE);
                    button.addActionListener(e -> {
                        // Dialog box do dodawanie organizmow
                        String selectedOrganizm = (String) JOptionPane.showInputDialog(
                                frame,
                                "Wybierz organizm:",
                                "Dodaj organizm",
                                JOptionPane.PLAIN_MESSAGE,
                                null,
                                organismsNames,
                                organismsNames[0]);

                        // Dodanie wybranego organizmu do planszy
                        if (selectedOrganizm != null) {
                            Organism organism = createOrganism(selectedOrganizm, x, y);
                            organisms[y][x] = organism;
                            updateBoard();
                        }
                    });
                }
                gameBoardPanel.add(button);
            }
        }
        frame.add(gameBoardPanel, BorderLayout.WEST);

        // prawy panel z wydarzeniami i przysciskami
        JPanel rightPanel = new JPanel(new BorderLayout());
        JTextArea commentsTextArea = new JTextArea();
        commentsTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(commentsTextArea);
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new GridLayout(1, 4));

        // Gorne przyciski
        JButton saveButton = new JButton("Zapisz");
        JButton loadButton = new JButton("Wczytaj");
        JButton abilityButton = new JButton("Umiejętność");
        JButton infoButton = new JButton("Info");

        buttonsPanel.add(saveButton);
        buttonsPanel.add(loadButton);
        buttonsPanel.add(abilityButton);
        buttonsPanel.add(infoButton);

        saveButton.addActionListener(e -> {
            save();
        });

        loadButton.addActionListener((ActionListener) new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                File[] saveFiles = getSaveFiles();

                // Sprawdzienie czy sa  savey
                if (saveFiles.length == 0) {
                    JOptionPane.showMessageDialog(frame, "Nie masz zapisanej gry.", "Wczytaj gre", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                // Dialog box z lista saveow
                String[] saveNames = new String[saveFiles.length];
                for (int i = 0; i < saveFiles.length; i++) {
                    saveNames[i] = saveFiles[i].getName();
                }
                String selectedSave = (String) JOptionPane.showInputDialog(
                        frame,
                        "Wybierz zapis gry:",
                        "Wczytaj Gre",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        saveNames,
                        saveNames[0]);

                // Wczytaj wybrany save
                if (selectedSave != null) {
                    String fileName = "saves/" + selectedSave;
                    load(fileName);
                    updateBoard();
                    commentsTextArea.setText("");
                    clearLogs();
                }
            }
        });

        infoButton.addActionListener(e -> {
            JFrame infoFrame = new JFrame("Organizmy");
            infoFrame.setLayout(new GridLayout(organismsNames.length, 1));

            for (String organizmName : organismsNames) {
                JPanel panel = new JPanel(new BorderLayout());

                JLabel label = new JLabel(organizmName);
                label.setOpaque(true);
                panel.add(label, BorderLayout.WEST);

                JButton button = new JButton();
                button.setPreferredSize(new Dimension(20, 20));
                button.setBackground(getOrganismColor(organizmName));
                panel.add(button, BorderLayout.EAST);

                infoFrame.add(panel);
            }

            infoFrame.pack();
            infoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            infoFrame.setVisible(true);
        });


        // Bindowanie klawiszy strzalek
        InputMap inputMap = gameBoardPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = gameBoardPanel.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "up");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "down");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "left");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "right");

        actionMap.put("up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (human.getCzyZyje()) {
                    human.setKierunek(1); // Set kierunek to up (1)
                    makeTurn();
                    updateBoard();
                    // Czyszczenie starych wydarzen
                    commentsTextArea.setText("");

                    // update wydarzen

                    for (String event : log.snapshot()) {
                        commentsTextArea.append(event + "\n");
                    }

                    // Scroll do wydarzen
                    commentsTextArea.setCaretPosition(commentsTextArea.getDocument().getLength());
                    clearLogs();
                }
            }

        });

        actionMap.put("down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (human.getCzyZyje()) {
                    human.setKierunek(2); // Set kierunek to down (2)
                    makeTurn();
                    updateBoard();

                    commentsTextArea.setText("");


                    for (String event : log.snapshot()) {
                        commentsTextArea.append(event + "\n");
                    }


                    commentsTextArea.setCaretPosition(commentsTextArea.getDocument().getLength());
                    clearLogs();
                }
            }
        });

        actionMap.put("left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (human.getCzyZyje()) {
                    human.setKierunek(3); // Set kierunek to left (3)
                    makeTurn();
                    updateBoard();

                    commentsTextArea.setText("");


                    for (String event : log.snapshot()) {
                        commentsTextArea.append(event + "\n");
                    }


                    commentsTextArea.setCaretPosition(commentsTextArea.getDocument().getLength());
                    clearLogs();
                }
            }
        });

        actionMap.put("right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (human.getCzyZyje()) {
                    human.setKierunek(4); // Set kierunek to right (4)
                    makeTurn();
                    updateBoard();

                    commentsTextArea.setText("");


                    for (String event : log.snapshot()) {
                        commentsTextArea.append(event + "\n");
                    }


                    commentsTextArea.setCaretPosition(commentsTextArea.getDocument().getLength());
                    clearLogs();
                }
            }
        });

        abilityButton.addActionListener(e -> {
            if (human.getCooldown() == 0 && human.getCzyZyje()) {
                human.setAbility(5);
                human.setCooldown(5);
                human.specialnaUmiejetnosc();
                updateBoard();
            }
        });


        frame.setFocusable(true);
        frame.requestFocus();

        rightPanel.add(buttonsPanel, BorderLayout.NORTH);
        frame.add(rightPanel, BorderLayout.CENTER);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private Color getOrganismColor(String organizmName) {
        switch (organizmName) {
            case "Owca":
                return Color.GRAY;
            case "Wilk":
                return Color.DARK_GRAY;
            case "Lis":
                return Color.ORANGE;
            case "Antylopa":
                Color brown = new Color(139, 69, 19);
                return brown;
            case "Trawa":
                return Color.GREEN;
            case "Mlecz":
                return Color.YELLOW;
            case "WilczeJagody":
                return Color.BLUE;
            case "BarszczSosnowskiego":
                return Color.RED;
            case "Guarana":
                return Color.CYAN;
            case "Zolw":
                Color darkGreen = new Color(0, 100, 0);
                return darkGreen;
            default:
                return Color.BLACK;
        }
    }

    public void updateBoard() {
        // Zczytaj panel z gra z frame
        JPanel gameBoardPanel = (JPanel) frame.getContentPane().getComponent(0);

        // Wyczyszczenie przyciskow z panelu
        gameBoardPanel.removeAll();

        // dodanie nowych przyciskow zgodnych z org array
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                final int y = i;
                final int x = j;
                Organism org = organisms[i][j];
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(20, 20));
                if (org != null) {
                    button.setBackground(org.getColor());
                } else {
                    button.setBackground(Color.WHITE);
                    button.addActionListener(e -> {
                        // Dialog do dodawania organizmow
                        String selectedOrganizm = (String) JOptionPane.showInputDialog(
                                frame,
                                "Wybierz organizm:",
                                "Dodaj organizm",
                                JOptionPane.PLAIN_MESSAGE,
                                null,
                                organismsNames,
                                organismsNames[0]);

                        if (selectedOrganizm != null) {
                            Organism organism = createOrganism(selectedOrganizm, x, y);
                            organisms[y][x] = organism;
                            updateBoard();
                        }
                    });
                }
                gameBoardPanel.add(button);

            }
        }

        // repaint planszy
        gameBoardPanel.revalidate();
        gameBoardPanel.repaint();
    }

    private Organism createOrganism(String organismName, int x, int y) {
        return switch (organismName) {
            case "Sheep" -> new Sheep(this, x, y);
            case "Wolf" -> new Wolf(this, x, y);
            case "Fox" -> new Fox(this, x, y);
            case "Antelope" -> new Antelope(this, x, y);
            case "Grass" -> new Grass(this, x, y);
            case "Milkweed" -> new Milkweed(this, x, y);
            case "Berries" -> new Berries(this, x, y);
            case "Borsch" -> new Borsch(this, x, y);
            case "Guarani" -> new Guarani(this, x, y);
            case "Turtle" -> new Turtle(this, x, y);
            default -> throw new IllegalArgumentException("Unknown organism class: " + organismName);
        };
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Organism getOrganism(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return organisms[y][x];
        } else {
            return null;
        }
    }

    public void setOrganism(int x, int y, Organism organism) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            organisms[y][x] = organism;
        }
    }

    public void addLog(String wydarzenie) {
        log.addLog(wydarzenie);
    }

    public void makeTurn() {
        List<Organism> organizmy_do_wykonania = new ArrayList<>();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Organism org = organisms[i][j];
                if (org != null) {
                    organizmy_do_wykonania.add(org);
                }
            }
        }

        // Sortowanie listy organizmow wzgledem inicjatywy a jak jest rowna to wieku
        Collections.sort(organizmy_do_wykonania, new Comparator<Organism>() {
            @Override
            public int compare(Organism a, Organism b) {
                if (a.getInicjatywa() != b.getInicjatywa()) {
                    return Integer.compare(a.getInicjatywa(), b.getInicjatywa()) * -1;
                } else {
                    return Integer.compare(a.getWiek(), b.getWiek()) * -1;
                }
            }
        });

        for (Organism org : organizmy_do_wykonania) {
            if (org.getCzyZyje()) {
                org.akcja();
            }
        }
        for (Organism org : organizmy_do_wykonania) {
            if (org.getCzyZyje()) {
                org.setWiek(org.getWiek() + 1);
            }
        }

        turn++;
    }

    private void clearLogs() {
        log.clear();
    }

    public void setHuman(Human h)
    {
        this.human = h;
    }
}
