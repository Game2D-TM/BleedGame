package fightinggame.animation.player;

import fightinggame.entity.Animation;
import fightinggame.entity.SpriteSheet;

public class PlayerAttack extends Animation{
    
    public PlayerAttack(int id, SpriteSheet sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public PlayerAttack(int id, SpriteSheet sheet) {
        super(id, sheet);
    }
    
    
}
