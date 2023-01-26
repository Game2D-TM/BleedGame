package fightinggame.entity;

import fightinggame.Gameplay;
import java.awt.Graphics;

public class Camera {

    private Player player;
    private GamePosition position;
    private final Gameplay gameplay;
    private boolean isCheckCameraPos = true;

    public Camera(Player player, GamePosition position, int windowWidth, int windowHeight, Gameplay gameplay) {
        this.player = player;
        this.position = position;
        position.setWidth(windowWidth);
        position.setHeight(windowHeight);
        this.gameplay = gameplay;
    }

    public void tick() {
        if (position != null) {
            if (player != null) {
                if (isCheckCameraPos) {
                    getNewCameraPosition();
                }
            }
        }
    }

    public void render(Graphics g) {
//        if (position != null) {
//            g.setColor(Color.red);
//            g.drawRect(position.getXPosition() - position.getXPosition(),
//                    position.getYPosition() - position.getYPosition(),
//                    position.getWidth(), position.getHeight());
//        }
    }

    public void getNewCameraPosition() {
        int nX = player.getPosition().getXPosition()
                - position.getWidth() / 4;
        int nY = player.getPosition().getYPosition()
                - position.getHeight() / 2;
        if (nX >= gameplay.getScenePosition().getXPosition() && nX + position.getWidth() <= gameplay.getScenePosition().getMaxX()) {
            position.setXPosition(nX);
        }
        if (nY >= gameplay.getScenePosition().getYPosition() && nY + position.getHeight() <= gameplay.getScenePosition().getMaxY()) {
            position.setYPosition(nY);
        }
    }

    public boolean checkPositionRelateToCamera(GamePosition position) {
        GamePosition cameraPos = this.position;
        if (cameraPos == null) {
            return false;
        }
        if (((cameraPos.getXPosition() <= position.getXPosition() && cameraPos.getMaxX() >= position.getMaxX())
                || (cameraPos.getXPosition() >= position.getXPosition() && cameraPos.getXPosition()
                <= position.getMaxX() && cameraPos.getMaxX() > position.getMaxX())
                || (cameraPos.getMaxX() >= position.getXPosition() && cameraPos.getMaxX() <= position.getMaxX()
                && cameraPos.getXPosition() < position.getXPosition())
                || (cameraPos.getXPosition() >= position.getXPosition() && cameraPos.getMaxX() <= position.getMaxX()))
                && ((cameraPos.getYPosition() <= position.getYPosition()
                && cameraPos.getMaxY() >= position.getMaxY())
                || (cameraPos.getYPosition() >= position.getYPosition() && cameraPos.getYPosition() <= position.getMaxY()
                && cameraPos.getMaxY() > position.getMaxY())
                || (cameraPos.getMaxY() >= position.getYPosition() && cameraPos.getMaxY() <= position.getMaxY()
                && cameraPos.getYPosition() < position.getYPosition()))) {
            return true;
        }
        return false;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public GamePosition getPosition() {
        return position;
    }

    public void setPosition(GamePosition position) {
        this.position = position;
    }

    public boolean isCheckCameraPos() {
        return isCheckCameraPos;
    }

    public void setIsCheckCameraPos(boolean isCheckCameraPos) {
        this.isCheckCameraPos = isCheckCameraPos;
    }

}
