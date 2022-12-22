package fightinggame.input.handler;

import fightinggame.Gameplay;
import fightinggame.entity.CharacterState;
import fightinggame.entity.GamePosition;
import fightinggame.entity.Player;
import fightinggame.entity.ability.type.throwable.Fireball;
import fightinggame.entity.item.Item;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

public class PlayerAbilityHandler extends Handler implements KeyListener {

    private Player player;
    private Gameplay gameplay;

    public PlayerAbilityHandler(Player player, String name, Gameplay gameplay) {
        super(name);
        this.player = player;
        this.gameplay = gameplay;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!player.isDeath()) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_1:
                    if (!player.isDeath()) {
                        player.getAbility(0).execute();
                    }
                    break;
                case KeyEvent.VK_2:
                    if (!player.getPosition().isMoving() && !player.isDeath()) {
                        int spawnX;
                        if (player.isLTR()) {
                            spawnX = player.getPosition().getMaxX() + 15;
                        } else {
                            spawnX = player.getPosition().getXPosition() - 215;
                        }
                        GamePosition spawnPosition
                                = new GamePosition(spawnX,
                                        player.getPosition().getYPosition() + 30, 200, 100);
                        boolean result = ((Fireball) player.getAbility(1)).execute(spawnPosition, gameplay.getPlayPosition());
                        if (result) {
                            if (player.isLTR()) {
                                player.setCurrAnimation(player.getAnimations().get(CharacterState.ATTACK_LTR));
                            } else {
                                player.setCurrAnimation(player.getAnimations().get(CharacterState.ATTACK_RTL));
                            }
                        }
                    }
                    break;
                case KeyEvent.VK_3:
                    if (!player.isDeath()) {
                        List<List<Item>> inventory = player.getInventory();
                        Item firstItem = null;
                        if (inventory != null && inventory.size() > 0) {
                            for (int i = 0; i < inventory.size(); i++) {
                                if (inventory.get(i) != null && inventory.get(i).size() > 0) {
                                    for (int j = 0; j < inventory.get(i).size(); j++) {
                                        firstItem = inventory.get(i).get(j);
                                        if (firstItem != null) {
                                            break;
                                        }
                                    }
                                }
                                if (firstItem != null) {
                                    break;
                                }
                            }
                        }
                        if (firstItem == null) {
                            break;
                        }
                        if (firstItem.use()) {
                            
                        } else {
                            System.out.println("Is Cooldown.");
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void tick() {

    }

}
