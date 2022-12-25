package fightinggame.entity.platform.tile;

import fightinggame.Gameplay;
import fightinggame.entity.GamePosition;
import fightinggame.entity.platform.Platform;
import java.awt.image.BufferedImage;

public class WallTile extends Platform{

    public WallTile(String name, BufferedImage image, GamePosition position, Gameplay gameplay
           ,int row, int column) {
        super(name, image, false, position, gameplay, row, column);
    }

    @Override
    public boolean checkValidPosition(GamePosition position) {
        GamePosition tilePos = this.position;
        if(tilePos == null) return false;
        if(((position.getXPosition() >= tilePos.getXPosition() && position.getXPosition() <= tilePos.getMaxX())
                || (position.getMaxX() >= tilePos.getXPosition() && position.getMaxX() <= tilePos.getMaxX()))
                && ((position.getYPosition() >= tilePos.getYPosition() && position.getMaxY() <= tilePos.getMaxY())
                || (position.getYPosition() < tilePos.getYPosition() && position.getMaxY() >= tilePos.getMaxY())
                || (position.getYPosition() >= tilePos.getYPosition() && position.getYPosition() <= tilePos.getMaxY()
                && position.getMaxY() > tilePos.getMaxY())
                || (position.getMaxY() >= tilePos.getYPosition() && position.getMaxY() <= tilePos.getMaxY()
                && position.getYPosition() < tilePos.getYPosition()))) {
            return true;
        }
        return false;
    }
    
}
