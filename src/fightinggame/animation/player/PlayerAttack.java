package fightinggame.animation.player;

import fightinggame.entity.Animation;
import fightinggame.resource.SpriteSheet;

public class PlayerAttack extends Animation{
    
    public PlayerAttack(int id, SpriteSheet sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public PlayerAttack(int id, SpriteSheet sheet) {
        super(id, sheet);
    }
    
    
    
}
