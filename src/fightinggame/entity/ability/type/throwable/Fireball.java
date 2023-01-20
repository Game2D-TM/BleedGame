package fightinggame.entity.ability.type.throwable;

import fightinggame.Gameplay;
import fightinggame.animation.effect.Effect;
import fightinggame.animation.effect.FireBallExploreEffect;
import fightinggame.entity.Animation;
import fightinggame.entity.GamePosition;
import fightinggame.entity.ability.type.Throwable;
import fightinggame.entity.enemy.Enemy;
import fightinggame.entity.SpriteSheet;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import fightinggame.entity.Character;
import fightinggame.entity.Player;
import fightinggame.resource.ImageManager;
import java.util.List;

public class Fireball extends Throwable {

    private Effect exploreEffect;
    private boolean isHit;

    public Fireball(int attackDamage, int speed, int id, long resetTime, int energyLost,
            SpriteSheet skillIcon, GamePosition position, Animation animationLTR, Animation animationRTL,
            Gameplay gameplay, Character character) {
        super(attackDamage, speed, id, "Fire Ball", resetTime, energyLost, skillIcon, position, animationLTR, animationRTL, gameplay, character);
        SpriteSheet exploreFireBallSheet = new SpriteSheet(ImageManager.loadImage("assets/res/effect/Mini_Effect_2D/Effect10.png"),
                0, 0, 48, 48,
                0, 0, 48, 48, 4);
        exploreEffect = new FireBallExploreEffect(0, exploreFireBallSheet, 150);
    }

    public Fireball(int attackDamage, int speed, int id, long resetTime, int energyLost, SpriteSheet skillIcon,
            GamePosition position, Animation animationLTR, Animation animationRTL,
            BufferedImage border, Gameplay gameplay, Character character) {
        super(attackDamage, speed, id, "Fire Ball", resetTime, energyLost, skillIcon, position, animationLTR, animationRTL, border, gameplay, character);
        SpriteSheet exploreFireBallSheet = new SpriteSheet(ImageManager.loadImage("assets/res/effect/Mini_Effect_2D/Effect10.png"),
                0, 0, 48, 48,
                0, 0, 48, 48, 4);
        exploreEffect = new FireBallExploreEffect(0, exploreFireBallSheet, 150);
    }

    @Override
    public void tick() {
        super.tick();
        if (isHit) {
            if (exploreEffect != null) {
                exploreEffect.tick();
                if (!exploreEffect.isActive()) {
                    spawnPosition = null;
                    endPosition = null;
                    isThrow = false;
                    isHit = false;
                }
            } else {
                spawnPosition = null;
                endPosition = null;
                isThrow = false;
                isHit = false;
            }
        } else {
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
                                    if (enemy.checkHit(spawnPosition, true, character, attackDamage)) {
                                        gameplay.getAudioPlayer().startThread("fireball_hit", false, gameplay.getOptionHandler().getOptionMenu().getSfxVolume());
                                        exploreEffect.setActive(true);
                                        isHit = true;
                                        break;
                                    }
                                }
                            }
                        } else {
                            if (gameplay.getPlayer().checkHit(spawnPosition, true, character, attackDamage)) {
                                gameplay.getAudioPlayer().startThread("fireball_hit", false, gameplay.getOptionHandler().getOptionMenu().getSfxVolume());
                                exploreEffect.setActive(true);
                                gameplay.getPlayer().getHitEffect().resetEffectCounter();
                                isHit = true;
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
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        if (currAnimation != null) {
            if (spawnPosition != null && isThrow) {
//                if (gameplay.getCamera().checkPositionRelateToCamera(spawnPosition)) {
                    if (isHit) {
                        int nX = spawnPosition.getXPosition();
                        if(isLTR) {
                            nX += 50;
                        } else {
                            nX -= 50;
                        }
                        exploreEffect.render(g, nX - gameplay.getCamera().getPosition().getXPosition(),
                                spawnPosition.getYPosition() - gameplay.getCamera().getPosition().getYPosition(),
                                spawnPosition.getWidth(), spawnPosition.getHeight());
                        // effect hitbox
//                        g.setColor(Color.red);
//                        g.drawRect(spawnPosition.getXPosition() - gameplay.getCamera().getPosition().getXPosition(),
//                                spawnPosition.getYPosition() - gameplay.getCamera().getPosition().getYPosition(),
//                                spawnPosition.getWidth(), spawnPosition.getHeight());
                    } else {
                        currAnimation.render(g, spawnPosition.getXPosition() - gameplay.getCamera().getPosition().getXPosition(),
                                spawnPosition.getYPosition() - gameplay.getCamera().getPosition().getYPosition(),
                                spawnPosition.getWidth(), spawnPosition.getHeight());
                    }
//                }
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
                gameplay.getAudioPlayer().startThread("fireball_throw", false, gameplay.getOptionHandler().getOptionMenu().getSfxVolume());
                return true;
            }
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
