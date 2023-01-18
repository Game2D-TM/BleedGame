package fightinggame.entity.platform.tile.trap;

import fightinggame.Gameplay;
import fightinggame.entity.Animation;
import fightinggame.entity.GamePosition;
import fightinggame.entity.platform.tile.TrapTile;
import java.awt.image.BufferedImage;

public class SpikeTrap extends TrapTile {

    public SpikeTrap(Animation trapAnimation, int hitDamage, TrapLocation location, String name, BufferedImage image, GamePosition position, Gameplay gameplay) {
        super(trapAnimation, hitDamage, location, name, image, position, gameplay);
        switch(location) {
            case TOP:
                this.position.setYPosition(position.getYPosition() + 60);
                break;
            case LEFT:
                this.position.setXPosition(position.getXPosition() + 60);
                break;
            case RIGHT:
                this.position.setXPosition(position.getXPosition() - 60);
                break;
            case BOTTOM:
                this.position.setYPosition(position.getYPosition() - 60);
                break;
        }
    }

    @Override
    public int getXHitBox() {
        switch (location) {
            case TOP:
                break;
            case BOTTOM:
                break;
            case RIGHT:
                return position.getXPosition() + 45;
            case LEFT:
                return position.getXPosition() + 35;
        }
        return super.getXHitBox();
    }

    @Override
    public int getYHitBox() {
        switch (location) {
            case TOP:
                return position.getYPosition() + 35;
            case BOTTOM:
                return position.getYPosition() + 55;
            case RIGHT:
                break;
            case LEFT:
                break;
        }
        return super.getYHitBox();
    }

    @Override
    public int getWidthHitBox() {
        switch (location) {
            case TOP:
                break;
            case BOTTOM:
                break;
            case RIGHT:
                return position.getWidth() - 82;
            case LEFT:
                return position.getWidth() - 85;
        }
        return super.getWidthHitBox();
    }

    @Override
    public int getHeightHitBox() {
        switch (location) {
            case TOP:
                return position.getHeight() - 85;
            case BOTTOM:
                return position.getHeight() - 92;
            case RIGHT:
                break;
            case LEFT:
                break;
        }
        return super.getHeightHitBox();
    }

}
