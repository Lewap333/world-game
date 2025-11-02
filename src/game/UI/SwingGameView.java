package game.UI;

import game.ReadOnlyWorld;
import game.World;
import game.controller.GameController;
import game.organisms.Organism;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SwingGameView implements GameView {

    private GameController controller;

    private JFrame frame;
    private JPanel root;
    private JPanel boardPanel;
    private JPanel topBar;
    private JLabel turnLabel;


    private JButton nextTurnBtn, abilityBtn, saveBtn, loadBtn, infoBtn;
    private JTextArea logArea;

    private JButton[][] tiles;
    private int cachedW = -1, cachedH = -1;

    private final Color emptyColor = new Color(235, 235, 235);

    @Override
    public void setController(GameController controller) {
        this.controller = controller;
    }

    @Override
    public void render(ReadOnlyWorld world) {
        ensureUi(world);

        // nagłówek
        turnLabel.setText("Tura: " + world.getTurn());

        // rysowanie planszy
        int w = world.getWidth();
        int h = world.getHeight();

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                JButton btn = tiles[y][x];
                Organism org = world.getOrganism(x, y);

                if (org == null) {
                    btn.setText("");
                    btn.setBackground(emptyColor);
                    btn.setToolTipText(null);
                } else {
                    
                    btn.setToolTipText(org.getClass().getSimpleName());

                    // jeśli organizm ma getColor(), użyj go
                    try {
                        Color c = (Color) org.getClass().getMethod("getColor").invoke(org);
                        btn.setBackground(c != null ? c : UIManager.getColor("Button.background"));
                    } catch (ReflectiveOperationException e) {
                        btn.setBackground(UIManager.getColor("Button.background"));
                    }
                }
            }
        }

        // logi po prawej
        logArea.setText("");
        for (String line : world.getLogs()) {
            logArea.append(line);
            if (!line.endsWith("\n")) logArea.append("\n");
        }
        logArea.setCaretPosition(logArea.getDocument().getLength());

        boardPanel.revalidate();
        boardPanel.repaint();
        frame.setVisible(true);
    }

    @Override
    public void promptAddOrganism(int x, int y, List<String> allowed) {
        if (allowed == null || allowed.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Brak dostępnych organizmów do dodania.",
                    "Informacja", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Object choice = JOptionPane.showInputDialog(
                frame,
                "Wybierz organizm do dodania na (" + x + "," + y + "):",
                "Dodaj organizm",
                JOptionPane.PLAIN_MESSAGE,
                null,
                allowed.toArray(),
                allowed.get(0)
        );

        if (choice != null && controller != null) {
            controller.onOrganismChosen(choice.toString(), x, y);
        }
    }

    @Override
    public void showInfo(Map<String, Color> legend) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;

        if (legend == null || legend.isEmpty()) {
            panel.add(new JLabel("Brak zdefiniowanej legendy."), gbc);
        } else {
            for (Map.Entry<String, Color> e : legend.entrySet()) {
                JLabel swatch = new JLabel("  ");
                swatch.setOpaque(true);
                swatch.setBackground(e.getValue());
                swatch.setPreferredSize(new Dimension(18, 18));
                panel.add(swatch, gbc);

                gbc.gridx = 1;
                panel.add(new JLabel(e.getKey()), gbc);

                gbc.gridx = 0; gbc.gridy++;
            }
        }

        JOptionPane.showMessageDialog(frame, panel, "Legenda", JOptionPane.INFORMATION_MESSAGE);
    }

    // ================== prywatne pomocnicze ==================

    private void ensureUi(ReadOnlyWorld world) {
        int w = world.getWidth();
        int h = world.getHeight();

        if (frame != null && w == cachedW && h == cachedH) return;

        cachedW = w;
        cachedH = h;

        frame = new JFrame("Symulacja Świata");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        root = new JPanel(new BorderLayout(8, 8));
        root.setBorder(new EmptyBorder(10, 10, 10, 10));

        buildTopBar();
        buildBoard(h, w);
        buildRightLogsPanel();

        setupKeyBindings(root);

        frame.setContentPane(root);
        frame.pack();
        frame.setSize(Math.max(800, w * 36 + 320), Math.max(550, h * 36));
        frame.setLocationRelativeTo(null);
    }

    private void buildTopBar() {
        topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        turnLabel = new JLabel("Tura: -");

        nextTurnBtn = new JButton("Następna tura");
        abilityBtn  = new JButton("Umiejętność");
        saveBtn     = new JButton("Zapisz");
        loadBtn     = new JButton("Wczytaj");
        infoBtn     = new JButton("Info");


        nextTurnBtn.addActionListener(e -> {
            if (controller != null) controller.onNextTurn();
        });
        abilityBtn.addActionListener(e -> {
            if (controller != null) controller.onAbility();
        });
        saveBtn.addActionListener(e -> {
            if (controller != null) {
                try {
                    controller.onSave();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        loadBtn.addActionListener(e -> {
            if (controller == null) return;
            JFileChooser fc = new JFileChooser(new java.io.File("saves"));
            fc.setDialogTitle("Wczytaj zapis");
            int res = fc.showOpenDialog(frame);
            if (res == JFileChooser.APPROVE_OPTION) {
                controller.onLoad(fc.getSelectedFile().getPath());
            }
        });
        infoBtn.addActionListener(e -> {
            if (controller != null) controller.onShowInfo();
        });

        topBar.add(turnLabel);
        topBar.add(nextTurnBtn);
        topBar.add(abilityBtn);
        topBar.add(saveBtn);
        topBar.add(loadBtn);
        topBar.add(infoBtn);

        root.add(topBar, BorderLayout.NORTH);
    }

    private void buildBoard(int h, int w) {
        boardPanel = new JPanel(new GridLayout(h, w, 2, 2));
        tiles = new JButton[h][w];

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                final int xx = x, yy = y;
                JButton btn = new JButton();
                btn.setFocusPainted(false);
                btn.setMargin(new Insets(2, 2, 2, 2));
                btn.setBackground(emptyColor);
                btn.addActionListener(e -> {
                    if (controller != null) controller.onTileClick(xx, yy);
                });
                tiles[y][x] = btn;
                boardPanel.add(btn);
            }
        }

        root.add(boardPanel, BorderLayout.CENTER);
    }

    private void buildRightLogsPanel() {
        JPanel right = new JPanel(new BorderLayout(6,6));
        right.setBorder(new EmptyBorder(0, 6, 0, 0));
        logArea = new JTextArea(20, 30);
        logArea.setEditable(false);
        right.add(new JScrollPane(logArea), BorderLayout.CENTER);

        root.add(right, BorderLayout.EAST);
    }

    private void setupKeyBindings(JComponent comp) {
        InputMap im = comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = comp.getActionMap();

        bind(im, am, "ARROW_UP",    KeyEvent.VK_UP,    () -> controller.onArrow(World.Direction.UP));
        bind(im, am, "ARROW_DOWN",  KeyEvent.VK_DOWN,  () -> controller.onArrow(World.Direction.DOWN));
        bind(im, am, "ARROW_LEFT",  KeyEvent.VK_LEFT,  () -> controller.onArrow(World.Direction.LEFT));
        bind(im, am, "ARROW_RIGHT", KeyEvent.VK_RIGHT, () -> controller.onArrow(World.Direction.RIGHT));
    }

    private void bind(InputMap im, ActionMap am, String key, int vk, Runnable r) {
        im.put(KeyStroke.getKeyStroke(vk, 0), key);
        am.put(key, new AbstractAction() {
            @Override public void actionPerformed(java.awt.event.ActionEvent e) {
                if (controller != null) r.run();
            }
        });
    }
}
