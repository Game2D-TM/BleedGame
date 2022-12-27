package fightinggame.entity;

import fightinggame.Gameplay;
import fightinggame.animation.player.PlayerAttack;
import fightinggame.animation.player.PlayerRun;
import fightinggame.entity.ability.Ability;
import fightinggame.resource.ImageManager;
import fightinggame.resource.SpriteSheet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Map;

public class Player extends Character {

    private int isStunCounter = 0;
    private int point = 0;

    public Player(int id, String name, int health, GamePosition position,
            Map<CharacterState, Animation> animations, Map<String, BufferedImage> characterAssets,
            Gameplay gameplay, SpriteSheet inventorySheet) {
        super(id, name, health, position, animations, characterAssets, gameplay, true, inventorySheet);
        healthBarInit(health);
        healthBar.setOvalImage(new java.awt.geom.Ellipse2D.Float(25f, 10f, 100, 100));
        stats.setSpeed(2);
        stats.setHealth(health);
        stats.setAttackDamage(50);
        stats.setCritChange(0.5f);
        stats.setCritDamage(10);
        healthBar.getPositions().put("player_score",
                new GamePosition(healthBar.getNamePos().getXPosition(),
                        healthBar.getNamePos().getYPosition() + 30, 0, 0));
        jumpSpeed = 300;
        fallDown = true;
        if (isLTR) {
            currAnimation = animations.get(CharacterState.FALLDOWN_LTR);
        } else {
            currAnimation = animations.get(CharacterState.FALLDOWN_LTR);
        }
    }

    @Override
    protected void healthBarInit(int maxHealth) {
        SpriteSheet healthBarSheet = new SpriteSheet();
        healthBarSheet.setImages(ImageManager.loadImagesWithCutFromFolderToList("assets/res/healthbar",
                1, 2, 126, 12));
        healthBar = new HealthBar(avatar, healthBarSheet, this,
                new GamePosition(120, 20, 550, 80), new GamePosition(15, 8, 100, 110),
                maxHealth);
    }

    @Override
    public void tick() {
        super.tick();
        healthBar.tick();
        if (isAttacked && !isDeath) {
            isStunCounter++;
            if (isStunCounter > 50) {
                isAttacked = false;
                isStunCounter = 0;
                if (isLTR) {
                    currAnimation = animations.get(CharacterState.IDLE_LTR);
                } else {
                    currAnimation = animations.get(CharacterState.IDLE_RTL);
                }
            }
        }
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        healthBar.render(g);
        g.drawString("Score: " + point, getPlayerScorePos().getXPosition(),
                getPlayerScorePos().getYPosition());
        g.setColor(Color.red);
        // hitbox
//        g.setColor(Color.red);
//        g.drawRect(getXHitBox() - gameplay.getCamera().getPosition().getXPosition(),
//                 getYHitBox() - gameplay.getCamera().getPosition().getYPosition(),
//                getWidthHitBox(), getHeightHitBox());
        //attackhitbox
//        int attackX;
//        if(isLTR) {
//            attackX = position.getXPosition() + position.getWidth();
//        } else attackX = position.getXPosition() - 20;
//        int attackY = position.getYPosition() + position.getHeight() / 3 - 10;
//        int attackHeight = position.getHeight() / 2 - 10;
//        g.fillRect(attackX - gameplay.getCamera().getPosition().getXPosition()
//                , attackY - gameplay.getCamera().getPosition().getYPosition(), 20, attackHeight);
    }

    @Override
    public void healthBarTick() {

    }

    public int getXHitBox() {
        if (currAnimation != null) {
            if (currAnimation instanceof PlayerAttack) {
                return position.getXPosition() + 110;
            }
        }
        return position.getXPosition() + 118;
    }

    public int getWidthHitBox() {
        if (currAnimation != null) {
            if (currAnimation instanceof PlayerRun) {
                return position.getWidth() / 3 + 15;
            } else if (currAnimation instanceof PlayerAttack) {
                return position.getWidth() / 3 + 30;
            }
        }
        return position.getWidth() / 3;
    }

    public int getHeightHitBox() {
        return position.getHeight() - 48;
    }

    public int getXMaxHitBox() {
        return getXHitBox() + getWidthHitBox();
    }

    public int getYHitBox() {
        return position.getYPosition() + position.getHeight() / 3 - 46;
    }

    public int getYMaxHitBox() {
        return getYHitBox() + getHeightHitBox();
    }

    @Override
    public boolean checkHit(int attackX, int attackY, int attackHeight, boolean isAttack, Stats attackerStats, int attackDamage) {
        int attackMaxY = attackY + attackHeight;
        if (!isAttack && !isDeath) {
            if (attackX >= getXHitBox() && attackX <= getXMaxHitBox()
                    && ((attackY <= getYHitBox() && attackMaxY >= getYMaxHitBox()
                    || (attackY >= getYHitBox() && attackMaxY <= getYMaxHitBox())
                    || (attackY > getYHitBox() && attackY <= getYMaxHitBox()
                    && attackMaxY > getYMaxHitBox())
                    || (attackMaxY > getYHitBox() && attackMaxY <= getYMaxHitBox()
                    && attackY < getYHitBox())))) {
                if (this.isAttack) {
                    return false;
                }
                return true;
            }
        } else {
            if (isAttack && !isAttacked && !isDeath) {
                if (attackX >= getXHitBox() && attackX <= getXMaxHitBox()
                        && ((attackY <= getYHitBox() && attackMaxY >= getYMaxHitBox()
                        || (attackY >= getYHitBox() && attackMaxY <= getYMaxHitBox())
                        || (attackY > getYHitBox() && attackY <= getYMaxHitBox()
                        && attackMaxY > getYMaxHitBox())
                        || (attackMaxY > getYHitBox() && attackMaxY <= getYMaxHitBox()
                        && attackY < getYHitBox())))) {
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
        }
        return false;
    }

    public int getPoint() {
        return point;
    }

    public void addPoint(int point) {
        this.point += point;
    }

    public Ability getAbility(int index) {
        if (abilities.size() == 0) {
            return null;
        }
        if (index > abilities.size() - 1) {
            return null;
        }
        return abilities.get(index);
    }

    public GamePosition getPlayerScorePos() {
        return healthBar.getPositions().get("player_score");
    }
}
