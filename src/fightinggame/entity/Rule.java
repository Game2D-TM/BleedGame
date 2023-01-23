package fightinggame.entity;

import fightinggame.Gameplay;
import fightinggame.entity.platform.Platform;
import fightinggame.entity.quest.Quest;
import fightinggame.entity.quest.QuestRequired;
import fightinggame.entity.state.QuestState;
import fightinggame.resource.DataManager;
import fightinggame.resource.ImageManager;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private int sceneTimeLimit;
    private final Map<String, Quest> quests = new HashMap<>();
    private Map<String, BufferedImage> questImages;
    private boolean isRenderQuest = true;

    public Rule(GamePosition position, Platform firPlatform, Platform secondPlatform, Gameplay gameplay) {
        this.victoryPosition = position;
        this.gameplay = gameplay;
        this.firstPlatform = firPlatform;
        this.secondPlatform = secondPlatform;
        questImages = ImageManager.loadImagesFromFolderToMap("assets/res/gui/quest");
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
                    gameplay.getAudioPlayer().closeThread("background_music");
                    gameplay.getPlayer().resetEnemiesKilled();
                    gameplay.loadScene(DataManager.getSceneDataName(scene), scene.getAbsolutePath());
                    resetGameCounter = 0;
                }
            } else {
                if (timeLimit != null) {
                    if (GameTimer.getInstance().countDownEnd(timeLimit)) {
//                        player.setIsDeath(true);
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
                                    missionComplete = false;
                                    gameplay.getAudioPlayer().closeThread("background_music");
                                    gameplay.getPlayer().resetEnemiesKilled();
                                    gameplay.loadScene(DataManager.getSceneDataName(scene), scene.getAbsolutePath());
                                }
                            }
                        }
                    }
                } else {
                    if (quests != null && quests.size() > 0) {
                        boolean completeAllQuest = true;
                        for (String id : quests.keySet()) {
                            Quest quest = quests.get(id);
                            if (quest != null) {
                                if (quest.getState() == QuestState.ON_GOING) {
                                    quest.tick();
                                    completeAllQuest = quest.isComplete();
                                }
                            }
                        }
                        if (completeAllQuest) {
                            missionComplete = true;
                            isTransition = false;
                        }
                    } else {
                        missionComplete = true;
                        isTransition = false;
                    }
                }
            }
        }
    }

    public void render(Graphics g) {
        if (!gameplay.isHideGUI()) {
            g.setFont(DataManager.getFont(50f));
            g.setColor(new Color(133, 0, 0));
            g.drawString(GameTimer.getInstance().countDownString(timeLimit, GameTimer.FORMAT_MS),
                    gameplay.getWidth() / 2 - 50, 80);
            if (isRenderQuest) {
                if (quests.size() > 0) {
                    g.drawImage(questImages.get("page"), gameplay.getWidth() - 260, 270,
                            250, 300, null);
                    g.drawImage(questImages.get("title"), gameplay.getWidth() - 260, 270,
                            250, 50, null, null);
                    g.setFont(DataManager.getFont(30f));
                    g.setColor(new Color(133, 0, 0));
                    g.drawString("Quest", gameplay.getWidth() - 170, 310);
                    g.setFont(DataManager.getFont(20f));
                    for (String id : quests.keySet()) {
                        Quest quest = quests.get(id);
                        if (quest != null) {
                            List<QuestRequired> requireds = quest.getRequireds();
                            if (requireds != null && requireds.size() > 0) {
                                int yTitle = 350;
                                int yCheck = 335;
                                for (int i = 0; i < requireds.size(); i++) {
                                    if (i > 4) {
                                        break;
                                    }
                                    QuestRequired required = requireds.get(i);
                                    if (required != null) {
                                        g.drawString(required.toString(),
                                                gameplay.getWidth() - 220, yTitle);
                                        if (required.isFinished()) {
                                            g.drawImage(questImages.get("check"), gameplay.getWidth() - 245, yCheck,
                                                    15, 15, null, null);
                                        }
                                        yTitle += 50;
                                        yCheck += 50;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        // victory position hitbox
//        g.setColor(Color.red);
//        g.drawRect(victoryPosition.getXPosition() - gameplay.getCamera().getPosition().getXPosition(),
//                victoryPosition.getYPosition() - gameplay.getCamera().getPosition().getYPosition(),
//                victoryPosition.getWidth(), victoryPosition.getHeight());
    }

    public void addQuest(Quest quest) {
        if (quest == null) {
            return;
        }
        quest.setState(QuestState.ON_GOING);
        quests.putIfAbsent(quest.getId(), quest);
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

    public void subtractSecondsTimeLimit(int seconds) {
        timeLimit = timeLimit.minusSeconds(seconds);
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

    public boolean isRenderQuest() {
        return isRenderQuest;
    }

    public void setIsRenderQuest(boolean isRenderQuest) {
        this.isRenderQuest = isRenderQuest;
    }

    public int getSceneTimeLimit() {
        return sceneTimeLimit;
    }

    public void setSceneTimeLimit(int sceneTimeLimit) {
        this.sceneTimeLimit = sceneTimeLimit;
    }

}
