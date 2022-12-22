package fightinggame.entity.item.healing;

import fightinggame.Gameplay;
import fightinggame.entity.Animation;
import fightinggame.entity.Character;
import fightinggame.entity.GamePosition;
import fightinggame.entity.ability.type.Heal;
import fightinggame.entity.item.Item;
import java.util.List;

public class HealthPotion extends Item {

    public HealthPotion(int id, String name, Animation animation, Character character, GamePosition position,
            Gameplay gameplay, int amount) {
        super(id, name, animation, character, position, gameplay, amount);
    }

    @Override
    public boolean use() {
        Heal heal = (Heal) abilities.get(0);
        if (heal == null) {
            return false;
        }
        boolean result = heal.healing();
        if (result) {
            if (amount > 0) {
                int nAmount = amount - 1;
                System.out.println("Used 1" + name
                        + " " + nAmount + " Left"
                        + " Wait for " + abilities.get(0).getCoolDownTime());
                if (nAmount == 0) {
                    boolean isRemove = false;
                    List<List<Item>> items = character.getInventory();
                    if (items != null && items.size() > 0) {
                        for (int i = 0; i < items.size(); i++) {
                            List<Item> list = items.get(i);
                            if (list != null && list.size() > 0) {
                                if (list.contains(this)) {
                                    list.remove(this);
                                    isRemove = true;
                                    System.out.println(isRemove);
                                    break;
                                }
                            }
                            if(isRemove) break;
                        }
                    }
                } else {
                    amount = nAmount;
                }
            }
        }
        return false;
    }

}
