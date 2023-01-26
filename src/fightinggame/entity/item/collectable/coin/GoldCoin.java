package fightinggame.entity.item.collectable.coin;

import fightinggame.Gameplay;
import fightinggame.animation.item.PotionAnimation;
import fightinggame.entity.Character;
import fightinggame.entity.SpriteSheet;

public class GoldCoin extends CoinItem {
    
    public GoldCoin(int id, Character character, Gameplay gameplay, int amount) {
        super(id, "Gold Coin", null, character, gameplay, amount, 30);
        SpriteSheet healthPotionSheet = new SpriteSheet();
        healthPotionSheet.add("assets/res/item/Coins/Gold.png");
        animation = new PotionAnimation(0, healthPotionSheet, -1);
    }
    
}
