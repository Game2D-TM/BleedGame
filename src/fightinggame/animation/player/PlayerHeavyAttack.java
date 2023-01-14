package fightinggame.animation.player;

import fightinggame.entity.Animation;
import fightinggame.entity.SpriteSheet;

public class PlayerHeavyAttack extends Animation{
    
    public PlayerHeavyAttack(int id, SpriteSheet sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public PlayerHeavyAttack(int id, SpriteSheet sheet) {
        super(id, sheet);
    }
    
    
}
