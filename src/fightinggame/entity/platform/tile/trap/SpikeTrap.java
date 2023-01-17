package fightinggame.entity.platform.tile.trap;

import fightinggame.Gameplay;
import fightinggame.entity.Animation;
import fightinggame.entity.GamePosition;
import fightinggame.entity.platform.tile.TrapTile;
import java.awt.image.BufferedImage;

public class SpikeTrap extends TrapTile{
    
    public SpikeTrap(Animation trapAnimation, int hitDamage, String name, BufferedImage image, GamePosition position, Gameplay gameplay) {
        super(trapAnimation, hitDamage, name, image, position, gameplay);
    }

    @Override
    public int getXHitBox() {
        return super.getXHitBox();
    }

    @Override
    public int getYHitBox() {
        return position.getYPosition() + 35;
    }

    @Override
    public int getWidthHitBox() {
        return super.getWidthHitBox();
    }

    @Override
    public int getHeightHitBox() {
        return position.getHeight() - 85;
    }
    
    
    
}
