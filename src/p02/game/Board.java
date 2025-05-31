package p02.game;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.table.AbstractTableModel;
import java.util.*;

//Model
public class Board extends AbstractTableModel {
    private final int rows = 6;
    private final int cols = 12;
    private final Integer[][] data = new Integer[rows][cols];
    private final Set<Integer> turtleCols = Set.of(1, 3, 5, 7, 9);//columns where are turtles
    private int manCol = 0;
    private int manRow = 0;
    private boolean manRotated = false;
    private final Queue<String> moveQueue = new LinkedList<>();
    private boolean isPaused = false;
    private long pauseStartTime = 0;
    private String autoContinueDirection = null;
    private int score = 0;
    private Runnable onLifeLost;
    private Runnable onScoreUpdate;

    public void setOnLifeLost(Runnable onLifeLost) {
        this.onLifeLost = onLifeLost;
    }

    public Board() {
        clear();
    }

    public void clear() {
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                data[r][c] = null;
        for (int col : turtleCols)
            data[2][col] = 2;
        manCol = 0;
        manRow = 0;
        manRotated = false;
        moveQueue.clear();
        isPaused = false;
        autoContinueDirection = null;
        score = 0;
        fireTableDataChanged();
    }

    public void tickAnimation() {
        if (isPaused) {
            if (System.currentTimeMillis() - pauseStartTime >= 300) {
                isPaused = false;
                if (autoContinueDirection != null) {
                    moveQueue.offer(autoContinueDirection);
                    autoContinueDirection = null;
                }
            } else {
                fireTableDataChanged();
                return;
            }
        }

        if (!moveQueue.isEmpty()) {
            String dir = moveQueue.poll();
            int nextCol = manCol + (dir.equals("right") ? 1 : -1);
            if (nextCol >= 0 && nextCol < cols) {
                manCol = nextCol;
                if (manCol == 11 || manCol == 0) {
                    score++;
                }
                if (turtleCols.contains(manCol)) {
                    if (data[2][manCol] != null && data[2][manCol] == 2) {
                        manRow = 1;
                        manRotated = false;
                    } else if (data[3][manCol] != null && data[3][manCol] == 1) {
                        // Man dies: show fall to row 3 then reset
                        manRow = 3;
                        fireTableDataChanged();
                        Timer timer = new Timer(500, e -> {
                            ((Timer) e.getSource()).stop();
                            manCol = 0;
                            manRow = 0;
                            manRotated = false;
                            if (onLifeLost != null) onLifeLost.run();
                            fireTableDataChanged();
                        });
                        timer.setRepeats(false);
                        timer.start();
                        return;
                    } else {
                        manRow = 0;
                        manRotated = false;
                    }
                } else {
                    manRow = 0;
                    manRotated = false;
                    if (manCol == 2 || manCol == 4 || manCol == 6 || manCol == 8) {
                        isPaused = true;
                        pauseStartTime = System.currentTimeMillis();
                        autoContinueDirection = dir;
                    }
                }
            }
        }
        fireTableDataChanged();
    }

    public void tick() {
        for (int col : turtleCols) {
            data[2][col] = 2;
        }

        for (int c = 0; c < cols; c++) {
            for (int r = rows - 1; r > 0; r--) {
                if (data[r][c] != null && data[r][c] == 1) {
                    data[r][c] = null;
                    if (r == 5) {
                        data[4][c] = 1;
                    } else if (r == 4) {
                        data[3][c] = 1;
                        if (turtleCols.contains(c)) {
                            data[2][c] = null;
                        }
                    } else if (r == 3) {
                        // eaten by turtle, disappear
                    }
                    break;
                }
            }
        }

        // Check if the man is standing on a turtle column, but turtle is gone (dived)
        if (turtleCols.contains(manCol)) {
            if (manRow == 1 && data[2][manCol] == null) {
                // Turtle dove under the man — he falls
                manRow = 3; // show the fall
                fireTableDataChanged();
                Timer timer = new Timer(500, e -> {
                    ((Timer) e.getSource()).stop();
                    manCol = 0;
                    manRow = 0;
                    manRotated = false;
                    if (onLifeLost != null) onLifeLost.run();
                    fireTableDataChanged();
                });
                timer.setRepeats(false);
                timer.start();
                return;
            }
        }

        fireTableDataChanged();
    }


    public void spawnFish() {
        Random rand = new Random();
        for (int attempt = 0; attempt < turtleCols.size(); attempt++) {
            int col = List.copyOf(turtleCols).get((int) (rand.nextInt(turtleCols.size())));
            if (data[5][col] == null && data[4][col] == null && data[3][col] == null) {
                data[5][col] = 1;
                break;
            }
        }
        fireTableDataChanged();
    }

    public void moveManLeft() {
        moveQueue.offer("left");
    }

    public void moveManRight() {
        moveQueue.offer("right");
    }

    public int getManCol() {
        return manCol;
    }

    public int getManRow() {
        return manRow;
    }

    public boolean isManRotated() {
        return manRotated;
    }

    @Override
    public int getRowCount() {
        return rows;
    }

    @Override
    public int getColumnCount() {
        return cols;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    public Integer[][] getGrid() {
        return data;
    }

    public Set<Integer> getTurtleCols() {
        return turtleCols;
    }

    public int getScore() {
        return score;
    }

    public void setOnScoreUpdate(Runnable r) {
        this.onScoreUpdate = r;
    }
}


