package fightinggame.entity.quest.type;

import fightinggame.entity.Player;
import fightinggame.entity.item.Item;
import fightinggame.entity.quest.Quest;
import fightinggame.entity.quest.QuestRequired;

public class ItemRequired extends QuestRequired {

    private final Item item;

    public ItemRequired(Item item, int amount, String name, Quest quest) {
        super(name, amount, quest);
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    @Override
    public void tick() {
        Player player = quest.getPlayer();
        if (player != null) {
            Item playerItem = player.getInventory().getItemByName(item.getName());
            if (playerItem != null) {
                increaseCurrentAmount();
                player.getInventory().removeItemFromInventory(playerItem);
            }
        }
    }

}
