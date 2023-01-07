package fightinggame.input.handler.menu;

import fightinggame.Game;
import fightinggame.entity.state.GameState;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MenuKeyboardHandler implements KeyListener {

    private final Game game;
    private String[] buttons = {"Start", "Options", "Quit"};
    private int currIndex = 0;

    public MenuKeyboardHandler(Game game) {
        this.game = game;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (Game.STATE == GameState.MENU_STATE) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    if (currIndex > 0) {
                        currIndex--;
                    } else {
                        currIndex = 2;
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (currIndex < 2) {
                        currIndex++;
                    } else {
                        currIndex = 0;
                    }
                    break;
                case KeyEvent.VK_ENTER:
                    String button = buttons[currIndex];
                    switch (button) {
                        case "Start":
                            game.start();
                            break;
                        case "Options":
                            break;
                        case "Quit":
                            game.dispose();
                            break;
                    }
                    break;
            }
            String button = buttons[currIndex];
            System.out.println(button);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
