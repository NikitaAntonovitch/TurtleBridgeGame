package p02.pres;

import p02.game.Board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class FishView extends JFrame {
    private final JTable table;
    public final SevenSegmentDisplayPanel scoreDisplay = new SevenSegmentDisplayPanel();

    private final JLabel[] lifeIcons = new JLabel[3];
    private int lives = 3;
    private boolean gameStarted = false;

    public FishView(Board model, Runnable levelA, Runnable levelB) {
        setTitle("Turtle Bridge Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 500);
        setLayout(null);

        // Panel with background image
        JPanel backgroundPanel = new JPanel() {
            Image backgroundImage = new ImageIcon("Images/background.png").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(null);

        table = new JTable(model);
        table.setOpaque(false);
        table.setRowHeight(60);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setDefaultRenderer(Object.class, new FishCellRenderer(model));
        table.setEnabled(false);
        table.setTableHeader(null);
        table.setBounds(50, 50, 700, 360);
        backgroundPanel.add(table);
        backgroundPanel.setBounds(0, 50, 800, 410);
        add(backgroundPanel);
        setLocationRelativeTo(null);
        setResizable(false);

        // Score display (Seven Segment)
        scoreDisplay.setBounds(350, 0, 200, 40);
        add(scoreDisplay);
        model.setOnScoreUpdate(() -> SwingUtilities.invokeLater(() -> {
            scoreDisplay.handlePlusOneEvent();
            if (model.getScore() >= 999) {
                JOptionPane.showMessageDialog(this, "You Win!", "Info", JOptionPane.INFORMATION_MESSAGE);
                model.clear();
                for (JLabel life : lifeIcons) life.setVisible(true);
                lives = 3;
                scoreDisplay.handleResetEvent();
                gameStarted = false;
            }
        }));

        JButton buttonA = new JButton("Game A");
        JButton buttonB = new JButton("Game B");
        // Buttons A and B
        buttonA.setBounds(100, 0, 80, 40);
        buttonB.setBounds(200, 0, 80, 40);
        add(buttonA);
        add(buttonB);

        buttonA.addActionListener(e -> {
                    levelA.run();
                    buttonA.setEnabled(false);
                    buttonB.setEnabled(true);
                    buttonA.setBackground(Color.PINK);
                    buttonB.setBackground(null);
                }
        );
        buttonB.addActionListener(e -> {
                    levelB.run();
                    buttonB.setEnabled(false);
                    buttonA.setEnabled(true);
                    buttonB.setBackground(Color.PINK);
                    buttonA.setBackground(null);
                }
        );

        JButton startButton = new JButton("S");
        // Start button
        startButton.setBounds(0, 0, 80, 40);
        add(startButton);

        // Shared start action
        Action startAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameStarted = true;
                scoreDisplay.handleStartEvent();
            }
        };
        startButton.addActionListener(startAction);

        // Bind "S" key to the start action
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getRootPane().getActionMap();
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "startAction");
        actionMap.put("startAction", startAction);

        // Life icons
        ImageIcon lifeIcon = new ImageIcon(new ImageIcon("Images/life.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
        for (int i = 0; i < 3; i++) {
            lifeIcons[i] = new JLabel(lifeIcon);
            lifeIcons[i].setBounds(600 + (i * 25), 10, 20, 20);
            add(lifeIcons[i]);
        }

        model.setOnLifeLost(() -> SwingUtilities.invokeLater(() -> {
            lives--;
            if (lives >= 0 && lives < lifeIcons.length) {
                lifeIcons[lives].setVisible(false);
            }
            if (lives <= 0) {
                JOptionPane.showMessageDialog(this, "Game Over!", "Info", JOptionPane.INFORMATION_MESSAGE);
                model.clear();
                for (JLabel life : lifeIcons) life.setVisible(true);
                lives = 3;
                scoreDisplay.handleResetEvent();
                gameStarted = false;
            }
        }));

        // Game control via keyboard
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!gameStarted) return;

                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) {
                    model.moveManLeft();
                } else if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) {
                    model.moveManRight();
                }
            }
        });
    }
}
