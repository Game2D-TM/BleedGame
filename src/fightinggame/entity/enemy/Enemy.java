package fightinggame.entity.enemy;

import fightinggame.Gameplay;
import fightinggame.animation.enemy.EnemyRunBack;
import fightinggame.animation.enemy.EnemyRunForward;
import fightinggame.entity.Animation;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Random;
import fightinggame.entity.Character;
import fightinggame.entity.CharacterState;
import fightinggame.entity.GamePosition;
import fightinggame.entity.HealthBar;
import fightinggame.entity.Stats;
import fightinggame.entity.inventory.Inventory;
import fightinggame.entity.item.Item;
import fightinggame.entity.platform.Platform;
import fightinggame.entity.platform.tile.Tile;
import fightinggame.entity.platform.tile.WallTile;
import fightinggame.resource.ImageManager;
import fightinggame.resource.SpriteSheet;
import java.util.List;

public class Enemy extends Character {

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
        stats.setSpeed(rand.nextInt(range));
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
                if (!isLTR) {
//                    if (position.getXPosition() >= gameplay.getPlayPosition().getXPosition()) {
//                        position.moveLeft(speed, true);
//                    } else {
//                        isLTR = true;
//                    }
                    position.isMoveLeft = true;
                } else {
//                    if (position.getXPosition() + position.getWidth() <= gameplay.getPlayPosition().getXPosition()
//                            + gameplay.getPlayPosition().getWidth()) {
//                        position.moveRight(speed, true);
//                    } else {
//                        isLTR = false;
//                    }
                    position.isMoveRight = true;
                }
                checkNextToWall();
                walkCounter = 0;
            }
        }
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        if (healthBar.isCanShow() && this.equals(ENEMY_HEALTHBAR_SHOW)) {
            healthBar.render(g);
        }
//        g.setColor(Color.red);
//        g.drawRect(position.getXPosition() - 400, position.getYPosition() - 100,
//                position.getXPosition() + position.getWidth(),
//                position.getHeight() + 200);

        //hitbox
//        g.setColor(Color.red);
//        g.drawRect(getXHitBox(), getYHitBox(),
//                position.getWidth(), position.getHeight() / 2 - 10);
    }

    public void setDefAttackedCounter() {
        isAttackedCounter = 0;
    }

    public boolean checkNextToWall() {
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
                                isLTR = true;
                                return true;
                            }
                        }
                    } else {
                        int nColumn = column + 1;
                        Platform platform = gameplay.getPlatforms().get(row).get(nColumn);
                        if (platform != null) {
                            if (platform instanceof WallTile || platform instanceof Tile) {
                                isLTR = false;
                                return true;
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
    public boolean checkHit(int attackX, int attackY, int attackHeight, boolean isAttack, Stats attackerStats) {
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
                if (isLTR) {
                    currAnimation = animations.get(CharacterState.GET_HIT_LTR);
                } else {
                    currAnimation = animations.get(CharacterState.GET_HIT_RTL);
                }
                isAttacked = true;
                receiveDamage = stats.getHit(attackerStats);
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
                if (isLTR) {
                    currAnimation = animations.get(CharacterState.GET_HIT_LTR);
                } else {
                    currAnimation = animations.get(CharacterState.GET_HIT_RTL);
                }
                isAttacked = true;
                if(attackDamage == -1) {
                    receiveDamage = stats.getHit(attackerStats);
                } else {
                    receiveDamage = stats.getHit(attackerStats, attackDamage);
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
    
    public int getXHitBox() {
        return position.getXPosition();
    }

    public int getXMaxHitBox() {
        return position.getMaxX();
    }

    public int getYHitBox() {
        return position.getYPosition() + position.getHeight() / 3 - 10;
    }

    public int getYMaxHitBox() {
        return getYHitBox() + position.getHeight() / 2 - 10;
    }

    public int getWidthHitBox() {
        return position.getWidth() / 3;
    }
    
    public int getHeightHitBox() {
        return position.getHeight() - 48;
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

}
