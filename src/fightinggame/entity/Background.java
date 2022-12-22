package fightinggame.entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Background {

    private int id;
    private String name;
    private final int width;
    private final int height;
    private Map<String, BufferedImage> backgrounds;
    private Map<String, BufferedImage> tiles;
    private Map<String, BufferedImage> objects;
    private final List<List<BufferedImage>> scene = new ArrayList<>();
    private String fileNameScene;

    public Background(int id, String name, Map<String, BufferedImage> backgrounds, int width, int height) {
        this.id = id;
        this.name = name;
        this.backgrounds = backgrounds;
        this.width = width;
        this.height = height;
    }

    public Background(int id, String name, Map<String, BufferedImage> backgrounds,
             int width, int height, Map<String, BufferedImage> tiles,
            Map<String, BufferedImage> objects, String fileNameScene) {
        this.id = id;
        this.name = name;
        this.width = width;
        this.height = height;
        this.backgrounds = backgrounds;
        this.tiles = tiles;
        this.objects = objects;
        this.fileNameScene = fileNameScene;
        loadImagesToScene();
    }

    public void renderScene(Graphics g, int x, int y, int width, int height) {         
        if (scene != null && scene.size() > 0) {
            width = this.width / scene.size();
            height = this.height / scene.get(0).size();
            int tempY = y;
            for (int i = 0; i < scene.size(); i++) {
                List<BufferedImage> images = scene.get(i);
                if (images != null && images.size() > 0) {
                    System.out.println(images.size());
                    int tempX = x;
                    for (int j = 0; j < images.size(); j++) {
                        BufferedImage image = images.get(j);
                        if (image != null) {
                            g.drawImage(image, tempX, tempY, width, height, null);
                        }
                        if (tempX <= this.width) {
                            tempX += width - 15;
                        }
                    }
                }
                if (tempY <= this.height) {
                    tempY += height;
                }
            }
        }
    }

    public void loadImagesToScene() {
        if (!fileNameScene.isBlank()) {
            Path path = Paths.get(fileNameScene);
            try {
                List<String> allLines = Files.readAllLines(path, StandardCharsets.UTF_8);
                if (allLines != null && allLines.size() > 0) {
                    for (String line : allLines) {
                        List<BufferedImage> images = new ArrayList<>();
                        String[] tilesStr = line.split(" ");
                        if (tilesStr != null && tilesStr.length > 0) {
                            for (int i = 0; i < tilesStr.length; i++) {
                                if (!tilesStr[i].isBlank()) {
                                    String key = tilesStr[i];
                                    if (key.equals("-1")) {
                                        images.add(null);
                                    } else {
                                        images.add(tiles.get(key));
                                    }
                                }
                            }
                        }
                        scene.add(images);
                    }
                }
            } catch (Exception ex) {
                Logger.getLogger(Background.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void render(Graphics g) {
        if (backgrounds != null && backgrounds.values().size() > 0) {
            for (BufferedImage image : backgrounds.values()) {
                g.drawImage(image, 0, 0, width, height, null);
            }
        }
        renderScene(g, -15, 0, 0, 0);
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

    public Map<String, BufferedImage> getTiles() {
        return tiles;
    }

    public void setTiles(Map<String, BufferedImage> tiles) {
        this.tiles = tiles;
    }

    public Map<String, BufferedImage> getObjects() {
        return objects;
    }

    public void setObjects(Map<String, BufferedImage> objects) {
        this.objects = objects;
    }

}
