package fightinggame.entity.item.collectable.coin;

import fightinggame.Gameplay;
import fightinggame.animation.item.PotionAnimation;
import fightinggame.entity.Character;
import fightinggame.entity.SpriteSheet;

public class SilverCoin extends CoinItem {
    
    public SilverCoin(int id, Character character, Gameplay gameplay, int amount) {
        super(id, "Silver Coin", null, character, gameplay, amount, 20);
        SpriteSheet healthPotionSheet = new SpriteSheet();
        healthPotionSheet.add("assets/res/item/Coins/Iron.png");
        animation = new PotionAnimation(0, healthPotionSheet, -1);
    }
    
}
