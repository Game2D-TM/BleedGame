package fightinggame.entity;

import fightinggame.input.handler.Handler;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

public class Player extends Character {

    private int isStunCounter = 0;
    private int point = 0;
    
    public Player(int id, String name, int health, GamePosition position, Map<CharacterState, Animation> animations, Map<String, BufferedImage> characterAssets, List<List<Item>> inventory) {
        super(id, name, health, position, animations, characterAssets, inventory);
        healthBar.setOvalImage(new java.awt.geom.Ellipse2D.Float(25f, 50f, 100, 100));
        speed = 2;
        attackDamage = 50;
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
        if (isAttacked) {
            isStunCounter++;
            if (isStunCounter >= 10) {
                isAttacked = false;
                isStunCounter = 0;
            }
        }
        if(controller.size() > 0) {
            for(int i = 0; i < controller.size(); i++) {
                Handler handler = controller.get(i);
                handler.tick();
            }
        }
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        healthBar.render(g, 120, 60, 550, 80,
                15, 48, 100, 110);
        g.drawString("Score: " + point, 120, 195);
    }

    @Override
    public void healthBarTick() {

    }
    
    @Override
    public boolean checkHit(int x, int y, boolean isAttack, int attackDmg) {
        if (!isAttack && !healthBar.isDeath()) {
            if (x >= position.getXPosition() && x <= position.getXPosition() + position.getWidth()
                    && y >= position.getYPosition() && y <= position.getYPosition() + position.getHeight()) {
                if(this.isAttack) return false;
                return true;
            }
        } else {
            if (isAttack && !isAttacked && !healthBar.isDeath()) {
                if (x >= position.getXPosition() && x <= position.getXPosition() + position.getWidth()
                        && y >= position.getYPosition() && y <= position.getYPosition() + position.getHeight()) {
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
        }
        return false;
    }

    public int getPoint() {
        return point;
    }

    public void addPoint(int point) {
        this.point += point;
    }
    
    
}
