# Task: Build a Production-Ready Native 360° Panorama Viewer for Jetpack Compose

I need you to implement a **fully working native 360° panorama viewer for Android Jetpack Compose**.

The viewer must provide functionality similar to Pannellum's multiresolution panorama viewer, but it must be implemented natively using Kotlin + Jetpack Compose + OpenGL ES.

## CRITICAL REQUIREMENTS

Do NOT use:

- WebView
- Pannellum JS
- JavaScript
- HTML
- Three.js
- browser-based rendering
- any web-based panorama viewer

The final viewer must be a native Android component usable directly from Jetpack Compose.

Use:

- Kotlin
- Jetpack Compose
- OpenGL ES 3.0
- GLSurfaceView hosted through AndroidView if necessary
- Kotlin Coroutines
- kotlinx.serialization
- an appropriate HTTP/image loading mechanism

The implementation must be production-oriented, not pseudocode.

All important classes and functions must contain actual working implementations.

---

# 1. Desired Compose API

The final component should be usable approximately like this:

```kotlin
PanoramaViewer(
    configUrl = "https://example.com/panorama/123/config.json",
    modifier = Modifier.fillMaxSize()
)
```

Prefer an API along these lines:

```kotlin
@Composable
fun PanoramaViewer(
    configUrl: String,
    modifier: Modifier = Modifier,
    state: PanoramaState = rememberPanoramaState(),
    onLoadingChanged: (Boolean) -> Unit = {},
    onError: (Throwable) -> Unit = {}
)
```

Create:

```kotlin
@Stable
class PanoramaState
```

with observable state including:

```kotlin
yaw
pitch
hfov
minHfov
maxHfov
```

Provide:

```kotlin
@Composable
fun rememberPanoramaState(): PanoramaState
```

The rest of the application should not need to know anything about OpenGL.

---

# 2. Input format

The panorama tiles are generated using Pannellum's official multiresolution generator.

A real generated `config.json` looks like:

```json
{
    "hfov": 100.0,
    "type": "multires",
    "multiRes": {
        "path": "/%l/%s%y_%x",
        "fallbackPath": "/fallback/%s",
        "extension": "jpg",
        "tileResolution": 512,
        "maxLevel": 3,
        "cubeResolution": 1888
    }
}
```

Example source panorama:

```text
5952 × 2976
```

The viewer MUST be compatible with this Pannellum multires tile format.

Do not invent a different tile format unless absolutely necessary.

---

# 3. Understand Pannellum path placeholders correctly

The generated path:

```text
/%l/%s%y_%x
```

contains placeholders.

Implement a proper path resolver for:

```text
%l = resolution level
%s = cube face
%x = tile X
%y = tile Y
```

The implementation must correctly map Pannellum cube-face identifiers and tile coordinates.

Do NOT guess the cube-face orientation.

Verify Pannellum's actual cubemap conventions and ensure that:

- front
- back
- left
- right
- up
- down

are rendered with the correct orientation.

There must be no:

- mirrored panorama
- upside-down face
- reversed horizontal movement
- seams caused by incorrect orientation

---

# 4. Rendering architecture

Use OpenGL ES 3.0.

Compose should host a:

```text
GLSurfaceView
```

using:

```kotlin
AndroidView
```

The rendering architecture should roughly be:

```text
PanoramaViewer Composable
        |
        v
PanoramaState
        |
        v
GLSurfaceView
        |
        v
PanoramaRenderer
        |
        +-- Camera
        |
        +-- Cube geometry
        |
        +-- Tile textures
        |
        +-- TileManager
```

The camera should be positioned inside the cube.

Render the inside surfaces of the cube.

Handle face winding / culling correctly.

---

# 5. OpenGL implementation

Provide actual working OpenGL code.

Implement:

- vertex shader
- fragment shader
- shader compilation
- shader linking
- VAO/VBO/EBO if appropriate
- projection matrix
- view matrix
- model matrix if required
- texture upload
- texture parameters
- cleanup
- GL error handling where useful

Do not leave comments such as:

```kotlin
// TODO render cube
```

or:

```kotlin
// implement texture loading
```

Everything required for the viewer to function must be implemented.

---

# 6. Camera

Implement a proper panorama camera.

Camera state:

```kotlin
yaw: Float
pitch: Float
hfov: Float
```

Yaw should rotate horizontally through the full 360°.

Pitch must be clamped appropriately, approximately:

```text
-90° ... +90°
```

Avoid gimbal / matrix problems around the poles.

FOV controls zoom.

Smaller HFOV means more zoomed-in.

Support approximately:

```text
minHfov = 10f
maxHfov = 120f
```

The projection must account for viewport aspect ratio.

---

# 7. Compose gestures

Implement native Compose touch gestures.

Support:

### One finger

Drag horizontally:

```text
change yaw
```

Drag vertically:

```text
change pitch
```

Movement should feel natural for a panorama viewer.

### Two fingers

Pinch:

```text
change HFOV
```

Zoom must remain between:

```kotlin
state.minHfov
state.maxHfov
```

Drag and pinch should be smooth.

Avoid gesture conflicts.

Use frame-rate-independent / sensible sensitivity.

---

# 8. Rendering strategy

Do NOT decode the original 5952×2976 equirectangular panorama.

The viewer must render the generated cubemap tiles.

Initially, a fallback / low-resolution representation should be displayed quickly.

Then progressively replace it with higher-resolution tiles.

Desired behavior:

```text
fallback
   ↓
Level 1
   ↓
Level 2
   ↓
Level 3
```

Only load resolution / tiles useful for the current viewport.

Do not download every Level 3 tile immediately.

---

# 9. Tile Manager

Create a proper:

```kotlin
PanoramaTileManager
```

or equivalent.

Create models such as:

```kotlin
data class TileKey(
    val level: Int,
    val face: CubeFace,
    val x: Int,
    val y: Int
)
```

and:

```kotlin
enum class CubeFace
```

The TileManager must determine:

1. which cube faces intersect the current camera frustum
2. which portions of those faces are visible
3. which tile coordinates intersect the visible region
4. which resolution level is appropriate for the current FOV and viewport resolution
5. which tiles are already loaded
6. which tiles are currently downloading
7. which tiles should be requested next

Do NOT use a simplistic permanent mapping such as:

```kotlin
if (hfov > 80) level = 1
else if (hfov > 40) level = 2
else level = 3
```

Instead derive the appropriate level from:

- viewport dimensions
- FOV
- projected texel density
- tile/cube resolution

A simplified mathematically sound approximation is acceptable.

---

# 10. Visible tile selection

This is important.

Do not download an entire cube face at maximum resolution simply because some portion of that face is visible.

Determine which tile regions are visible.

For example:

```text
FRONT FACE

+-----+-----+-----+-----+
|     |     |     |     |
+-----+-----+-----+-----+
|     | XXX | XXX |     |
+-----+-----+-----+-----+
|     | XXX | XXX |     |
+-----+-----+-----+-----+
|     |     |     |     |
+-----+-----+-----+-----+
```

Only the required `XXX` tiles should need high-resolution loading.

It is acceptable to preload a small margin around the viewport to avoid visible loading during slow camera movement.

---

# 11. Progressive rendering

Never leave an area blank while waiting for a high-resolution tile.

If Level 3 is unavailable:

```text
use Level 2
```

If Level 2 is unavailable:

```text
use Level 1
```

If Level 1 is unavailable:

```text
use fallback
```

When the better tile becomes available, replace the lower-resolution texture.

Transitions should not cause black flashes.

---

# 12. Networking

Fetch:

```text
config.json
```

from `configUrl`.

Resolve tile paths relative to the directory containing `config.json`.

For example:

```text
https://example.com/panoramas/123/config.json
```

should make:

```text
/%l/%s%y_%x
```

resolve under:

```text
https://example.com/panoramas/123/
```

NOT the website root unless the configuration explicitly requires that behavior.

Handle URL joining safely.

Use asynchronous networking.

Never perform network requests on the OpenGL render thread.

---

# 13. Image decoding

Tile JPEG files are typically:

```text
512 × 512
```

Decode them off the GL thread.

Then queue GPU texture uploads onto the GL thread.

Never call OpenGL texture APIs from arbitrary coroutine/background threads.

Use something such as:

```kotlin
glSurfaceView.queueEvent {
    // upload texture
}
```

or another thread-safe GL mechanism.

Recycle / release CPU bitmaps when no longer needed.

---

# 14. Memory cache

Implement an LRU cache for decoded / GPU tile resources.

The viewer should NOT retain every tile indefinitely.

Use a configurable memory budget.

For example:

```text
64–128 MB
```

depending on implementation.

Prefer calculating approximate texture memory:

```text
width × height × bytesPerPixel
```

Evict least-recently-used high-resolution tiles when over budget.

Do not evict tiles actively required by the current viewport unless necessary.

---

# 15. Request management

Avoid duplicate downloads.

Maintain states similar to:

```text
NOT_REQUESTED
LOADING
READY
FAILED
```

If the camera moves quickly:

- obsolete low-priority requests should not overwhelm the network
- visible tiles should have priority
- highest-priority visible tiles should load first

Implement sensible coroutine cancellation / request prioritization where practical.

---

# 16. Render scheduling

Do not render continuously at 60 FPS when nothing changes.

Prefer:

```kotlin
GLSurfaceView.RENDERMODE_WHEN_DIRTY
```

Call:

```kotlin
requestRender()
```

when:

- yaw changes
- pitch changes
- FOV changes
- a texture becomes available
- viewport changes
- panorama changes

During gestures, rendering should still be smooth.

---

# 17. Lifecycle

Correctly handle:

- Compose entering composition
- leaving composition
- Activity pause
- Activity resume
- GL context recreation
- orientation changes
- viewport resize
- config URL changing

If the OpenGL context is destroyed, textures must be recreated correctly.

Do not leak:

- Activity
- Context
- Bitmap
- GLSurfaceView
- CoroutineScope
- GPU textures

---

# 18. Loading UI

Expose loading state to Compose.

Show a simple Compose loading indicator while the first usable panorama representation is loading.

Example:

```kotlin
Box {
    PanoramaSurface(...)

    if (state.isLoading) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
```

Once fallback / initial usable tiles are displayed, remove the blocking loading indicator.

Higher-resolution tiles may continue loading silently.

---

# 19. Error handling

Handle:

- config download failure
- malformed config
- unsupported panorama type
- tile 404
- image decode failure
- network timeout
- GL shader failure

A failed high-resolution tile should NOT destroy the entire viewer if a lower-resolution tile is available.

Expose fatal errors through:

```kotlin
onError
```

---

# 20. Hotspot-ready architecture

Do not need to fully implement hotspots unless straightforward, but structure the camera math so that later we can convert:

```text
yaw + pitch
```

into:

```text
screen X + screen Y
```

This will later allow Compose overlays such as:

```text
             360 PANORAMA

      [Building]
           ●

                         ● [Pump]

    ● [Entrance]
```

If possible, provide:

```kotlin
fun projectToScreen(
    yaw: Float,
    pitch: Float
): ScreenPosition?
```

using the same camera matrices as the renderer.

---

# 21. Optional gyroscope-ready design

Gyroscope control does not need to be enabled initially.

However, keep camera state separated from gesture handling so sensor orientation can later modify camera yaw/pitch without rewriting the renderer.

---

# 22. Package structure

Use a clean structure similar to:

```text
panorama/
|
├── PanoramaViewer.kt
├── PanoramaState.kt
├── PanoramaConfig.kt
|
├── gesture/
│   └── PanoramaGestures.kt
|
├── renderer/
│   ├── PanoramaRenderer.kt
│   ├── PanoramaCamera.kt
│   ├── CubeGeometry.kt
│   ├── ShaderProgram.kt
│   └── TextureManager.kt
|
├── tiles/
│   ├── PanoramaTileManager.kt
│   ├── TileKey.kt
│   ├── TileState.kt
│   ├── TilePathResolver.kt
│   └── CubeFace.kt
|
├── network/
│   └── PanoramaRepository.kt
|
└── math/
    ├── Frustum.kt
    └── ProjectionUtils.kt
```

You may improve the structure if necessary.

Avoid unnecessary abstraction.

---

# 23. Dependencies

Before writing code, inspect the existing project's Gradle configuration.

Reuse dependencies already available where appropriate.

If additional dependencies are required, explicitly provide the exact Gradle additions.

Avoid introducing a large 3D engine.

Do NOT use:

- Sceneform
- Filament unless absolutely necessary
- Unity
- libGDX
- WebView

OpenGL ES directly is preferred.

---

# 24. Kotlin quality

Use idiomatic modern Kotlin.

Prefer:

- coroutines
- StateFlow where useful
- immutable models
- structured concurrency
- proper resource ownership

Avoid:

- GlobalScope
- blocking network calls
- unnecessary mutable global state
- reflection
- giant god classes

Document complicated rendering / tile-selection mathematics.

---

# 25. Performance target

Target typical modern Android devices.

Desired:

```text
camera interaction → ~60 FPS
```

Dragging should remain smooth even while tiles download.

Network decoding must never block the UI or GL rendering.

Avoid allocating objects every frame where possible.

Reuse matrices, arrays, buffers, and temporary objects in the renderer.

---

# 26. First milestone

Before implementing multiresolution streaming, make sure the fundamental renderer works.

Milestone 1:

```text
GLSurfaceView
    ↓
OpenGL ES 3
    ↓
inside-facing cube
    ↓
six fallback cube textures
    ↓
drag
    ↓
yaw/pitch
    ↓
pinch
    ↓
FOV
```

Verify:

- full horizontal 360°
- correct vertical movement
- no mirrored textures
- no inverted cube faces
- no obvious seams
- smooth gestures

Then implement multiresolution tiles.

Do not skip this validation step.

---

# 27. Multires milestone

Once basic cubemap rendering works:

```text
config.json
     ↓
parse configuration
     ↓
fallback
     ↓
Level 1
     ↓
Level 2
     ↓
Level 3
```

Implement progressive tile replacement.

Use the existing Pannellum-generated tile directory directly.

---

# 28. Testing

Provide tests for logic that doesn't require OpenGL, especially:

```text
TilePathResolver
level calculation
tile coordinate calculations
URL resolution
config parsing
camera angle normalization
```

Test a configuration like:

```json
{
    "hfov": 100.0,
    "type": "multires",
    "multiRes": {
        "path": "/%l/%s%y_%x",
        "fallbackPath": "/fallback/%s",
        "extension": "jpg",
        "tileResolution": 512,
        "maxLevel": 3,
        "cubeResolution": 1888
    }
}
```

---

# 29. Deliverables

Do NOT only explain how to build it.

Actually modify / create the necessary project files.

At completion provide:

1. List of files created
2. List of files modified
3. Dependencies added
4. Brief architecture explanation
5. Example usage
6. Known limitations
7. How to test it
8. Any assumptions made about Pannellum tile conventions

The project must compile.

Do not leave placeholder implementations.

---

# 30. IMPORTANT WORKFLOW

Work incrementally.

Before changing code:

1. inspect the existing project
2. understand its package/module structure
3. inspect Gradle versions and dependencies
4. determine where the panorama component belongs

Then implement.

After each major stage:

1. compile the project
2. fix compiler errors
3. continue only after compilation succeeds

At the end:

- run the appropriate Gradle build / compile task
- resolve all errors caused by your implementation
- do not claim completion unless the relevant module compiles

If tests are available, run them.

---

# 31. Critical correctness requirements

Pay special attention to these areas because they are easy to implement incorrectly:

### Cubemap orientation

Verify every Pannellum face orientation.

### Tile coordinates

Verify `%x` and `%y` ordering against Pannellum's generated filenames.

### Texture seams

Use correct texture coordinates and texture parameters to minimize seams.

Consider:

```text
GL_CLAMP_TO_EDGE
```

where appropriate.

### OpenGL threading

ALL OpenGL resource creation / modification / deletion must occur with the correct GL context/thread.

### Context recreation

Network/cache state and GPU texture state are different things.

A GL context recreation invalidates GPU texture IDs even if downloaded tile data remains cached.

### Camera

Do not accidentally rotate the cube in a way that reverses expected drag behavior.

### Tile level selection

Use screen-space / texel-density reasoning instead of arbitrary hardcoded FOV thresholds.

### Memory

Do not retain hundreds of decoded 512×512 ARGB bitmaps indefinitely.

---

# 32. Final goal

I want to be able to write:

```kotlin
PanoramaViewer(
    configUrl = panorama.configUrl,
    modifier = Modifier.fillMaxSize()
)
```

and get a native Android 360° panorama experience comparable to Pannellum:

```text
✓ 360° horizontal viewing
✓ vertical viewing
✓ smooth drag
✓ pinch zoom
✓ multiresolution tiles
✓ progressive loading
✓ Pannellum-generated tile compatibility
✓ only visible / useful high-resolution tiles loaded
✓ fallback rendering
✓ caching
✓ lifecycle handling
✓ loading state
✓ error handling
✓ no WebView
✓ no JavaScript
✓ native Jetpack Compose integration
```

Prioritize **correctness and a working implementation** over clever abstractions.

If there is a conflict between architectural elegance and getting a correct, performant panorama renderer working, choose the simpler correct implementation.

Do not replace difficult sections such as frustum / visible tile selection with pseudocode or TODOs. Implement them.