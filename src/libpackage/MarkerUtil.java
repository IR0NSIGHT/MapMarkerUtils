package libpackage;

import org.schema.game.client.view.camera.GameMapCamera;

import javax.vecmath.Vector3f;

/**
 * STARMADE MOD
 * CREATOR: Max1M
 * DATE: 20.10.2021
 * TIME: 19:05
 */
public class MarkerUtil {
    public static float getDistance(Vector3f a, Vector3f b) {
        Vector3f off = new Vector3f(a);
        off.sub(b);
        return off.length();
    }
    public static float getDistance(GameMapCamera cam, SimpleMapMarker marker) {
        return getDistance(cam.getPos(),marker.getPos());
    }
}
