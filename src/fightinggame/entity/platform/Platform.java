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
        canRender = gameplay.getCamera().checkPositionRelateToCamera(getHitBox());
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

    // Top Platform
    public GamePosition middleTopPlatform() {
        return middleTopPlatform(position.getWidth() / 2, position.getHeight() / 2);
    }

    public GamePosition middleTopPlatform(int width, int height) {
        int nX = position.getMaxX() - width;
        int nY = position.getYPosition() - height;
        int distance = -1;
        if (nX <= position.getXPosition()) {
            distance = 0;
        } else {
            distance = Math.abs((nX - position.getXPosition()) / 2);
        }
        return new GamePosition(nX - distance,
                nY, width, height);
    }

    public GamePosition leftCornerTopPlatform() {
        return leftCornerTopPlatform(position.getWidth() / 2, position.getHeight() / 2);
    }

    public GamePosition leftCornerTopPlatform(int width, int height) {
        int nX = position.getMaxX() - width;
        int nY = position.getYPosition() - height;
        int distance = -1;
        if (nX <= position.getXPosition()) {
            distance = 0;
        } else {
            int equalDistance = Math.abs((nX - position.getXPosition()) / 2);
            distance = Math.abs(equalDistance + equalDistance / 2);
        }
        return new GamePosition(nX - distance,
                nY, width, height);
    }

    public GamePosition rightCornerTopPlatform() {
        return rightCornerTopPlatform(position.getWidth() / 2, position.getHeight() / 2);
    }

    public GamePosition rightCornerTopPlatform(int width, int height) {
        int nX = position.getMaxX() - width;
        int nY = position.getYPosition() - height;
        int distance = -1;
        if (nX <= position.getXPosition()) {
            distance = 0;
        } else {
            int equalDistance = Math.abs((nX - position.getXPosition()) / 2);
            distance = equalDistance / 2;
        }
        return new GamePosition(nX - distance,
                nY, width, height);
    }

    // Right Platform
    public GamePosition middleRightPlatform() {
        return middleRightPlatform(position.getWidth() / 2, position.getHeight() / 2);
    }

    public GamePosition middleRightPlatform(int width, int height) {
        int nY = position.getMaxY() - height;
        int nX = position.getMaxX();
        int distance = -1;
        if (nY <= position.getYPosition()) {
            distance = 0;
        } else {
            distance = Math.abs((nY - position.getYPosition()) / 2);
        }
        return new GamePosition(nX,
                nY - distance, width, height);
    }

    public GamePosition upCornerRightPlatform() {
        return upCornerRightPlatform(position.getWidth() / 2, position.getHeight() / 2);
    }

    public GamePosition upCornerRightPlatform(int width, int height) {
        int nY = position.getMaxY() - height;
        int nX = position.getMaxX();
        int distance = -1;
        if (nY <= position.getYPosition()) {
            distance = 0;
        } else {
            int equalDistance = Math.abs((nY - position.getYPosition()) / 2);
            distance = Math.abs(equalDistance + equalDistance / 2);
        }
        return new GamePosition(nX,
                nY - distance, width, height);
    }

    public GamePosition downCornerRightPlatform() {
        return downCornerRightPlatform(position.getWidth() / 2, position.getHeight() / 2);
    }

    public GamePosition downCornerRightPlatform(int width, int height) {
        int nY = position.getMaxY() - height;
        int nX = position.getMaxX();
        int distance = -1;
        if (nY <= position.getYPosition()) {
            distance = 0;
        } else {
            int equalDistance = Math.abs((nY - position.getYPosition()) / 2);
            distance = equalDistance / 2;
        }
        return new GamePosition(nX,
                nY - distance, width, height);
    }

    // Left Platform
    public GamePosition middleLeftPlatform() {
        return middleLeftPlatform(position.getWidth() / 2, position.getHeight() / 2);
    }

    public GamePosition middleLeftPlatform(int width, int height) {
        int nY = position.getMaxY() - height;
        int nX = position.getXPosition() - width;
        int distance = -1;
        if (nY <= position.getYPosition()) {
            distance = 0;
        } else {
            distance = Math.abs((nY - position.getYPosition()) / 2);
        }
        return new GamePosition(nX,
                nY - distance, width, height);
    }

    public GamePosition upCornerLeftPlatform() {
        return upCornerLeftPlatform(position.getWidth() / 2, position.getHeight() / 2);
    }

    public GamePosition upCornerLeftPlatform(int width, int height) {
        int nY = position.getMaxY() - height;
        int nX = position.getXPosition() - width;
        int distance = -1;
        if (nY <= position.getYPosition()) {
            distance = 0;
        } else {
            int equalDistance = Math.abs((nY - position.getYPosition()) / 2);
            distance = Math.abs(equalDistance + equalDistance / 2);
        }
        return new GamePosition(nX,
                nY - distance, width, height);
    }

    public GamePosition downCornerLeftPlatform() {
        return downCornerLeftPlatform(position.getWidth() / 2, position.getHeight() / 2);
    }

    public GamePosition downCornerLeftPlatform(int width, int height) {
        int nY = position.getMaxY() - height;
        int nX = position.getXPosition() - width;
        int distance = -1;
        if (nY <= position.getYPosition()) {
            distance = 0;
        } else {
            int equalDistance = Math.abs((nY - position.getYPosition()) / 2);
            distance = equalDistance / 2;
        }
        return new GamePosition(nX,
                nY - distance, width, height);
    }

    // Bottom Platform
    public GamePosition middleBottomPlatform() {
        return middleBottomPlatform(position.getWidth() / 2, position.getHeight() / 2);
    }

    public GamePosition middleBottomPlatform(int width, int height) {
        int nX = position.getMaxX() - width;
        int nY = position.getMaxY();
        int distance = -1;
        if (nX <= position.getXPosition()) {
            distance = 0;
        } else {
            distance = Math.abs((nX - position.getXPosition()) / 2);
        }
        return new GamePosition(nX - distance,
                nY, width, height);
    }

    public GamePosition leftCornerBottomPlatform() {
        return leftCornerBottomPlatform(position.getWidth() / 2, position.getHeight() / 2);
    }

    public GamePosition leftCornerBottomPlatform(int width, int height) {
        int nX = position.getMaxX() - width;
        int nY = position.getMaxY();
        int distance = -1;
        if (nX <= position.getXPosition()) {
            distance = 0;
        } else {
            int equalDistance = Math.abs((nX - position.getXPosition()) / 2);
            distance = Math.abs(equalDistance + equalDistance / 2);
        }
        return new GamePosition(nX - distance,
                nY, width, height);
    }

    public GamePosition rightCornerBottomPlatform() {
        return rightCornerBottomPlatform(position.getWidth() / 2, position.getHeight() / 2);
    }

    public GamePosition rightCornerBottomPlatform(int width, int height) {
        int nX = position.getMaxX() - width;
        int nY = position.getMaxY();
        int distance = -1;
        if (nX <= position.getXPosition()) {
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

    public GamePosition getHitBox() {
        return new GamePosition(getXHitBox(), getYHitBox(), getWidthHitBox(), getHeightHitBox());
    }

    public int getXHitBox() {
        return position.getXPosition();
    }

    public int getYHitBox() {
        return position.getYPosition();
    }

    public int getWidthHitBox() {
        return position.getWidth();
    }

    public int getHeightHitBox() {
        return position.getHeight();
    }

    public int getXMaxHitBox() {
        return getXHitBox() + getWidthHitBox();
    }

    public int getYMaxHitBox() {
        return getYHitBox() + getHeightHitBox();
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
