package fightinggame.animation;

import fightinggame.entity.Animation;
import fightinggame.entity.SpriteSheet;

public class BackgroundAnimation extends Animation{
    
    public BackgroundAnimation(int id, SpriteSheet sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public BackgroundAnimation(int id, SpriteSheet sheet) {
        super(id, sheet);
    }
    
    
}
