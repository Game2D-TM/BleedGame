package fightinggame.animation.player;

import fightinggame.entity.Animation;
import fightinggame.resource.SpriteSheet;


public class PlayerRun extends Animation{
    
    public PlayerRun(int id, SpriteSheet sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public PlayerRun(int id, SpriteSheet sheet) {
        super(id, sheet);
    }
   
    
}
