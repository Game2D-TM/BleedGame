package fightinggame.entity;

import fightinggame.Gameplay;
import fightinggame.entity.item.Item;
import fightinggame.resource.DataManager;
import java.awt.Color;
import java.awt.Graphics;
import java.io.File;

public class Rule {

    private GamePosition victoryPosition;
    private boolean missionComplete;
    private final Gameplay gameplay;
    private int resetGameCounter = 0;
    private int resetGameLimit = 1000;
    private boolean isTransition;

    public Rule(GamePosition position, Gameplay gameplay) {
        this.victoryPosition = position;
        this.gameplay = gameplay;
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
                    gameplay.initScene(DataManager.getSceneDataName(scene), scene.getAbsolutePath());
                    resetGameCounter = 0;
                }
            } else {
                if (missionComplete) {
                    GamePosition playerHitBox = player.getHitBoxPosition();
                    if (playerHitBox != null) {
                        if (playerHitBox.getXPosition() >= victoryPosition.getXPosition()
                                && playerHitBox.getMaxX() <= victoryPosition.getMaxX()
                                && playerHitBox.getYPosition() >= victoryPosition.getYPosition()
                                && playerHitBox.getMaxY() <= victoryPosition.getMaxY()) {
                            if(!isTransition) {
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
                                    gameplay.initScene(DataManager.getSceneDataName(scene), scene.getAbsolutePath());
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

    public GamePosition getPosition() {
        return victoryPosition;
    }

    public void setPosition(GamePosition position) {
        this.victoryPosition = position;
    }

    public boolean isMissionComplete() {
        return missionComplete;
    }

    public void setMissionComplete(boolean missionComplete) {
        this.missionComplete = missionComplete;
    }
}
