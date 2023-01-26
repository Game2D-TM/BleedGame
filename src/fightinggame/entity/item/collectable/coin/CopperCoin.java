package fightinggame.entity.item.collectable.coin;

import fightinggame.Gameplay;
import fightinggame.animation.item.PotionAnimation;
import fightinggame.entity.Character;
import fightinggame.entity.SpriteSheet;

public class CopperCoin extends CoinItem {
    
    public CopperCoin(int id, Character character, Gameplay gameplay, int amount) {
        super(id, "Copper Coin", null, character, gameplay, amount, 10);
        SpriteSheet healthPotionSheet = new SpriteSheet();
        healthPotionSheet.add("assets/res/item/Coins/Bronze.png");
        animation = new PotionAnimation(0, healthPotionSheet, -1);
    }
    
}
