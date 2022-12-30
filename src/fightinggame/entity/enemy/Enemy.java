package fightinggame.entity.enemy;

import fightinggame.Gameplay;
import fightinggame.animation.enemy.*;
import fightinggame.entity.Animation;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Random;
import fightinggame.entity.Character;
import fightinggame.entity.CharacterState;
import fightinggame.entity.GamePosition;
import fightinggame.entity.HealthBar;
import fightinggame.entity.Player;
import fightinggame.entity.Stats;
import fightinggame.entity.inventory.Inventory;
import fightinggame.entity.item.Item;
import fightinggame.entity.platform.Platform;
import fightinggame.entity.platform.tile.Tile;
import fightinggame.entity.platform.tile.WallTile;
import fightinggame.resource.ImageManager;
import fightinggame.resource.SpriteSheet;
import java.util.List;

public abstract class Enemy extends Character {

    public static int DEF_X_VISION_POS = -500;
    public static int DEF_Y_VISION_POS = -100;
    public static int DEF_WIDTH_VISION_POS = 1000;
    public static int DEF_HEIGHT_VISION_POS = 100;
    public static Enemy ENEMY_HEALTHBAR_SHOW;
    protected int deathCounter = 0;
    protected int isAttackedCounter = 0;
    protected int walkCounter = 0;
    protected boolean animateChange = false;
    protected int point = 10;
    protected double experience = 0;
    protected int stunTime = 300;

    public Enemy(int id, String name, int health, GamePosition position, Map<CharacterState, Animation> animations, Map<String, BufferedImage> characterAssets,
            Gameplay gameplay, int rangeRandomSpeed, SpriteSheet inventorySheet) {
        super(id, name, health, position, animations, characterAssets, gameplay, false, inventorySheet);
        healthBarInit(health);
        healthBar.setOvalImage(new java.awt.geom.Ellipse2D.Float(1530f, 10f, 100, 100));
        healthBar.setAppearTimeLimit(1000);
        Random rand = new Random();
        int range = rand.nextInt(rangeRandomSpeed);
        stats.setHealth(health);
        stats.setSpeed(rand.nextInt(range) + 1);
    }

    @Override
    protected void healthBarInit(int maxHealth) {
        SpriteSheet healthBarSheet = new SpriteSheet();
        healthBarSheet.setImages(ImageManager.loadImagesWithCutFromFolderToList("assets/res/healthbar",
                1, 2, 126, 12));
        healthBar = new HealthBar(avatar, healthBarSheet, this,
                new GamePosition(975, 20, 550, 80), new GamePosition(1540, 8, 180, 120),
                maxHealth);
    }

    @Override
    public void tick() {
        super.tick();
        healthBar.tick();
        if (isDeath) {
            deathCounter++;
            if (deathCounter >= 1500) {
                List<Item> itemsInInventory = inventory.getAllItemsFromInventory();
                if (itemsInInventory != null && itemsInInventory.size() > 0) {
                    for (int i = 0; i < itemsInInventory.size(); i++) {
                        Item item = itemsInInventory.get(i);
                        if (item == null) {
                            continue;
                        }
                        GamePosition itemPos = item.getPosition();
                        if (itemPos == null) {
                            itemPos = new GamePosition(0, 0, Inventory.ITEM_WIDTH, Inventory.ITEM_HEIGHT);
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
                gameplay.getPlayer().addPoint(point);
                gameplay.getEnemies().remove(this);
                gameplay.getPositions().remove(name);
                deathCounter = 0;
            }
        }
        if (isAttacked && !isDeath) {
            isAttackedCounter++;
            if (isAttackedCounter >= stunTime) {
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
        if (!isDeath) {
            checkPlayerOnSight();
        }
        if (!isDeath && !isAttacked && !isAttack && !animateChange) {
            walkCounter++;
            if (walkCounter >= 100) {
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
        if (player.isDeath()) {
            return false;
        }
        GamePosition visionPos = getVisionPosition();
        GamePosition playerPos = gameplay.getPlayer().getHitBoxPosition();
        if (visionPos == null || playerPos == null || player == null) {
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
    public boolean checkHit(int attackX, int attackY, int attackHeight, boolean isAttack, Stats attackerStats, int attackDamage) {
        int attackMaxY = attackY + attackHeight;
        if (isAttack && !isDeath) {
            if (attackX >= getXHitBox() && attackX <= getXMaxHitBox()
                    && ((attackY <= getYHitBox() && attackMaxY >= getYMaxHitBox()
                    || (attackY >= getYHitBox() && attackMaxY <= getYMaxHitBox())
                    || (attackY > getYHitBox() && attackY <= getYMaxHitBox()
                    && attackMaxY > getYMaxHitBox())
                    || (attackMaxY > getYHitBox() && attackMaxY <= getYMaxHitBox()
                    && attackY < getYHitBox())))) {
                setDefAttackedCounter();
                if (ENEMY_HEALTHBAR_SHOW != null) {
                    ENEMY_HEALTHBAR_SHOW.getHealthBar().resetShowCounter();
                }
                ENEMY_HEALTHBAR_SHOW = this;
                healthBar.setCanShow(true);
                gameplay.setRenderMap(false);
                if (isLTR) {
                    currAnimation = animations.get(CharacterState.GET_HIT_LTR);
                } else {
                    currAnimation = animations.get(CharacterState.GET_HIT_RTL);
                }
                isAttacked = true;
                if (attackDamage == -1) {
                    receiveDamage = stats.getHit(attackerStats);
                } else {
                    receiveDamage = stats.getHit(attackerStats, attackDamage);
                }
                if (isLTR) {
                    position.setXPosition(position.getXPosition() - 30);
                } else {
                    position.setXPosition(position.getXPosition() + 30);
                }
                if (stats.getHealth() <= 0) {
                    isDeath = true;
                    if (isLTR) {
                        currAnimation = animations.get(CharacterState.DEATH_LTR);
                    } else {
                        currAnimation = animations.get(CharacterState.DEATH_RTL);
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

    @Override
    public abstract int getXHitBox();

    @Override
    public abstract int getWidthHitBox();

    @Override
    public abstract int getHeightHitBox();

    @Override
    public abstract int getXMaxHitBox();

    @Override
    public abstract int getYHitBox();

    @Override
    public abstract int getYMaxHitBox();

}
