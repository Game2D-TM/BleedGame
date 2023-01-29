package fightinggame.input.handler.game.player;

import fightinggame.Game;
import fightinggame.Gameplay;
import fightinggame.animation.player.PlayerIdle;
import fightinggame.entity.GamePosition;
import fightinggame.entity.Player;
import fightinggame.entity.Rule;
import fightinggame.entity.ability.type.skill.ItemSlot;
import fightinggame.entity.ability.type.throwable.Fireball;
import fightinggame.entity.item.Item;
import fightinggame.entity.state.CharacterState;
import fightinggame.entity.state.GameState;
import fightinggame.input.handler.GameHandler;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

public class PlayerAbilityHandler extends GameHandler implements KeyListener {

    private final Player player;
    private final Gameplay gameplay;
    private int useItemCounter = 0;
    private int useItemLimit = 100;

    private int itemIndex = 0;

    public PlayerAbilityHandler(Player player, String name, Gameplay gameplay) {
        super(name);
        this.player = player;
        this.gameplay = gameplay;
    }

    @Override
    public void tick() {
        if (player.isUseItem()) {
            if (useItemCounter <= useItemLimit) {
                useItemCounter++;
            } else {
                if (player.isLTR()) {
                    player.setCurrAnimation(player.getAnimations().get(CharacterState.IDLE_LTR));
                } else {
                    player.setCurrAnimation(player.getAnimations().get(CharacterState.IDLE_RTL));
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
                player.setIsUseItem(false);
                useItemCounter = 0;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (Game.STATE == GameState.GAME_STATE) {
            if (e.getKeyCode() == KeyEvent.VK_8) {
                player.getStats().setHealth(player.getStatusBar().getMaxHealth());
                player.getStats().addEnergy(player.getStatusBar().getMaxEnergy());
                player.setIsDeath(false);
            }
            if (e.getKeyCode() == KeyEvent.VK_5) {
                player.getStats().setHealth(50);
            }
        }
        if (!player.isDeath()) {
            if (Game.STATE == GameState.GAME_STATE
                    || Game.STATE == GameState.PLAYER_STATE) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_I:
                        if (!player.isAttack() && !player.getPosition().isMoving()) {
                            player.getInventory().open();
                        }
                        break;
                    case KeyEvent.VK_ESCAPE:
                        if (player.getInventory().isOpen()) {
                            player.getInventory().open();
                        }
                        break;
                }
            }
            if (Game.STATE == GameState.GAME_STATE) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_1:
                        player.getAbility(0).execute();
                        break;
                    case KeyEvent.VK_2:
//                    if(player.isInAir() || player.isFallDown()) {
//                        break;
//                    }
                        if (!player.isSpellCast() && !player.isAttack() && !player.getPosition().isMoving()
                                && !player.getPosition().isCrouch && !player.isDeath()) {
                            int spawnX;
                            int xChange;
                            GamePosition endPos;
                            if (player.isLTR()) {
                                xChange = 15;
                                spawnX = player.getPosition().getMaxX() + xChange;
                                endPos = new GamePosition(
                                        player.getPosition().getMaxX() + xChange, 0,
                                        gameplay.getCamera().getPosition().getMaxX()
                                        - (player.getPosition().getMaxX() + xChange), 0);

                            } else {
                                xChange = 215;
                                spawnX = player.getPosition().getXPosition() - xChange;
                                endPos = new GamePosition(
                                        gameplay.getCamera().getPosition().getXPosition() - xChange, 0,
                                        0, 0);
                            }
                            GamePosition spawnPosition
                                    = new GamePosition(spawnX,
                                            player.getPosition().getYPosition() + 70, 200, 100);
                            Fireball fireball = ((Fireball) player.getAbility(1));
                            if (fireball.isCanUse()) {
                                player.getAbility(1).setCanUse(false);
                                fireball = (Fireball) fireball.clone();
                                player.getAbilities().add(fireball);
                                boolean result = fireball.execute(spawnPosition, endPos);
                                if (result) {
                                    player.setIsSpellCast(true);
                                    if (player.isLTR()) {
                                        player.setCurrAnimation(player.getAnimations().get(CharacterState.SPELLCAST_LTR));
                                    } else {
                                        player.setCurrAnimation(player.getAnimations().get(CharacterState.SPELLCASTLOOP_RTL));
                                    }
                                }
                            }
//                try {
//                    Thread.sleep(200);
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(PlayerAbilityHandler.class.getName()).log(Level.SEVERE, null, ex);
//                }
                        }
                        break;
                    case KeyEvent.VK_3:
                        ItemSlot itemSlot = ((ItemSlot) player.getAbility(2));
                        if (itemSlot == null) {
                            break;
                        }
                        if (itemSlot.haveItem()) {
                            if (itemSlot.execute()) {
                                player.setIsUseItem(true);
                                if (player.isLTR()) {
                                    player.setCurrAnimation(player.getAnimations().get(CharacterState.USEITEM_LTR));
                                } else {
                                    player.setCurrAnimation(player.getAnimations().get(CharacterState.USEITEM_RTL));
                                }
                            } else {
                                System.out.println("Is Cooldown.");
                            }
                        }
                        break;
                    case KeyEvent.VK_U:
                        List<Item> items = player.getInventory().getAllItemsFromInventory();
                        if (items == null || items.isEmpty()) {
                            break;
                        }
                        if (itemIndex < items.size() - 1) {
                            itemIndex++;
                        } else {
                            itemIndex = 0;
                        }
                        Item item = items.get(itemIndex);
                        if (item == null) {
                            break;
                        }
                        itemSlot = ((ItemSlot) player.getAbility(2));
                        if (itemSlot == null) {
                            break;
                        }
                        itemSlot.setItem(item);
                        break;
                    case KeyEvent.VK_O:
                        items = player.getInventory().getAllItemsFromInventory();
                        if (items == null || items.isEmpty()) {
                            break;
                        }
                        if (itemIndex > 0) {
                            itemIndex--;
                        } else {
                            itemIndex = items.size() - 1;
                        }
                        item = items.get(itemIndex);
                        if (item == null) {
                            break;
                        }
                        itemSlot = ((ItemSlot) player.getAbility(2));
                        if (itemSlot == null) {
                            break;
                        }
                        itemSlot.setItem(item);
                        break;
                    case KeyEvent.VK_Q:
                        Rule rule = gameplay.getRule();
                        if (rule == null) {
                            break;
                        }
                        rule.setIsRenderQuest(!rule.isRenderQuest());
                        break;
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_2:
                if (player.isSpellCast()) {
                    player.setIsSpellCast(false);
                    if (player.isLTR()) {
                        player.setCurrAnimation(player.getAnimations().get(CharacterState.IDLE_LTR));
                    } else {
                        player.setCurrAnimation(player.getAnimations().get(CharacterState.IDLE_RTL));
                    }
                }
                break;
        }
    }

}
