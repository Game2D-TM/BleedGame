package fightinggame.animation.player;

import fightinggame.entity.Animation;
import fightinggame.entity.CharacterState;
import fightinggame.resource.SpriteSheet;

public class PlayerDeath extends Animation{
    
    public PlayerDeath(int id, CharacterState state, SpriteSheet sheet) {
        super(id, state, sheet);
        tickToExecute = 75;
    }

    @Override
    public void tick() {
        spriteCounter++;
        if (spriteCounter > tickToExecute) {
            if (spriteIndex < sheet.getImages().size() - 1) {
                spriteIndex++;
            } else {
                spriteCounter = 0;
            }
        }
    }
    
}
