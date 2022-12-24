package fightinggame.entity.platform.tile;

import fightinggame.Gameplay;
import fightinggame.entity.GamePosition;
import fightinggame.entity.platform.Platform;
import java.awt.image.BufferedImage;

public class Tile extends Platform {

    public Tile(String name, BufferedImage image, GamePosition position, Gameplay gameplay
        ,int row, int column) {
        super(name, image, true, position, gameplay, row, column);
    }

    @Override
    public boolean checkValidPosition(GamePosition position) {
        GamePosition tilePos = this.position;
        if(tilePos == null) return false;
        position.setXPosition(position.getXPosition() - gameplay.getCamera().getPosition().getXPosition());
        position.setYPosition(position.getYPosition() - gameplay.getCamera().getPosition().getYPosition());
        if(((position.getXPosition() >= tilePos.getXPosition() && position.getMaxX() <= tilePos.getMaxX())
                || (position.getXPosition() >= tilePos.getXPosition() && position.getXPosition() <= tilePos.getMaxX()
                && position.getMaxX() >= tilePos.getMaxX())
                || (position.getMaxX() >= tilePos.getXPosition() && position.getMaxX() <= tilePos.getMaxX()
                && position.getXPosition() <= tilePos.getXPosition())
                || (position.getXPosition() < tilePos.getXPosition() && position.getMaxX() > tilePos.getMaxX()))
                && position.getMaxY() <= tilePos.getYPosition()) {
            return true;
        } 
        return false;
    }

}
