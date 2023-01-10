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
                int attackX = -1, attackY = -1, attackHeight = -1, x = player.getPosition().getXPosition(), width = player.getPosition().getWidth();
                if (player.isLTR()) {
                    width = width + 20; // 120
                } else {
                    x = x - 20;
                    width = width + 20;
                }
                if (player.isLTR()) {
                    attackX = x + width + 20 - 2;
                } else {
                    attackX = x + 2;
                }
                attackY = player.getPosition().getYPosition() + player.getPosition().getHeight() / 3 - 10;
                attackHeight = player.getPosition().getHeight() / 2 - 10;
                checkHit(attackX, attackY, attackHeight);
            }
        }
    }

    public boolean checkHit(int attackX, int attackY, int attackHeight) {
        int attackMaxY = attackY + attackHeight;
        if (attackX >= position.getXPosition() && attackX <= position.getMaxX()
                && ((attackY <= position.getYPosition() && attackMaxY >= position.getMaxY()
                || (attackY >= position.getYPosition() && attackMaxY <= position.getMaxY())
                || (attackY > position.getYPosition() && attackY <= position.getMaxY()
                && attackMaxY > position.getMaxY())
                || (attackMaxY > position.getYPosition() && attackMaxY <= position.getMaxY()
                && attackY < position.getYPosition())))) {
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
