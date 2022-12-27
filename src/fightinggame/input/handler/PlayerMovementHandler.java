package fightinggame.input.handler;

import fightinggame.Gameplay;
import fightinggame.animation.player.PlayerHit;
import fightinggame.entity.Animation;
import fightinggame.entity.CharacterState;
import fightinggame.entity.Player;
import fightinggame.entity.enemy.Enemy;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerMovementHandler extends MovementHandler implements KeyListener {

    private final Player player;
    private final Set<Integer> pressedKeys = new HashSet<>();
    private int keyboardCounter = 0;
    private boolean canAttack = false;
    private boolean lastPressKeys = false;
    private double yAfterJump = 0;

    public PlayerMovementHandler() {
        super(null, null);
        this.player = null;
    }

    public PlayerMovementHandler(Player player, String name, Gameplay gameplay) {
        super(gameplay, name);
        this.player = player;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public void tick() {
        if (!player.isInAir()) {
            canMoveCheck(MoveState.LEFT, player);
            canMoveCheck(MoveState.RIGHT, player);
        }
        if (!player.isInAir()) {
            if (canMoveCheck(MoveState.UP, player)) {
                yAfterJump = player.getPosition().getYPosition() - (player.getStats().getSpeed() + player.getJumpSpeed());
            }
        } else {
            if (yAfterJump != 0) {
                player.getPosition().isMoveUp = false;
                System.out.println("is in air");
                System.out.println(yAfterJump);
                System.out.println(player.getPosition().getYPosition());
                if (player.getPosition().getYPosition() - player.getJumpFlySpeed() > yAfterJump) {
                    player.getPosition().setYPosition(player.getPosition().getYPosition() - player.getJumpFlySpeed());
                    if (player.getPosition().isMoveRight) {
                        canMoveCheck(MoveState.RIGHT, player);
                    } else if (player.getPosition().isMoveLeft) {
                        canMoveCheck(MoveState.LEFT, player);
                    }
                } else {
                    yAfterJump = 0;
                    player.setFallDown(true);
                }
            }
        }
        keyboardCounter++;
        if (keyboardCounter > 8) {
            canAttack = true;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (player.isAttacked() || player.isDeath()) {
            return;
        }
        pressedKeys.add(e.getKeyCode());
        if (!pressedKeys.isEmpty()) {
            for (Iterator<Integer> it = pressedKeys.iterator(); it.hasNext();) {
                switch (it.next()) {
                    case KeyEvent.VK_W:
                    case KeyEvent.VK_UP:
                        if (player.isAttack()) {
                            break;
                        }
                        if (player.isLTR()) {
                            player.setCurrAnimation(player.getAnimations().get(CharacterState.JUMP));
                        } else {
                            player.setCurrAnimation(player.getAnimations().get(CharacterState.JUMP));
                        }
                        player.getPosition().isMoveUp = true;
                        break;
                    
                    case KeyEvent.VK_A:
                    case KeyEvent.VK_LEFT:
                        if (player.isAttack()) {
                            break;
                        }
                        player.setIsLTR(false);
                        player.setCurrAnimation(player.getAnimations().get(CharacterState.RUNBACK));
                        player.getPosition().isMoveLeft = true;
                        break;
                    case KeyEvent.VK_S:
                    case KeyEvent.VK_DOWN:
                        if (player.isAttack()) {
                            break;
                        }
                        if (player.isLTR()) {
                            player.setCurrAnimation(player.getAnimations().get(CharacterState.RUNFORWARD));
                        } else {
                            player.setCurrAnimation(player.getAnimations().get(CharacterState.RUNBACK));
                        }
                        player.getPosition().isMoveDown = true;
                        break;
                    case KeyEvent.VK_D:
                    case KeyEvent.VK_RIGHT:
                        if (player.isAttack()) {
                            break;
                        }
                        player.setIsLTR(true);
                        player.setCurrAnimation(player.getAnimations().get(CharacterState.RUNFORWARD));
                        player.getPosition().isMoveRight = true;
                        break;
                    case KeyEvent.VK_SPACE:
                        if (!canAttack) {
                            break;
                        }
                        if (!player.isAttack() && !player.getPosition().isMoving() && !player.isAttacked()) {
                            if (player.getCurrAnimation() != null) {
                                if (player.getCurrAnimation() instanceof PlayerHit) {
                                    break;
                                }
                            }
                            Animation attack = null;
                            if (player.isLTR()) {
                                attack = player.getAnimations().get(CharacterState.ATTACK_LTR);
                            } else {
                                attack = player.getAnimations().get(CharacterState.ATTACK_RTL);
                            }
                            player.setIsAttack(true);
                            player.setCurrAnimation(attack);
                            gameplay.getAudioPlayer().startThread("swing_sword", false, 0.8f);
                            if (player.isLTR()) {
                                player.getPosition().setWidth(player.getPosition().getWidth() + 120); // 120
                            } else {
                                player.getPosition().setXPosition(player.getPosition().getXPosition() - 120);
                                player.getPosition().setWidth(player.getPosition().getWidth() + 120);
                            }
                            player.getPosition().setYPosition(player.getPosition().getYPosition() - 50);
                            player.getPosition().setHeight(player.getPosition().getHeight() + 50);
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

                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(PlayerMovementHandler.class
                                        .getName()).log(Level.SEVERE, null, ex);
                            }

                        }
                        canAttack = false;
                        keyboardCounter = 0;
                        break;
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                player.getPosition().isMoveUp = false;
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                player.getPosition().isMoveLeft = false;
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                player.getPosition().isMoveDown = false;
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                player.getPosition().isMoveRight = false;
                break;
        }
        if (!player.isDeath()) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                if (player.isAttack()) {
                    if (player.isLTR()) {
                        player.setCurrAnimation(player.getAnimations().get(CharacterState.IDLE_LTR));
                    } else {
                        player.setCurrAnimation(player.getAnimations().get(CharacterState.IDLE_RTL));
                    }
                }
            } else {
                if (player.getPosition().isNotPressKey()) {
                    if (player.isLTR()) {
                        player.setCurrAnimation(player.getAnimations().get(CharacterState.IDLE_LTR));
                    } else {
                        player.setCurrAnimation(player.getAnimations().get(CharacterState.IDLE_RTL));
                    }
                }
            }
            if (player.isAttack()) {
//                if (player.getPosition().getWidth() > 200) {
                if (player.isLTR()) {
                    player.getPosition().setWidth(player.getPosition().getWidth() - 120);
                } else {
                    player.getPosition().setWidth(player.getPosition().getWidth() - 120);
                    player.getPosition().setXPosition(player.getPosition().getXPosition() + 120);
                }
                player.getPosition().setYPosition(player.getPosition().getYPosition() + 50);
                player.getPosition().setHeight(player.getPosition().getHeight() - 50);
                player.setIsAttack(false);
//                }
            }
        }
    }

}
