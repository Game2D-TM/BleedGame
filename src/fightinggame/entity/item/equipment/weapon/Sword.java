package fightinggame.entity.item.equipment.weapon;

import fightinggame.Gameplay;
import fightinggame.entity.Animation;
import fightinggame.entity.Character;
import fightinggame.entity.state.CharacterState;
import fightinggame.entity.ability.type.IncreaseStat;
import fightinggame.entity.item.Item;
import fightinggame.entity.item.equipment.Equipment;
import java.util.Map;

public class Sword extends Equipment {

    public Sword(int id, String name, Animation animation, Character character, Gameplay gameplay, int amount) {
        super(id, name, animation, character, gameplay, amount);
    }

    public Sword(int id, String name, Animation animation, Character character, Gameplay gameplay, int amount, Map<CharacterState, Animation> itemEquipAnimations) {
        super(id, name, animation, character, gameplay, amount, itemEquipAnimations);
    }

    @Override
    public boolean use() {
        if (!isEquip) {
            if (abilities.size() <= 0) {
                return false;
            }
            boolean result = false;
            for (int i = 0; i < abilities.size(); i++) {
                if (abilities.get(i) instanceof IncreaseStat) {
                    IncreaseStat increaseStat = (IncreaseStat) abilities.get(i);
                    if (increaseStat != null) {
                        increaseStat.execute();
                        if (!result) {
                            result = true;
                        }
                    }
                }
            }
            if (result) {
                if (itemEquipAnimations != null && itemEquipAnimations.size() > 0) {
                    for (CharacterState state : itemEquipAnimations.keySet()) {
                        character.getAnimations().put(state, itemEquipAnimations.get(state));
                    }
                }
                isEquip = true;
                return true;
            }
        }
        return false;
    }

    @Override
    public Item clone() {
        return new Sword(id, name, animation, character, gameplay, amount);
    }

    @Override
    public int getXHitBox() {
        return position.getXPosition() + 30;
    }

    @Override
    public int getWidthHitBox() {
        return position.getWidth() - 55;
    }

    @Override
    public int getHeightHitBox() {
        return position.getHeight() - 55;
    }

    @Override
    public int getXMaxHitBox() {
        return getXHitBox() + getWidthHitBox();
    }

    @Override
    public int getYHitBox() {
        return position.getYPosition() + 25;
    }

    @Override
    public int getYMaxHitBox() {
        return getYHitBox() + getHeightHitBox();
    }

}
