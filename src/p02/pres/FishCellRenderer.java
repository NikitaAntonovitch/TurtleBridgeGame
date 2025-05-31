package p02.pres;

import p02.game.Board;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.image.BufferedImage;


// FishCellRenderer remains unchanged
public class FishCellRenderer extends DefaultTableCellRenderer {
    private final ImageIcon fishIcon;
    private final ImageIcon turtleIcon;
    private final ImageIcon rotatedTurtleIcon;
    private final ImageIcon manIcon;
    private final ImageIcon manRotatedIcon;
    private final Board model;

    public FishCellRenderer(Board model) {
        this.model = model;
        fishIcon = new ImageIcon(new ImageIcon("Images/fish.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        turtleIcon = new ImageIcon(new ImageIcon("Images/turtle.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        rotatedTurtleIcon = new ImageIcon(rotateImage(turtleIcon.getImage(), 240));
        manIcon = new ImageIcon(new ImageIcon("Images/man.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        manRotatedIcon = new ImageIcon(rotateImage(manIcon.getImage(), 180));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);
        label.setOpaque(false);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBorder(null);

        Integer[][] grid = model.getGrid();
        boolean isFish = grid[row][column] != null && grid[row][column] == 1;
        boolean isTurtle = grid[row][column] != null && grid[row][column] == 2;
        boolean turtleDiving = row == 3 && model.getTurtleCols().contains(column) && grid[2][column] == null;

        if (row == model.getManRow() && column == model.getManCol()) {
            label.setIcon(model.isManRotated() ? manRotatedIcon : manIcon);
        } else if (isFish && turtleDiving) {
            label.setIcon(new LayeredIcon(fishIcon, rotatedTurtleIcon));
        } else if (isFish) {
            label.setIcon(fishIcon);
        } else if (isTurtle) {
            label.setIcon(turtleIcon);
        } else {
            label.setIcon(null);
        }

        return label;
    }

    private Image rotateImage(Image img, double angleDegrees) {
        int w = img.getWidth(null);
        int h = img.getHeight(null);
        BufferedImage rotated = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotated.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.rotate(Math.toRadians(angleDegrees), w / 2.0, h / 2.0);
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();
        return rotated;
    }

    static class LayeredIcon extends ImageIcon {
        private final ImageIcon bottom;
        private final ImageIcon top;

        public LayeredIcon(ImageIcon bottom, ImageIcon top) {
            this.bottom = bottom;
            this.top = top;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            bottom.paintIcon(c, g, x, y);
            top.paintIcon(c, g, x, y);
        }

        @Override
        public int getIconWidth() {
            return Math.max(bottom.getIconWidth(), top.getIconWidth());
        }

        @Override
        public int getIconHeight() {
            return Math.max(bottom.getIconHeight(), top.getIconHeight());
        }
    }
}

