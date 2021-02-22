package modules.visuals

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

/** AnimateJson decodes a json file created by adobe animate
 * it only takes the path of the file as input and figures the rest on its own
 * dispose() is called automatically
 * getSubTextures() generates the SubTextures according to the relevant parameters
 */
data class AnimateJson(var path: FileHandle, var texturePath: FileHandle? = null) {
    val meta: Meta
    private val texture: Texture
    val frames: Map<String, FrameData>

    init {
        var f: String
        var j: Map<String, JsonElement>
        val m = path.readString()
        try {
            f = path.readString() //necessary to windows for some reason
            j = Json.decodeFromString(f) //decodes the outer layer
        } catch (e: Exception) {
            try {
                f = path.readString().drop(3) //necessary to windows for some reason
                j = Json.decodeFromString(f) //decodes the outer layer
            } catch (e: Exception) {
                f = path.readString().drop(1) //necessary to mac for some reason
                j = Json.decodeFromString(f) //decodes the outer layer
            }
        }



        meta = Json.decodeFromString(j["meta"].toString()) //meta is filled
        val auxFrames: Map<String, JsonElement> = Json.decodeFromString(j["frames"].toString()) //generates objects for each frame
        frames = auxFrames.mapValues { Json.decodeFromString(it.value.toString()) } //decodes the individual frames
        val pathList = path.path().split("/").toMutableList().dropLast(1).joinToString("/") //creates the path for the next line
        texture = if (texturePath == null) { // if this is null, then file is consulted
            Texture(Gdx.files.internal("$pathList/${meta.image}")) //finds and loads the texture
        } else {
            meta.image = texturePath!!.path() //im very certain this will work
            Texture(texturePath) //finds and loads the texture
        }.also {
            it.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        }

    }


    /** This is a basic SubTexture opener that takes region as input
     */
    fun getSubTextures(region: String? = null): MutableList<Sprite> {
        val relevant: MutableList<FrameData>
        if (region == null) {

            relevant = mutableListOf()
            frames.forEach {
                relevant.add(it.value)
            }
        } else {
            relevant = frames.filterKeys { region in it }.values.toMutableList()
        }
        return relevant.map { Sprite(texture, it.frame.x, it.frame.y, it.frame.w, it.frame.h) }.toMutableList()
    }

    /**This is for advanced users, it directly takes the filtering function
     *
     */
    fun getSubTextures(func: (String) -> Boolean): MutableList<Sprite> {
        val relevant: MutableList<FrameData> = frames.filterKeys(func).values.toMutableList()
        return relevant.map { Sprite(texture, it.frame.x, it.frame.y, it.frame.w, it.frame.h) }.toMutableList()
    }

    /**Disposes the texture
     */
    fun dispose() {
        texture.dispose()
    }


    /** Definitions of the serialized data classes required to open the json file
     *
     */
    @Serializable
    data class FrameData(var frame: Coords, var rotated: Boolean, var trimmed: Boolean, var spriteSourceSize: Coords, var sourceSize: Coords)

    @Serializable
    data class Coords(var x: Int = 0, var y: Int = 0, var w: Int = 0, var h: Int = 0)

    @Serializable
    data class Meta(var app: String, var version: String, var image: String, var format: String, var size: Coords, var scale: Int)
}