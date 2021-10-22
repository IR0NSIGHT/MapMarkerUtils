package libpackage;

import org.schema.schine.graphicsengine.forms.Sprite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * STARMADE MOD
 * CREATOR: Max1M
 * DATE: 21.10.2021
 * TIME: 11:43
 * object that maps Sprite -> subsprite, for drawing.
 * each list has a sprite, and a class derived from SimpleMapMarkers, defining the type of marker it can store
 */
class SubSpriteList {
    private Sprite sprite; //sprite
    private Class<? extends SimpleMapMarker> clazz; //allowed marker class (only this class, no childs)

    private final ArrayList<SimpleMapMarker> markers = new ArrayList<>();
    private SimpleMapMarker[] array;
    private boolean flagRebuild;

    /**
     * instantiate list
     * @param s Sprite to collect markers for
     * @param clazz specific marker type
     */
    SubSpriteList(Sprite s, Class<? extends SimpleMapMarker> clazz) {
        this.sprite = s;
        this.clazz = clazz;
    }

    /**
     * add all markers that fit sprite and clazz to this list
     * @param markers
     */
    void  addAllMarkers(Collection<SimpleMapMarker> markers) {
        for (SimpleMapMarker marker: markers) {
           addMarker(marker);
        }
    }

    /**
     * add marker. only adds if matches sprite and clazz
     * @param marker
     */
    void addMarker(SimpleMapMarker marker) {
        if(marker.getClass().equals(clazz) && marker.getSprite().equals(sprite)) {
            markers.add(marker);
            flagForRebuild();
        }
    }

    /**
     * remove marker if is contained by list and matches clazz and sprite
     * @param marker
     */
    void removeMarker(SimpleMapMarker marker) {
        if (marker.getClass().equals(clazz) && marker.getSprite().equals(sprite)) {
            markers.remove(marker);
            flagForRebuild();
        }
    }

    void update() {
        if (!flagRebuild)
            return;
        array = new SimpleMapMarker[markers.size()];
        markers.toArray(array);
    }

    /**
     * clear all markers.
     */
    void clearMarkers() {
        markers.clear();
        flagForRebuild();
    }

    private void flagForRebuild() {
        flagRebuild = true;
    }

    Sprite getSprite() {
        return sprite;
    }

    SimpleMapMarker[] getArray() {
        return array;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubSpriteList that = (SubSpriteList) o;
        return sprite.equals(that.sprite) &&
                clazz.equals(that.clazz);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sprite, clazz);
    }
}
