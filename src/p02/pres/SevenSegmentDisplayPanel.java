package p02.pres;

import javax.swing.*;

class SevenSegmentDisplayPanel extends JPanel {
    private int value = 0;
    private final SevenSegmentDigit[] digits = new SevenSegmentDigit[3];

    public SevenSegmentDisplayPanel() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        for (int i = 0; i < 3; i++) {
            digits[i] = new SevenSegmentDigit();
            add(digits[i]);
        }
        updateDigits();
    }

    public void handleStartEvent() {
        value = 0;
        updateDigits();
    }

    public void handlePlusOneEvent() {
        value = (value + 1) % 1000;
        updateDigits();
    }

    public void handleResetEvent() {
        for (SevenSegmentDigit digit : digits) {
            digit.setDigit(0);  // You can optionally blank them instead
        }
    }

    private void updateDigits() {
        int n = value;
        digits[2].setDigit(n % 10);         // units
        digits[1].setDigit((n / 10) % 10);  // tens
        digits[0].setDigit(n / 100);        // hundreds
    }
}
