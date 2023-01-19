package fightinggame.animation.player;

import fightinggame.entity.Animation;
import fightinggame.entity.Entity;


public class PlayerSpellCast extends Animation{
    
    public PlayerSpellCast(int id, Entity sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public PlayerSpellCast(int id, Entity sheet) {
        super(id, sheet);
    }
   
    
}
