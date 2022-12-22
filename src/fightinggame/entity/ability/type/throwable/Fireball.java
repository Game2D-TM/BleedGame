package fightinggame.entity.ability.type.throwable;

import fightinggame.Gameplay;
import fightinggame.entity.Animation;
import fightinggame.entity.GamePosition;
import fightinggame.entity.ability.type.Throwable;
import fightinggame.entity.enemy.Enemy;
import fightinggame.resource.SpriteSheet;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import fightinggame.entity.Character;
import fightinggame.entity.Player;
import java.util.List;

public class Fireball extends Throwable {

    public Fireball(int attackDamage, int speed, int id, long resetTime,
            SpriteSheet skillIcon, GamePosition position, Animation animationLTR, Animation animationRTL
            , Gameplay gameplay, Character character) {
        super(attackDamage, speed, id, "Fire Ball", resetTime, skillIcon, position, animationLTR, animationRTL, gameplay, character);
    }

    public Fireball(int attackDamage, int speed, int id, long resetTime, SpriteSheet skillIcon,
            GamePosition position, Animation animationLTR, Animation animationRTL
            , BufferedImage border, Gameplay gameplay, Character character) {
        super(attackDamage, speed, id, "Fire Ball", resetTime, skillIcon, position, animationLTR, animationRTL, border, gameplay, character);
    }

    @Override
    public void tick() {
        super.tick();
        if (spawnPosition != null && isThrow) {
            throwCounter++;
            if (throwCounter > 10) {
                if (character != null) {
                    if (isLTR) {
                        spawnPosition.moveRight(speed, true);
                    } else {
                        spawnPosition.moveLeft(speed, true);
                    }
                    int attackX = -1;
                    int attackY = spawnPosition.getYPosition();
                    int attackHeight = spawnPosition.getHeight();
                    if (character instanceof Player) {
                        attackX = spawnPosition.getMaxX();
                        if (!gameplay.getEnemies().isEmpty()) {
                            for (int i = 0; i < gameplay.getEnemies().size(); i++) {
                                Enemy enemy = gameplay.getEnemies().get(i);
                                if (enemy.checkHit(attackX, attackY, attackHeight, true, attackDamage)) {
                                    gameplay.getAudioPlayer().startThread("fireball_hit", false, 0.8f);
                                    spawnPosition = null;
                                    endPosition = null;
                                    isThrow = false;
                                    break;
                                }
                            }
                        }
                    } else {
                        attackX = spawnPosition.getXPosition();
                        if (gameplay.getPlayer().checkHit(attackX, attackY, attackHeight, true, attackDamage)) {
                            gameplay.getAudioPlayer().startThread("fireball_hit", false, 0.8f);
                            spawnPosition = null;
                            endPosition = null;
                            isThrow = false;
                        }
                    }

                    if (endPosition != null) {
                        if (isLTR) {
                            if (spawnPosition.getXPosition() >= endPosition.getMaxX()) {
                                spawnPosition = null;
                                endPosition = null;
                                isThrow = false;
                            }
                        } else {
                            if (spawnPosition.getMaxX() <= endPosition.getXPosition()) {
                                spawnPosition = null;
                                endPosition = null;
                                isThrow = false;
                            }
                        }
                    }
                    throwCounter = 0;
                }
            }
        }
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        if (currAnimation != null) {
            if (spawnPosition != null && isThrow) {
                currAnimation.render(g, spawnPosition.getXPosition(), spawnPosition.getYPosition(),
                        spawnPosition.getWidth(), spawnPosition.getHeight());
            }
        }
        // attack hitbox
//        if (spawnPosition != null) {
//            int attackX = spawnPosition.getXPosition();
//            if(character.isLTR()) attackX = spawnPosition.getMaxX();
//            else attackX = spawnPosition.getXPosition() - 20;
//            int attackY = spawnPosition.getYPosition();
//            int attackHeight = spawnPosition.getHeight();
//            g.setColor(Color.red);
//            g.fillRect(attackX, attackY, 20, attackHeight);
//        }
    }

    @Override
    public boolean execute(GamePosition spawnPosition, GamePosition endPosition) {
        if (canUse) {
            this.spawnPosition = spawnPosition;
            this.endPosition = endPosition;
            if(character != null) {
                if(character.isLTR()) {
                    isLTR = true;
                    currAnimation = animationLTR;
                }
                else {
                    isLTR = false;
                    currAnimation = animationRTL;
                }
            }
            isThrow = true;
            canUse = false;
            gameplay.getAudioPlayer().startThread("fireball_throw", false, 0.8f);
            return true;
        }
        return false;
    }

    @Override
    public boolean execute() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean execute(Character character) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public boolean execute(List<Character> characters) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
