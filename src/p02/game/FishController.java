package p02.game;

import p02.pres.FishView;

import javax.swing.*;


public class FishController {
    private final Board model = new Board();
    private FishView view;
    private Timer timer;
    private Timer timerAnimation;//animation timer is different then the main game timer

    private static FishController instance;

    public FishController() {
        Runnable levelA = () -> setLevel(900 + (model.getScore() * 200)); // increase speed based on score
        Runnable levelB = () -> setLevel(500 + (model.getScore() * 200)); // increase speed
        Runnable startGame = this::start;
        view = new FishView(model, levelA, levelB);
        setLevel(900 + (model.getScore() * 200)); // default to level A
    }

    public static FishController getInstance() { // make the game Singleton
        if (instance == null) {
            instance = new FishController();
        }
        return instance;
    }

    public void start() {
        view.setVisible(true);
        timerAnimation.start();//animation timer
        timer.start();
    }

    private void setLevel(int tickInterval) {
        if (timer != null) timer.stop();
        if (timerAnimation != null) timerAnimation.stop();

        timerAnimation = new Timer(100, e -> model.tickAnimation());
        timer = new Timer(tickInterval, e -> {
            model.tick();
            model.spawnFish();
        });

        if (view != null) {
            view.requestFocusInWindow();
        }

        timerAnimation.start();
        timer.start();
    }
}

