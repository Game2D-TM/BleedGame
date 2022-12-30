package fightinggame.input.handler;

import fightinggame.Gameplay;
import fightinggame.animation.player.PlayerHit;
import fightinggame.entity.Animation;
import fightinggame.entity.CharacterState;
import fightinggame.entity.Player;
import fightinggame.entity.enemy.Enemy;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MouseHandler extends Handler implements MouseListener {

    private final Player player;
    private Gameplay gameplay;
    private boolean isClicked = false;
    private boolean canClick = false;
    private int mouseCounter = 0;

    public MouseHandler(Player player, String name, Gameplay gameplay) {
        super(name);
        this.player = player;
        this.gameplay = gameplay;
    }

    @Override
    public void tick() {
        mouseCounter++;
        if (mouseCounter > 8) {
            canClick = true;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() >= 1 && !isClicked) {
            isClicked = true;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (isClicked && !player.isDeath()) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (canClick) {
                    if (!player.isAttack() && !player.getPosition().isMoving() && !player.isAttacked()) {
                        if (player.getCurrAnimation() != null) {
                            if (player.getCurrAnimation() instanceof PlayerHit) {
                                return;
                            }
                        }
                        Animation attack = null;
                        if (!player.isAttack()) {
                            if (player.isFallDown() || player.isInAir()) {
                                if (player.isLTR()) {
                                    attack = player.getAnimations().get(CharacterState.AIRATTACK01_LTR);
                                } else {
                                    attack = player.getAnimations().get(CharacterState.AIRATTACK01_RTL);
                                }
                            } else {
                                if (player.isLTR()) {
                                    attack = player.getAnimations().get(CharacterState.ATTACK01_LTR);
                                } else {
                                    attack = player.getAnimations().get(CharacterState.ATTACK01_RTL);
                                }
//                            player.getPosition().setYPosition(player.getPosition().getYPosition() - 50);
//                            player.getPosition().setHeight(player.getPosition().getHeight() + 50);
                            }
                            if (player.isLTR()) {
                                player.getPosition().setWidth(player.getPosition().getWidth() + 20); // 120
                            } else {
                                player.getPosition().setXPosition(player.getPosition().getXPosition() - 20);
                                player.getPosition().setWidth(player.getPosition().getWidth() + 20);
                            }
                            player.setCurrAnimation(attack);
                            player.setIsAttack(true);
                            gameplay.getAudioPlayer().startThread("swing_sword", false, 0.8f);
                            if (gameplay.getEnemies() != null && gameplay.getEnemies().size() > 0) {
                                int attackX;
                                int attackY;
                                int attackHeight;
                                if (player.isLTR()) {
                                    attackX = player.getPosition().getXPosition() + player.getPosition().getWidth();
                                } else {
                                    attackX = player.getPosition().getXPosition();
                                }
                                attackY = player.getPosition().getYPosition() + player.getPosition().getHeight() / 3 - 10;
                                attackHeight = player.getPosition().getHeight() / 2 - 10;
                                for (int i = 0; i < gameplay.getEnemies().size(); i++) {
                                    Enemy enemy = gameplay.getEnemies().get(i);
                                    if (enemy.checkHit(attackX, attackY, attackHeight, true, player.getStats())) {
                                        enemy.setStunTime(50);
                                        if (enemy.getStats().getHealth() <= 0) {
                                            gameplay.getAudioPlayer().startThread("kill_sound", false, 0.8f);
                                        } else {
                                            gameplay.getAudioPlayer().startThread("hit_dior_firror", false, 0.8f);
                                        }
                                    }
                                }
                            }
                        }
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(PlayerMovementHandler.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    canClick = false;
                    mouseCounter = 0;
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (isClicked) {
            if (!player.isDeath()) {
                if (player.isAttack()) {
                    if (player.isLTR()) {
                        player.setCurrAnimation(player.getAnimations().get(CharacterState.IDLE_LTR));
                    } else {
                        player.setCurrAnimation(player.getAnimations().get(CharacterState.IDLE_RTL));
                    }
                    if (player.getPosition().getWidth() > 200) {
                        if (player.isLTR()) {
                            player.getPosition().setWidth(player.getPosition().getWidth() - 20);
                        } else {
                            player.getPosition().setWidth(player.getPosition().getWidth() - 20);
                            player.getPosition().setXPosition(player.getPosition().getXPosition() + 20);
                        }
//                        player.getPosition().setYPosition(player.getPosition().getYPosition() + 50);
//                        player.getPosition().setHeight(player.getPosition().getHeight() - 50);
                        player.setIsAttack(false);
                    }
//                    if (gameplay.getEnemies() != null && gameplay.getEnemies().size() > 0) {
//                        for (int i = 0; i < gameplay.getEnemies().size(); i++) {
//                            Enemy enemy = gameplay.getEnemies().get(i);
//                            if (enemy.getCurrAnimation() != null
//                                    && enemy.getCurrAnimation() instanceof EnemyHit) {
//                                enemy.setCurrAnimation(enemy.getAnimations().get(CharacterState.NORMAL));
//                            }
//                        }
//                    }
                }
            }
            isClicked = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}
