package fightinggame.entity.item.healing;

import fightinggame.Gameplay;
import fightinggame.entity.Animation;
import fightinggame.entity.Character;
import fightinggame.entity.ability.type.Heal;
import fightinggame.entity.inventory.Inventory;
import fightinggame.entity.item.Item;

public class HealthPotion extends Item {

    public HealthPotion(int id, String name, Animation animation, Character character,
            Gameplay gameplay, int amount) {
        super(id, name, animation, character, gameplay, amount);
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
                    Inventory inventory = character.getInventory();
                    inventory.removeItemFromInventory(this);
                } else {
                    amount = nAmount;
                }
            }
        }
        return false;
    }

    @Override
    public int getXHitBox() {
        return position.getXPosition() + 20;
    }

    @Override
    public int getWidthHitBox() {
        return position.getWidth() - 40;
    }

    @Override
    public int getHeightHitBox() {
        return position.getHeight();
    }

    @Override
    public int getXMaxHitBox() {
        return getXHitBox() + getWidthHitBox();
    }

    @Override
    public int getYHitBox() {
        return position.getYPosition();
    }

    @Override
    public int getYMaxHitBox() {
        return getYHitBox() + getHeightHitBox();
    }

    @Override
    public Item clone() {
        return new HealthPotion(id, name, animation,
                 character, gameplay, amount);
    }

}
