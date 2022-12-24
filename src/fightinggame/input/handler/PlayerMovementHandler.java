package fightinggame.input.handler;

import fightinggame.Gameplay;
import fightinggame.animation.player.PlayerHit;
import fightinggame.entity.Animation;
import fightinggame.entity.CharacterState;
import fightinggame.entity.enemy.Enemy;
import fightinggame.entity.GamePosition;
import fightinggame.entity.Player;
import fightinggame.entity.platform.Platform;
import fightinggame.entity.platform.tile.BlankTile;
import fightinggame.entity.platform.tile.WallTile;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerMovementHandler extends Handler implements KeyListener {

    private final Player player;
    private Gameplay gameplay;
    private final Set<Integer> pressedKeys = new HashSet<>();
    private int keyboardCounter = 0;
    private boolean canAttack = false;
    private boolean lastPressKeys = false;

    public PlayerMovementHandler(Player player, String name, Gameplay gameplay) {
        super(name);
        this.player = player;
        this.gameplay = gameplay;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public void tick() {
        int curXpos = 0;
        int curYpos = 0;
        boolean canGoLeft = true, canGoRight = true, canJump = true;
        GamePosition position = player.getPosition();
        int speed = player.getSpeed();
        if (player.getCurPlatform() != null) {
            List<List<Platform>> platforms = gameplay.getSurroundPlatform(player.getCurPlatform().getRow(),
                    player.getCurPlatform().getColumn());
            if (platforms != null && platforms.size() > 0) {
                for (int i = 0; i < platforms.size(); i++) {
                    List<Platform> canStands = platforms.get(i);
                    if (canStands != null && canStands.size() > 0) {
                        for (int j = 0; j < canStands.size(); j++) {
                            Platform platform = canStands.get(i);
                            if (platform == null) {
                                break;
                            }
                            if (platform.getPosition() == null) {
                                break;
                            }
                            canGoLeft = true;
                            canGoRight = true;
//                            curYpos = position.getYPosition() - speed + position.getHeight();
//                            player.moveUp();
                            curXpos = position.getXPosition() - speed;
                            GamePosition positionLeft = new GamePosition(curXpos, position.getYPosition(), position.getWidth(), position.getHeight());
                            if (platform instanceof WallTile) {
                                if (platform.checkValidPosition(positionLeft)) {
                                    continue;
                                }
                            }
                            if (canGoLeft) {
                                if (platform instanceof BlankTile) {
                                    if (platform.checkValidPosition(positionLeft)) {
                                        boolean result = player.moveLeft();
                                        if (result) {
                                            player.setCurPlatform(platform);
                                            System.out.println("go left");
                                            System.out.println(player.getCurPlatform().getRow() + " "
                                                    + player.getCurPlatform().getColumn());

                                            break;
                                        }
                                    }
                                }
                            }
//                        curYpos = position.getYPosition() + speed + position.getHeight();
//                        player.moveDown();
                            curXpos = position.getXPosition() + speed; // position.getWidth()
                            GamePosition positionRight = new GamePosition(curXpos, position.getYPosition(),
                                    position.getWidth(), position.getHeight());
                            if (platform instanceof WallTile) {
                                if (platform.checkValidPosition(positionRight)) {
                                    continue;
                                }
                            }
                            if (canGoRight) {
                                if (platform instanceof BlankTile) {
                                    if (platform.checkValidPosition(positionRight)) {
                                        boolean result = player.moveRight();
                                        if (result) {
                                            player.setCurPlatform(platform);
                                            System.out.println("go right");
                                            System.out.println(player.getCurPlatform().getRow() + " "
                                                    + player.getCurPlatform().getColumn());
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
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
                            player.setCurrAnimation(player.getAnimations().get(CharacterState.RUNFORWARD));
                        } else {
                            player.setCurrAnimation(player.getAnimations().get(CharacterState.RUNBACK));
                        }
//                        player.getPosition().isMoveUp = true;
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
//                        player.getPosition().isMoveDown = true;
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
                                    if (enemy.checkHit(attackX, attackY, attackHeight, true, player.getAttackDamage())) {
                                        enemy.setStunTime(50);
                                        if (enemy.getHealthBar().getHealth() <= 0) {
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
                                Logger.getLogger(PlayerMovementHandler.class.getName()).log(Level.SEVERE, null, ex);
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
