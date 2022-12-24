package fightinggame.entity;

public class Camera {

    private Player player;
    private GamePosition position;
    private int windowWidth;
    private int windowHeight;

    public Camera(Player player, GamePosition position, int windowWidth, int windowHeight) {
        this.player = player;
        this.position = position;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        position.setWidth(windowWidth);
        position.setHeight(windowHeight);
    }

    public void tick() {
        if (position != null) {
            if (player != null) {
                if (!player.isAttack) {
//                if (player.getPosition().getXPosition() <= position.getXPosition() + 300 || player.getPosition().getMaxX() >= position.getMaxX() - 300) {
                    position.setXPosition(player.getPosition().getXPosition() - windowWidth / 4);
                    position.setYPosition(player.getPosition().getYPosition() - windowHeight / 2);
//                }
                }
            }
        }
    }

    public int getWindowWidth() {
        return windowWidth;
    }

    public void setWindowWidth(int windowWidth) {
        this.windowWidth = windowWidth;
    }

    public int getWindowHeight() {
        return windowHeight;
    }

    public void setWindowHeight(int windowHeight) {
        this.windowHeight = windowHeight;
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

}
