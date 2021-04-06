package modules.simpleUi.images

import com.badlogic.gdx.files.FileHandle
import kotlinx.serialization.Serializable
import modules.basic.Colour

@Serializable
data class ImageInfo(val filePath: String?, val visualPath: String?, val cr: Float, val cg: Float, val cb: Float, val ca: Float)
