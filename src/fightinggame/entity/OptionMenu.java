package fightinggame.entity;

import fightinggame.Gameplay;
import fightinggame.resource.DataManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Map;

public class OptionMenu {

    public static int OPTION_WIDTH = 1000;
    public static int OPTION_HEIGHT = 800;
    public static int DISTANCE_CAMERA_X = 400;
    public static int DISTANCE_CAMERA_Y = 100;

    private boolean open;
    private Map<String, BufferedImage> optionGuis;
    private boolean fullscreen;
    private BufferedImage fullscreenCheck;
    private BufferedImage resumeCheck;
    private BufferedImage backToMenuCheck;
    private float musicVolume;
    private float sfxVolume;
    private GamePosition mainPosition;
    private GamePosition selectPosition;
    private final Gameplay gameplay;
    private String[] buttons = {"resume", "fullscreen", "music_volume", "sfx_volume", "to_menu"};
    private String[] options = {"exit", "sub_options"};
    private int subOptionIndex = 0;
    private int optionIndex = 0;
    private Font customFont;

    public OptionMenu(Map<String, BufferedImage> optionGuis, Gameplay gameplay) {
        this.optionGuis = optionGuis;
        this.gameplay = gameplay;
        mainPosition = new GamePosition(0, 0, OPTION_WIDTH, OPTION_HEIGHT);
        fullscreenCheck = optionGuis.get("wrong");
        backToMenuCheck = optionGuis.get("empty");
        resumeCheck = optionGuis.get("empty");
        customFont = DataManager.getFont(60);
    }

    public void tick() {
        if (open) {
            mainPosition.setXPosition(gameplay.getCamera().getPosition().getXPosition() + DISTANCE_CAMERA_X);
            mainPosition.setYPosition(gameplay.getCamera().getPosition().getYPosition() + DISTANCE_CAMERA_Y);
            switch (optionIndex) {
                case 0:
                    resumeCheck = optionGuis.get("empty");
                    backToMenuCheck = optionGuis.get("empty");
                    if (fullscreen) {
                        fullscreenCheck = optionGuis.get("correct");
                    } else {
                        fullscreenCheck = optionGuis.get("wrong");
                    }
                    selectPosition = new GamePosition(mainPosition.getXPosition() + 15,
                            mainPosition.getYPosition() + 15, 70, 70);
                    break;
                case 1:
                    if (subOptionIndex == 0) {
                        resumeCheck = optionGuis.get("correct");
                    } else {
                        resumeCheck = optionGuis.get("empty");
                    }
                    if (subOptionIndex == 4) {
                        backToMenuCheck = optionGuis.get("correct");
                    } else {
                        backToMenuCheck = optionGuis.get("empty");
                    }
                    if (subOptionIndex == 1) {
                        if (fullscreen) {
                            fullscreenCheck = optionGuis.get("wrong");
                        } else {
                            fullscreenCheck = optionGuis.get("correct");
                        }
                    } else {
                        if (fullscreen) {
                            fullscreenCheck = optionGuis.get("correct");
                        } else {
                            fullscreenCheck = optionGuis.get("wrong");
                        }
                    }
                    switch (subOptionIndex) {
                        case 0:
                            selectPosition = new GamePosition(mainPosition.getXPosition() + 875,
                                    mainPosition.getYPosition() + 45, 80, 80);
                            break;
                        case 1:
                            selectPosition = new GamePosition(mainPosition.getXPosition() + 875,
                                    mainPosition.getYPosition() + 125, 80, 80);
                            break;
                        case 2:
                            selectPosition = new GamePosition(mainPosition.getXPosition() + 585,
                                    mainPosition.getYPosition() + 223, 375, 40);
                            break;
                        case 3:
                            selectPosition = new GamePosition(mainPosition.getXPosition() + 585,
                                    mainPosition.getYPosition() + 303, 375, 40);
                            break;
                        case 4:
                            selectPosition = new GamePosition(mainPosition.getXPosition() + 495 + 100 + 350 - 70,
                                    mainPosition.getYPosition() + 365, 80, 80);
                            break;
                    }
                    break;
            }
        }
    }

    public void render(Graphics g) {
        if (open) {
            if (optionGuis != null) {
                if (mainPosition != null) {
                    // Image Frame
                    g.drawImage(optionGuis.get("main_paper"), mainPosition.getXPosition() - gameplay.getCamera().getPosition().getXPosition(),
                            mainPosition.getYPosition() - gameplay.getCamera().getPosition().getYPosition(),
                            mainPosition.getWidth(), mainPosition.getHeight(), null);
                    g.setColor(Color.white);
                    g.drawImage(optionGuis.get("bottom_paper"), mainPosition.getXPosition() + 90 - gameplay.getCamera().getPosition().getXPosition(),
                            mainPosition.getYPosition() + mainPosition.getHeight() - 100 - gameplay.getCamera().getPosition().getYPosition(),
                            mainPosition.getWidth() - 90, 100, null);
                    g.drawImage(optionGuis.get("side_paper"), mainPosition.getXPosition() - gameplay.getCamera().getPosition().getXPosition(),
                            mainPosition.getYPosition() - gameplay.getCamera().getPosition().getYPosition(),
                            100, mainPosition.getHeight(), null);
                    g.drawImage(optionGuis.get("wrong"), mainPosition.getXPosition() + 20 - gameplay.getCamera().getPosition().getXPosition(),
                            mainPosition.getYPosition() + 20 - gameplay.getCamera().getPosition().getYPosition(),
                            60, 60, null);

                    // Titles
//                    g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 60));
                    g.setFont(customFont);
                    g.drawString("Resume", mainPosition.getXPosition() + 150 - gameplay.getCamera().getPosition().getXPosition(),
                            mainPosition.getYPosition() + 100 - gameplay.getCamera().getPosition().getYPosition());
                    g.drawString("Fullscreen", mainPosition.getXPosition() + 150 - gameplay.getCamera().getPosition().getXPosition(),
                            mainPosition.getYPosition() + 180 - gameplay.getCamera().getPosition().getYPosition());
                    g.drawString("Music Volume", mainPosition.getXPosition() + 150 - gameplay.getCamera().getPosition().getXPosition(),
                            mainPosition.getYPosition() + 260 - gameplay.getCamera().getPosition().getYPosition());
                    g.drawString("SFX Volume", mainPosition.getXPosition() + 150 - gameplay.getCamera().getPosition().getXPosition(),
                            mainPosition.getYPosition() + 340 - gameplay.getCamera().getPosition().getYPosition());
                    g.drawString("Back To Main Menu", mainPosition.getXPosition() + 150 - gameplay.getCamera().getPosition().getXPosition(),
                            mainPosition.getYPosition() + 420 - gameplay.getCamera().getPosition().getYPosition());

                    // Choose Box
                    g.drawImage(resumeCheck, mainPosition.getXPosition() + 880 - gameplay.getCamera().getPosition().getXPosition(),
                            mainPosition.getYPosition() + 50 - gameplay.getCamera().getPosition().getYPosition(), 70, 70, null);
                    g.drawImage(fullscreenCheck, mainPosition.getXPosition() + 880 - gameplay.getCamera().getPosition().getXPosition(),
                            mainPosition.getYPosition() + 130 - gameplay.getCamera().getPosition().getYPosition(), 70, 70, null);
                    g.drawImage(optionGuis.get("progress_bar"), mainPosition.getXPosition() + 600 - gameplay.getCamera().getPosition().getXPosition(),
                            mainPosition.getYPosition() + 228 - gameplay.getCamera().getPosition().getYPosition(), 350, 30, null);
                    g.drawImage(optionGuis.get("progress_bar"), mainPosition.getXPosition() + 600 - gameplay.getCamera().getPosition().getXPosition(),
                            mainPosition.getYPosition() + 308 - gameplay.getCamera().getPosition().getYPosition(), 350, 30, null);
                    g.drawImage(backToMenuCheck, mainPosition.getXPosition() + 880 - gameplay.getCamera().getPosition().getXPosition(),
                            mainPosition.getYPosition() + 370 - gameplay.getCamera().getPosition().getYPosition(), 70, 70, null);
                    if (selectPosition != null) {
                        g.drawImage(optionGuis.get("selection"), selectPosition.getXPosition() - gameplay.getCamera().getPosition().getXPosition(),
                                selectPosition.getYPosition() - gameplay.getCamera().getPosition().getYPosition(), selectPosition.getWidth(), selectPosition.getHeight(), null);
                    }
                }
            }
        }
    }

    public void resetOptionsIndex() {
        optionIndex = 0;
        subOptionIndex = 0;
    }

    public void setOpen(boolean isOpen) {
        open = isOpen;
    }

    public Map<String, BufferedImage> getOptionGuis() {
        return optionGuis;
    }

    public void setOptionGuis(Map<String, BufferedImage> optionGuis) {
        this.optionGuis = optionGuis;
    }

    public String[] getButtons() {
        return buttons;
    }

    public void setButtons(String[] buttons) {
        this.buttons = buttons;
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
        if (fullscreen) {
            fullscreenCheck = optionGuis.get("correct");
        } else {
            fullscreenCheck = optionGuis.get("wrong");
        }
    }

    public float getMusicVolume() {
        return musicVolume;
    }

    public void setMusicVolume(float musicVolume) {
        this.musicVolume = musicVolume;
    }

    public float getSfxVolume() {
        return sfxVolume;
    }

    public void setSfxVolume(float sfxVolume) {
        this.sfxVolume = sfxVolume;
    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public GamePosition getMainPosition() {
        return mainPosition;
    }

    public void setMainPosition(GamePosition mainPosition) {
        this.mainPosition = mainPosition;
    }

    public boolean isOpen() {
        return open;
    }

    public boolean isClose() {
        return open;
    }

    public GamePosition getSelectPosition() {
        return selectPosition;
    }

    public void setSelectPosition(GamePosition selectPosition) {
        this.selectPosition = selectPosition;
    }

    public int getSubOptionIndex() {
        return subOptionIndex;
    }

    public void setSubOptionIndex(int subOptionIndex) {
        this.subOptionIndex = subOptionIndex;
    }

    public int getOptionIndex() {
        return optionIndex;
    }

    public void setOptionIndex(int optionIndex) {
        this.optionIndex = optionIndex;
    }

}
