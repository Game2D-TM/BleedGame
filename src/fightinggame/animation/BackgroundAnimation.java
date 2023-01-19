package fightinggame.animation;

import fightinggame.entity.Animation;
import fightinggame.entity.Entity;

public class BackgroundAnimation extends Animation{
    
    public BackgroundAnimation(int id, Entity sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public BackgroundAnimation(int id, Entity sheet) {
        super(id, sheet);
    }
    
    
}
