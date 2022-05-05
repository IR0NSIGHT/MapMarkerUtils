# MapMarkerUtils
a library for drawing on the map in starmade
IMPORTANT: THIS PROJECT HAS VERY LIMITED DOCUMENTATION AND IS DISCONTINUED!
## Overview
this library is not a mod (!). It is not loaded into starmade or runnable. its literally just a library, bunch of class files for you to use.
the library offers utilities for:
- sprite loading
- mapmarker creation
- mapmarker drawing and internal organisation
- multiple types of mapmarkers that have different functionality
  - simpleMapMarker: cant do anything, just show on map, autofade if camera gets to close
  - clickableMapMarker:  can be clicked/notices onMouseOver.
## Usage
to display a marker you need 3 things:
- a loaded sprite
- a marker
- a mapdrawer
all those objects are provided by the library.
1. load your sprite
  - instantiate a MarkerSpriteLoader, pointing at your resource image file
  - load the sprite
2. create a marker using the loaded sprite
3. create a MarkerMapDrawer, add the marker to it.
4. ??
5. profit.
