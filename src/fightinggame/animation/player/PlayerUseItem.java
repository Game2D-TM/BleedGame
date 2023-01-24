package fightinggame.animation.player;

import fightinggame.entity.Animation;
import fightinggame.entity.SpriteSheet;

public class PlayerUseItem extends Animation{
    
    public PlayerUseItem(int id, SpriteSheet sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public PlayerUseItem(int id, SpriteSheet sheet) {
        super(id, sheet);
    }
    
    
    
}
