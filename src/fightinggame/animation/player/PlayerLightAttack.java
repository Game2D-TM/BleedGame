package fightinggame.animation.player;

import fightinggame.entity.Animation;
import fightinggame.entity.Entity;

public class PlayerLightAttack extends Animation{
    
    public PlayerLightAttack(int id, Entity sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public PlayerLightAttack(int id, Entity sheet) {
        super(id, sheet);
    }
    
    
}
