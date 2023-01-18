package fightinggame.animation.item;

import fightinggame.entity.Animation;
import fightinggame.entity.SpriteSheet;

public class PotionAnimation extends Animation{
    
    public PotionAnimation(int id, SpriteSheet sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public PotionAnimation(int id, SpriteSheet sheet) {
        super(id, sheet);
    }
    
    
}
