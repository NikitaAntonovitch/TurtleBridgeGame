import javax.swing.SwingUtilities;
import p02.game.FishController;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FishController().getInstance().start());
    }
}
