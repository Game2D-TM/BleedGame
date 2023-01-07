package fightinggame.input.handler.menu;

import fightinggame.Game;
import fightinggame.Gameplay;
import fightinggame.entity.state.GameState;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class OptionKeyboardHandler implements KeyListener {

    private final Gameplay gameplay;
    public OptionKeyboardHandler(Gameplay gameplay) {
        this.gameplay = gameplay;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(Game.STATE == GameState.MENU_STATE){
            return;
        }
        switch (e.getKeyCode()) {
            case KeyEvent.VK_P:
                GameState state = Game.STATE;
                if (state == GameState.GAME_STATE) {
                    Game.STATE = GameState.OPTION_STATE;
                } else {
                    Game.STATE = GameState.GAME_STATE;
                }
                break;
            case KeyEvent.VK_Q:
                gameplay.getAudioPlayer().closeThread("background_music");
                gameplay.getGame().goBackToMenu();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
