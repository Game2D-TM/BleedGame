package fightinggame.input.handler.game.player;

import fightinggame.Game;
import fightinggame.entity.state.MoveState;
import fightinggame.Gameplay;
import fightinggame.animation.player.PlayerCrouch;
import fightinggame.animation.player.PlayerHeavyAttack;
import fightinggame.animation.player.PlayerIdle;
import fightinggame.animation.player.PlayerJump_LTR;
import fightinggame.animation.player.PlayerJump_RTL;
import fightinggame.animation.player.PlayerLightAttack;
import fightinggame.animation.player.PlayerRun_LTR;
import fightinggame.animation.player.PlayerRun_RTL;
import fightinggame.animation.player.PlayerSlide_LTR;
import fightinggame.animation.player.PlayerSlide_RTL;
import fightinggame.animation.player.PlayerSprint_LTR;
import fightinggame.animation.player.PlayerSprint_RTL;
import fightinggame.entity.Animation;
import fightinggame.entity.GamePosition;
import fightinggame.entity.state.CharacterState;
import fightinggame.entity.Player;
import fightinggame.entity.enemy.Enemy;
import fightinggame.entity.platform.Platform;
import fightinggame.entity.platform.tile.BlankTile;
import fightinggame.entity.platform.tile.Tile;
import fightinggame.entity.platform.tile.WallTile;
import fightinggame.entity.state.GameState;
import fightinggame.input.handler.game.MovementHandler;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class PlayerKeyboardHandler extends MovementHandler implements KeyListener {

    private final Player player;
    private double yAfterJump = 0;
    private Set<Integer> keyPresses = new HashSet();
    private int attackCounter = 0;
    private int attackLimit = 80;
    private int specialAttackCounter = 0;
    private int specialAttackLimit = 200;
    private int heavyAttackCounter = 0;
    private int heavyAttackLimit = 100;
    private boolean canHeavyAttack = false;

    private boolean eBtnPressed;
    private boolean spaceBtnPressed;
    private boolean upArrowPressed;
    private boolean downArrowPressed;
    private boolean enterPressed;
    private boolean rightArrowPressed;
    private boolean leftArrowPressed;
    private boolean escPressed;

    private int lastPresseed = -1;

    public PlayerKeyboardHandler() {
        super(null, null);
        this.player = null;
    }

    public PlayerKeyboardHandler(Player player, String name, Gameplay gameplay) {
        super(gameplay, name);
        this.player = player;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public boolean checkIsCrouch() {
        if (player.getPosition().isCrouch && !player.getPosition().isMoving()) {
            if (player.isLTR()) {
                if (player.getCurrAnimation() instanceof PlayerCrouch); else {
                    player.setCurrAnimation(player.getAnimations().get(CharacterState.CROUCH_LTR));
                }
            } else {
                if (player.getCurrAnimation() instanceof PlayerCrouch); else {
                    player.setCurrAnimation(player.getAnimations().get(CharacterState.CROUCH_RTL));
                }
            }
            return true;
        } else {
            player.getPosition().isCrouch = false;
        }
        return false;
    }

    public void tick() {
        if (!player.isDeath()) {
            if (player.isAttack()) {
                if (attackCounter <= attackLimit) {
                    attackCounter++;
                } else {
                    if (player.isLTR()) {
                        player.getPosition().setWidth(player.getPosition().getWidth() - 20); // 120
                    } else {
                        player.getPosition().setWidth(player.getPosition().getWidth() - 20);
                        player.getPosition().setXPosition(player.getPosition().getXPosition() + 20);
                    }
                    gameplay.getCamera().setIsCheckCameraPos(true);
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
                    if (player.getPosition().isMoveRight) {
                        if (player.getCurrAnimation() != null && player.getCurrAnimation() instanceof PlayerIdle) {
                            if (player.isSprint()) {
                                player.setCurrAnimation(player.getAnimations().get(CharacterState.SPRINT_LTR));
                            } else {
                                player.setCurrAnimation(player.getAnimations().get(CharacterState.RUNFORWARD));
                            }
                        }
                    } else if (player.getPosition().isMoveLeft) {
                        if (player.getCurrAnimation() != null && player.getCurrAnimation() instanceof PlayerIdle) {
                            if (player.isSprint()) {
                                player.setCurrAnimation(player.getAnimations().get(CharacterState.SPRINT_RTL));
                            } else {
                                player.setCurrAnimation(player.getAnimations().get(CharacterState.RUNBACK));
                            }
                        }
                    }
                    player.setIsAttack(false);
                    attackCounter = 0;
                }
            }
            if (player.isSpecialAttack()) {
                if (specialAttackCounter <= specialAttackLimit) {
                    specialAttackCounter++;
                } else {
                    if (player.isLTR()) {
                        player.setCurrAnimation(player.getAnimations().get(CharacterState.IDLE_LTR));
                    } else {
                        player.setCurrAnimation(player.getAnimations().get(CharacterState.IDLE_RTL));
                    }
                    player.setIsSpecialAttack(false);
                    specialAttackCounter = 0;
                }
            }
        }
        if (!player.isInAir()) {
            canMoveCheck(MoveState.LEFT, player);
            canMoveCheck(MoveState.RIGHT, player);
            if (player.isFallDown()) {
                if (player.isSprint()) {
                    player.getPosition().isSprint = false;
                }
            }
            if (player.isSlide()) {
                canMoveCheck(MoveState.SLIDE, player);
            }
            checkIsCrouch();
        }
        if (!player.isInAir()) {
            if (canMoveCheck(MoveState.JUMP, player)) {
                yAfterJump = player.getPosition().getYPosition() - (player.getStats().getSpeed() + player.getStats().getJumpSpeed());
            }
        } else {
            if (yAfterJump != 0) {
                if (!player.isDoubleJump()) {
                    if (player.isLTR()) {
                        if (player.getCurrAnimation() instanceof PlayerJump_LTR); else {
                            player.setCurrAnimation(player.getAnimations().get(CharacterState.JUMP_LTR));
                        }
                    } else {
                        if (player.getCurrAnimation() instanceof PlayerJump_RTL); else {
                            player.setCurrAnimation(player.getAnimations().get(CharacterState.JUMP_RTL));
                        }
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
                    if (player.isSprint()) {
                        player.getPosition().isSprint = false;
                    }
                }
            }
        }
        applyGravityCharacter(player);
        if (!canHeavyAttack) {
            if (heavyAttackCounter <= heavyAttackLimit) {
                heavyAttackCounter++;
            } else {
                canHeavyAttack = true;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (player.isAttacked() || player.isDeath() || player.isSpecialAttack()
                || Game.STATE == GameState.OPTION_STATE) {
            return;
        }
        switch (e.getKeyCode()) {
            case KeyEvent.VK_E:
                eBtnPressed = true;
                break;
            case KeyEvent.VK_ENTER:
                enterPressed = true;
                break;
            case KeyEvent.VK_SPACE:
                spaceBtnPressed = true;
                break;
            case KeyEvent.VK_UP:
                upArrowPressed = true;
                break;
            case KeyEvent.VK_DOWN:
                downArrowPressed = true;
                break;
            case KeyEvent.VK_RIGHT:
                rightArrowPressed = true;
                break;
            case KeyEvent.VK_LEFT:
                leftArrowPressed = true;
                break;
            case KeyEvent.VK_ESCAPE:
                escPressed = true;
                break;
        }
        if (Game.STATE == GameState.DIALOGUE_STATE || Game.STATE == GameState.PLAYER_STATE) {
            return;
        }
        keyPresses.add(e.getKeyCode());
        if (keyPresses.size() > 0) {
            for (Iterator<Integer> it = keyPresses.iterator(); it.hasNext();) {
                int keyCode = it.next();
                switch (keyCode) {
                    case KeyEvent.VK_SPACE:
                        player.getDialogue().next();
                        break;
                    case KeyEvent.VK_W:
                    case KeyEvent.VK_UP:
                        if (player.isWallSlide()) {
                            player.getStats().setVely(0);
                            player.getPosition().isJump = true;
                            if (player.isAirAttack()) {
                                player.setIsAirAttack(false);
                            }
                            player.setWallSlide(false);
                            break;
                        }
                        if (player.isInAir() && !player.isDoubleJump()) {
                            player.setIsDoubleJump(true);
                            if (player.isLTR()) {
                                player.setCurrAnimation(player.getAnimations().get(CharacterState.JUMPROLL_LTR));
                            } else {
                                player.setCurrAnimation(player.getAnimations().get(CharacterState.JUMPROLL_RTL));
                            }
                            if (yAfterJump != 0) {
                                yAfterJump = player.getPosition().getYPosition() - (player.getStats().getSpeed() + player.getStats().getJumpSpeed());
                            }
                            break;
                        }
                        if (player.isAttack() || player.isAttacked() || player.isInAir() || player.isFallDown() || player.isSlide()) {
                            break;
                        }
                        if (lastPresseed > 0) {
                            lastPresseed = -1;
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
                        if (player.isAttacked() || player.isSlide()) {
                            break;
                        }
                        if (player.isAttack()) {
                            gameplay.getCamera().setIsCheckCameraPos(true);
                            if (player.getCurrAnimation() != null) {
                                if (player.getCurrAnimation().getSheet().getSpriteIndex() <= 1) {
                                    break;
                                }
                            }
                        }
                        if (lastPresseed > 0) {
                            lastPresseed = -1;
                        }
                        player.setIsLTR(false);
                        if (!player.isInAir() && !player.isFallDown()) {
                            if (player.getPosition().isSprint) {
                                if (player.getCurrAnimation() instanceof PlayerSprint_RTL); else {
                                    player.setCurrAnimation(player.getAnimations().get(CharacterState.SPRINT_RTL));
                                }
                            } else {
                                if (player.getCurrAnimation() instanceof PlayerRun_RTL); else {
                                    player.setCurrAnimation(player.getAnimations().get(CharacterState.RUNBACK));
                                }
                            }
                        }
                        player.getPosition().isMoveLeft = true;
                        break;
                    case KeyEvent.VK_S:
                    case KeyEvent.VK_DOWN:
                        if (player.isAttack() || player.isAttacked() || player.getPosition().isMoving()) {
                            break;
                        }
                        player.getPosition().isCrouch = true;
                        break;
                    case KeyEvent.VK_D:
                    case KeyEvent.VK_RIGHT:
                        if (player.isAttacked() || player.isSlide()) {
                            break;
                        }
                        if (player.isAttack()) {
                            gameplay.getCamera().setIsCheckCameraPos(true);
                            if (player.getCurrAnimation() != null
                                    && (player.getCurrAnimation() instanceof PlayerHeavyAttack
                                    || player.getCurrAnimation() instanceof PlayerLightAttack)) {
                                if (player.getCurrAnimation().getSheet().getSpriteIndex() <= 1) {
                                    break;
                                }
                            }
                        }
                        if (lastPresseed > 0) {
                            lastPresseed = -1;
                        }
                        player.setIsLTR(true);
                        if (!player.isInAir() && !player.isFallDown()) {
                            if (player.getPosition().isSprint) {
                                if (player.getCurrAnimation() instanceof PlayerSprint_LTR); else {
                                    player.setCurrAnimation(player.getAnimations().get(CharacterState.SPRINT_LTR));
                                }
                            } else {
                                if (player.getCurrAnimation() instanceof PlayerRun_LTR); else {
                                    player.setCurrAnimation(player.getAnimations().get(CharacterState.RUNFORWARD));
                                }
                            }
                        }
                        player.getPosition().isMoveRight = true;
                        break;
                    case KeyEvent.VK_CONTROL:
                        if (player.isAttack() || player.isAttacked() || player.getPosition().isMoving()
                                || player.isSlide() || player.isInAir()
                                || player.isFallDown()) {
                            break;
                        }
                        if (player.isLTR()) {
                            if (player.getCurrAnimation() != null
                                    && player.getCurrAnimation() instanceof PlayerSlide_LTR); else {
                                player.setCurrAnimation(player.getAnimations().get(CharacterState.SLIDE_LTR));
                            }
                        } else {
                            if (player.getCurrAnimation() != null
                                    && player.getCurrAnimation() instanceof PlayerSlide_RTL); else {
                                player.setCurrAnimation(player.getAnimations().get(CharacterState.SLIDE_RTL));
                            }
                        }
                        if (lastPresseed > 0) {
                            lastPresseed = -1;
                        }
                        player.getPosition().isSlide = true;
                        break;
                    case KeyEvent.VK_SHIFT:
                        if (player.isAttack() || player.isAttacked() || player.isSprint() || player.isSlide()
                                || player.isInAir() || player.isFallDown()) {
                            break;
                        }
                        if (lastPresseed > 0) {
                            lastPresseed = -1;
                        }
                        player.getPosition().isSprint = true;
                        break;
                    case KeyEvent.VK_J:
                    case KeyEvent.VK_K:
                        if (player.isAttack() || player.isAttacked() || player.getPosition().isCrouch) {
                            break;
                        }
                        if (!player.getPosition().isMoving()) {
                            Animation attack = null;
                            if ((player.isFallDown() || player.isInAir())) {
                                if (player.isAirAttack()) {
                                    break;
                                }
                                if (keyCode == KeyEvent.VK_K) {
                                    if (!canHeavyAttack) {
                                        break;
                                    }
                                }
                                int attackRange = player.getStats().getAttackRange();
                                if (player.isLTR()) {
                                    player.getPosition().setWidth(player.getPosition().getWidth() + attackRange); // 120
                                } else {
                                    player.getPosition().setXPosition(player.getPosition().getXPosition() - attackRange);
                                    player.getPosition().setWidth(player.getPosition().getWidth() + attackRange);
                                }
                                if (player.isLTR()) {
                                    switch (keyCode) {
                                        case KeyEvent.VK_J:
                                            attack = player.getAnimations().get(CharacterState.AIRATTACK01_LTR);
                                            attackEnemies(attack, player.attackHitBox(), 50);
                                            break;
                                        case KeyEvent.VK_K:
                                            attack = player.getAnimations().get(CharacterState.AIRATTACK02_LTR);
                                            attackEnemies(attack, player.attackHitBox(), 50, player.getStats().getAttackDamage() + 5);
                                            canHeavyAttack = false;
                                            heavyAttackCounter = 0;
                                            break;
                                        case KeyEvent.VK_L:
                                            attack = player.getAnimations().get(CharacterState.AIRATTACK03_LTR);
                                            attackEnemies(attack, player.attackHitBox(), 50, player.getStats().getAttackDamage() + 5);
                                            break;
                                    }
                                } else {
                                    switch (keyCode) {
                                        case KeyEvent.VK_J:
                                            attack = player.getAnimations().get(CharacterState.AIRATTACK01_RTL);
                                            attackEnemies(attack, player.attackHitBox(), 50);
                                            break;
                                        case KeyEvent.VK_K:
                                            attack = player.getAnimations().get(CharacterState.AIRATTACK02_RTL);
                                            attackEnemies(attack, player.attackHitBox(), 50, player.getStats().getAttackDamage() + 5);
                                            canHeavyAttack = false;
                                            heavyAttackCounter = 0;
                                            break;
                                        case KeyEvent.VK_L:
                                            attack = player.getAnimations().get(CharacterState.AIRATTACK03_RTL);
                                            attackEnemies(attack, player.attackHitBox(), 50);
                                            break;
                                    }
                                }
                                player.setIsAirAttack(true);
                            } else {
                                if (lastPresseed > 0) {
                                    if (keyCode == KeyEvent.VK_J) {
                                        if (lastPresseed == KeyEvent.VK_L) {
                                            keyCode = KeyEvent.VK_J;
                                        } else if (lastPresseed == KeyEvent.VK_J) {
                                            keyCode = KeyEvent.VK_K;
                                        } else if (lastPresseed == KeyEvent.VK_K) {
                                            keyCode = KeyEvent.VK_L;
                                        }
                                    } else {
                                        if (lastPresseed == KeyEvent.VK_L) {
                                            keyCode = KeyEvent.VK_K;
                                        } else if (lastPresseed == KeyEvent.VK_K) {
                                            keyCode = KeyEvent.VK_L;
                                        }
                                    }
                                }
                                lastPresseed = keyCode;
                                if (keyCode == KeyEvent.VK_K) {
                                    if (!canHeavyAttack) {
                                        break;
                                    }
                                }
                                int attackRange = player.getStats().getAttackRange();
                                if (player.isLTR()) {
                                    player.getPosition().setWidth(player.getPosition().getWidth() + attackRange); // 120
                                } else {
                                    player.getPosition().setXPosition(player.getPosition().getXPosition() - attackRange);
                                    player.getPosition().setWidth(player.getPosition().getWidth() + attackRange);
                                }
                                if (keyCode == KeyEvent.VK_J) {
                                    if (player.isLTR()) {
                                        attack = player.getAnimations().get(CharacterState.ATTACK01_LTR);
                                    } else {
                                        attack = player.getAnimations().get(CharacterState.ATTACK01_RTL);
                                    }
                                    attackEnemies(attack, player.attackHitBox(), 50);
                                } else if (keyCode == KeyEvent.VK_K) {
                                    if (player.isLTR()) {
                                        attack = player.getAnimations().get(CharacterState.ATTACK02_LTR);
                                    } else {
                                        attack = player.getAnimations().get(CharacterState.ATTACK02_RTL);
                                    }
                                    attackEnemies(attack, player.attackHitBox(), 60, player.getStats().getAttackDamage() + 5);
                                    canHeavyAttack = false;
                                    heavyAttackCounter = 0;
                                } else if (keyCode == KeyEvent.VK_L) {
                                    if (player.isLTR()) {
                                        attack = player.getAnimations().get(CharacterState.ATTACK03_LTR);
                                    } else {
                                        attack = player.getAnimations().get(CharacterState.ATTACK03_RTL);
                                    }
//                                    player.setIsSpecialAttack(true);
                                    attackEnemies(attack, player.attackHitBox(), 50);
                                }
                            }
                            gameplay.getCamera().setIsCheckCameraPos(false);
                        } else {
                            if ((player.isFallDown() || player.isInAir() || player.isSlide())) {
                                break;
                            }
                            if (lastPresseed > 0) {
                                lastPresseed = -1;
                            }
                            Animation attack = null;
                            if (player.isLTR()) {
                                attack = player.getAnimations().get(CharacterState.ATTACK03_LTR);
                            } else {
                                attack = player.getAnimations().get(CharacterState.ATTACK03_RTL);
                            }
                            int attackRange = player.getStats().getAttackRange();
                            if (player.isLTR()) {
                                player.getPosition().setWidth(player.getPosition().getWidth() + attackRange); // 120
                            } else {
                                player.getPosition().setXPosition(player.getPosition().getXPosition() - attackRange);
                                player.getPosition().setWidth(player.getPosition().getWidth() + attackRange);
                            }
                            attackEnemies(attack, player.attackHitBox(), 50);
                        }
                        break;
                }
            }
        }
    }

    public void attackEnemies(Animation attack, GamePosition attackHitBox, int stunTime) {
        attackEnemies(attack, attackHitBox, stunTime, -1);
    }

    public void attackEnemies(Animation attack, GamePosition attackHitBox, int stunTime, int attackDamage) {
        player.setCurrAnimation(attack);
        player.setIsAttack(true);
        boolean hitEnemy = false;
        if (gameplay.getEnemies() != null && gameplay.getEnemies().size() > 0) {
            for (int j = 0; j < gameplay.getEnemies().size(); j++) {
                Enemy enemy = gameplay.getEnemies().get(j);
                if (attackDamage > 0) {
                    hitEnemy = enemy.checkHit(attackHitBox, true, player, attackDamage);
                } else {
                    hitEnemy = enemy.checkHit(attackHitBox, true, player);
                }
                if (hitEnemy) {
                    enemy.setStunTime(stunTime);
                    if (enemy.getStats().getHealth() <= 0) {
                        gameplay.getAudioPlayer().startThread("kill_sound", false, gameplay.getOptionHandler().getOptionMenu().getSfxVolume());
                    } else {
                        gameplay.getAudioPlayer().startThread("hit_dior_firror", false, gameplay.getOptionHandler().getOptionMenu().getSfxVolume());
                    }
                }
            }
        }
        if (!hitEnemy) {
            gameplay.getAudioPlayer().startThread("swing_sword", false, gameplay.getOptionHandler().getOptionMenu().getSfxVolume());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyPresses.remove(e.getKeyCode());
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                upArrowPressed = false;
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                leftArrowPressed = false;
                if (player.isSprint()) {
                    player.getPosition().isSprint = false;
                }
                player.getPosition().isMoveLeft = false;
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                downArrowPressed = false;
                player.getPosition().isCrouch = false;
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                rightArrowPressed = false;
                if (player.isSprint()) {
                    player.getPosition().isSprint = false;
                }
                player.getPosition().isMoveRight = false;
                break;
            case KeyEvent.VK_CONTROL:
                player.getPosition().isSlide = false;
                break;
            case KeyEvent.VK_E:
                eBtnPressed = false;
                break;
            case KeyEvent.VK_SPACE:
                spaceBtnPressed = false;
                break;
            case KeyEvent.VK_ENTER:
                enterPressed = false;
                break;
            case KeyEvent.VK_ESCAPE:
                escPressed = false;
                break;
        }
        if (!player.isDeath()) {
            if (player.isUseItem()) {
                return;
            }
            if (!player.isAttack() && !player.isAttacked()) {
                if (player.getPosition().isNotPressKey() && !player.isSpecialAttack()
                        && !player.isInAir() && !player.isFallDown()) {
                    if (player.isLTR()) {
                        player.setCurrAnimation(player.getAnimations().get(CharacterState.IDLE_LTR));
                    } else {
                        player.setCurrAnimation(player.getAnimations().get(CharacterState.IDLE_RTL));
                    }
                }
            }
        }
    }

    public boolean eBtnPressed() {
        return eBtnPressed;
    }

    public boolean isSpaceBtnPressed() {
        return spaceBtnPressed;
    }

    public void setSpaceBtnPressed(boolean isSpacePressed) {
        spaceBtnPressed = isSpacePressed;
    }

    public boolean isUpArrowPressed() {
        return upArrowPressed;
    }

    public void setUpArrowPressed(boolean upArrowPressed) {
        this.upArrowPressed = upArrowPressed;
    }

    public boolean isDownArrowPressed() {
        return downArrowPressed;
    }

    public void setDownArrowPressed(boolean downArrowPressed) {
        this.downArrowPressed = downArrowPressed;
    }

    public boolean isEnterPressed() {
        return enterPressed;
    }

    public void setEnterPressed(boolean enterPressed) {
        this.enterPressed = enterPressed;
    }

    public boolean isRightArrowPressed() {
        return rightArrowPressed;
    }

    public void setRightArrowPressed(boolean rightArrowPressed) {
        this.rightArrowPressed = rightArrowPressed;
    }

    public boolean isLeftArrowPressed() {
        return leftArrowPressed;
    }

    public void setLeftArrowPressed(boolean leftArrowPressed) {
        this.leftArrowPressed = leftArrowPressed;
    }

    public boolean isEscPressed() {
        return escPressed;
    }

    public void setEscPressed(boolean escPressed) {
        this.escPressed = escPressed;
    }

}
