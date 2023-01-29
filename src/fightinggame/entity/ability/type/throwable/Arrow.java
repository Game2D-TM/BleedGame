package fightinggame.entity.ability.type.throwable;

import fightinggame.Gameplay;
import fightinggame.entity.Animation;
import fightinggame.entity.Character;
import fightinggame.entity.GamePosition;
import fightinggame.entity.Player;
import fightinggame.entity.SpriteSheet;
import fightinggame.entity.ability.type.Throwable;
import fightinggame.entity.enemy.Enemy;
import java.awt.Graphics;
import java.util.List;

public class Arrow extends Throwable {

    public Arrow(int attackDamage, int speed, int id, String name, long resetTime, SpriteSheet skillIcon, Animation animationLTR, Animation animationRTL, Gameplay gameplay, Character character) {
        super(attackDamage, speed, id, name, resetTime, 0, skillIcon, animationLTR, animationRTL, gameplay, character);
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        if (currAnimation != null) {
            if (spawnPosition != null && isThrow) {
                if (gameplay.getCamera().checkPositionRelateToCamera(spawnPosition)) {
                    currAnimation.render(g, spawnPosition.getXPosition() - gameplay.getCamera().getPosition().getXPosition(),
                            spawnPosition.getYPosition() - gameplay.getCamera().getPosition().getYPosition(),
                            spawnPosition.getWidth(), spawnPosition.getHeight());
                    //hitbox
//                    g.setColor(Color.red);
//                    g.drawRect(getXHitBox() - gameplay.getCamera().getPosition().getXPosition(),
//                            getYHitBox() - gameplay.getCamera().getPosition().getYPosition(),
//                            getWidthHitBox(), getHeightHitBox());
                }
            }
        }
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
                    if (character instanceof Player) {
                        if (!gameplay.getEnemies().isEmpty()) {
                            for (int i = 0; i < gameplay.getEnemies().size(); i++) {
                                Enemy enemy = gameplay.getEnemies().get(i);
                                if (enemy.checkHit(getHitBoxPos(), true, character, attackDamage)) {
                                    spawnPosition = null;
                                    endPosition = null;
                                    isThrow = false;
                                    character.getAbilities().remove(this);
                                    break;
                                }
                            }
                        }
                    } else {
                        if (gameplay.getPlayer().checkHit(getHitBoxPos(), true, character, attackDamage)) {
                            spawnPosition = null;
                            endPosition = null;
                            isThrow = false;
                            character.getAbilities().remove(this);
                        }
                    }

                    if (endPosition != null) {
                        if (isLTR) {
                            if (spawnPosition.getXPosition() >= endPosition.getMaxX()) {
                                spawnPosition = null;
                                endPosition = null;
                                isThrow = false;
                                character.getAbilities().remove(this);
                            }
                        } else {
                            if (spawnPosition.getMaxX() <= endPosition.getXPosition()) {
                                spawnPosition = null;
                                endPosition = null;
                                isThrow = false;
                                character.getAbilities().remove(this);
                            }
                        }
                    }
                    throwCounter = 0;
                }
            }
        }
    }

    @Override
    public boolean execute(GamePosition spawnPosition, GamePosition endPosition) {
        if (super.execute(spawnPosition, endPosition)) {
            if (canUse) {
                this.spawnPosition = spawnPosition;
                this.endPosition = endPosition;
                if (character != null) {
                    if (character.isLTR()) {
                        isLTR = true;
                        currAnimation = animationLTR;
                    } else {
                        isLTR = false;
                        currAnimation = animationRTL;
                    }
                    if (character instanceof Player) {
                        character.getStats().useEnergy(energyLost);
                    }
                }
                isThrow = true;
                canUse = false;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean execute() {
        return false;
    }

    @Override
    public boolean execute(Character character) {
        return false;
    }

    @Override
    public boolean execute(List<Character> characters) {
        return false;
    }

    @Override
    public int getXHitBox() {
        if (spawnPosition != null) {
            if (isLTR) {
                return spawnPosition.getXPosition() + spawnPosition.getWidth() / 2 + spawnPosition.getWidth() / 3;
            } else {
                return spawnPosition.getXPosition();
            }
        }
        return 0;
    }

    @Override
    public int getYHitBox() {
        if (spawnPosition != null) {
            return spawnPosition.getYPosition() + 80;
        }
        return 0;
    }

    @Override
    public int getWidthHitBox() {
        if (spawnPosition != null) {
            return spawnPosition.getWidth() / 2 - spawnPosition.getWidth() / 3;
        }
        return 0;
    }

    @Override
    public int getHeightHitBox() {
        if (spawnPosition != null) {
            return spawnPosition.getHeight() - 135;
        }
        return 0;
    }

    @Override
    public Throwable clone() {
        return new Arrow(attackDamage, speed, id, name, resetTime, skillIcon, animationLTR, animationRTL, gameplay, character);
    }

}
