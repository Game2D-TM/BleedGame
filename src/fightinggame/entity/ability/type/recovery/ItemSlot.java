package fightinggame.entity.ability.type.recovery;

import fightinggame.Gameplay;
import fightinggame.entity.Character;
import fightinggame.entity.GamePosition;
import fightinggame.entity.SpriteSheet;
import fightinggame.entity.ability.Ability;
import fightinggame.entity.ability.type.skill.ActiveSkill;
import fightinggame.entity.item.Item;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

public class ItemSlot extends ActiveSkill {

    private Item item;

    public ItemSlot(BufferedImage border, GamePosition position, int id, long resetTime, int energyLost, SpriteSheet skillIcon, Gameplay gameplay, Character character) {
        super(border, position, id, "Item Slot", resetTime, energyLost, skillIcon, null, gameplay, character);
    }

    @Override
    public void tick() {
        if (haveItem()) {
            super.tick();
        } else {
            if (character != null) {
                setItem(character.getInventory().getItemAscending());
            }
        }
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
    }

    @Override
    public boolean execute() {
        if (haveItem()) {
            if (item.use()) {
                if (item.getAmount() == 0) {
                    item = null;
                    resetTime = 0;
                    skillIcon = null;
                } else {
                    canUse = false;
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean execute(Character character) {
        return false;
    }

    @Override
    public boolean execute(List<Character> characters) {
        return false;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        if (item == null) {
            return;
        }
        this.item = item;
        resetTime = item.getAbilityResetTime(0);
        skillIcon = new SpriteSheet();
        skillIcon.getImages().add(item.getIcon());
        Ability ability = item.getAbilities().get(0);
        if (ability != null) {
            setSkillCounter(ability.getSkillCounter());
            setCanUse(ability.isCanUse());
        }
    }

    public boolean haveItem() {
        if (item != null) {
            return true;
        }
        return false;
    }

}
