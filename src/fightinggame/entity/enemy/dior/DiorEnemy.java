package fightinggame.entity.enemy.dior;

import fightinggame.Gameplay;
import fightinggame.animation.enemy.EnemyAttack;
import fightinggame.entity.Animation;
import fightinggame.entity.CharacterState;
import fightinggame.entity.enemy.Enemy;
import fightinggame.entity.GamePosition;
import fightinggame.entity.ability.type.throwable.Fireball;
import fightinggame.resource.SpriteSheet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Map;

public class DiorEnemy extends Enemy {

    private DiorColor color;

    public DiorEnemy(DiorColor color, int id, String name, int health,
            GamePosition position, Map<CharacterState, Animation> animations,
            Map<String, BufferedImage> characterAssets,
            Gameplay gameplay, int rangeRandomSpeed, SpriteSheet inventorySheet) {
        super(id, name, health, position, animations, characterAssets, gameplay, rangeRandomSpeed, inventorySheet);
        this.color = color;
        switch (color) {
            case Red:
                point += 10;
                experience = 500;
                stats.setAttackDamage(stats.getAttackDamage() + 20);
                stats.setSpeed(stats.getSpeed() - 15);
                stats.setHealth(stats.getHealth() - 200);
                break;
            case Blue:
                experience = 70;
                stats.setAttackDamage(stats.getAttackDamage() + 5);
                stats.setSpeed(stats.getSpeed() + 10);
                stats.setHealth(stats.getHealth() - 150);
                break;
            case Green:
                point += 5;
                experience = 70;
                stats.setAttackDamage(stats.getAttackDamage() + 10);
                stats.setSpeed(stats.getSpeed() - 5);
                stats.setHealth(stats.getHealth() - 100);
                break;
            case Orange:
                experience = 60;
                stats.setAttackDamage(stats.getAttackDamage() - 5);
                stats.setSpeed(stats.getSpeed() + 20);
                stats.setHealth(stats.getHealth() + 100);
                break;
            case Purple:
                experience = 50;
                stats.setSpeed(stats.getSpeed() - 10);
                stats.setHealth(stats.getHealth() + 200);
                break;
            case White:
                experience = 80;
                point += 5;
                stats.setAttackDamage(stats.getAttackDamage() - 8);
                stats.setSpeed(stats.getSpeed() - 10);
                stats.setHealth(stats.getHealth() + 400);
                break;
            case Yellow:
                experience = 80;
                stats.setAttackDamage(stats.getAttackDamage() - 5);
                stats.setSpeed(stats.getSpeed() + 40);
                stats.setHealth(stats.getHealth() + 50);
                break;
        }
        healthBar.setMaxHealth(stats.getHealth());
        if (stats.getSpeed() <= 0) {
            stats.setSpeed(1);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!isAttack && !isAttacked && !isDeath) {
            int xAttack = -1;
            int attackY = position.getYPosition() + position.getHeight() / 3 - 10;
            int attackHeight = position.getHeight() / 2 - 10;
            if (!isLTR) {
                xAttack = position.getXPosition();
            } else {
                xAttack = position.getMaxX();
            }
            if (gameplay.getPlayer().checkHit(xAttack, attackY, attackHeight, false, null)) {
                if (isLTR) {
                    currAnimation = animations.get(CharacterState.ATTACK01_LTR);
                } else {
                    currAnimation = animations.get(CharacterState.ATTACK01_RTL);
                }
                isAttack = true;
                gameplay.getPlayer().checkHit(xAttack, attackY, attackHeight, isAttack, stats);
                int falloutX = gameplay.getPlayer().getPosition().getXPosition() - 100;
//                if (falloutX >= gameplay.getPlayPosition().getXPosition()) {
//                    gameplay.getPlayer().getPosition().setXPosition(falloutX);
//                } else {
//                    int falloutY = gameplay.getPlayer().getPosition().getYPosition() - 5;
//                    if (falloutY > gameplay.getPlayPosition().getYPosition() + 15) {
//                        gameplay.getPlayer().getPosition().setYPosition(
//                                gameplay.getPlayer().getPosition().getYPosition() - 5);
//                    } else {
//                        falloutY = gameplay.getPlayer().getPosition().getMaxY() + 5;
//                        if (falloutY < gameplay.getMaxYPlayArea() - 20) {
//                            gameplay.getPlayer().getPosition().setYPosition(
//                                    gameplay.getPlayer().getPosition().getYPosition() + 5);
//                        } else {
//                            gameplay.getPlayer().getPosition().setXPosition(
//                                    gameplay.getPlayer().getPosition().getXPosition() + 150);
//                            gameplay.getPlayer().getPosition().setYPosition(
//                                    gameplay.getPlayer().getPosition().getYPosition() - 50);
//                        }
//                    }
//                }
            }
        }

        if (color == DiorColor.Red) {
            if (!isAttack && !isDeath) {
                Fireball fireBallAbility = ((Fireball) abilities.get(0));
                if (fireBallAbility.isCanUse()) {
                    int spawnX;
                    int xChange;
                    int skillDistance = 1400;
                    GamePosition endPos;
                    if (!isLTR) {
                        xChange = 215;
                        spawnX = position.getXPosition() - xChange;
                        endPos = new GamePosition(
                                position.getXPosition() - skillDistance - xChange, 0,
                                 position.getMaxX() + skillDistance, 0);
                    } else {
                        xChange = 15;
                        spawnX = position.getMaxX() + xChange;
                        endPos = new GamePosition(
                                position.getXPosition() - skillDistance,
                                 0, position.getMaxX() + skillDistance + xChange, 0);
                    }
                    GamePosition fireBallPos = new GamePosition(spawnX,
                            position.getYPosition() + position.getHeight() / 2 - 10, 200, 100);
                    boolean result = fireBallAbility.execute(fireBallPos, endPos);
                    if (result) {
                        if (isLTR) {
                            currAnimation = animations.get(CharacterState.ATTACK01_LTR);
                        } else {
                            currAnimation = animations.get(CharacterState.ATTACK01_RTL);
                        }
                        isAttack = true;
                    }
                }
            }
        }

        if (currAnimation != null && !isAttacked && !isDeath) {
            if (currAnimation instanceof EnemyAttack) {
                if (animateChange) {
                    if (!isLTR) {
                        position.setXPosition(position.getXPosition() + 30);
                        position.setWidth(position.getWidth() - 30);
                    } else {
                        position.setXPosition(position.getXPosition() - 30);
                        position.setWidth(position.getWidth() + 30);
                    }
                    animateChange = false;
                }
                if (isAttack) {
                    if (!isLTR) {
                        position.setXPosition(position.getXPosition() - 30);
                        position.setWidth(position.getWidth() + 30);
                    } else {
                        position.setXPosition(position.getXPosition() + 30);
                        position.setWidth(position.getWidth() - 30);
                    }
                    isAttack = false;
                    animateChange = true;
                }
            }
        }
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        //hitbox
        g.setColor(Color.red);
        g.drawRect(getXHitBox() - gameplay.getCamera().getPosition().getXPosition()
                , getYHitBox() - gameplay.getCamera().getPosition().getYPosition(),
                position.getWidth(), position.getHeight() / 2 - 10);
        // attack hitbox
//        int attackX = position.getXPosition();
//        if(isLTR) {
//            attackX = position.getMaxX();
//        }
//        int attackY = position.getYPosition() + position.getHeight() / 3 - 10;
//        int attackHeight = position.getHeight() / 2 - 10;
//        g.fillRect(attackX, attackY, 20, attackHeight);
    }

    @Override
    public int getXHitBox() {
        return position.getXPosition();
    }

    @Override
    public int getXMaxHitBox() {
        return position.getMaxX();
    }

    @Override
    public int getYHitBox() {
        return position.getYPosition() + position.getHeight() / 3 - 10;
    }

    @Override
    public int getYMaxHitBox() {
        return getYHitBox() + position.getHeight() / 2 - 10;
    }

    @Override
    public int getWidthHitBox() {
        return position.getWidth() / 3;
    }

    @Override
    public int getHeightHitBox() {
        return position.getHeight() - 48;
    }

    public DiorColor getColor() {
        return color;
    }

    public void setColor(DiorColor color) {
        this.color = color;
    }

}
