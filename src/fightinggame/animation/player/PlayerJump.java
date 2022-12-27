package fightinggame.animation.player;

import fightinggame.entity.Animation;
import fightinggame.resource.SpriteSheet;

public class PlayerJump extends Animation{
    
    public PlayerJump(int id, SpriteSheet sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public PlayerJump(int id, SpriteSheet sheet) {
        super(id, sheet);
    }
    
    
    
}
