package fightinggame.entity;

import fightinggame.Gameplay;
import fightinggame.animation.enemy.EnemyAttack;
import fightinggame.animation.enemy.EnemyRunBack;
import fightinggame.animation.enemy.EnemyRunForward;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Enemy extends Character {

    public static Enemy ENEMY_HEALTHBAR_SHOW;
    
    private Gameplay gameplay;
    private int deathCounter = 0;
    private int isAttackedCounter = 0;
    private int walkCounter = 0;
    private boolean isForward = true;
    private boolean animateChange = false;
    private int point = 10;

    public Enemy(int id, String name, int health, GamePosition position, Map<CharacterState, Animation> animations, Map<String, BufferedImage> characterAssets, List<List<Item>> inventory,
            Gameplay gameplay, int rangeRandomSpeed) {
        super(id, name, health, position, animations, characterAssets, inventory);
        this.gameplay = gameplay;
        healthBar.setOvalImage(new java.awt.geom.Ellipse2D.Float(1530f, 50f, 100, 100));
        Random rand = new Random();
        int range = rand.nextInt(rangeRandomSpeed);
        speed = rand.nextInt(range);
    }

    public Enemy() {
    }

    @Override
    public void tick() {
        super.tick();
        healthBar.tick();
        if (healthBar.isDeath()) {
            deathCounter++;
            if (deathCounter >= 1500) {
                gameplay.getPlayer().addPoint(point);
                gameplay.getEnemies().remove(this);
                gameplay.getPositions().remove(name);
                deathCounter = 0;
            }
        }
        if (isAttacked) {
            isAttackedCounter++;
            if (isAttackedCounter >= 300) {
                isAttacked = false;
                isAttackedCounter = 0;
            }
        }
        if (!healthBar.isDeath() && !isAttacked && !isAttack && !animateChange) {
            walkCounter++;
            if (walkCounter >= 100) {
                if (currAnimation == null) {
                    if (isForward) {
                        currAnimation = animations.get(CharacterState.RUNFORWARD);
                    } else {
                        currAnimation = animations.get(CharacterState.RUNBACK);
                    }
                } else {
                    if (isForward) {
                        if (currAnimation instanceof EnemyRunForward); else {
                            currAnimation = animations.get(CharacterState.RUNFORWARD);
                        }
                    } else {
                        if (currAnimation instanceof EnemyRunBack); else {
                            currAnimation = animations.get(CharacterState.RUNBACK);
                        }
                    }
                }
                if (isForward) {
                    if (position.getXPosition() >= gameplay.getPlayPosition().getXPosition()) {
                        position.isMoveLeft = true;
                        position.moveLeft(speed);
                    } else {
                        isForward = false;
                        position.isMoveLeft = false;
                    }
                } else {
                    if (position.getXPosition() + position.getWidth() <= gameplay.getPlayPosition().getXPosition()
                            + gameplay.getPlayPosition().getWidth()) {
                        position.isMoveRight = true;
                        position.moveRight(speed);
                    } else {
                        isForward = true;
                        position.isMoveRight = false;
                    }
                }
                walkCounter = 0;
            }
        }
        if (!isAttack && !isAttacked && !healthBar.isDeath()) {
            int xAttack = -1;
            int yAttack = position.getYPosition() + position.getHeight() / 2 + 4;
            if (isForward) {
                xAttack = position.getXPosition();
            } else {
                xAttack = position.getXPosition() + position.getWidth();
            }
            if (gameplay.getPlayer().checkHit(xAttack, yAttack, false, -1)) {
                currAnimation = animations.get(CharacterState.ATTACK);
                isAttack = true;
                gameplay.getPlayer().checkHit(xAttack, yAttack, isAttack, attackDamage);
                int falloutX = gameplay.getPlayer().getPosition().getXPosition() - 100;
                if(falloutX >= gameplay.getPlayPosition().getXPosition()) {
                    gameplay.getPlayer().getPosition().setXPosition(falloutX);
                } else {
                    int falloutY = gameplay.getPlayer().getPosition().getYPosition() - 30;
                    if(falloutY >= gameplay.getPlayPosition().getYPosition()) {
                        gameplay.getPlayer().getPosition().setYPosition(
                            gameplay.getPlayer().getPosition().getYPosition() - 30);
                    } else {
                        falloutY = gameplay.getPlayer().getPosition().getYPosition() 
                                + gameplay.getPlayer().getPosition().getHeight() + 30;
                        if(falloutY <= gameplay.getMaxYPlayArea()) {
                            gameplay.getPlayer().getPosition().setYPosition(
                            gameplay.getPlayer().getPosition().getYPosition() + 30);
                        }
                    }
                }
            }
        }
        if (currAnimation != null && !isAttacked && !healthBar.isDeath()) {
            if (currAnimation instanceof EnemyAttack) {
                if (animateChange) {
                    position.setXPosition(position.getXPosition() + 30);
                    position.setWidth(position.getWidth() - 30);
                    animateChange = false;
                }
                if (isAttack) {
                    position.setWidth(position.getWidth() + 30);
                    position.setXPosition(position.getXPosition() - 30);
                    isAttack = false;
                    animateChange = true;
                }
            }
        }
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        if (isAttacked && this.equals(ENEMY_HEALTHBAR_SHOW)) {
            healthBar.render(g, 975, 60, 550, 80,
                    1540, 45, 180, 120);
        }
        g.setColor(Color.red);
        g.drawRect(position.getXPosition() - 400, position.getYPosition() - 100,
                position.getXPosition() + position.getWidth(),
                position.getHeight() + 200);
    }

    public void setDefAttackedCounter() {
        isAttackedCounter = 0;
    }

    @Override
    public void healthBarTick() {

    }

    @Override
    public boolean checkHit(int attackX, int attackY, boolean isAttack, int attackDmg) {
        if (isAttack && !healthBar.isDeath()) {
            if (attackX >= position.getXPosition() && attackX <= position.getXPosition() + position.getWidth()
                    && attackY >= position.getYPosition() && attackY <= position.getYPosition() + position.getHeight()) {
                currAnimation = animations.get(CharacterState.GET_HIT);
                isAttacked = true;
                int health = healthBar.getHealth() - attackDmg;
                healthBar.setHealth(health);
                if (health <= 0) {
                    healthBar.setIsDeath(true);
                    currAnimation = animations.get(CharacterState.DEATH);
                }
                return true;
            }
        }
        return false;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
    
    
}
