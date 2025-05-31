package p02.pres;

import javax.swing.*;
import java.awt.*;


// output score SevenSegment
class SevenSegmentDigit extends JPanel {
    private boolean[] segments = new boolean[7];

    private static final boolean[][] DIGIT_SEGMENTS = {
            {true, true, true, true, true, true, false},    // 0
            {false, true, true, false, false, false, false},// 1
            {true, true, false, true, true, false, true},   // 2
            {true, true, true, true, false, false, true},   // 3
            {false, true, true, false, false, true, true},  // 4
            {true, false, true, true, false, true, true},   // 5
            {true, false, true, true, true, true, true},    // 6
            {true, true, true, false, false, false, false}, // 7
            {true, true, true, true, true, true, true},     // 8
            {true, true, true, true, false, true, true}     // 9
    };

    public SevenSegmentDigit() {
        setPreferredSize(new Dimension(60, 100));
        setOpaque(false);
    }

    public void setDigit(int digit) {
        if (digit < 0 || digit > 9) return;
        System.arraycopy(DIGIT_SEGMENTS[digit], 0, segments, 0, 7);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.setColor(Color.RED);
        g2.setStroke(new BasicStroke(8));

        int w = getWidth();
        int h = getHeight();
        int sw = 10;

        if (segments[0]) g2.drawLine(w / 5, sw, 4 * w / 5, sw);            // a
        if (segments[1]) g2.drawLine(4 * w / 5, sw, 4 * w / 5, h / 2);     // b
        if (segments[2]) g2.drawLine(4 * w / 5, h / 2, 4 * w / 5, h - sw); // c
        if (segments[3]) g2.drawLine(w / 5, h - sw, 4 * w / 5, h - sw);    // d
        if (segments[4]) g2.drawLine(w / 5, h / 2, w / 5, h - sw);         // e
        if (segments[5]) g2.drawLine(w / 5, sw, w / 5, h / 2);              // f
        if (segments[6]) g2.drawLine(w / 5, h / 2, 4 * w / 5, h / 2);      // g
    }
}

