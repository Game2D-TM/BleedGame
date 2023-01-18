package fightinggame.entity.platform.tile.trap;

import fightinggame.Gameplay;
import fightinggame.entity.Animation;
import fightinggame.entity.GamePosition;
import fightinggame.entity.platform.tile.TrapTile;
import java.awt.image.BufferedImage;

public class SpearTrap extends TrapTile {

    public SpearTrap(Animation trapAnimation, int hitDamage, TrapLocation location, String name, BufferedImage image, GamePosition position, Gameplay gameplay) {
        super(trapAnimation, hitDamage, location, name, image, position, gameplay);
    }

    @Override
    public int getXHitBox() {
        switch (location) {
            case TOP:
                return position.getXPosition() + 23;
            case BOTTOM:
                return position.getXPosition() + 19;
            case RIGHT:
                break;
            case LEFT:
                break;
        }
        return super.getXHitBox();
    }

    @Override
    public int getYHitBox() {
        switch (location) {
            case TOP:
                break;
            case BOTTOM:
                break;
            case RIGHT:
                return position.getYPosition() + 23;
            case LEFT:
                return position.getYPosition() + 19;
        }
        return super.getYHitBox();
    }

    @Override
    public int getWidthHitBox() {
        switch (location) {
            case TOP:
                return position.getWidth() - 43;
            case BOTTOM:
                return position.getWidth() - 43;
            case RIGHT:
                break;
            case LEFT:
                break;
        }
        return super.getWidthHitBox();
    }

    @Override
    public int getHeightHitBox() {
        switch (location) {
            case TOP:
                break;
            case BOTTOM:
                break;
            case RIGHT:
                return position.getHeight() - 43;
            case LEFT:
                return position.getHeight() - 43;
        }
        return super.getHeightHitBox();
    }

}
