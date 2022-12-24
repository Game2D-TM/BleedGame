package fightinggame.entity;

import fightinggame.Gameplay;
import fightinggame.entity.platform.Platform;
import fightinggame.entity.platform.tile.BlankTile;
import fightinggame.entity.platform.tile.Tile;
import fightinggame.entity.platform.tile.WallTile;
import java.awt.Color;
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

    public static int NUMBER_WALL_COL = 3;
    public static int NUMBER_SKY_ROW = 5;
    
    private int id;
    private String name;
    private Gameplay gameplay;
    private int width;
    private int height;
    private Map<String, BufferedImage> backgrounds;
    private Map<String, BufferedImage> tiles;
    private Map<String, BufferedImage> objects;
    private final List<List<Platform>> scene = new ArrayList<>();
    private String fileNameScene;
    
    public Background(int id, String name, Map<String, BufferedImage> backgrounds, int width, int height, Gameplay gameplay) {
        this.id = id;
        this.name = name;
        this.backgrounds = backgrounds;
        this.width = width;
        this.height = height;
        this.gameplay = gameplay;
    }

    public Background(int id, String name, Map<String, BufferedImage> backgrounds,
            int width, int height, Map<String, BufferedImage> tiles,
            Map<String, BufferedImage> objects, Gameplay gameplay, String fileNameScene) {
        this.id = id;
        this.name = name;
        this.width = width;
        this.height = height;
        this.backgrounds = backgrounds;
        this.tiles = tiles;
        this.objects = objects;
        this.fileNameScene = fileNameScene;
        this.gameplay = gameplay;
        loadImagesToScene();
    }

    public void renderScene(Graphics g, int x, int y, int width, int height) {
        if (scene != null && scene.size() > 0) {
            int nHeight = 0;
            int maxSize = Integer.MIN_VALUE;
            int tempY = y;
            for (int i = 0; i < scene.size(); i++) {
                List<Platform> images = scene.get(i);
                if (images != null && images.size() > 0) {
                    int tempX = x;
                    for (int j = 0; j < images.size(); j++) {
                        Platform platform = images.get(j);
                        if (platform != null) {
                            platform.setPosition(new GamePosition(tempX, tempY, width, height));
                            g.drawImage(platform.getImage(), tempX, tempY, width, height, null);
                            g.setColor(Color.red);
                            g.drawRect(tempX, tempY, width, height);
                        }
                        tempX += width;
                    }
                    if(images.size() > maxSize) {
                        maxSize = images.size();
                    }
                }
                tempY += height;
                nHeight += height;
            }
            this.width = maxSize * width;
            this.height = nHeight;
        }
    }

    public void loadImagesToScene() {
        if (!fileNameScene.isBlank()) {
            Path path = Paths.get(fileNameScene);
            try {
                List<String> allLines = Files.readAllLines(path, StandardCharsets.UTF_8);
                if (allLines != null && allLines.size() > 0) {                   
                    int row = 0;
                    for (String line : allLines) {
                        List<Platform> platforms = new ArrayList<>();
                        String[] tilesStr = line.split(" ");
                        if (tilesStr != null && tilesStr.length > 0) {
                            int col = 0;
                            for (int i = 0; i < tilesStr.length; i++) {
                                if (!tilesStr[i].isBlank()) {
                                    String key = tilesStr[i];
                                    if (key.equals("-1")) {
                                        platforms.add(new BlankTile("Blank " + (row + col)
                                                        , tiles.get(key), null, gameplay, row, col));
                                    } else {
                                        if(col < NUMBER_WALL_COL) {
                                            platforms.add(new WallTile("Wall " + (row + col), 
                                                    tiles.get(key), null, gameplay, row, col));
                                        } else {
                                            if(row < NUMBER_SKY_ROW) {
                                                platforms.add(new BlankTile("Blank " + (row + col)
                                                        , tiles.get(key), null, gameplay, row, col));
                                            } else {
                                                platforms.add(new Tile("Tile " + (row + col)
                                                        , tiles.get(key), null, gameplay, row, col));
                                            }
                                        }
                                    }
                                }
                                col++;
                            }
                        }
                        scene.add(platforms);
                        row++;
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
                g.drawImage(image, 0 - gameplay.getCamera().getPosition().getXPosition(),
                         0 - gameplay.getCamera().getPosition().getYPosition(), width, height, null);
            }
        }
        renderScene(g, -15 - gameplay.getCamera().getPosition().getXPosition(),
                0 - gameplay.getCamera().getPosition().getYPosition(),
                250, 150);
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

    public List<List<Platform>> getScene() {
        return scene;
    }
    
}
