package libpackage;

import org.schema.game.client.view.gamemap.GameMapDrawer;
import org.schema.schine.graphicsengine.forms.PositionableSubColorSprite;
import org.schema.schine.graphicsengine.forms.SelectableSprite;
import org.schema.schine.graphicsengine.forms.Sprite;

import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

/**
 * STARMADE MOD
 * CREATOR: Max1M
 * DATE: 25.09.2021
 * TIME: 11:31
 * container class that is basically:
 * name, position and icon to be drawn on map
 */
public class SimpleMapMarker implements PositionableSubColorSprite, SelectableSprite {
    /**
     * make a new simple mapmarker. doesnt do anything, just shows on map.
     */
    public SimpleMapMarker(Sprite sprite, int subSpriteIndex, Vector4f color, Vector3f mapPos) {
        this.color.set(color);
        this.subSprite = subSpriteIndex;
        this.sprite = sprite;
        this.pos = mapPos;
    }

    private int subSprite;
    private Sprite sprite;
    private Vector4f color = new Vector4f();
    private final Vector4f fadeColor = new Vector4f();
    private Vector3f pos;
    private float scale = 0.1f; //base scale

    @Override
    public Vector4f getColor() {
        if (fadeColor != null)
            return fadeColor;
        return color;
    }

    @Override
    public float getScale(long l) {
        return scale;
    }

    @Override
    public int getSubSprite(Sprite sprite) {
        return subSprite;
    }

    @Override
    public boolean canDraw() {
        return true;
    }

    public void preDraw(GameMapDrawer drawer) {
        float dist = MarkerUtil.getDistance(drawer.getCamera(),this);
        fadeColor.set(color);
        fadeColor.w = Math.max(0,Math.min(color.w,(dist-50)/100));
    }

    //getters and setters
    @Override
    public Vector3f getPos() {
        return pos;
    }

    public int getSubSprite() {
        return subSprite;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public void setColor(Vector4f color) {
        this.color = color;
    }

    public void setPos(Vector3f pos) {
        this.pos = pos;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    @Override
    public float getSelectionDepth() {
        return 0;
    }

    @Override
    public boolean isSelectable() {
        return false;
    }

    @Override
    public void onSelect(float v) {

    }

    @Override
    public void onUnSelect() {

    }
}
