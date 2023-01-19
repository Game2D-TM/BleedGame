package fightinggame.entity;

import fightinggame.resource.ImageManager;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Entity {

    private BufferedImage sprite;
    private List<BufferedImage> images;
    private int spriteCounter = 0;
    private int spriteIndex = 0;

    public Entity() {
        images = new ArrayList<>();
    }

    private Entity(BufferedImage sprite, List<BufferedImage> images) {
        this.sprite = sprite;
        this.images = images;
    }

    public Entity(BufferedImage sprite, int cutX, int cutY, int cutWidth, int cutHeight,
            int x, int y, int width, int height, int imageNum) {
        this.sprite = sprite;
        getImages(cutX, cutY, cutWidth, cutHeight, x, y, width, height, imageNum);
    }

    public void tick(int tickToExecute) {
        if (tickToExecute < 0) {
            return;
        }
        spriteCounter++;
        if (spriteCounter > tickToExecute) {
            if (spriteIndex < images.size() - 1) {
                spriteIndex++;
            } else {
                spriteIndex = 0;
            }
            spriteCounter = 0;
        }
    }

    public void render(Graphics g, int x, int y, int width, int height) {
        Image image = images.get(spriteIndex);
        if (image != null) {
            g.drawImage(image, x, y, width, height, null);
            //rectangle
//            g.setColor(Color.red);
//            g.drawRect(x, y, width, height);
        }
    }

    public List<BufferedImage> getImages(int cutX, int cutY, int cutWidth, int cutHeight,
            int x, int y, int width, int height, int imageNum) {
        images = new ArrayList<>();
        int sheetXPos = cutX;
        for (int i = 0; i < imageNum; i++) {
            BufferedImage imageHaveBlank = sprite.getSubimage(sheetXPos, cutY, cutWidth, cutHeight);
            images.add(imageHaveBlank.getSubimage(x, y, width, height));
            sheetXPos += cutWidth;
        }
        return images;
    }

    public void reverseImages() {
        Collections.reverse(images);
    }

    public boolean add(String filePath) {
        try {
            if (images == null) {
                images = new ArrayList<>();
            }
            images.add(ImageManager.loadImage(filePath));
            return true;
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return false;
    }

    public List<BufferedImage> getImages() {
        return images;
    }

    public void setImages(List<BufferedImage> images) {
        this.images = images;
    }

    public BufferedImage getImage(int index) {
        if (images == null || images.size() == 0) {
            return null;
        }
        if (index > images.size() - 1) {
            return null;
        }
        return images.get(index);
    }

    public static Map<String, Entity> loadSpriteSheetFromFolder(String folderPath) {
        try {
            File[] directories = new File(folderPath).listFiles(File::isDirectory);
            if (directories == null) {
                return null;
            }
            if (directories.length == 0) {
                return null;
            }
            Map<String, Entity> spriteSheetMap = new HashMap<>();
            for (File dir : directories) {
                Entity a = new Entity();
                if (dir.listFiles().length == 0) {
                    continue;
                }
                for (int i = 0; i < dir.listFiles().length; i++) {
                    File f = dir.listFiles()[i];
                    a.add(f.getAbsolutePath());
                }
                spriteSheetMap.put(dir.getName(), a);
            }
            return spriteSheetMap;
        } catch (Exception e) {
            System.out.println("Assets not found in path " + folderPath + ".");
        }
        return null;
    }

    public Entity convertRTL() {
        List<BufferedImage> flipped = new ArrayList<>();
        Entity result = new Entity();
        for (int i = 0; i < this.images.size(); i++) {
            BufferedImage sprite = images.get(i);
            sprite = ImageManager.flipImage(sprite);
            flipped.add(sprite);
        }
        result.setImages(flipped);
        return result;
    }

    public Entity rotateSheet(int radian) {
        List<BufferedImage> rotate = new ArrayList<>();
        Entity result = new Entity();
        for (int i = 0; i < this.images.size(); i++) {
            BufferedImage sprite = images.get(i);
            sprite = ImageManager.rotateImage(sprite, radian);
            rotate.add(sprite);
        }
        result.setImages(rotate);
        return result;
    }

    @Override
    public Entity clone() {
        List<BufferedImage> nImages = new ArrayList<>();
        nImages.addAll(images);
        return new Entity(sprite, nImages);
    }

    public int getSpriteCounter() {
        return spriteCounter;
    }

    public void setSpriteCounter(int spriteCounter) {
        this.spriteCounter = spriteCounter;
    }

    public int getSpriteIndex() {
        return spriteIndex;
    }

    public void setSpriteIndex(int spriteIndex) {
        this.spriteIndex = spriteIndex;
    }

}
