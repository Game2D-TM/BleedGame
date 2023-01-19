package fightinggame.animation.item;

import fightinggame.entity.Animation;
import fightinggame.entity.Entity;

public class PotionAnimation extends Animation{
    
    public PotionAnimation(int id, Entity sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public PotionAnimation(int id, Entity sheet) {
        super(id, sheet);
    }
    
    
}
