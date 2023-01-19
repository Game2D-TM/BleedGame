package fightinggame.animation.player;

import fightinggame.entity.Animation;
import fightinggame.entity.Entity;

public class PlayerSlide_RTL extends Animation{
    
    public PlayerSlide_RTL(int id, Entity sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }
    
    public PlayerSlide_RTL(int id, Entity sheet) {
        super(id, sheet);
    }
}
