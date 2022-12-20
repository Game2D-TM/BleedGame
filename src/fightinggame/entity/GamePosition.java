package fightinggame.entity;

public class GamePosition {

    public boolean isMoveUp = false;
    public boolean isMoveDown = false;
    public boolean isMoveRight = false;
    public boolean isMoveLeft = false;
    private int xPosition;
    private int yPosition;
    private int width;
    private int height;

    public GamePosition(int xPosition, int yPosition, int width, int height) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.width = width;
        this.height = height;
    }
    
    public boolean isPressKeys() {
        if(isMoveUp && isMoveRight || isMoveUp && isMoveLeft ||
                isMoveDown && isMoveRight || isMoveDown && isMoveLeft ||
                isMoveUp && isMoveDown || isMoveUp && isMoveLeft && isMoveRight ||
                isMoveDown && isMoveLeft && isMoveRight ||
                isMoveUp && isMoveLeft && isMoveRight && isMoveDown) {
            return true;
        }
        return false;
    }
    public boolean isNotPressKey() {
        if(!isMoveDown && !isMoveUp && !isMoveLeft && !isMoveRight) {
            return true;
        }
        return false;
    }
    
    public boolean isMoving() {
        if(isMoveUp || isMoveDown || isMoveLeft || isMoveRight) {
            return true;
        }
        return false;
    }
    
    public boolean moveUp(int speed) {
        if (isMoveUp) {
            yPosition = yPosition - speed;
            return true;
        }
        return false;
    }

    public boolean moveDown(int speed) {
        if (isMoveDown) {
            yPosition = yPosition + speed;
            return true;
        }
        return false;
    }

    public boolean moveLeft(int speed) {
        if (isMoveLeft) {
            xPosition = xPosition - speed;
            return true;
        }
        return false;
    }

    public boolean moveRight(int speed) {
        if (isMoveRight) {
            xPosition = xPosition + speed;
            return true;
        }
        return false;
    }
    
    public boolean moveUp(int speed, boolean isMoveUp) {
        if (isMoveUp) {
            yPosition = yPosition - speed;
            return true;
        }
        return false;
    }

    public boolean moveDown(int speed, boolean isMoveDown) {
        if (isMoveDown) {
            yPosition = yPosition + speed;
            return true;
        }
        return false;
    }

    public boolean moveLeft(int speed, boolean isMoveLeft) {
        if (isMoveLeft) {
            xPosition = xPosition - speed;
            return true;
        }
        return false;
    }

    public boolean moveRight(int speed, boolean isMoveRight) {
        if (isMoveRight) {
            xPosition = xPosition + speed;
            return true;
        }
        return false;
    }
    
    public int getMaxY() {
        return yPosition + height;
    }

    public int getMaxX() {
        return xPosition + width;
    }
    
    public GamePosition() {
    }

    public int getXPosition() {
        return xPosition;
    }

    public void setXPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    public int getYPosition() {
        return yPosition;
    }

    public void setYPosition(int yPosition) {
        this.yPosition = yPosition;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

}
