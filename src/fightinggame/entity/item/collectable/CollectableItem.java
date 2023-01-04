package fightinggame.entity.item.collectable;

import fightinggame.Gameplay;
import fightinggame.entity.Animation;
import fightinggame.entity.Character;
import fightinggame.entity.ability.Ability;
import fightinggame.entity.inventory.Inventory;
import fightinggame.entity.item.Item;
import java.util.List;

public abstract class CollectableItem extends Item {

    public CollectableItem(int id, String name, Animation animation, Character character, Gameplay gameplay, int amount) {
        super(id, name, animation, character, gameplay, amount);
    }

    @Override
    public boolean checkHit(Character character) {
        boolean result = super.checkHit(character);
        if (result) {
            List<Item> itemsOnGround = gameplay.getItemsOnGround();
            if (itemsOnGround.size() > 0) {
                if (itemsOnGround.contains(this)) {
                    this.character = character;
                    if (abilities.size() > 0) {
                        for (int i = 0; i < abilities.size(); i++) {
                            Ability ability = abilities.get(i);
                            if (ability != null) {
                                ability.setCharacter(character);
                            }
                        }
                    }
                    itemsOnGround.remove(this);
                    Inventory inventory = character.getInventory();
                    inventory.addItemToInventory(this);
                    spawnDrop = false;
                    dropExpireCounter = 0;
                    return true;
                }
            }
        }
        return false;
    }

}
