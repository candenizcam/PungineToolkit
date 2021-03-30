package modules.visuals

import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import modules.simpleUi.images.PunSprite


object TextureCache {
    private var curatorList = mutableListOf<Curator>()

    fun atlasOpener(path: FileHandle, region: String? = null): MutableList<PunSprite> {
        openAtlasTexture(path).also {
            val that = mutableListOf<PunSprite>()
            (if (region == null) {
                it.createSprites()
            } else {
                it.createSprites(region)
            }).forEach { it2 ->
                that.add(PunSprite(it2))
            }
            return that
        }
    }

    fun getCurator(texture: Texture): Curator? {
        return curatorList.firstOrNull {  it.contains(texture) }
    }

    fun getCurator(textureAtlas: TextureAtlas): Curator? {
        return curatorList.firstOrNull {  it.contains(textureAtlas) }
    }

    fun getCurator(animateJson: AnimateJson): Curator? {
        return curatorList.firstOrNull {  it.contains(animateJson) }
    }

    fun getCurator(punSprite: PunSprite): Curator? {
        return curatorList.firstOrNull {  it.contains(punSprite) }
    }

    private fun getCurator(path: FileHandle,visualPath: FileHandle?=null): Curator{
        return curatorList.firstOrNull { it.path==path }.let {
            it ?: Curator(path,visualPath).also { it2->
                curatorList.add(it2)
            }
        }
    }

    fun jsonOpener(path: FileHandle, visualPath: FileHandle? = null, func: (String) -> Boolean = { true }): MutableList<PunSprite> {
        return getCurator(path,visualPath).getAnimateJson().getSubTextures(func)
    }

    fun openAtlasTexture(path: FileHandle): TextureAtlas {
        return getCurator(path).getTextureAtlas()
    }

    fun openTexture(path: FileHandle): Texture {
        return getCurator(path).getTexture()
    }

    fun dispose() {
        curatorList.forEach {
            it.dispose()
        }
    }
}