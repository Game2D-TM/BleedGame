package fightinggame;

import fightinggame.entity.state.GameState;
import fightinggame.input.handler.menu.MenuKeyboardHandler;
import fightinggame.resource.DataManager;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import javax.swing.JFrame;

public class Game extends JFrame {

    public enum ScreenState {
        fullscreen, borderless, windowed, none
    };

    public static ScreenState current;
    private static GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
    private static GraphicsDevice ev = env.getDefaultScreenDevice();
    public static final int FPS = 144;
    public static GameState STATE = GameState.MENU_STATE;

    private Gameplay gameplay;
    private boolean isRunning = true;
    private Thread gameThread;
    private MenuKeyboardHandler menuHandler;

    public static void main(String[] args) {
        new Game(1650, 950);
    }

    public Game(int width, int height) throws HeadlessException {
        super("Bleed Game");
        setSize(width, height);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        DataManager.loadSceneData();
//        changeWindowMode(ScreenState.fullscreen);
        menuHandler = new MenuKeyboardHandler(this, getWidth(), getHeight());
        addKeyListener(menuHandler);
        setVisible(true);

    }

    @Override
    public void paint(Graphics g) {
        menuHandler.render(g);
    }

    public void init() {
        gameplay = new Gameplay(this);
        gameplay.setPreferredSize(new Dimension(getWidth() - 16, getHeight() - 39));
        add(gameplay, BorderLayout.CENTER);
        pack();
        gameplay.initCamera();
        gameplay.initFirstScene();
    }

    private void formWindowClosing(java.awt.event.WindowEvent evt) {
        exitGame();
    }

    private void exitGame() {
        isRunning = false;
        try {
            if (gameplay.getThread() != null && gameplay.getThread().isAlive()) {
                gameplay.getThread().interrupt();
                gameplay.getThread().join();
            }
            if (gameThread != null && gameThread.isAlive()) {
                gameThread.interrupt();
                gameThread.join();
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    public void goBackToMenu() {
        exitGame();
        remove(gameplay);
        removeKeyListener(menuHandler);
        gameplay = null;
        gameThread = null;
        STATE = GameState.MENU_STATE;
        menuHandler = new MenuKeyboardHandler(this, getWidth(), getHeight());
        addKeyListener(menuHandler);
        repaint();
    }

    public void start() {
        isRunning = true;
        init();
        STATE = GameState.GAME_STATE;
        if (gameplay != null) {
            if (gameThread != null && gameThread.isAlive()) {
                gameThread.interrupt();
            }
            gameThread = new Thread(gameplay);
            gameThread.start();
        }
    }

    public void setIsRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void changeWindowMode(ScreenState state) {
        if (state == ScreenState.fullscreen && current != ScreenState.fullscreen) {
            if (ev.isFullScreenSupported()) {
                ev.setFullScreenWindow(this);
            }
            current = ScreenState.fullscreen;
        }
        if (state == ScreenState.borderless && current != ScreenState.borderless) {
            setUndecorated(true);
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            current = ScreenState.borderless;
        }
        if (state == ScreenState.windowed && current != ScreenState.windowed) {
            // you can choose to make the screen fit or not
            if (ev.isFullScreenSupported()) {
                ev.setFullScreenWindow(null);
            }
            current = ScreenState.windowed;
        }
        System.out.println(getWidth() + " " + getHeight());
    }

    public Thread getGameThread() {
        return gameThread;
    }

}
