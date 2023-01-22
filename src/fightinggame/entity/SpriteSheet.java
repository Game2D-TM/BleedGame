package fightinggame.entity;

import fightinggame.resource.ImageManager;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpriteSheet {

    private BufferedImage sprite;
    private List<BufferedImage> images;
    private int spriteCounter = 0;
    private int spriteIndex = 0;

    public SpriteSheet() {
        images = new ArrayList<>();
    }

    private SpriteSheet(BufferedImage sprite, List<BufferedImage> images) {
        this.sprite = sprite;
        this.images = images;
    }

    public SpriteSheet(BufferedImage sprite, int cutX, int cutY, int cutWidth, int cutHeight,
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
        BufferedImage image = images.get(spriteIndex);
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

    public static Map<String, SpriteSheet> loadSpriteSheetFromFolder(String folderPath) {
        try {
            File[] directories = new File(folderPath).listFiles(File::isDirectory);
            if (directories == null) {
                return null;
            }
            if (directories.length == 0) {
                return null;
            }
            Map<String, SpriteSheet> spriteSheetMap = new HashMap<>();
            for (File dir : directories) {
                SpriteSheet a = new SpriteSheet();
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

    public SpriteSheet convertRTL() {
        List<BufferedImage> flipped = new ArrayList<>();
        SpriteSheet result = new SpriteSheet();
        for (int i = 0; i < this.images.size(); i++) {
            BufferedImage sprite = images.get(i);
            sprite = ImageManager.flipImage(sprite);
            flipped.add(sprite);
        }
        result.setImages(flipped);
        return result;
    }

    public SpriteSheet rotateSheet(int radian) {
        List<BufferedImage> rotate = new ArrayList<>();
        SpriteSheet result = new SpriteSheet();
        for (int i = 0; i < this.images.size(); i++) {
            BufferedImage sprite = images.get(i);
            sprite = ImageManager.rotateImage(sprite, radian);
            rotate.add(sprite);
        }
        result.setImages(rotate);
        return result;
    }

    @Override
    public SpriteSheet clone() {
        List<BufferedImage> nImages = new ArrayList<>();
        nImages.addAll(images);
        return new SpriteSheet(sprite, nImages);
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
