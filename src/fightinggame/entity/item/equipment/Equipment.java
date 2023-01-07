package fightinggame.entity.item.equipment;

import fightinggame.Gameplay;
import fightinggame.entity.Animation;
import fightinggame.entity.Character;
import fightinggame.entity.state.CharacterState;
import fightinggame.entity.ability.Ability;
import fightinggame.entity.item.Item;
import java.util.List;
import java.util.Map;

public abstract class Equipment extends Item {

    protected boolean isEquip;
    protected Map<CharacterState, Animation> itemEquipAnimations;

    public Equipment(int id, String name, Animation animation,
            Character character, Gameplay gameplay, int amount) {
        super(id, name, animation, character, gameplay, amount);
    }

    public Equipment(int id, String name, Animation animation,
            Character character, Gameplay gameplay, int amount, Map<CharacterState, Animation> itemEquipAnimations) {
        super(id, name, animation, character, gameplay, amount);
        this.itemEquipAnimations = itemEquipAnimations;
    }

    public Equipment(boolean isEquip, int id, String name,
            Animation animation, Character character, Gameplay gameplay, int amount) {
        super(id, name, animation, character, gameplay, amount);
        this.isEquip = isEquip;
    }

    public Map<CharacterState, Animation> getItemEquipAnimations() {
        return itemEquipAnimations;
    }

    public void setItemEquipAnimations(Map<CharacterState, Animation> itemEquipAnimations) {
        this.itemEquipAnimations = itemEquipAnimations;
    }

    public boolean isEquip() {
        return isEquip;
    }

    public void setIsEquip(boolean isEquip) {
        this.isEquip = isEquip;
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
                    spawnDrop = false;
                    dropExpireCounter = 0;
                    result = use();
                    if (result) {
                        if (itemEquipAnimations != null && itemEquipAnimations.size() > 0) {
                            for (CharacterState state : itemEquipAnimations.keySet()) {
                                character.getAnimations().put(state, itemEquipAnimations.get(state));
                            }
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
