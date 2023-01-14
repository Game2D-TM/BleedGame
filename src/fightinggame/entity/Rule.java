package fightinggame.entity;

import fightinggame.Gameplay;
import fightinggame.entity.item.Item;
import fightinggame.entity.platform.Platform;
import fightinggame.resource.DataManager;
import java.awt.Color;
import java.awt.Graphics;
import java.io.File;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Rule {

    private GamePosition victoryPosition;
    private boolean missionComplete;
    private final Gameplay gameplay;
    private int resetGameCounter = 0;
    private int resetGameLimit = 1000;
    private boolean isTransition;
    private Platform firstPlatform;
    private Platform secondPlatform;
    private LocalTime timeLimit;

    public Rule(GamePosition position, Platform firPlatform, Platform secondPlatform, Gameplay gameplay) {
        this.victoryPosition = position;
        this.gameplay = gameplay;
        this.firstPlatform = firPlatform;
        this.secondPlatform = secondPlatform;
        setTimeLimit(7);
    }

    public void tick() {
        Player player = gameplay.getPlayer();
        if (player != null) {
            if (player.isDeath()) {
                resetGameCounter++;
                if (resetGameCounter > resetGameLimit) {
                    File scene = DataManager.getCurrentScene();
                    if (scene == null) {
                        scene = DataManager.getFirstScene(1);
                    }
                    gameplay.loadScene(DataManager.getSceneDataName(scene), scene.getAbsolutePath());
                    resetGameCounter = 0;
                }
            } else {
                if (timeLimit != null) {
                    if (GameTimer.getInstance().countDownEnd(timeLimit)) {
                        player.setIsDeath(true);
                    }
                }
                if (missionComplete) {
                    GamePosition playerHitBox = player.getHitBoxPosition();
                    if (playerHitBox != null) {
                        if (playerHitBox.getXPosition() >= victoryPosition.getXPosition()
                                && playerHitBox.getMaxX() <= victoryPosition.getMaxX()
                                && playerHitBox.getYPosition() >= victoryPosition.getYPosition()
                                && playerHitBox.getMaxY() <= victoryPosition.getMaxY()) {
                            if (!isTransition) {
                                gameplay.getTransitionScreen().startTransitionForward();
                                isTransition = true;
                            }
                            if (!gameplay.getTransitionScreen().isTransition()) {
                                File scene = DataManager.getNextScene();
                                if (scene != null) {
                                    Item key = player.getInventory().getItemByName("Key Item");
                                    if (key != null) {
                                        player.getInventory().removeItemFromInventory(key);
                                    }
                                    missionComplete = false;
                                    gameplay.loadScene(DataManager.getSceneDataName(scene), scene.getAbsolutePath());
                                }
                            }
                        }
                    }
                } else {
                    Item key = player.getInventory().getItemByName("Key Item");
                    if (key != null) {
                        missionComplete = true;
                        isTransition = false;
                    }
                }
            }
        }
    }

    public void render(Graphics g) {
        g.setColor(Color.red);
        g.drawRect(victoryPosition.getXPosition() - gameplay.getCamera().getPosition().getXPosition(),
                victoryPosition.getYPosition() - gameplay.getCamera().getPosition().getYPosition(),
                victoryPosition.getWidth(), victoryPosition.getHeight());
    }

    public GamePosition getVictoryPosition() {
        return victoryPosition;
    }

    public boolean isMissionComplete() {
        return missionComplete;
    }

    public void setMissionComplete(boolean missionComplete) {
        this.missionComplete = missionComplete;
    }

    public List<Platform> getSurroundVictoryPlatforms() {
        if (firstPlatform != null && secondPlatform != null) {
            List<Platform> platforms = new ArrayList<>();
            int row1 = firstPlatform.getRow();
            int column1 = firstPlatform.getColumn();
            int row2 = secondPlatform.getRow();
            int column2 = secondPlatform.getColumn();
            while (true) {
                Platform platform = gameplay.getPlatforms().get(row1).get(column1);
                if (platform != null) {
                    platforms.add(platform);
                }
                if (column1 < column2) {
                    column1++;
                } else {
                    if (row1 < row2) {
                        row1++;
                        column1 = firstPlatform.getColumn();
                    } else {
                        break;
                    }
                }
            }
            return platforms;
        }
        return null;
    }

    public void setTimeLimit(int minutes) {
        timeLimit = GameTimer.getInstance().addMinutes(minutes);
    }

    public void addSecondsTimeLimit(int seconds) {
        timeLimit = timeLimit.plusSeconds(seconds);
    }

    public void addMinutesTimeLimit(int minutes) {
        timeLimit = timeLimit.plusMinutes(minutes);
    }

    public void addHoursTimeLimit(int hours) {
        timeLimit = timeLimit.plusHours(hours);
    }

    public Gameplay getGameplay() {
        return gameplay;
    }

    public int getResetGameCounter() {
        return resetGameCounter;
    }

    public int getResetGameLimit() {
        return resetGameLimit;
    }

    public boolean isTransition() {
        return isTransition;
    }

    public Platform getFirstPlatform() {
        return firstPlatform;
    }

    public Platform getSecondPlatform() {
        return secondPlatform;
    }

    public LocalTime getTimeLimit() {
        return timeLimit;
    }

}
