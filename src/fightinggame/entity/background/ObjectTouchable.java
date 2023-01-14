package fightinggame.entity.background;

import fightinggame.Gameplay;
import fightinggame.entity.GamePosition;
import fightinggame.entity.Player;
import fightinggame.entity.background.GameObject;
import fightinggame.entity.platform.Platform;
import java.awt.image.BufferedImage;

public abstract class ObjectTouchable extends GameObject {

    protected BufferedImage imageAfterTouch;
    protected BufferedImage imageBeforeTouch;
    protected boolean isTouch;

    public ObjectTouchable(BufferedImage imageAfterTouch, BufferedImage image, String name, Platform platform, GamePosition position, Gameplay gameplay) {
        super(image, name, platform, position, gameplay);
        this.imageAfterTouch = imageAfterTouch;
        imageBeforeTouch = image;
    }

    @Override
    public void tick() {
        if (!isTouch) {
            Player player = gameplay.getPlayer();
            if (player.isAttack() || player.isAirAttack()) {
                checkHit(player.attackHitBox());
            }
        }
    }

    public boolean checkHit(GamePosition attackHitBox) {
        if (((attackHitBox.getXPosition() >= position.getXPosition() && attackHitBox.getXPosition() <= position.getMaxX())
                    || (attackHitBox.getXPosition() >= position.getXPosition() && attackHitBox.getXPosition() <= position.getMaxX()
                    && attackHitBox.getMaxX() > position.getMaxX())
                    || (attackHitBox.getMaxX() >= position.getXPosition() && attackHitBox.getMaxX() <= position.getMaxX()
                    && attackHitBox.getXPosition() < position.getXPosition())
                    || (attackHitBox.getXPosition() < position.getXPosition() && attackHitBox.getMaxX() > position.getMaxX()))
                    && ((attackHitBox.getYPosition() <= position.getYPosition() && attackHitBox.getYPosition() >= position.getMaxY()
                    || (attackHitBox.getYPosition() >= position.getYPosition() && attackHitBox.getMaxY() <= position.getMaxY())
                    || (attackHitBox.getYPosition() > position.getYPosition() && attackHitBox.getYPosition() <= position.getMaxY()
                    && attackHitBox.getMaxY() > position.getMaxY())
                    || (attackHitBox.getMaxY() > position.getYPosition() && attackHitBox.getMaxY() <= position.getMaxY()
                    && attackHitBox.getYPosition() < position.getYPosition())))) {
            if (imageAfterTouch == null) {
                return false;
            }
            image = imageAfterTouch;
            isTouch = true;
            return true;
        }
        return false;
    }
    
    public BufferedImage getImageBeforeTouch() {
        return imageBeforeTouch;
    }

    public void setImageBeforeTouch(BufferedImage imageBeforeTouch) {
        this.imageBeforeTouch = imageBeforeTouch;
    }

    public BufferedImage getImageAfterTouch() {
        return imageAfterTouch;
    }

    public void setImageAfterTouch(BufferedImage imageAfterTouch) {
        this.imageAfterTouch = imageAfterTouch;
    }

    public boolean isTouch() {
        return isTouch;
    }

    public void setIsTouch(boolean isTouch) {
        this.isTouch = isTouch;
    }
}
