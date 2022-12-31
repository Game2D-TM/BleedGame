package fightinggame.input.handler;

import fightinggame.Gameplay;
import fightinggame.animation.player.PlayerHit;
import fightinggame.entity.Animation;
import fightinggame.entity.CharacterState;
import fightinggame.entity.Player;
import fightinggame.entity.enemy.Enemy;
import fightinggame.entity.platform.Platform;
import fightinggame.entity.platform.tile.BlankTile;
import fightinggame.entity.platform.tile.Tile;
import fightinggame.entity.platform.tile.WallTile;
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
            canMoveCheck(MoveState.SLIDE, player);
        }
        if (!player.isInAir()) {
            if (canMoveCheck(MoveState.JUMP, player)) {
                yAfterJump = player.getPosition().getYPosition() - (player.getStats().getSpeed() + player.getStats().getJumpSpeed());
            }
        } else {
            if (yAfterJump != 0) {
                if (!player.isDoubleJump()) {
                    if (player.isLTR()) {
                        player.setCurrAnimation(player.getAnimations().get(CharacterState.JUMP_LTR));
                    } else {
                        player.setCurrAnimation(player.getAnimations().get(CharacterState.JUMP_RTL));
                    }
                }
                Platform insidePlatform = player.getInsidePlatform();
                if (insidePlatform != null) {
                    try {
                        Platform platformAbove = gameplay.getPlatforms().get(insidePlatform.getRow() - 1).get(insidePlatform.getColumn());
                        if (platformAbove != null) {
                            if (platformAbove instanceof Tile || platformAbove instanceof WallTile) {
                                yAfterJump = platformAbove.getPosition().getMaxY();
                            }
                        }
                    } catch (Exception ex) {

                    }
                }
                if (player.getYHitBox() - player.getStats().getJumpFlySpeed() > yAfterJump) {
                    player.getPosition().setYPosition(player.getPosition().getYPosition() - player.getStats().getJumpFlySpeed());
                    falldownMove(player);
                } else {
                    yAfterJump = 0;
                    player.setFallDown(true);
                    player.setInAir(false);
                    if (player.isLTR()) {
                        player.setCurrAnimation(player.getAnimations().get(CharacterState.FALLDOWN_LTR));
                    } else {
                        player.setCurrAnimation(player.getAnimations().get(CharacterState.FALLDOWN_RTL));
                    }
                    player.getPosition().isJump = false;
                    if (player.isDoubleJump()) {
                        player.setIsDoubleJump(false);
                    }
                }
            }
        }
        applyGravityCharacter(player);
        keyboardCounter++;
        if (keyboardCounter > 8) {
            canAttack = true;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (player.isAttacked() || player.isDeath() || player.isSpecialAttack()) {
            return;
        }
        pressedKeys.add(e.getKeyCode());
        if (!pressedKeys.isEmpty()) {
            for (Iterator<Integer> it = pressedKeys.iterator(); it.hasNext();) {
                int keyCode = it.next();
                switch (keyCode) {
                    case KeyEvent.VK_W:
                    case KeyEvent.VK_UP:
                        if (player.isInAir() && !player.isDoubleJump()) {
                            player.setIsDoubleJump(true);
                            if (player.isLTR()) {
                                player.setCurrAnimation(player.getAnimations().get(CharacterState.JUMPROLL_LTR));
                            } else {
                                player.setCurrAnimation(player.getAnimations().get(CharacterState.JUMPROLL_RTL));
                            }
                            if(yAfterJump != 0) yAfterJump = player.getPosition().getYPosition() - (player.getStats().getSpeed() + player.getStats().getJumpSpeed());
                            break;
                        }
                        if (player.isAttack() || player.isInAir() || player.isFallDown()) {
                            break;
                        }
                        if (player.getStandPlatform() != null) {
                            if (player.getStandPlatform() instanceof BlankTile) {
                                break;
                            }
                        } else {
                            break;
                        }
                        player.getPosition().isJump = true;
                        break;
                    case KeyEvent.VK_A:
                    case KeyEvent.VK_LEFT:
                        if (player.isAttack()) {
                            break;
                        }
                        player.setIsLTR(false);
                        if (!player.isInAir() && !player.isFallDown()) {
                            player.setCurrAnimation(player.getAnimations().get(CharacterState.RUNBACK));
                        }
                        player.getPosition().isMoveLeft = true;
                        break;
                    case KeyEvent.VK_S:
                    case KeyEvent.VK_DOWN:
                        if (player.isAttack() || player.getPosition().isMoving()) {
                            break;
                        }
                        if (player.isLTR()) {
                            player.setCurrAnimation(player.getAnimations().get(CharacterState.CROUCH_LTR));
                        } else {
                            player.setCurrAnimation(player.getAnimations().get(CharacterState.CROUCH_RTL));
                        }
                        player.getPosition().isCrouch = true;
                        break;
                    case KeyEvent.VK_D:
                    case KeyEvent.VK_RIGHT:
                        if (player.isAttack()) {
                            break;
                        }
                        player.setIsLTR(true);
                        if (!player.isInAir() && !player.isFallDown()) {
                            player.setCurrAnimation(player.getAnimations().get(CharacterState.RUNFORWARD));
                        }
                        player.getPosition().isMoveRight = true;
                        break;
                    case KeyEvent.VK_CONTROL:
                        if (player.isAttack() || player.getPosition().isMoving() || player.isInAir()
                                || player.isFallDown()) {
                            break;
                        }
                        if (player.isLTR()) {
                            player.setCurrAnimation(player.getAnimations().get(CharacterState.SLIDE_LTR));
                        } else {
                            player.setCurrAnimation(player.getAnimations().get(CharacterState.SLIDE_RTL));
                        }
                        player.getPosition().isSlide = true;
                        break;
                    case KeyEvent.VK_J:
                    case KeyEvent.VK_K:
                    case KeyEvent.VK_L:
                        if (!canAttack || player.isAttack()) {
                            break;
                        }
                        if (!player.isAttack() && !player.getPosition().isMoving() && !player.isAttacked()) {
                            if (player.getCurrAnimation() != null) {
                                if (player.getCurrAnimation() instanceof PlayerHit) {
                                    break;
                                }
                            }
                            int attackX = -1, attackY = -1, attackHeight = 1;
                            Animation attack = null;
                            if ((player.isFallDown() || player.isInAir()) && keyCode != KeyEvent.VK_L) {
                                if (player.isAirAttack()) {
                                    break;
                                }
                                if (player.isLTR()) {
                                    attack = player.getAnimations().get(CharacterState.AIRATTACK01_LTR);
                                } else {
                                    attack = player.getAnimations().get(CharacterState.AIRATTACK01_RTL);
                                }
                                if (player.isLTR()) {
                                    player.getPosition().setWidth(player.getPosition().getWidth() + 20); // 120
                                } else {
                                    player.getPosition().setXPosition(player.getPosition().getXPosition() - 20);
                                    player.getPosition().setWidth(player.getPosition().getWidth() + 20);
                                }
                                if (player.isLTR()) {
                                    attackX = player.getPosition().getXPosition() + player.getPosition().getWidth() - 12;
                                } else {
                                    attackX = player.getPosition().getXPosition() + 12;
                                }
                                attackY = player.getPosition().getYPosition() + player.getPosition().getHeight() / 3 - 10;
                                attackHeight = player.getPosition().getHeight() / 2 - 50;
                                player.setIsAirAttack(true);
                                attackEnemies(attack, attackX, attackY, attackHeight, 50);
                            } else {
                                if (player.isLTR()) {
                                    player.getPosition().setWidth(player.getPosition().getWidth() + 20); // 120
                                } else {
                                    player.getPosition().setXPosition(player.getPosition().getXPosition() - 20);
                                    player.getPosition().setWidth(player.getPosition().getWidth() + 20);
                                }
                                if (player.isLTR()) {
                                    attackX = player.getPosition().getXPosition() + player.getPosition().getWidth() - 2;
                                } else {
                                    attackX = player.getPosition().getXPosition() + 2;
                                }
                                attackY = player.getPosition().getYPosition() + player.getPosition().getHeight() / 3 - 10;
                                attackHeight = player.getPosition().getHeight() / 2 - 10;
                                if (keyCode == KeyEvent.VK_J) {
                                    if (player.isLTR()) {
                                        attack = player.getAnimations().get(CharacterState.ATTACK01_LTR);
                                    } else {
                                        attack = player.getAnimations().get(CharacterState.ATTACK01_RTL);
                                    }
                                    attackEnemies(attack, attackX, attackY, attackHeight, 50);
                                } else if (keyCode == KeyEvent.VK_K) {
                                    if (player.isLTR()) {
                                        attackX -= 16;
                                        attack = player.getAnimations().get(CharacterState.ATTACK02_LTR);
                                    } else {
                                        attackX += 16;
                                        attack = player.getAnimations().get(CharacterState.ATTACK02_RTL);
                                    }
                                    try {
                                        Thread.sleep(50);
                                    } catch (InterruptedException ex) {
                                        Logger.getLogger(PlayerMovementHandler.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    attackEnemies(attack, attackX, attackY, attackHeight, 50, player.getStats().getAttackDamage() + 5);
                                } else if (keyCode == KeyEvent.VK_L) {
                                    if (player.isLTR()) {
                                        attack = player.getAnimations().get(CharacterState.ATTACK03_LTR);
                                    } else {
                                        attack = player.getAnimations().get(CharacterState.ATTACK03_RTL);
                                    }
                                    player.setIsSpecialAttack(true);
                                    attackEnemies(attack, attackX, attackY, attackHeight, 50);
                                }
//                            player.getPosition().setYPosition(player.getPosition().getYPosition() - 50);
//                            player.getPosition().setHeight(player.getPosition().getHeight() + 50);
                            }
                            try {
                                Thread.sleep(500);
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

    public void attackEnemies(Animation attack, int attackX,
            int attackY, int attackHeight, int stunTime) {
        attackEnemies(attack, attackX, attackY, attackHeight, stunTime, -1);
    }

    public void attackEnemies(Animation attack, int attackX,
            int attackY, int attackHeight, int stunTime, int attackDamage) {
        player.setCurrAnimation(attack);
        player.setIsAttack(true);
        gameplay.getAudioPlayer().startThread("swing_sword", false, 0.8f);
//        int additionAttackX = 30;
//        int stunTime = 50;
//        for (int i = 0; i < attackCount; i++) {
        if (gameplay.getEnemies() != null && gameplay.getEnemies().size() > 0) {
//                if (i > 0) {
//                    if (player.isLTR()) {
//                        player.getPosition().setXPosition(player.getPosition().getXPosition() + additionAttackX);
//                        attackX += additionAttackX;
//                    } else {
//                        player.getPosition().setXPosition(player.getPosition().getXPosition() - additionAttackX);
//                    }
//                }
            for (int j = 0; j < gameplay.getEnemies().size(); j++) {
                Enemy enemy = gameplay.getEnemies().get(j);
                if (attackDamage > 0) {
                    if (enemy.checkHit(attackX, attackY, attackHeight, true, player, attackDamage)) {
                        enemy.setStunTime(stunTime);
                        if (enemy.getStats().getHealth() <= 0) {
                            gameplay.getAudioPlayer().startThread("kill_sound", false, 0.8f);
                        } else {
                            gameplay.getAudioPlayer().startThread("hit_dior_firror", false, 0.8f);
                        }
                    }
                } else {
                    if (enemy.checkHit(attackX, attackY, attackHeight, true, player)) {
                        enemy.setStunTime(stunTime);
                        if (enemy.getStats().getHealth() <= 0) {
                            gameplay.getAudioPlayer().startThread("kill_sound", false, 0.8f);
                        } else {
                            gameplay.getAudioPlayer().startThread("hit_dior_firror", false, 0.8f);
                        }
                    }
                }
            }
//                stunTime += 50;
//            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                player.getPosition().isMoveLeft = false;
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                player.getPosition().isCrouch = false;
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                player.getPosition().isMoveRight = false;
                break;
            case KeyEvent.VK_CONTROL:
                player.getPosition().isSlide = false;
                break;
        }
        if (!player.isDeath()) {
            if (e.getKeyCode() == KeyEvent.VK_J || e.getKeyCode() == KeyEvent.VK_K || e.getKeyCode() == KeyEvent.VK_L) {
                if (player.isAttack() && !player.isSpecialAttack()
                        && !player.isInAir() && !player.isFallDown()) {
                    if (player.isLTR()) {
                        player.setCurrAnimation(player.getAnimations().get(CharacterState.IDLE_LTR));
                    } else {
                        player.setCurrAnimation(player.getAnimations().get(CharacterState.IDLE_RTL));
                    }
                }
                if (player.isAirAttack()) {
                    if (player.isLTR()) {
                        player.setCurrAnimation(player.getAnimations().get(CharacterState.FALLDOWN_LTR));
                    } else {
                        player.setCurrAnimation(player.getAnimations().get(CharacterState.FALLDOWN_RTL));
                    }
                }
            } else {
                if (player.getPosition().isNotPressKey() && !player.isSpecialAttack()
                        && !player.isInAir() && !player.isFallDown()) {
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
                    player.getPosition().setWidth(player.getPosition().getWidth() - 20); // 120
                } else {
                    player.getPosition().setWidth(player.getPosition().getWidth() - 20);
                    player.getPosition().setXPosition(player.getPosition().getXPosition() + 20);
                }
//                player.getPosition().setYPosition(player.getPosition().getYPosition() + 50);
//                player.getPosition().setHeight(player.getPosition().getHeight() - 50);
                player.setIsAttack(false);
//                }
            }
        }
    }

}
