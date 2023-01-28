package fightinggame.entity.platform.tile;

import fightinggame.Gameplay;
import fightinggame.entity.Animation;
import fightinggame.entity.GamePosition;
import fightinggame.entity.Player;
import fightinggame.entity.Stats;
import fightinggame.entity.ability.Ability;
import fightinggame.entity.ability.type.harm.Bleeding;
import fightinggame.entity.enemy.Enemy;
import fightinggame.entity.platform.tile.trap.TrapLocation;
import fightinggame.entity.state.CharacterState;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

public abstract class TrapTile extends Tile {

    protected int hitDamage;
    protected int hitCounter = 0;
    protected int hitLimit = 100;
    protected Animation trapAnimation;
    protected boolean activeTrap;
    protected int trapCounter = 0;
    protected int trapAniLimit = 150;
    protected int hitBounce = 80;
    protected TrapLocation location;
    protected Ability bleeding;

    public TrapTile(Animation trapAnimation, int hitDamage, TrapLocation location, String name, BufferedImage image, GamePosition position, Gameplay gameplay) {
        super(name, image, position, gameplay, -1, -1);
        this.hitDamage = hitDamage;
        this.trapAnimation = trapAnimation;
        this.location = location;
        bleeding = new Bleeding(1400, 200, 2, 0, gameplay, null);
    }

    @Override
    public void render(Graphics g) {
        if (canRender || isMapRender) {
            if (activeTrap) {
                trapAnimation.render(g, position.getXPosition() - gameplay.getCamera().getPosition().getXPosition(),
                        position.getYPosition() - gameplay.getCamera().getPosition().getYPosition(),
                        position.getWidth(), position.getHeight());
            } else {
                super.render(g);
            }
            // hitbox
//            g.setColor(Color.red);
//            g.drawRect(getXHitBox() - gameplay.getCamera().getPosition().getXPosition(),
//                    getYHitBox() - gameplay.getCamera().getPosition().getYPosition(),
//                    getWidthHitBox(), getHeightHitBox());
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (canRender) {
            if (!activeTrap) {
                Player player = gameplay.getPlayer();
                if (player != null) {
                    if (checkValidPosition(player.getHitBoxPosition())) {
                        Stats stats = player.getStats();
                        if (stats != null) {
                            int nHealth = stats.getHealth() - hitDamage;
                            player.setIsAttacked(true);
                            if (player.isLTR()) {
                                player.setCurrAnimation(player.getAnimations().get(CharacterState.KNOCKDOWN_LTR));
                                player.getPosition().setXPosition(player.getPosition().getXPosition() - hitBounce);
                            } else {
                                player.setCurrAnimation(player.getAnimations().get(CharacterState.KNOCKDOWN_RTL));
                                player.getPosition().setXPosition(player.getPosition().getXPosition() + hitBounce);
                            }
                            player.addReceiveDamage(hitDamage);
                            player.setStunTime(150);
                            if (nHealth > 0) {
                                stats.setHealth(nHealth);
                            } else {
                                player.setIsDeath(true);
                            }
                            bleeding.execute(player);
                        }
                        activeTrap = true;
                    }
                }
                List<Enemy> enemies = gameplay.getEnemies();
                if (enemies != null && enemies.size() > 0) {
                    for (int i = 0; i < enemies.size(); i++) {
                        Enemy enemy = enemies.get(i);
                        if (enemy != null) {
                            if (checkValidPosition(enemy.getHitBoxPosition())) {
                                Stats stats = enemy.getStats();
                                if (stats != null) {
                                    int nHealth = stats.getHealth() - hitDamage;
                                    enemy.setIsAttacked(true);
                                    if (enemy.isLTR()) {
                                        enemy.setCurrAnimation(enemy.getAnimations().get(CharacterState.GET_HIT_LTR));
                                        enemy.getPosition().setXPosition(enemy.getPosition().getXPosition() - hitBounce);
                                    } else {
                                        enemy.setCurrAnimation(enemy.getAnimations().get(CharacterState.GET_HIT_RTL));
                                        enemy.getPosition().setXPosition(enemy.getPosition().getXPosition() + hitBounce);
                                    }
                                    if (nHealth > 0) {
                                        stats.setHealth(nHealth);
                                    } else {
                                        enemy.setIsDeath(true);
                                    }
                                    enemy.addReceiveDamage(hitDamage);
                                }
                                activeTrap = true;
                            }
                        }
                    }
                }
            } else {
                if (trapAnimation != null) {
                    trapAnimation.tick();
                }
                if (trapCounter <= trapAniLimit) {
                    trapCounter++;
                } else {
                    trapCounter = 0;
                    activeTrap = false;
                }
            }
            if(bleeding != null) {
                bleeding.tick();
            }
        }
    }

    public int getHitDamage() {
        return hitDamage;
    }

    public void setHitDamage(int hitDamage) {
        this.hitDamage = hitDamage;
    }

    public int getHitCounter() {
        return hitCounter;
    }

    public int getHitLimit() {
        return hitLimit;
    }

    public Animation getTrapAnimation() {
        return trapAnimation;
    }

    public boolean isActiveTrap() {
        return activeTrap;
    }

    public int getTrapCounter() {
        return trapCounter;
    }

    public int getTrapAniLimit() {
        return trapAniLimit;
    }

    public int getHitBounce() {
        return hitBounce;
    }

    public void setHitBounce(int hitBounce) {
        this.hitBounce = hitBounce;
    }

    public TrapLocation getLocation() {
        return location;
    }

    public void setLocation(TrapLocation location) {
        this.location = location;
    }
    
}
