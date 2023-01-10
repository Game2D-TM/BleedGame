package fightinggame.entity;

import fightinggame.Gameplay;
import static fightinggame.entity.Background.NUMBER_SKY_ROW;
import static fightinggame.entity.Background.NUMBER_WALL_COL;
import fightinggame.entity.background.GameObject;
import fightinggame.entity.enemy.Enemy;
import fightinggame.entity.platform.Platform;
import fightinggame.entity.platform.tile.BlankTile;
import fightinggame.entity.platform.tile.Tile;
import fightinggame.entity.platform.tile.WallTile;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameMap extends Background {

    public static int DEF_SURROUND_PLATFORM = 8;
    public static int DEF_MINUS_DOWN = 3;
    public static int DEF_MINUS_UP = 0;
    public static int DEF_MINUS_RIGHT = 0;
    public static int DEF_MINUS_LEFT = 0;

    private BufferedImage mapImageBorder;

    public GameMap(int id, String name, Gameplay gameplay, GamePosition position,
            Map<String, BufferedImage> backgrounds,
            Map<String, BufferedImage> tiles,
            Map<String, BufferedImage> objects,
            String fileNameScene, int tileWidth, int tileHeight) {
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
        mapImageBorder = gameplay.getOptionHandler().getOptionMenu().getOptionGuis().get("main_paper");
    }

    @Override
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
                                    if (key.equals("0")) {
                                        nPlatform = new BlankTile("Blank " + (row + col),
                                                tiles.get(key), null, gameplay, row, col);
                                    } else {
                                        if (col < NUMBER_WALL_COL) {
                                            nPlatform = new WallTile("Wall " + (row + col),
                                                    tiles.get(key), null, gameplay, row, col);
                                        } else {
                                            if (row < NUMBER_SKY_ROW) {
                                                if (key.equals("0")) {
                                                    nPlatform = new BlankTile("Blank " + (row + col),
                                                            tiles.get(key), null, gameplay, row, col);
                                                } else {
                                                    nPlatform = new Tile("Tile " + (row + col),
                                                            tiles.get(key), null, gameplay, row, col);
                                                }
                                            } else {
                                                nPlatform = new Tile("Tile " + (row + col),
                                                        tiles.get(key), null, gameplay, row, col);
                                            }
                                        }
                                    }
                                    nPlatform.setIsMapRender(true);
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

    public void initMap(List<List<Platform>> scene) {
        if (scene != null && scene.size() > 0) {
            int nHeight = 0;
            int maxSize = Integer.MIN_VALUE;
            int tempY = position.getYPosition();
            for (int i = 0; i < scene.size(); i++) {
                List<Platform> images = scene.get(i);
                if (images != null && images.size() > 0) {
                    int tempX = position.getXPosition();
                    for (int j = 0; j < images.size(); j++) {
                        Platform platform = images.get(j);
                        if (platform != null) {
                            platform.setPosition(new GamePosition(tempX, tempY, tileWidth, tileHeight));
                        }
                        tempX += tileWidth;
                        if (images.size() > maxSize) {
                            maxSize = images.size();
                        }
                    }
                }
                tempY += tileHeight;
                nHeight += tileHeight;
            }
            position.setWidth(maxSize * tileWidth);
            position.setHeight(nHeight);
        }
    }

    @Override
    public void tick() {
        position.setXPosition(gameplay.getCamera().getPosition().getMaxX() - position.getWidth() - 10);
        position.setYPosition(gameplay.getCamera().getPosition().getYPosition() + 10);
        Platform insidePlatform = gameplay.getPlayer().getInsidePlatform();
        if (insidePlatform != null) {
            initMap(getSurroundPlatform(insidePlatform.getRow(), insidePlatform.getColumn(),
                    DEF_SURROUND_PLATFORM, DEF_MINUS_DOWN, DEF_MINUS_LEFT, DEF_MINUS_UP, DEF_MINUS_RIGHT));
        }
    }

    @Override
    public void render(Graphics g) {
        if (backgrounds != null && backgrounds.values().size() > 0) {
            if (mapImageBorder != null) {
                g.drawImage(mapImageBorder, position.getXPosition() - 10 - gameplay.getCamera().getPosition().getXPosition(),
                        position.getYPosition() - 10 - gameplay.getCamera().getPosition().getYPosition(),
                        position.getWidth() + 10,
                        position.getHeight() + 20, null);
            }
//            for (BufferedImage image : backgrounds.values()) {
//                g.drawImage(image, position.getXPosition() - gameplay.getCamera().getPosition().getXPosition(),
//                        position.getYPosition() - gameplay.getCamera().getPosition().getYPosition(), position.getWidth(),
//                        position.getHeight(), null);
//            }
        }
        if (gameplay.getPlayer().getInsidePlatform() != null) {
            Platform insidePlatform = gameplay.getPlayer().getInsidePlatform();
            if (insidePlatform != null) {
                List<List<Platform>> surroundPlatforms = getSurroundPlatform(insidePlatform.getRow(), insidePlatform.getColumn(),
                        DEF_SURROUND_PLATFORM, DEF_MINUS_DOWN, DEF_MINUS_LEFT, DEF_MINUS_UP, DEF_MINUS_RIGHT);
                if (surroundPlatforms != null && surroundPlatforms.size() > 0) {
                    renderScene(g, surroundPlatforms);
                    List<Platform> realPlatforms = gameplay.getRule().getSurroundVictoryPlatforms();
                    if (realPlatforms != null && realPlatforms.size() > 0) {
                        for (Platform realPlatform : realPlatforms) {
                            Platform platform = scene.get(realPlatform.getRow()).get(realPlatform.getColumn());
                            if (platform != null && platform.getPosition() != null) {
                                int platformRow = platform.getRow();
                                int platformColumn = platform.getColumn();
                                int playerRow = insidePlatform.getRow();
                                int playerColumn = insidePlatform.getColumn();
                                if ((platformRow >= playerRow - 8 && platformRow <= playerRow + 3)
                                        && (platformColumn >= playerColumn - 8 && platformColumn <= playerColumn + 8)) {
                                    g.setColor(Color.LIGHT_GRAY);
                                    g.fillRect(platform.getPosition().getXPosition() - gameplay.getCamera().getPosition().getXPosition(),
                                            platform.getPosition().getYPosition() - gameplay.getCamera().getPosition().getYPosition(),
                                            tileWidth, tileHeight);
                                }
                            }
                        }
                    }
                    if (gameObjectsTouchable.size() > 0) {
                        for (String key : gameObjectsTouchable.keySet()) {
                            GameObject obj = gameObjectsTouchable.get(key);
                            if (obj != null) {
                                int objRow = obj.getPlatform().getRow();
                                int objColumn = obj.getPlatform().getColumn();
                                int playerRow = insidePlatform.getRow();
                                int playerColumn = insidePlatform.getColumn();
                                if ((objRow >= playerRow - 8 && objRow <= playerRow + 3)
                                        && (objColumn >= playerColumn - 8 && objColumn <= playerColumn + 8)) {
                                    Platform platform = scene.get(objRow).get(objColumn);
                                    if (platform != null && platform.getPosition() != null) {
                                        obj.setPosition(platform.middlePlatform(tileWidth, tileHeight));
                                        obj.render(g);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        Platform playerInsidePlatform = gameplay.getPlayer().getInsidePlatform();
        if (playerInsidePlatform != null) {
            Platform mapPlatform = scene.get(playerInsidePlatform.getRow()).get(playerInsidePlatform.getColumn());
            if (mapPlatform != null) {
                if (mapPlatform.getPosition() != null) {
                    g.setColor(Color.white);
                    g.fillRect(mapPlatform.getPosition().getXPosition() - gameplay.getCamera().getPosition().getXPosition(),
                            mapPlatform.getPosition().getYPosition() - gameplay.getCamera().getPosition().getYPosition(),
                            tileWidth, tileHeight);
                }
            }
        }
        List<Enemy> enemies = gameplay.getEnemies();
        if (enemies != null && enemies.size() > 0) {
            Platform playerPlatform = gameplay.getPlayer().getInsidePlatform();
            if (playerPlatform != null) {
                List<List<Platform>> surroundPlatforms = getSurroundPlatform(playerPlatform.getRow(), playerPlatform.getColumn(),
                        DEF_SURROUND_PLATFORM, DEF_MINUS_DOWN, DEF_MINUS_LEFT, DEF_MINUS_UP, DEF_MINUS_RIGHT);
                if (surroundPlatforms != null && surroundPlatforms.size() > 0) {
                    for (int i = 0; i < enemies.size(); i++) {
                        Enemy enemy = enemies.get(i);
                        if (enemy != null) {
                            Platform enemyInsidePlatform = enemy.getInsidePlatform();
                            if (enemyInsidePlatform != null) {
                                if (checkExistPlatformInSurroundList(surroundPlatforms, enemyInsidePlatform)) {
                                    Platform mapPlatform = scene.get(enemyInsidePlatform.getRow()).get(enemyInsidePlatform.getColumn());
                                    if (mapPlatform != null) {
                                        if (mapPlatform.getPosition() != null) {
                                            g.setColor(Color.red);
                                            g.fillRect(mapPlatform.getPosition().getXPosition() - gameplay.getCamera().getPosition().getXPosition(),
                                                    mapPlatform.getPosition().getYPosition() - gameplay.getCamera().getPosition().getYPosition(),
                                                    tileWidth, tileHeight);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
