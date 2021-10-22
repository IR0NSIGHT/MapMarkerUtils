package libpackage;

import api.mod.StarMod;
import api.utils.textures.StarLoaderTexture;
import org.schema.schine.graphicsengine.forms.Sprite;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * STARMADE MOD
 * CREATOR: Max1M
 * DATE: 20.10.2021
 * TIME: 18:27
 */
public class MarkerSpriteLoader {
    /**
     *
     * @param resourcePath path to resource from src: f.e. "org/ithirahad/resourcesresourced/assets/image/map/"
     * @param name name of image: f.e "zoneMarker.png"
     * @param width width for image to have when show
     * @param height height for image to have when show
     * @param x how many subsprites image has in horizontal direction (width)
     * @param y how many subsprites image has in vertical direction (depth)
     */
    public MarkerSpriteLoader(String resourcePath, String name, int width, int height, int x, int y) {
        this.resourcePath = resourcePath;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.name = name;
    }
    private final String resourcePath;
    private final String name;
    private Sprite sprite;
    int width;
    int height;
    int x;
    int y;

    /**
     * (attempt) loading sprite. will log errors. only use in starmod.onResourceLoad.
     * @param mod calling mod
     * @return if success
     */
    public boolean loadSprite(StarMod mod) {
        try {
            InputStream in = mod.getJarResource(resourcePath+name);
            BufferedImage img = ImageIO.read(in);
            this.sprite = StarLoaderTexture.newSprite(img,mod, name);
            sprite.setHeight(height);
            sprite.setWidth(width);
            sprite.setPositionCenter(true);
            sprite.setMultiSpriteMax(x,y);
            return true;
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Sprite getSprite() {
        return sprite;
    }
}
