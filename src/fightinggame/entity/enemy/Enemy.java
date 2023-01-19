package fightinggame.entity.enemy;

import fightinggame.Gameplay;
import fightinggame.animation.enemy.EnemyHeavyAttack;
import fightinggame.animation.enemy.EnemyLightAttack;
import fightinggame.animation.enemy.EnemyRunBack;
import fightinggame.animation.enemy.EnemyRunForward;
import fightinggame.entity.Animation;
import java.awt.Graphics;
import java.util.Map;
import java.util.Random;
import fightinggame.entity.Character;
import fightinggame.entity.Dialogue;
import fightinggame.entity.state.CharacterState;
import fightinggame.entity.GamePosition;
import fightinggame.entity.HealthBar;
import fightinggame.entity.Player;
import fightinggame.entity.item.Item;
import fightinggame.entity.platform.Platform;
import fightinggame.entity.platform.tile.Tile;
import fightinggame.entity.platform.tile.WallTile;
import fightinggame.resource.ImageManager;
import fightinggame.entity.Entity;
import java.io.File;
import java.util.List;

public abstract class Enemy extends Character {

    public static int DEF_X_VISION_POS = -500;
    public static int DEF_Y_VISION_POS = -100;
    public static int DEF_WIDTH_VISION_POS = 1000;
    public static int DEF_HEIGHT_VISION_POS = 100;
    public static Enemy ENEMY_HEALTHBAR_SHOW;

    protected int deathCounter = 0;
    protected int deathExpireLimit = 1500;
    protected int isAttackedCounter = 0;
    protected int walkCounter = 0;
    protected int walkLimit = 100;
    protected boolean animateChange = false;
    protected int point = 10;
    protected double experience = 0;
    protected int stunTime = 50;
    protected boolean isSpeak;
    protected Dialogue dialogue;
    protected int speakCounter = 0;
    protected int speakTimeDialogueCounter = 0;
    protected File dialogueFile;
    protected int attackCounter = 0;
    protected int attackLimit = 50;

    public Enemy(int id, String name, int health, GamePosition position, Map<CharacterState, Animation> animations,
            Gameplay gameplay, int rangeRandomSpeed) {
        super(id, name, health, position, animations, gameplay, false, null);
        healthBarInit(health);
        healthBar.setOvalImage(new java.awt.geom.Ellipse2D.Float(1530f, 10f, 100, 100));
        healthBar.setAppearTimeLimit(1000);
        Random rand = new Random();
        stats.setHealth(health);
        stats.setSpeed(rand.nextInt(rangeRandomSpeed) + 30);
    }

    @Override
    protected void healthBarInit(int maxHealth) {
        Entity healthBarSheet = new Entity();
        healthBarSheet.setImages(ImageManager.loadImagesWithCutFromFolderToList("assets/res/healthbar",
                1, 2, 126, 12));
        healthBar = new HealthBar(avatar, healthBarSheet, this,
                new GamePosition(975, 20, 550, 80), new GamePosition(1540, 8, 180, 120),
                maxHealth, 100);
    }

    @Override
    public void tick() {
        super.tick();
        healthBar.tick();
        if (isDeath) {
            deathCounter++;
            if (deathCounter >= deathExpireLimit) {
                List<Item> itemsInInventory = inventory.getAllItemsFromInventory();
                if (itemsInInventory != null && itemsInInventory.size() > 0) {
                    for (int i = 0; i < itemsInInventory.size(); i++) {
                        Item item = itemsInInventory.get(i);
                        if (item == null) {
                            continue;
                        }
                        GamePosition itemPos = item.getPosition();
                        if (itemPos == null) {
                            itemPos = new GamePosition(0, 0, Item.ITEM_WIDTH, Item.ITEM_HEIGHT);
                            itemPos.setXPosition(position.getXPosition() + position.getWidth() / 2 - 20);
                            itemPos.setYPosition(position.getMaxY() - itemPos.getHeight());
                            item.setPosition(itemPos);
                        } else {
                            itemPos.setXPosition(position.getXPosition() + position.getWidth() / 2 - 20);
                            itemPos.setYPosition(position.getMaxY() - itemPos.getHeight());
                        }
                        gameplay.getItemsOnGround().add(item);
                        item.setSpawnDrop(true);
                    }
                }
                gameplay.getPlayer().getStats().addExperience(experience);
                gameplay.getPlayer().addScore(point);
                gameplay.getEnemies().remove(this);
                gameplay.getRule().addSecondsTimeLimit(10);
                gameplay.getPlayer().getEnemiesKilled().add(this);
                deathCounter = 0;
            }
        }
        if (isAttacked && !isDeath) {
            isAttackedCounter++;
            if (isAttackedCounter > stunTime) {
                if (isLTR) {
                    currAnimation = animations.get(CharacterState.IDLE_LTR);
                } else {
                    currAnimation = animations.get(CharacterState.IDLE_RTL);
                }
                isAttacked = false;
                isAttackedCounter = 0;
                if (isAttack) {
                    isAttack = false;
                }
            }
        }
        if (!isDeath && !inAir && !fallDown) {
            checkPlayerOnSight();
        }
        if (!isDeath && !isAttacked && !isAttack && !animateChange
                && !inAir && !fallDown) {
            if (walkCounter <= walkLimit) {
                walkCounter++;
            } else {
                if (currAnimation == null) {
                    if (!isLTR) {
                        currAnimation = animations.get(CharacterState.RUNFORWARD);
                    } else {
                        currAnimation = animations.get(CharacterState.RUNBACK);
                    }
                } else {
                    if (!isLTR) {
                        if (currAnimation instanceof EnemyRunForward); else {
                            currAnimation = animations.get(CharacterState.RUNFORWARD);
                        }
                    } else {
                        if (currAnimation instanceof EnemyRunBack); else {
                            currAnimation = animations.get(CharacterState.RUNBACK);
                        }
                    }
                }
                checkInvalidPlatform();
                if (!isLTR) {
                    position.isMoveLeft = true;
                } else {
                    position.isMoveRight = true;
                }
                walkCounter = 0;
            }
        }
        if (dialogue != null) {
            if (checkPlayerOnSight() && !isDeath && !gameplay.getPlayer().isSpeak()) {
                if (isSpeak) {
                    if (speakTimeDialogueCounter <= 500) {
                        speakTimeDialogueCounter++;
                    }
                    dialogue.tick();
                    if (speakTimeDialogueCounter > 500) {
                        dialogue.next();
                        speakTimeDialogueCounter = 0;
                    }
                    if (dialogue.isEndDialogue()) {
                        isSpeak = false;
                        speakTimeDialogueCounter = 0;
                    }
                } else {
                    if (speakCounter <= 100) {
                        speakCounter++;
                    }
                    if (speakCounter > 100) {
                        boolean result = dialogue.loadDialogue(dialogueFile);
                        if (result) {
                            isSpeak = true;
                            speakCounter = 0;
                            dialogue.setEndDialogue(false);
                        }
                    }
                }
            }
            if (Enemy.ENEMY_HEALTHBAR_SHOW != null
                    && this == Enemy.ENEMY_HEALTHBAR_SHOW) {
                if (isDeath) {
                    if (isSpeak) {
                        isSpeak = false;
                        speakTimeDialogueCounter = 0;
                        dialogue.setEndDialogue(true);
                    }
                }
            }
        }
        if (!isAttack && !isAttacked && !isDeath && !animateChange) {
            if (!gameplay.getPlayer().isAttacked()) {
                if (gameplay.getPlayer().checkHit(attackHitBox(), false, null)) {
                    if (isLTR) {
                        if (currAnimation != null && currAnimation instanceof EnemyHeavyAttack); else {
                            currAnimation = animations.get(CharacterState.ATTACK01_LTR);
                        }
                    } else {
                        if (currAnimation != null && currAnimation instanceof EnemyHeavyAttack); else {
                            currAnimation = animations.get(CharacterState.ATTACK01_RTL);
                        }
                    }
                    isAttack = true;
                    gameplay.getPlayer().checkHit(attackHitBox(), isAttack, this);
                }
            }
        }
        if (!isAttacked && !isDeath) {
            if (animateChange) {
                if (attackCounter <= attackLimit) {
                    attackCounter++;
                } else {
                    if (!isLTR) {
                        position.setXPosition(position.getXPosition() + stats.getAttackRange());
                    } else {
                        position.setXPosition(position.getXPosition() - stats.getAttackRange());
                    }
                    position.setWidth(position.getWidth() - stats.getAttackRange());
                    animateChange = false;
                    if (isLTR) {
                        currAnimation = animations.get(CharacterState.IDLE_LTR);
                    } else {
                        currAnimation = animations.get(CharacterState.IDLE_RTL);
                    }
                    attackCounter = 0;
                }
            } else {
                if (isAttack) {
                    if (!isLTR) {
                        position.setXPosition(position.getXPosition() - stats.getAttackRange());
                    } else {
                        position.setXPosition(position.getXPosition() + stats.getAttackRange());
                    }
                    position.setWidth(position.getWidth() + stats.getAttackRange());
                    isAttack = false;
                    animateChange = true;
                }
            }
        }
        if (!healthBar.isCanShow() && this.equals(ENEMY_HEALTHBAR_SHOW)) {
            gameplay.setRenderMap(true);
        }
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        if (healthBar.isCanShow() && this.equals(ENEMY_HEALTHBAR_SHOW)) {
            healthBar.render(g);
        }
        // vision hitbox
//        g.setColor(Color.red);
//        g.drawRect(position.getXPosition() + DEF_X_VISION_POS - gameplay.getCamera().getPosition().getXPosition(),
//                position.getYPosition() + DEF_Y_VISION_POS - gameplay.getCamera().getPosition().getYPosition(),
//                position.getWidth() + DEF_WIDTH_VISION_POS,
//                getHeightHitBox() + DEF_HEIGHT_VISION_POS);

    }

    public void setDefAttackedCounter() {
        isAttackedCounter = 0;
    }

    public boolean checkPlayerOnSight() {
        Player player = gameplay.getPlayer();
        if (player == null || player.isDeath()) {
            return false;
        }
        GamePosition visionPos = getVisionPosition();
        GamePosition playerPos = gameplay.getPlayer().getHitBoxPosition();
        if (visionPos == null || playerPos == null) {
            return false;
        }
        if (((playerPos.getXPosition() >= visionPos.getXPosition() && playerPos.getMaxX() <= visionPos.getMaxX())
                || (playerPos.getXPosition() >= visionPos.getXPosition() && playerPos.getXPosition() <= visionPos.getMaxX()
                && playerPos.getMaxX() > visionPos.getMaxX())
                || (playerPos.getMaxX() >= visionPos.getXPosition() && playerPos.getMaxX() <= visionPos.getMaxX()
                && playerPos.getXPosition() < visionPos.getXPosition()))
                && (playerPos.getYPosition() >= visionPos.getYPosition() && playerPos.getMaxY() <= visionPos.getMaxY())) {
            if (playerPos.getMaxX() < getXHitBox()) {
                isLTR = false;
            } else if (playerPos.getXPosition() > getXMaxHitBox()) {
                isLTR = true;
            }
            return true;
        }
        return false;
    }

    public GamePosition getVisionPosition() {
        return new GamePosition(position.getXPosition() + DEF_X_VISION_POS,
                position.getYPosition() + DEF_Y_VISION_POS,
                position.getWidth() + DEF_WIDTH_VISION_POS, getHeightHitBox() + DEF_HEIGHT_VISION_POS);
    }

    public boolean checkInvalidPlatform() {
        if (insidePlatform != null) {
            try {
                int row = insidePlatform.getRow();
                int column = insidePlatform.getColumn();
                if (row >= 0) {
                    if (!isLTR) {
                        int nColumn = column - 1;
                        Platform platform = gameplay.getPlatforms().get(row).get(nColumn);
                        if (platform != null) {
                            if (platform instanceof WallTile || platform instanceof Tile) {
                                if (platform.checkValidPosition(
                                        new GamePosition(getXHitBox() - stats.getSpeed(), getYHitBox(), getWidthHitBox(), getHeightHitBox()))) {
                                    isLTR = true;
                                    return true;
                                }
                            }
                            platform = gameplay.getPlatforms().get(row + 1).get(nColumn);
                            if (platform != null) {
                                if (!platform.isCanStand()) {
                                    isLTR = true;
                                    return true;
                                }
                            }
                        }
                    } else {
                        int nColumn = column + 1;
                        Platform platform = gameplay.getPlatforms().get(row).get(nColumn);
                        if (platform != null) {
                            if (platform instanceof WallTile || platform instanceof Tile) {
                                if (platform.checkValidPosition(
                                        new GamePosition(getXHitBox() + stats.getSpeed(), getYHitBox(), getWidthHitBox(), getHeightHitBox()))) {
                                    isLTR = false;
                                    return true;
                                }
                            }
                            platform = gameplay.getPlatforms().get(row + 1).get(nColumn);
                            if (platform != null) {
                                if (!platform.isCanStand()) {
                                    isLTR = false;
                                    return true;
                                }
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                if (!isLTR) {
                    isLTR = true;
                } else {
                    isLTR = false;
                }
            }
        }
        return false;
    }

    @Override
    public void healthBarTick() {

    }

    @Override
    public boolean checkHit(GamePosition attackHitBox, boolean isAttack,
            Character character, int attackDamage) {
        if (isAttack && !isDeath) {
            if (((attackHitBox.getXPosition() >= getXHitBox() && attackHitBox.getXPosition() <= getXMaxHitBox())
                    || (attackHitBox.getXPosition() >= getXHitBox() && attackHitBox.getXPosition() <= getXMaxHitBox()
                    && attackHitBox.getMaxX() > getXMaxHitBox())
                    || (attackHitBox.getMaxX() >= getXHitBox() && attackHitBox.getMaxX() <= getXMaxHitBox()
                    && attackHitBox.getXPosition() < getXHitBox())
                    || (attackHitBox.getXPosition() < getXHitBox() && attackHitBox.getMaxX() > getXMaxHitBox()))
                    && ((attackHitBox.getYPosition() <= getYHitBox() && attackHitBox.getYPosition() >= getYMaxHitBox()
                    || (attackHitBox.getYPosition() >= getYHitBox() && attackHitBox.getMaxY() <= getYMaxHitBox())
                    || (attackHitBox.getYPosition() > getYHitBox() && attackHitBox.getYPosition() <= getYMaxHitBox()
                    && attackHitBox.getMaxY() > getYMaxHitBox())
                    || (attackHitBox.getMaxY() > getYHitBox() && attackHitBox.getMaxY() <= getYMaxHitBox()
                    && attackHitBox.getYPosition() < getYHitBox())))) {
                setDefAttackedCounter();
                if (ENEMY_HEALTHBAR_SHOW != null) {
                    ENEMY_HEALTHBAR_SHOW.getHealthBar().resetShowCounter();
                }
                ENEMY_HEALTHBAR_SHOW = this;
                healthBar.setCanShow(true);
                gameplay.setRenderMap(false);
                isAttacked = true;
                if (attackDamage == -1) {
                    receiveDamage = stats.getHit(character.getStats());
                } else {
                    receiveDamage = stats.getHit(character.getStats(), attackDamage);
                }
                if (character.isLTR()) {
                    position.setXPosition(position.getXPosition() + character.getStats().getBounceRange());
                } else {
                    position.setXPosition(position.getXPosition() - character.getStats().getBounceRange());
                }
                if (stats.getHealth() <= 0) {
                    isDeath = true;
                    if (isLTR) {
                        currAnimation = animations.get(CharacterState.DEATH_LTR);
                    } else {
                        currAnimation = animations.get(CharacterState.DEATH_RTL);
                    }
                } else {
                    if (character.isLTR()) {
                        if (currAnimation != null
                                && (currAnimation instanceof EnemyHeavyAttack
                                || currAnimation instanceof EnemyLightAttack)); else {
                            currAnimation = animations.get(CharacterState.GET_HIT_RTL);
                        }
                        isLTR = false;
                    } else {
                        if (currAnimation != null
                                && (currAnimation instanceof EnemyHeavyAttack
                                || currAnimation instanceof EnemyLightAttack)); else {
                            currAnimation = animations.get(CharacterState.GET_HIT_LTR);
                        }
                        isLTR = true;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public double getExperience() {
        return experience;
    }

    public void addExperience(double experience) {
        this.experience += experience;
    }

    public int getPoint() {
        return point;
    }

    public void addPoint(int point) {
        this.point += point;
    }

    public int getStunTime() {
        return stunTime;
    }

    public void setStunTime(int stunTime) {
        this.stunTime = stunTime;
    }

    public boolean isSpeak() {
        return isSpeak;
    }

    public void setIsSpeak(boolean isSpeak) {
        this.isSpeak = isSpeak;
    }

    public int getSpeakCounter() {
        return speakCounter;
    }

    public void setSpeakCounter(int speakCounter) {
        this.speakCounter = speakCounter;
    }

    public int getSpeakTimeDialogueCounter() {
        return speakTimeDialogueCounter;
    }

    public void setSpeakTimeDialogueCounter(int speakTimeDialogueCounter) {
        this.speakTimeDialogueCounter = speakTimeDialogueCounter;
    }

}
