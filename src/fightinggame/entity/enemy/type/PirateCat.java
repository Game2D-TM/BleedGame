package fightinggame.entity.enemy.type;

import fightinggame.Gameplay;
import fightinggame.animation.enemy.*;
import fightinggame.entity.Animation;
import fightinggame.entity.GamePosition;
import fightinggame.entity.Player;
import fightinggame.entity.ability.type.healing.TimeHeal;
import fightinggame.entity.enemy.Enemy;
import fightinggame.entity.state.CharacterState;
import fightinggame.resource.ImageManager;
import java.awt.Graphics;
import java.util.Map;

public class PirateCat extends Enemy {

    private boolean pushBackAttack;
    private int pushBackAttackCounter = 0;
    private int pushBackUseCounter = 0;

    public PirateCat(int id, String name, int health, GamePosition position, Map<CharacterState, Animation> animations, Gameplay gameplay, int rangeRandomSpeed) {
        super(id, name, health, position, animations, gameplay, rangeRandomSpeed);
        avatar = ImageManager.loadImage("assets/res/gui/avatar/pirate_cat.png");
        avatar = ImageManager.flipImage(avatar);
        healthBar.setAvatar(avatar);
        healthBar.setAvatarPos(new GamePosition(1500, 8, 180, 120));
        stunTime = 100;
        stats.setBounceRange(180);
        stats.setAttackDamage(30);
        stats.setAttackRange(180);
        point = 500;
        experience = 500;
    }

    private boolean checkNextToPlayer() {
        Player player = gameplay.getPlayer();
        if (player != null) {
            GamePosition playerPos = player.getHitBoxPosition();
            if (playerPos != null) {
                if (((playerPos.getXPosition() >= getXHitBox() && playerPos.getMaxX() <= getXMaxHitBox())
                        || (playerPos.getXPosition() >= getXHitBox() && playerPos.getXPosition() <= getXMaxHitBox()
                        && playerPos.getMaxX() > getXMaxHitBox())
                        || (playerPos.getMaxX() >= getXHitBox() && playerPos.getMaxX() <= getXMaxHitBox()
                        && playerPos.getXPosition() < getXHitBox()))
                        && (playerPos.getYPosition() >= getYHitBox() && playerPos.getMaxY() <= getYMaxHitBox())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (!isAttack && !animateChange && !isAttacked && !isDeath) {
            if (pushBackUseCounter <= 1000) {
                pushBackUseCounter++;
            } else {
                if (!pushBackAttack) {
                    if (checkNextToPlayer()) {
                        pushBackAttack = true;
                        if (currAnimation instanceof EnemyLightAttack); else {
                            if (isLTR) {
                                currAnimation = animations.get(CharacterState.ATTACK02_LTR);
                            } else {
                                currAnimation = animations.get(CharacterState.ATTACK02_RTL);
                            }
                        }
                        Player player = gameplay.getPlayer();
                        if (player != null && !player.isDeath()) {
                            if (isLTR) {
                                player.getPosition().setXPosition(player.getPosition().getXPosition() + 50);
                            } else {
                                player.getPosition().setXPosition(player.getPosition().getXPosition() - 50);
                            }
                            player.setIsAttacked(true);
                        }
                    }
                } else {
                    if (pushBackAttackCounter <= 50) {
                        pushBackAttackCounter++;
                    } else {
                        pushBackAttack = false;
                        if (currAnimation instanceof EnemyLightAttack) {
                            if (isLTR) {
                                currAnimation = animations.get(CharacterState.IDLE_LTR);
                            } else {
                                currAnimation = animations.get(CharacterState.IDLE_RTL);
                            }
                        }
                        pushBackAttackCounter = 0;
                        pushBackUseCounter = 0;
                    }
                }
            }
        }
        if (!isDeath) {
            if (stats.getHealth() < healthBar.getMaxHealth()) {
                TimeHeal timeHeal = ((TimeHeal) abilities.get(0));
                if (timeHeal != null && timeHeal.isCanUse()) {
                    timeHeal.execute();
                }
            }
        }
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        //hitbox
//        g.setColor(Color.red);
//        g.drawRect(getXHitBox() - gameplay.getCamera().getPosition().getXPosition(),
//                getYHitBox() - gameplay.getCamera().getPosition().getYPosition(),
//                getWidthHitBox(), getHeightHitBox());
        // attack hitbox
//        g.setColor(Color.red);
//        g.fillRect(attackHitBox().getXPosition() - gameplay.getCamera().getPosition().getXPosition(),
//                attackHitBox().getYPosition() - gameplay.getCamera().getPosition().getYPosition(),
//                attackHitBox().getWidth(), attackHitBox().getHeight());
    }

    @Override
    public int getXHitBox() {
        if (currAnimation != null) {
            if (currAnimation instanceof EnemyRunForward) {
                return position.getXPosition() + 25;
            } else if (currAnimation instanceof EnemyRunBack) {
                return position.getXPosition() + 65;
            }
        }
        return position.getXPosition();
    }

    @Override
    public int getWidthHitBox() {
        if (currAnimation != null) {
            if (currAnimation instanceof EnemyRunForward) {
                return position.getWidth() - 90;
            } else if (currAnimation instanceof EnemyRunBack) {
                return position.getWidth() - 90;
            }
        }
        return position.getWidth();
    }

    @Override
    public int getHeightHitBox() {
        return position.getHeight();
    }

    @Override
    public int getYHitBox() {
        return position.getYPosition();
    }

    @Override
    public GamePosition attackHitBox() {
        int attackX, attackY, attackWidth, attackHeight;
        attackY = position.getYPosition();
        attackHeight = position.getHeight() / 2 - 10 + position.getHeight() / 3 - 10;
        attackWidth = stats.getAttackRange();
        if (!isLTR) {
            attackX = position.getXPosition() - stats.getAttackRange() - 10;
        } else {
            attackX = position.getMaxX() + 10;
        }
        return new GamePosition(attackX, attackY, attackWidth, attackHeight);
    }

}
