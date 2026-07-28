package tech.sumato.avn.mp.component.panorama

import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.cValue
import kotlinx.cinterop.usePinned
import platform.CoreGraphics.CGRect
import platform.CoreGraphics.CGRectMake
import platform.Foundation.NSTemporaryDirectory
import platform.SceneKit.SCNBox
import platform.SceneKit.SCNCamera
import platform.SceneKit.SCNMaterial
import platform.SceneKit.SCNNode
import platform.SceneKit.SCNScene
import platform.SceneKit.SCNVector3
import platform.SceneKit.SCNView
import platform.UIKit.UIColor
import platform.UIKit.UIImage
import platform.posix.fclose
import platform.posix.fopen
import platform.posix.fwrite
import platform.posix.remove
import kotlin.math.PI

@OptIn(ExperimentalForeignApi::class)
class SceneKitSetup {

    var sceneView: SCNView? = null
        private set

    private val scene: SCNScene = SCNScene.scene()
    private val cameraNode: SCNNode = SCNNode()

    private val faceMaterials = mutableListOf<SCNMaterial>()

    fun createSceneView(frame: CValue<CGRect> = CGRectMake(0.0, 0.0, 0.0, 0.0)): SCNView {
        val scnView = SCNView(frame, null)
        scnView.scene = scene
        scnView.allowsCameraControl = false
        scnView.showsStatistics = false
        scnView.backgroundColor = UIColor.blackColor

        setupCamera()
        setupCube()

        sceneView = scnView
        return scnView
    }

    fun updateCamera(yaw: Float, pitch: Float, hfov: Float) {
        val pitchRad = (pitch * PI / 180.0)
        val yawRad = (yaw * PI / 180.0)

        cameraNode.eulerAngles = scnVector3(pitchRad.toFloat(), yawRad.toFloat(), 0f)

        val camera = cameraNode.camera
        if (camera != null) {
            camera.setFieldOfView(hfov.toDouble())
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    fun updateFaceTexture(faceIndex: Int, imageBytes: ByteArray) {
        if (faceIndex < 0 || faceIndex >= faceMaterials.size) return
        val tempDir = NSTemporaryDirectory().trimEnd('/')
        val filePath = "$tempDir/pano_face_$faceIndex.jpg"

        imageBytes.usePinned { pinned ->
            val file = fopen(filePath, "wb") ?: return
            fwrite(pinned.addressOf(0), 1.toULong(), imageBytes.size.toULong(), file)
            fclose(file)
        }

        val image = UIImage.imageWithContentsOfFile(filePath)
        if (image != null) {
            faceMaterials[faceIndex].diffuse.contents = image
        }
        remove(filePath)
    }

    fun updateFaceMaterial(faceIndex: Int, image: UIImage) {
        if (faceIndex < 0 || faceIndex >= faceMaterials.size) return
        faceMaterials[faceIndex].diffuse.contents = image
    }

    private fun setupCamera() {
        cameraNode.camera = SCNCamera()
        cameraNode.position = scnVector3(0f, 0f, 0f)
        scene.rootNode.addChildNode(cameraNode)
    }

    private fun setupCube() {
        val boxGeometry = SCNBox.boxWithWidth(
            20.0, height = 20.0, length = 20.0, chamferRadius = 0.0
        )

        val fallbackColors = listOf(
            UIColor.redColor, UIColor.greenColor, UIColor.blueColor,
            UIColor.yellowColor, UIColor.magentaColor, UIColor.cyanColor,
        )

        faceMaterials.clear()
        val materials = fallbackColors.map { color ->
            SCNMaterial().apply {
                diffuse.contents = color
                setDoubleSided(true)
            }.also { faceMaterials.add(it) }
        }
        boxGeometry.materials = materials

        val boxNode = SCNNode.nodeWithGeometry(boxGeometry)
        scene.rootNode.addChildNode(boxNode)
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun scnVector3(x: Float, y: Float, z: Float): CValue<SCNVector3> {
    return cValue {
        this.x = x
        this.y = y
        this.z = z
    }
}
