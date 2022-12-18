package fightinggame.entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Map;

public class Background {

    private int id;
    private String name;
    private final int width;
    private final int height;
    private Map<String, BufferedImage> backgrounds;

    public Background(int id, String name, Map<String, BufferedImage> backgrounds, int width, int height) {
        this.id = id;
        this.name = name;
        this.backgrounds = backgrounds;
        this.width = width;
        this.height = height;
    }

    public void render(Graphics g) {
        if (backgrounds != null && backgrounds.values().size() > 0) {
            for (BufferedImage image : backgrounds.values()) {
                g.drawImage(image, 0, 0, width, height, null);
            }
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, BufferedImage> getBackgrounds() {
        return backgrounds;
    }

    public void setBackgrounds(Map<String, BufferedImage> backgrounds) {
        this.backgrounds = backgrounds;
    }

}
