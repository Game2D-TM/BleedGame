package fightinggame.entity.item.collectable.healing;

import fightinggame.Gameplay;
import fightinggame.animation.item.PotionAnimation;
import fightinggame.entity.Character;
import fightinggame.entity.SpriteSheet;
import fightinggame.entity.ability.Ability;
import fightinggame.entity.ability.type.Heal;
import fightinggame.entity.ability.type.healing.PotionHeal;
import fightinggame.entity.inventory.Inventory;
import fightinggame.entity.item.Item;
import fightinggame.entity.item.collectable.CollectableItem;

public class SmallHealthPotion extends CollectableItem {

    public SmallHealthPotion(int id, Character character, Gameplay gameplay, int amount) {
        super(id, "S Health Potion", null, character, gameplay, amount);
        SpriteSheet healthPotionSheet = new SpriteSheet();
        healthPotionSheet.add("assets/res/item/s_health_potion.png");
        animation = new PotionAnimation(0, healthPotionSheet, -1);
        Ability potionHeal = new PotionHeal(10, 0, 1000, null, null, null, null, gameplay, character);
        abilities.add(potionHeal);
        description = "[S Health Potion] Recover 10 HP.";
    }

    public SmallHealthPotion(int id, Character character, Gameplay gameplay, int amount, int price) {
        super(id, "S Health Potion", null, character, gameplay, amount, price);
        SpriteSheet healthPotionSheet = new SpriteSheet();
        healthPotionSheet.add("assets/res/item/s_health_potion.png");
        animation = new PotionAnimation(0, healthPotionSheet, -1);
        Ability potionHeal = new PotionHeal(10, 0, 1000, null, null, null, null, gameplay, character);
        abilities.add(potionHeal);
        description = "[S Health Potion] Recover 10 HP.";
    }

    @Override
    public boolean checkHit(Character character) {
        boolean result = super.checkHit(character);
        if (result) {
            gameplay.getAudioPlayer().startThread("health_pickup", false, gameplay.getOptionHandler().getOptionMenu().getSfxVolume());
            return true;
        }
        return false;
    }

    @Override
    public boolean use() {
        Heal heal = (Heal) abilities.get(0);
        if (heal == null) {
            return false;
        }
        boolean result = heal.execute();
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
                return true;
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
        return new SmallHealthPotion(id, character, gameplay, amount);
    }

}
