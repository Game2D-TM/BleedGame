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
    private int chooseIndex = 0;

    public OptionKeyboardHandler(Map<String, BufferedImage> optionGuis, Gameplay gameplay) {
        this.gameplay = gameplay;
        optionMenu = new OptionMenu(optionGuis, gameplay);
        if (Game.current == Game.ScreenState.fullscreen) {
            optionMenu.setFullscreen(true);
        }
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
        if (Game.STATE == GameState.GAME_STATE
                || Game.STATE == GameState.OPTION_STATE) {
            int optionsIndex, subOptionIndex;
            GameState state;
            switch (e.getKeyCode()) {
                case KeyEvent.VK_ESCAPE, KeyEvent.VK_P:
                    if (chooseIndex > 0) {
                        chooseIndex = 0;
                    }
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
                    if (chooseIndex > 0) {
                        chooseIndex = 0;
                    }
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
                    if (chooseIndex > 0) {
                        chooseIndex = 0;
                    }
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
                    if (chooseIndex > 0) {
                        switch (chooseIndex) {
                            case 2:
                                optionMenu.decreaseMusicVolume();
                                break;
                            case 3:
                                optionMenu.decreaseSfxVolume();
                                break;
                        }
                    } else {
                        optionsIndex = optionMenu.getOptionIndex();
                        if (optionsIndex > 0) {
                            optionsIndex--;
                        } else {
                            optionsIndex = optionMenu.getOptions().length - 1;
                        }
                        optionMenu.setOptionIndex(optionsIndex);
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (chooseIndex > 0) {
                        switch (chooseIndex) {
                            case 2:
                                optionMenu.increaseMusicVolume();
                                break;
                            case 3:
                                optionMenu.increaseSfxVolume();
                                break;
                        }
                    } else {
                        optionsIndex = optionMenu.getOptionIndex();
                        if (optionsIndex < optionMenu.getOptions().length - 1) {
                            optionsIndex++;
                        } else {
                            optionsIndex = 0;
                        }
                        optionMenu.setOptionIndex(optionsIndex);
                    }
                    break;
                case KeyEvent.VK_ENTER:
                    optionsIndex = optionMenu.getOptionIndex();
                    if (optionsIndex > 0) {
                        subOptionIndex = optionMenu.getSubOptionIndex();
                        switch (subOptionIndex) {
                            case 0:
                                if (chooseIndex > 0) {
                                    chooseIndex = 0;
                                }
                                Game.STATE = GameState.GAME_STATE;
                                optionMenu.setOpen(false);
                                optionMenu.resetOptionsIndex();
                                break;
                            case 1:
                                if (chooseIndex > 0) {
                                    chooseIndex = 0;
                                }
                                if (optionMenu.isFullscreen()) {
                                    gameplay.getGame().changeWindowMode(Game.ScreenState.windowed);
                                    gameplay.resolutionChange(1650, 950);
                                } else {
                                    gameplay.getGame().changeWindowMode(Game.ScreenState.fullscreen);
                                    gameplay.resolutionChange(1680, 1050);
                                }
                                optionMenu.setFullscreen(!optionMenu.isFullscreen());
                                break;
                            case 2:
                                chooseIndex = 2;
                                break;
                            case 3:
                                chooseIndex = 3;
                                break;
                            case 4:
                                if (chooseIndex > 0) {
                                    chooseIndex = 0;
                                }
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
