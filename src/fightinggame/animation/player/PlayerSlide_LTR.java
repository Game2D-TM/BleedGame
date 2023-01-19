package fightinggame.animation.player;

import fightinggame.entity.Animation;
import fightinggame.entity.Entity;

public class PlayerSlide_LTR extends Animation{
    
    public PlayerSlide_LTR(int id, Entity sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public PlayerSlide_LTR(int id, Entity sheet) {
        super(id, sheet);
    }
    
}
