package fightinggame.animation.item;

import fightinggame.entity.Animation;
import fightinggame.entity.SpriteSheet;

public class HealthPotionAnimation extends Animation{
    
    public HealthPotionAnimation(int id, SpriteSheet sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public HealthPotionAnimation(int id, SpriteSheet sheet) {
        super(id, sheet);
    }
    
    
}
