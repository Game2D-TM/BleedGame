package fightinggame.animation.player;

import fightinggame.entity.Animation;
import fightinggame.resource.SpriteSheet;

public class PlayerGetUp_LTR extends Animation{
    
    public PlayerGetUp_LTR(int id, SpriteSheet sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }
    
    @Override
    public void tick() {
        if(sheet.getSpriteIndex() == sheet.getImages().size() - 1);
        else sheet.tick(tickToExecute);
    }
}
