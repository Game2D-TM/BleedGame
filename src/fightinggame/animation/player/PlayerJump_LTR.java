package fightinggame.animation.player;

import fightinggame.entity.Animation;
import fightinggame.entity.Entity;

public class PlayerJump_LTR extends Animation{
    
    public PlayerJump_LTR(int id, Entity sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public PlayerJump_LTR(int id, Entity sheet) {
        super(id, sheet);
    }

    @Override
    public void tick() {
        if(sheet.getSpriteIndex() == sheet.getImages().size() - 1);
        else sheet.tick(tickToExecute);
    }
    
}
