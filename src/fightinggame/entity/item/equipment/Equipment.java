package fightinggame.entity.item.equipment;

import fightinggame.Gameplay;
import fightinggame.entity.Animation;
import fightinggame.entity.Character;
import fightinggame.entity.CharacterState;
import fightinggame.entity.item.Item;
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

    public boolean isIsEquip() {
        return isEquip;
    }

    public void setIsEquip(boolean isEquip) {
        this.isEquip = isEquip;
    }

    @Override
    public boolean checkHit(Character character) {
        boolean result = super.checkHit(character);
        if (result) {
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
        return false;
    }

}
