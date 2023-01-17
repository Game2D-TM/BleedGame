package fightinggame.entity.platform.tile.trap;

import fightinggame.Gameplay;
import fightinggame.entity.Animation;
import fightinggame.entity.GamePosition;
import fightinggame.entity.platform.tile.TrapTile;
import java.awt.image.BufferedImage;

public class SpearTrap extends TrapTile {
    
    public SpearTrap(Animation trapAnimation, int hitDamage, String name, BufferedImage image, GamePosition position, Gameplay gameplay) {
        super(trapAnimation, hitDamage, name, image, position, gameplay);
    }

    @Override
    public int getXHitBox() {
        return position.getXPosition() + 23;
    }

    @Override
    public int getYHitBox() {
        return super.getYHitBox();
    }

    @Override
    public int getWidthHitBox() {
        return position.getWidth() - 43;
    }

    @Override
    public int getHeightHitBox() {
        return super.getHeightHitBox();
    }
    
    
    
}
