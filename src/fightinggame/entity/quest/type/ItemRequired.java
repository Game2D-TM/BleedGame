package fightinggame.entity.quest.type;

import fightinggame.entity.Player;
import fightinggame.entity.item.Item;
import fightinggame.entity.quest.Quest;
import fightinggame.entity.quest.QuestRequired;

public class ItemRequired extends QuestRequired {

    private final String itemName;

    public ItemRequired(String itemName, int amount, String name, Quest quest) {
        super(name, amount, quest);
        this.itemName = itemName;
    }

    @Override
    public void tick() {
        Player player = quest.getPlayer();
        if (player != null) {
            Item playerItem = player.getInventory().getItemByName(itemName);
            if (playerItem != null) {
                increaseCurrentAmount();
                player.getInventory().removeItemFromInventory(playerItem);
            }
        }
    }

}
