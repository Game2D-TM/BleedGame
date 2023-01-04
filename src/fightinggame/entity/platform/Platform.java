package fightinggame.entity.platform;

import fightinggame.Gameplay;
import fightinggame.entity.GamePosition;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public abstract class Platform {

    protected String name;
    protected BufferedImage image;
    protected boolean canStand;
    protected GamePosition position;
    protected Gameplay gameplay;
    protected int row;
    protected int column;
    protected boolean canRender = false;
    protected boolean isMapRender = false;

    public Platform(String name, BufferedImage image, boolean canStand, GamePosition position, Gameplay gameplay,
            int row, int column) {
        this.name = name;
        this.image = image;
        this.canStand = canStand;
        this.position = position;
        this.gameplay = gameplay;
        this.row = row;
        this.column = column;
    }

    public abstract boolean checkValidPosition(GamePosition position);

    public void tick() {
        canRender = gameplay.getCamera().checkPositionRelateToCamera(position);
    }

    public void render(Graphics g) {
        if (canRender || isMapRender) {
            g.drawImage(image, position.getXPosition() - gameplay.getCamera().getPosition().getXPosition(),
                    position.getYPosition() - gameplay.getCamera().getPosition().getYPosition(), position.getWidth(),
                    position.getHeight(), null);
//      hitbox
//            g.setColor(Color.red);
//            g.drawRect(position.getXPosition() - gameplay.getCamera().getPosition().getXPosition(),
//                    position.getYPosition() - gameplay.getCamera().getPosition().getYPosition(), position.getWidth(),
//                    position.getHeight());
            canRender = false;
        }
    }

    public GamePosition middlePlatform() {
        return middlePlatform(position.getWidth() / 2, position.getHeight() / 2);
    }

    public GamePosition middlePlatform(int width, int height) {
        int nX = position.getMaxX() - width;
        int nY = position.getYPosition() - height;
        int distance = -1;
        if (nX < position.getXPosition()) {
            distance = 0;
        } else {
            distance = Math.abs((nX - position.getXPosition()) / 2);
        }
        return new GamePosition(nX - distance,
                nY, width, height);
    }

    public GamePosition leftCornerPlatform() {
        return leftCornerPlatform(position.getWidth() / 2, position.getHeight() / 2);
    }

    public GamePosition leftCornerPlatform(int width, int height) {
        int nX = position.getMaxX() - width;
        int nY = position.getYPosition() - height;
        int distance = -1;
        if (nX < position.getXPosition()) {
            distance = 0;
        } else {
            int equalDistance = Math.abs((nX - position.getXPosition()) / 2);
            distance = Math.abs(equalDistance + equalDistance / 2);
        }
        return new GamePosition(nX - distance,
                nY, width, height);
    }

    public GamePosition rightCornerPlatform() {
        return rightCornerPlatform(position.getWidth() / 2, position.getHeight() / 2);
    }

    public GamePosition rightCornerPlatform(int width, int height) {
        int nX = position.getMaxX() - width;
        int nY = position.getYPosition() - height;
        int distance = -1;
        if (nX < position.getXPosition()) {
            distance = 0;
        } else {
            int equalDistance = Math.abs((nX - position.getXPosition()) / 2);
            distance = equalDistance / 2;
        }
        return new GamePosition(nX - distance,
                nY, width, height);
    }

    @Override
    public boolean equals(Object obj) {
        return row == ((Platform) obj).getRow() && column == ((Platform) obj).getColumn();
    }

    public String getName() {
        return name;
    }

    public Gameplay getGameplay() {
        return gameplay;
    }

    public void setGameplay(Gameplay gameplay) {
        this.gameplay = gameplay;
    }

    public boolean isCanRender() {
        return canRender;
    }

    public void setCanRender(boolean canRender) {
        this.canRender = canRender;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public boolean isCanStand() {
        return canStand;
    }

    public void setCanStand(boolean canStand) {
        this.canStand = canStand;
    }

    public GamePosition getPosition() {
        return position;
    }

    public void setPosition(GamePosition position) {
        this.position = position;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public boolean isMapRender() {
        return isMapRender;
    }

    public void setIsMapRender(boolean isMapRender) {
        this.isMapRender = isMapRender;
    }

}
