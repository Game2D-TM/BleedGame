package fightinggame.input.handler.menu;

import fightinggame.Game;
import fightinggame.Gameplay;
import fightinggame.entity.OptionMenu;
import fightinggame.entity.state.GameState;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.Map;

public class OptionKeyboardHandler implements KeyListener {

    private final Gameplay gameplay;
    private OptionMenu optionMenu;

    public OptionKeyboardHandler(Map<String, BufferedImage> optionGuis, Gameplay gameplay) {
        this.gameplay = gameplay;
        optionMenu = new OptionMenu(optionGuis, gameplay);
    }

    public void tick() {
        optionMenu.tick();
    }

    public void render(Graphics g) {
        optionMenu.render(g);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (Game.STATE == GameState.MENU_STATE) {
            return;
        }
        int optionsIndex, subOptionIndex;
        GameState state;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE, KeyEvent.VK_P:
                state = Game.STATE;
                if (state == GameState.GAME_STATE) {
                    Game.STATE = GameState.OPTION_STATE;
                    optionMenu.setOpen(true);
                } else {
                    Game.STATE = GameState.GAME_STATE;
                    optionMenu.setOpen(false);
                    optionMenu.resetOptionsIndex();
                }
                break;
            case KeyEvent.VK_UP:
                optionsIndex = optionMenu.getOptionIndex();
                if (optionsIndex > 0) {
                    subOptionIndex = optionMenu.getSubOptionIndex();
                    if (subOptionIndex > 0) {
                        subOptionIndex--;
                    } else {
                        subOptionIndex = optionMenu.getButtons().length - 1;
                    }
                    optionMenu.setSubOptionIndex(subOptionIndex);
                }
                break;
            case KeyEvent.VK_DOWN:
                optionsIndex = optionMenu.getOptionIndex();
                if (optionsIndex > 0) {
                    subOptionIndex = optionMenu.getSubOptionIndex();
                    if (subOptionIndex < optionMenu.getButtons().length - 1) {
                        subOptionIndex++;
                    } else {
                        subOptionIndex = 0;
                    }
                    optionMenu.setSubOptionIndex(subOptionIndex);
                }
                break;
            case KeyEvent.VK_LEFT:
                optionsIndex = optionMenu.getOptionIndex();
                if (optionsIndex > 0) {
                    optionsIndex--;
                } else {
                    optionsIndex = optionMenu.getOptions().length - 1;
                }
                optionMenu.setOptionIndex(optionsIndex);
                break;
            case KeyEvent.VK_RIGHT:
                optionsIndex = optionMenu.getOptionIndex();
                if (optionsIndex < optionMenu.getOptions().length - 1) {
                    optionsIndex++;
                } else {
                    optionsIndex = 0;
                }
                optionMenu.setOptionIndex(optionsIndex);
                break;
            case KeyEvent.VK_ENTER:
                optionsIndex = optionMenu.getOptionIndex();
                if (optionsIndex > 0) {
                    subOptionIndex = optionMenu.getSubOptionIndex();
                    switch (subOptionIndex) {
                        case 0:
                            Game.STATE = GameState.GAME_STATE;
                            optionMenu.setOpen(false);
                            optionMenu.resetOptionsIndex();
                            break;
                        case 1:
                            if(optionMenu.isFullscreen()) {
                                gameplay.getGame().changeWindowMode(Game.ScreenState.windowed);
                                gameplay.resolutionChange(1650, 950);
                            } else {
                                gameplay.getGame().changeWindowMode(Game.ScreenState.fullscreen);
                                gameplay.resolutionChange(1680, 1050);
                            }
                            optionMenu.setFullscreen(!optionMenu.isFullscreen());
                            break;
                        case 2:
                            break;
                        case 3:
                            break;
                        case 4:
                            gameplay.getAudioPlayer().closeThread("background_music");
                            gameplay.getGame().goBackToMenu();
                            optionMenu.resetOptionsIndex();
                            break;
                    }
                } else {
                    Game.STATE = GameState.GAME_STATE;
                    optionMenu.setOpen(false);
                    optionMenu.resetOptionsIndex();
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public OptionMenu getOptionMenu() {
        return optionMenu;
    }

    public void setOptionMenu(OptionMenu optionMenu) {
        this.optionMenu = optionMenu;
    }

}
