package libpackage;

import org.schema.game.client.data.gamemap.entry.SelectableMapEntry;
import org.schema.schine.graphicsengine.forms.PositionableSubColorSprite;
import org.schema.schine.graphicsengine.forms.SelectableSprite;
import org.schema.schine.graphicsengine.forms.Sprite;

import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

/**
 * STARMADE MOD
 * CREATOR: Max1M
 * DATE: 20.10.2021
 * TIME: 23:52
 */
public class ClickableMapMarker extends SimpleMapMarker implements SelectableMapEntry {
    /**
     * make a new clickable mapmarker. can be selected, will grow on mouseover, etc
     *
     * @param sprite
     * @param subSpriteIndex
     * @param color
     * @param mapPos
     */
    public ClickableMapMarker(Sprite sprite, int subSpriteIndex, Vector4f color, Vector3f mapPos) {
        super(sprite, subSpriteIndex, color, mapPos);
    }
    private boolean selected;
    @Override
    public float getSelectionDepth() {
        return 0;
    }

    @Override
    public boolean isSelectable() {
        return true;
    }

    @Override
    public void onSelect(float v) {
        selected = true;
    }

    @Override
    public void onUnSelect() {
        selected = false;
    }

    public boolean isSelected() {
        return selected;
    }

    @Override
    public boolean isDrawIndication() {
        return false;
    }

    @Override
    public void setDrawIndication(boolean b) {

    }
}
