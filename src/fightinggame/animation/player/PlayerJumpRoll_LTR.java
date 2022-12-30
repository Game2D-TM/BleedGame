package fightinggame.animation.player;

import fightinggame.entity.Animation;
import fightinggame.resource.SpriteSheet;

public class PlayerJumpRoll_LTR extends Animation{
    
    public PlayerJumpRoll_LTR(int id, SpriteSheet sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public PlayerJumpRoll_LTR(int id, SpriteSheet sheet) {
        super(id, sheet);
    }

    @Override
    public void tick() {
        if(sheet.getSpriteIndex() == sheet.getImages().size() - 1);
        else sheet.tick(tickToExecute);
    }
}
