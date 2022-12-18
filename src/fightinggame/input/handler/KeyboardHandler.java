package fightinggame.input.handler;

import fightinggame.Gameplay;
import fightinggame.animation.enemy.EnemyHit;
import fightinggame.entity.Animation;
import fightinggame.entity.CharacterState;
import fightinggame.entity.Enemy;
import fightinggame.entity.GamePosition;
import fightinggame.entity.Player;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KeyboardHandler extends Handler implements KeyListener {

    private final Player player;
    private Gameplay gameplay;
    private final Set<Integer> pressedKeys = new HashSet<>();

    public KeyboardHandler(Player player, String name, Gameplay gameplay) {
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
        GamePosition position = player.getPosition();
        GamePosition playPosition = gameplay.getPlayPosition();
        int speed = player.getSpeed();
        curYpos = position.getYPosition() - speed + position.getHeight();
        if (curYpos > playPosition.getYPosition() && curYpos < playPosition.getYPosition() + playPosition.getHeight()) {
            player.moveUp();
        }
        curXpos = position.getXPosition() - speed;
        if (curXpos > playPosition.getXPosition() && curXpos < playPosition.getXPosition() + playPosition.getWidth()) {
            player.moveLeft();
        }
        curYpos = position.getYPosition() + speed + position.getHeight();
        if (curYpos > playPosition.getYPosition() && curYpos < playPosition.getYPosition() + playPosition.getHeight()) {
            player.moveDown();
        }
        curXpos = position.getXPosition() + speed + position.getWidth();
        if (curXpos > playPosition.getXPosition() && curXpos < playPosition.getXPosition() + playPosition.getWidth()) {
            player.moveRight();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (player.isAttacked() || player.getHealthBar().isDeath()) {
            return;
        }
        pressedKeys.add(e.getKeyCode());
        if (!pressedKeys.isEmpty()) {
            for (Iterator<Integer> it = pressedKeys.iterator(); it.hasNext();) {
                switch (it.next()) {
                    case KeyEvent.VK_W:
                    case KeyEvent.VK_UP:
                        player.setCurrAnimation(player.getAnimations().get(CharacterState.RUNFORWARD));
                        player.getPosition().isMoveUp = true;
                        break;
                    case KeyEvent.VK_A:
                    case KeyEvent.VK_LEFT:
                        player.setCurrAnimation(player.getAnimations().get(CharacterState.RUNFORWARD));
                        player.getPosition().isMoveLeft = true;
                        break;
                    case KeyEvent.VK_S:
                    case KeyEvent.VK_DOWN:
                        player.setCurrAnimation(player.getAnimations().get(CharacterState.RUNFORWARD));
                        player.getPosition().isMoveDown = true;
                        break;
                    case KeyEvent.VK_D:
                    case KeyEvent.VK_RIGHT:
                        player.setCurrAnimation(player.getAnimations().get(CharacterState.RUNFORWARD));
                        player.getPosition().isMoveRight = true;
                        break;
                    case KeyEvent.VK_SPACE:
                        if (!player.isAttack()) {
                            Animation attack = player.getAnimations().get(CharacterState.ATTACK);
                            if (!player.isAttack()) {
                                player.getPosition().setWidth(player.getPosition().getWidth() + 120);
                                player.getPosition().setYPosition(player.getPosition().getYPosition() - 50);
                                player.getPosition().setHeight(player.getPosition().getHeight() + 50);
                                player.setIsAttack(true);
                                if (gameplay.getEnemies() != null && gameplay.getEnemies().size() > 0) {
                                    int attackX = player.getPosition().getXPosition() + player.getPosition().getWidth();
                                    int attackY = player.getPosition().getYPosition() + player.getPosition().getHeight() / 2;
                                    for (int i = 0; i < gameplay.getEnemies().size(); i++) {
                                        Enemy enemy = gameplay.getEnemies().get(i);
                                        if (enemy.checkHit(attackX, attackY, true, player.getAttackDamage())) {
                                            enemy.setDefAttackedCounter();
                                            Enemy.ENEMY_HEALTHBAR_SHOW = enemy;
                                        }
                                    }
                                }
                            }
                            player.setCurrAnimation(attack);
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(KeyboardHandler.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        break;

                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
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
        pressedKeys.remove(e.getKeyCode());
        if (!player.getHealthBar()
                .isDeath()) {
            if (player.isAttack()) {
                if (player.getPosition().getWidth() > 200) {
                    player.getPosition().setWidth(player.getPosition().getWidth() - 120);
                    player.getPosition().setYPosition(player.getPosition().getYPosition() + 50);
                                player.getPosition().setHeight(player.getPosition().getHeight() - 50);
                    player.setIsAttack(false);
                }
                if (gameplay.getEnemies() != null && gameplay.getEnemies().size() > 0) {
                    for (int i = 0; i < gameplay.getEnemies().size(); i++) {
                        Enemy enemy = gameplay.getEnemies().get(i);
                        if (enemy.getCurrAnimation() != null
                                && enemy.getCurrAnimation() instanceof EnemyHit) {
                            enemy.setCurrAnimation(enemy.getAnimations().get(CharacterState.NORMAL));
                        }
                    }
                }
            }
            player.setCurrAnimation(player.getAnimations().get(CharacterState.NORMAL));
        }
    }

}
