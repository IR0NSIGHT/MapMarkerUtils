package libpackage;

import api.DebugFile;
import api.listener.Listener;
import api.listener.events.input.MousePressEvent;
import api.listener.fastevents.FastListenerCommon;
import api.listener.fastevents.GameMapDrawListener;
import api.mod.StarLoader;
import api.mod.StarMod;
import com.bulletphysics.linearmath.Transform;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.client.view.effects.ConstantIndication;
import org.schema.game.client.view.gamemap.GameMapDrawer;
import org.schema.game.client.view.gui.shiphud.HudIndicatorOverlay;
import org.schema.game.common.data.world.VoidSystem;
import org.schema.schine.common.language.Lng;
import org.schema.schine.graphicsengine.forms.Sprite;

import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

/**
 * STARMADE MOD
 * CREATOR: Max1M
 * DATE: 21.09.2021
 * TIME: 13:55
 * clientside fastutil listener that draws to the map
 */
public class MarkerMapDrawer implements GameMapDrawListener {
    private static final float sectorScale = 100f/ VoidSystem.SYSTEM_SIZE;
    private static final Vector3f halfSectorOffset = new Vector3f(sectorScale/2f,sectorScale/2f,sectorScale/2f);

    //new internat stuff for drawing
    final HashMap<Integer, SubSpriteList> subSpriteListHashMap = new HashMap<>();

    //internal stuff for drawing
    private final HashSet<SimpleMapMarker> simpleMapMarkers = new HashSet<>(); //input list of which markers to use, will be used for internal reconstruction.
    private Vector3f centerOn;
    public SimpleMapMarker selected;
    private boolean flagRebuildInternal;

    private StarMod mod;

    /**
     * create a new marker mapdrawer. adds itself to FastListeners.gameMapListeners.
     * @param mod
     */
    public MarkerMapDrawer(StarMod mod) {
        super();
        this.mod = mod;
        FastListenerCommon.gameMapListeners.add(this);
        StarLoader.registerListener(MousePressEvent.class, new Listener<MousePressEvent>() {
            @Override
            public void onEvent(MousePressEvent mouseEvent) {
                if (mouseEvent.getRawEvent().pressedLeftMouse() && selected != null) {
                //   if (selected.canCenterOn())
                //       centerOn = selected.mapPos;
                }
                if (mouseEvent.getRawEvent().pressedRightMouse() && selected != null) {
                //   if (selected.canNavigateTo())
                //      GameClientState.instance.getController().getClientGameData().setWaypoint(selected.getSector());
                }
            }
        },mod);
        flagForRebuild();
    }

    /**
     * will add the marker, requires updateLists() to become effective
     * @param marker
     * @return true if added, false if already exists, no update required.
     */
    public void addMarker(SimpleMapMarker marker) {
        synchronized (simpleMapMarkers) {
            simpleMapMarkers.add(marker);
            flagForRebuild();
        }
    }

    /**
     * will remove a marker from the lists. requires updateInternalList to be applied
     * @param marker
     */
    public void removeMarker(SimpleMapMarker marker) {
        synchronized (simpleMapMarkers) {
            simpleMapMarkers.remove(marker);
            flagForRebuild();
        }
    }

    /**
     * returns the list of markers this drawer has. Do not direclty modify it, use the add and remove methods, this list doenst directly influence what is drawn.
     * @return
     */
    public HashSet<SimpleMapMarker> getMapMarkers() {
        synchronized (simpleMapMarkers) {
            return simpleMapMarkers;
        }
    }

    public void clearMarkers() {
        synchronized (simpleMapMarkers) {
            HashSet<SimpleMapMarker> temp = new HashSet<>(simpleMapMarkers);
            for (SimpleMapMarker mapMarker: temp) {
                removeMarker(mapMarker);
            }
        }
    }

    /**
     * will copy internal mapping of sprite->subsprite hashset to sprite->subsprite[]
     */
    public void rebuildInternalList() {
        synchronized (simpleMapMarkers) {
            subSpriteListHashMap.clear();
            for (SimpleMapMarker marker: simpleMapMarkers) {
                //test if a sublist exists
                Class<? extends SimpleMapMarker> clazz = marker.getClass();
                Sprite sprite = marker.getSprite();
                if (sprite == null) {
                    DebugFile.logError(new NullPointerException("given marker has no sprite " + marker.toString()),mod);
                }
                int hashCode = Objects.hash(sprite,clazz);
                SubSpriteList subList = subSpriteListHashMap.get(hashCode);
                //if sublist doesnt exist, make new one put into hashmap
                if (subList == null) {
                    subList = new SubSpriteList(sprite,clazz);
                    subSpriteListHashMap.put(subList.hashCode(),subList);
                }
                //add marker to sublist
                subList.addMarker(marker);
            }

            for (SubSpriteList subList: subSpriteListHashMap.values()) {
                //rebuild inner
                subList.update();
            }
        }
    }

    //todo "clear all" method

    @Override
    public void system_PreDraw(GameMapDrawer gameMapDrawer, Vector3i vector3i, boolean b) {
        if (flagRebuildInternal) {
            try {
                rebuildInternalList();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            flagRebuildInternal = false;
        }
        if (centerOn != null) {
            gameMapDrawer.getGameMapPosition().set((int)centerOn.x,(int)centerOn.y,(int)centerOn.z,true);
            centerOn = null;
        }
    }

    @Override
    public void system_PostDraw(GameMapDrawer gameMapDrawer, Vector3i vector3i, boolean b) {
    }

    @Override
    public void galaxy_PreDraw(GameMapDrawer gameMapDrawer) {
    }

    @Override
    public void galaxy_PostDraw(GameMapDrawer gameMapDrawer) {

    }

    @Override
    public void galaxy_DrawLines(GameMapDrawer gameMapDrawer) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void galaxy_DrawSprites(GameMapDrawer gameMapDrawer) {
        for (SubSpriteList subList: subSpriteListHashMap.values()) {
            for (SimpleMapMarker m: subList.getArray()) {
                m.preDraw(gameMapDrawer);
            }
            DrawUtils.drawSprite(gameMapDrawer,subList.getSprite(),subList.getArray()); //TODO sort simplemarkers and advaned markers into different classes -> cant share same
        }
    }

    @Override
    public void galaxy_DrawQuads(GameMapDrawer gameMapDrawer) {

    }

    public void drawLinesSector(Vector3i from, Vector3i to, Vector4f startColor, Vector4f endColor) {
        Vector3f start = posFromSector(from,false);
        Vector3f end = posFromSector(to,false);
        DrawUtils.drawFTLLine(start,end,startColor,endColor);
    }

    public void drawText(Vector3i sector, String text) {
        drawText(posFromSector(sector,true),text);
    }

    public void drawText(Vector3f mapPos, String text) {
        Transform t = new Transform();
        t.setIdentity();
        t.origin.set(mapPos);
        ConstantIndication indication = new ConstantIndication(t, Lng.str(text));
        HudIndicatorOverlay.toDrawMapTexts.add(indication);
    }

    //helper stuff
    public static Vector3f posFromSector(Vector3i sector, boolean isSprite) {

        Vector3f out = sector.toVector3f();
        if (isSprite) {
            out.add(new Vector3f(-VoidSystem.SYSTEM_SIZE_HALF,-VoidSystem.SYSTEM_SIZE_HALF,-VoidSystem.SYSTEM_SIZE_HALF));
        }
        out.scale(sectorScale); out.add(halfSectorOffset);
        return out;
    }

    private void flagForRebuild() {
        flagRebuildInternal = true;
    }

}
