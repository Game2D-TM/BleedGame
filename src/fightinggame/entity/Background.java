package fightinggame.entity;

import fightinggame.Gameplay;
import fightinggame.entity.background.GameObject;
import fightinggame.entity.platform.Platform;
import fightinggame.entity.platform.tile.BlankTile;
import fightinggame.entity.platform.tile.Tile;
import fightinggame.entity.platform.tile.WallTile;
import fightinggame.entity.platform.tile.WaterTile;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Background {

    public static int NUMBER_WALL_COL = 3;
    public static int NUMBER_SKY_ROW = 10;
    public static int DEF_SURROUND_TILE = 1;
    public static int DEF_SURROUND_RENDER_TILE = 4;

    protected int id;
    protected String name;
    protected Gameplay gameplay;
    protected GamePosition position;
    protected Map<String, BufferedImage> backgrounds;
    protected Map<String, BufferedImage> tiles;
    protected Map<String, BufferedImage> objects;
    protected Map<String, Tile> specialTiles;
    protected final Map<String, GameObject> gameObjectsTouchable = new HashMap<>();
    protected final Map<String, GameObject> gameObjectsNonTouchable = new HashMap<>();
    protected final List<List<Platform>> scene = new ArrayList<>();
    protected String fileNameScene;
    protected int tileWidth;
    protected int tileHeight;

    protected Background() {

    }

    public Background(int id, String name, Map<String, BufferedImage> backgrounds,
            Map<String, BufferedImage> tiles, Map<String, BufferedImage> objects,
            Gameplay gameplay, String fileNameScene,
            int tileWidth, int tileHeight) {
        this.id = id;
        this.name = name;
        this.backgrounds = backgrounds;
        this.tiles = tiles;
        this.objects = objects;
        this.fileNameScene = fileNameScene;
        this.gameplay = gameplay;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.position = new GamePosition(-15, 0, 0, 0);
        loadImagesToScene();
        initScene(position.getXPosition(), position.getYPosition(), tileWidth, tileHeight);
    }

    public Background(int id, String name, Gameplay gameplay,
            GamePosition position,
            Map<String, BufferedImage> backgrounds,
            Map<String, BufferedImage> tiles, Map<String, BufferedImage> objects, String fileNameScene,
            int tileWidth, int tileHeight) {
        this.id = id;
        this.name = name;
        this.gameplay = gameplay;
        this.position = position;
        this.backgrounds = backgrounds;
        this.tiles = tiles;
        this.objects = objects;
        this.fileNameScene = fileNameScene;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        loadImagesToScene();
        initScene(position.getXPosition(), position.getYPosition(), tileWidth, tileHeight);
    }

    public void initScene(int x, int y, int width, int height) {
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
                        }
                        tempX += width;
                        if (images.size() > maxSize) {
                            maxSize = images.size();
                        }
                    }
                }
                tempY += height;
                nHeight += height;
            }
            position.setWidth(maxSize * width);
            position.setHeight(nHeight);
        }
    }

    public void renderScene(Graphics g, List<List<Platform>> scene) {
        if (scene != null && scene.size() > 0) {
            for (int i = 0; i < scene.size(); i++) {
                List<Platform> images = scene.get(i);
                if (images != null && images.size() > 0) {
                    for (int j = 0; j < images.size(); j++) {
                        Platform platform = images.get(j);
                        if (platform != null && platform.getPosition() != null) {
                            g.drawImage(platform.getImage(), platform.getPosition().getXPosition()
                                    - gameplay.getCamera().getPosition().getXPosition(), platform.getPosition().getYPosition()
                                    - gameplay.getCamera().getPosition().getYPosition(), platform.getPosition().getWidth(),
                                    platform.getPosition().getHeight(), null);
                            // hitbox
//                            g.setColor(Color.red);
//                            g.drawRect(platform.getPosition().getXPosition()
//                                    - gameplay.getCamera().getPosition().getXPosition(), platform.getPosition().getYPosition()
//                                    - gameplay.getCamera().getPosition().getYPosition(), platform.getPosition().getWidth(),
//                                    platform.getPosition().getHeight());
                        }
                    }
                }
            }
        }
    }

    public void renderScene(Graphics g) {
        renderScene(g, scene);
    }

    public void loadImagesToScene() {
        if (!fileNameScene.isBlank()) {
            Path path = Paths.get(fileNameScene);

            List<String> allLines;
            try {
                allLines = Files.readAllLines(path, StandardCharsets.UTF_8);
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
                                    Platform nPlatform;
                                    if (col < NUMBER_WALL_COL) {
                                        nPlatform = new WallTile("Wall " + (row + col),
                                                tiles.get(key), null, gameplay, row, col);
                                    } else {
                                        switch (key) {
                                            case "0":
                                                nPlatform = new BlankTile("Blank " + (row + col),
                                                        tiles.get(key), null, gameplay, row, col);
                                                break;
                                            case "3", "4":
                                                nPlatform = new WaterTile("Tile " + (row + col),
                                                        tiles.get(key), null, gameplay, row, col);
                                                break;
                                            default:
                                                nPlatform = new Tile("Tile " + (row + col),
                                                        tiles.get(key), null, gameplay, row, col);
                                                break;
                                        }
                                    }
                                    platforms.add(nPlatform);
                                }
                                col++;
                            }
                        }
                        scene.add(platforms);
                        row++;
                    }
                }
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(Background.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        }
    }

    public boolean checkExistPlatformInSurroundList(List<List<Platform>> surroundPlatforms, Platform platform) {
        if (surroundPlatforms == null) {
            return false;
        }
        if (surroundPlatforms.isEmpty()) {
            return false;
        }
        for (int i = 0; i < surroundPlatforms.size(); i++) {
            List<Platform> listPlatform = surroundPlatforms.get(i);
            if (listPlatform == null) {
                continue;
            }
            if (listPlatform.isEmpty()) {
                continue;
            }
            if (listPlatform.contains(platform)) {
                return true;
            }
        }
        return false;
    }

    public void tick() {
        if (scene != null && scene.size() > 0) {
            for (int i = 0; i < scene.size(); i++) {
                List<Platform> platforms = scene.get(i);
                if (platforms != null && platforms.size() > 0) {
                    for (int j = 0; j < platforms.size(); j++) {
                        Platform platform = platforms.get(j);
                        if (platform != null) {
                            platform.tick();
                        }
                    }
                }
            }
        }
        if (gameObjectsTouchable.size() > 0) {
            for (String key : gameObjectsTouchable.keySet()) {
                GameObject obj = gameObjectsTouchable.get(key);
                if (obj != null) {
                    obj.tick();
                }
            }
        }
        if(specialTiles != null) {
            if(specialTiles.size() > 0) {
                for(String key : specialTiles.keySet()) {
                    Tile specialTile = specialTiles.get(key);
                    if(specialTile != null) {
                        specialTile.tick();
                    }
                }
            }
        }
    }

    public void render(Graphics g) {
        if (backgrounds != null && backgrounds.values().size() > 0) {
            for (BufferedImage image : backgrounds.values()) {
                g.drawImage(image, position.getXPosition() - gameplay.getCamera().getPosition().getXPosition(),
                        position.getYPosition() - gameplay.getCamera().getPosition().getYPosition(), position.getWidth(),
                        position.getHeight(), null);
            }
        }
        if (gameObjectsNonTouchable.size() > 0) {
            for (String key : gameObjectsNonTouchable.keySet()) {
                GameObject obj = gameObjectsNonTouchable.get(key);
                if (obj != null) {
                    obj.render(g);
                }
            }
        }
        if (scene != null && scene.size() > 0) {
            for (int i = 0; i < scene.size(); i++) {
                List<Platform> platforms = scene.get(i);
                if (platforms != null && platforms.size() > 0) {
                    for (int j = 0; j < platforms.size(); j++) {
                        Platform platform = platforms.get(j);
                        if (platform != null) {
                            if (platform.isCanRender()) {
                                platform.render(g);
                            }
                        }
                    }
                }
            }
        }
        if (gameObjectsTouchable.size() > 0) {
            for (String key : gameObjectsTouchable.keySet()) {
                GameObject obj = gameObjectsTouchable.get(key);
                if (obj != null) {
                    obj.render(g);
                }
            }
        }
        if(specialTiles != null) {
            if(specialTiles.size() > 0) {
                for(String key : specialTiles.keySet()) {
                    Tile specialTile = specialTiles.get(key);
                    if(specialTile != null) {
                        specialTile.render(g);
                    }
                }
            }
        }
    }

    public List<List<Platform>> getSurroundPlatform(int i, int j, int surroundTile,
            int minusDown, int minusLeft, int minusUp, int minusRight) {
        List<List<Platform>> scenePlatforms = scene;
        if (scenePlatforms != null && scenePlatforms.size() > 0) {
            List<List<Platform>> platforms = new ArrayList<List<Platform>>();
            int left = j, up = i, right = j, down = i;
            if (left - surroundTile - minusLeft >= 0) {
                left = left - surroundTile - minusLeft;
            } else {
                left = 0;
            }
            if (up - surroundTile - minusUp >= 0) {
                up = up - surroundTile - minusUp;
            } else {
                up = 0;
            }
            if (scenePlatforms.get(i) != null && scenePlatforms.get(i).size() > 2) {
                if (right + surroundTile - minusRight <= scenePlatforms.get(i).size() - 1) {
                    right = right + surroundTile - minusRight;
                } else {
                    right = scenePlatforms.get(i).size() - 1;
                }
            } else {
                return null;
            }
            if (scenePlatforms.size() > 2) {
                if (down + surroundTile - minusDown <= scenePlatforms.size() - 1) {
                    down = down + surroundTile - minusDown;
                } else {
                    down = scenePlatforms.size() - 1;
                }
            } else {
                return null;
            }
//            System.out.println(up + " " + down + " " + right + " " + left);
            for (int row = up; row <= down; row++) {
                List<Platform> list = new ArrayList<Platform>();
                for (int column = left; column <= right; column++) {
                    Platform platform = scenePlatforms.get(row).get(column);
                    if (platform != null) {
                        list.add(platform);
                    }
                }
                platforms.add(list);
            }
            return platforms;
        }
        return null;
    }

    public Map<String, GameObject> getGameObjectsTouchable() {
        return gameObjectsTouchable;
    }

    public Map<String, GameObject> getGameObjectsNonTouchable() {
        return gameObjectsNonTouchable;
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

    public GamePosition getPosition() {
        return position;
    }

    public void setPosition(GamePosition position) {
        this.position = position;
    }

    public Map<String, Tile> getSpecialTiles() {
        return specialTiles;
    }

    public void setSpecialTiles(Map<String, Tile> specialTiles) {
        this.specialTiles = specialTiles;
    }

}
