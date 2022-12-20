package fightinggame.animation.player;

import fightinggame.entity.Animation;
import fightinggame.resource.SpriteSheet;


public class PlayerHit extends Animation{
    
    public PlayerHit(int id, SpriteSheet sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public PlayerHit(int id, SpriteSheet sheet) {
        super(id, sheet);
    }
    
    
    
}
