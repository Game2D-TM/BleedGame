package fightinggame.animation.player;

import fightinggame.entity.Animation;
import fightinggame.resource.SpriteSheet;

public class PlayerAirAttack extends Animation{
    
    public PlayerAirAttack(int id, SpriteSheet sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public PlayerAirAttack(int id, SpriteSheet sheet) {
        super(id, sheet);
    }
    
    
}
