package fightinggame.entity.item.collectable.buff;

import fightinggame.Gameplay;
import fightinggame.entity.Animation;
import fightinggame.entity.Character;
import fightinggame.entity.ability.Ability;
import fightinggame.entity.ability.type.EnergyRecovery;
import fightinggame.entity.ability.type.recovery.PotionEnergyRecovery;
import fightinggame.entity.ability.type.recovery.PotionHeal;
import fightinggame.entity.inventory.Inventory;
import fightinggame.entity.item.collectable.CollectableItem;

public abstract class ElixirPotion extends CollectableItem {

    protected int healPoint;
    protected int energyPoint;

    public ElixirPotion(int id, String name,
            Animation animation, Character character,
            Gameplay gameplay, int amount, int healPoint, int energyPoint) {
        super(id, name, animation, character, gameplay, amount);
        this.healPoint = healPoint;
        this.energyPoint = energyPoint;
        Ability potionHeal = new PotionHeal(healPoint, 0, 1000, null, null, gameplay, character);
        abilities.add(potionHeal);
        EnergyRecovery energyRecovery = new PotionEnergyRecovery(energyPoint, 0, 1000,
                null, null, gameplay, character);
        abilities.add(energyRecovery);
        description = "[" + name + "] Recover " + healPoint + " HP And " + energyPoint + " MP.";
    }

    public ElixirPotion(int id, String name,
            Animation animation, Character character,
            Gameplay gameplay, int amount, int price, int healPoint, int energyPoint) {
        super(id, name, animation, character, gameplay, amount, price);
        this.healPoint = healPoint;
        this.energyPoint = energyPoint;
        Ability potionHeal = new PotionHeal(healPoint, 0, 1000, null, null, gameplay, character);
        abilities.add(potionHeal);
        EnergyRecovery energyRecovery = new PotionEnergyRecovery(energyPoint, 0, 1000,
                null, null, gameplay, character);
        abilities.add(energyRecovery);
        description = "[" + name + "] Recover " + healPoint + " HP And " + energyPoint + " MP.";
    }

    @Override
    public boolean checkHit(Character character) {
        boolean result = super.checkHit(character);
        if (result) {
            gameplay.getAudioPlayer().startThread("potion_pickup", false, gameplay.getOptionHandler().getOptionMenu().getSfxVolume());
            return true;
        }
        return false;
    }

    @Override
    public boolean use() {
        if (abilities.size() <= 0) {
            return false;
        }
        boolean result = false;
        for (int i = 0; i < abilities.size(); i++) {
            Ability ability = abilities.get(i);
            if (ability != null) {
                boolean temp = ability.execute();
                if (temp) {
                    result = true;
                }
            }
        }
        if (result) {
            if (amount > 0) {
                int nAmount = amount - 1;
                System.out.println("Used 1" + name
                        + " " + nAmount + " Left"
                        + " Wait for " + abilities.get(0).getCoolDownTime());
                if (nAmount == 0) {
                    Inventory inventory = character.getInventory();
                    inventory.removeItemFromInventory(this);
                }
                amount = nAmount;
                gameplay.getAudioPlayer().startThread("potion_use", false, gameplay.getOptionHandler().getOptionMenu().getSfxVolume());
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
    public int getYHitBox() {
        return position.getYPosition();
    }

    public int getHealPoint() {
        return healPoint;
    }

    public void setHealPoint(int healPoint) {
        this.healPoint = healPoint;
    }

    public int getEnergyPoint() {
        return energyPoint;
    }

    public void setEnergyPoint(int energyPoint) {
        this.energyPoint = energyPoint;
    }

}
