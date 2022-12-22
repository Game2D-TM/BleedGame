package fightinggame.entity;

import fightinggame.entity.ability.Ability;
import fightinggame.input.handler.Handler;
import fightinggame.resource.ImageManager;
import fightinggame.resource.SpriteSheet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Map;

public class Player extends Character {

    private int isStunCounter = 0;
    private int point = 0;

    public Player(int id, String name, int health, GamePosition position, Map<CharacterState, Animation> animations, Map<String, BufferedImage> characterAssets) {
        super(id, name, health, position, animations, characterAssets, true);
        healthBarInit(health);
        healthBar.setOvalImage(new java.awt.geom.Ellipse2D.Float(25f, 10f, 100, 100));
        speed = 2;
        attackDamage = 50;
        healthBar.getPositions().put("player_score",
                new GamePosition(healthBar.getNamePos().getXPosition(),
                        healthBar.getNamePos().getYPosition() + 30, 0, 0));
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

    public Player() {

    }

    public boolean moveRight() {
        return position.moveRight(speed);
    }

    public boolean moveLeft() {
        return position.moveLeft(speed);
    }

    public boolean moveUp() {
        return position.moveUp(speed);
    }

    public boolean moveDown() {
        return position.moveDown(speed);
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
                if(isLTR) {
                    currAnimation = animations.get(CharacterState.IDLE_LTR);
                } else currAnimation = animations.get(CharacterState.IDLE_RTL);
            }
        }
        if (controller.size() > 0) {
            for (int i = 0; i < controller.size(); i++) {
                Handler handler = controller.get(i);
                handler.tick();
            }
        }
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        healthBar.render(g);
        g.drawString("Score: " + point, getPlayerScorePos().getXPosition(),
                getPlayerScorePos().getYPosition());
        // hitbox
        g.setColor(Color.red);
        g.drawRect(getXHitBox(), getYHitBox(),
                position.getWidth(), position.getHeight() / 2 - 10);
        //attackhitbox
//        int attackX;
//        if(isLTR) {
//            attackX = position.getXPosition() + position.getWidth();
//        } else attackX = position.getXPosition() - 20;
//        int attackY = position.getYPosition() + position.getHeight() / 3 - 10;
//        int attackHeight = position.getHeight() / 2 - 10;
//        g.fillRect(attackX, attackY, 20, attackHeight);
    }

    @Override
    public void healthBarTick() {

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

    @Override
    public boolean checkHit(int attackX, int attackY, int attackHeight, boolean isAttack, int attackDmg) {
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
                    if(isLTR) {
                        currAnimation = animations.get(CharacterState.GET_HIT_LTR);
                    } else currAnimation = animations.get(CharacterState.GET_HIT_RTL);
                    isAttacked = true;
                    int health = healthBar.getHealth() - attackDmg;
                    if(health < 0) health = 0;
                    receiveDamage = attackDmg;
                    healthBar.setHealth(health);
                    if (health <= 0) {
                        isDeath = true;
                        if(isLTR) {
                            currAnimation = animations.get(CharacterState.DEATH_LTR);
                        } else currAnimation = animations.get(CharacterState.DEATH_RTL);
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
