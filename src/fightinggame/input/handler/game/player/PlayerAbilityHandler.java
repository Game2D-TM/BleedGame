package fightinggame.input.handler.game.player;

import fightinggame.Game;
import fightinggame.Gameplay;
import fightinggame.entity.GamePosition;
import fightinggame.entity.Player;
import fightinggame.entity.Rule;
import fightinggame.entity.ability.type.throwable.Fireball;
import fightinggame.entity.item.Item;
import fightinggame.entity.item.collectable.buff.EnergyPotion;
import fightinggame.entity.item.collectable.buff.HealthPotion;
import fightinggame.entity.state.CharacterState;
import fightinggame.entity.state.GameState;
import fightinggame.input.handler.GameHandler;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PlayerAbilityHandler extends GameHandler implements KeyListener {

    private final Player player;
    private final Gameplay gameplay;
    private int useItemCounter = 0;
    private int useItemLimit = 100;

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
        if (e.getKeyCode() == KeyEvent.VK_8) {
            player.getStats().setHealth(100);
            player.getStats().addEnergy(100);
            player.setIsDeath(false);
        }
        if (!player.isDeath() && Game.STATE == GameState.GAME_STATE) {
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
                        boolean result = ((Fireball) player.getAbility(1)).execute(spawnPosition, endPos);
                        if (result) {
                            player.setIsSpellCast(true);
                            if (player.isLTR()) {
                                player.setCurrAnimation(player.getAnimations().get(CharacterState.SPELLCAST_LTR));
                            } else {
                                player.setCurrAnimation(player.getAnimations().get(CharacterState.SPELLCASTLOOP_RTL));
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
                    Item item = player.getInventory().getItemByName("S Health Potion");
                    if (item == null) {
                        item = player.getInventory().getItemByName("M Health Potion");
                    }
                    if (item == null) {
                        item = player.getInventory().getItemByName("L Health Potion");
                    }
                    if (item == null) {
                        break;
                    }
                    if (item instanceof HealthPotion) {
                        if (item.use()) {
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
                case KeyEvent.VK_4:
                    item = player.getInventory().getItemByName("S Energy Potion");
                    if (item == null) {
                        item = player.getInventory().getItemByName("M Energy Potion");
                    }
                    if (item == null) {
                        item = player.getInventory().getItemByName("L Energy Potion");
                    }
                    if (item == null) {
                        break;
                    }
                    if (item instanceof EnergyPotion) {
                        if (item.use()) {
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
                case KeyEvent.VK_I:
                    if (!player.isAttack() && !player.getPosition().isMoving()) {
                        player.getInventory().open();
                    }
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
