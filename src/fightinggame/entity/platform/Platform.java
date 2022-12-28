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
//            g.setColor(Color.red);
//            g.drawRect(position.getXPosition() - gameplay.getCamera().getPosition().getXPosition(),
//                    position.getYPosition() - gameplay.getCamera().getPosition().getYPosition(), position.getWidth(),
//                    position.getHeight());
            canRender = false;
        }
    }

    public String getName() {
        return name;
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
