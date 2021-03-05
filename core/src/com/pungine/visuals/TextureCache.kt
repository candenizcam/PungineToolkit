package modules.visuals

import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureAtlas


object TextureCache {
    private var textureList = mutableMapOf<FileHandle, Texture>()
    private var textureAtlasList = mutableMapOf<FileHandle, TextureAtlas>()
    private var jsonAtlasList = mutableMapOf<FileHandle, AnimateJson>()



    fun jsonOpener(path: FileHandle, visualPath: FileHandle? = null, func: (String) -> Boolean = { true }): MutableList<Sprite> {
        val at: AnimateJson
        if (jsonAtlasList.containsKey(path)) {
            at = jsonAtlasList[path]!!
        } else {
            at = AnimateJson(path, visualPath)
            jsonAtlasList[path] = at
        }
        return at.getSubTextures(func)
    }


    fun atlasOpener(path: FileHandle, region: String? = null): MutableList<Sprite> {
        openAtlasTexture(path).also {
            //val that = mutableListOf<SubTexture>()
            val that = mutableListOf<Sprite>()
            (if (region == null) {
                it.createSprites()
            } else {
                it.createSprites(region)
            }).forEach { it2 ->
                that.add(Sprite(it2))
            }
            return that
        }
    }


    private fun openAtlasTexture(path: FileHandle): TextureAtlas {
        return if (textureAtlasList.containsKey(path)) {
            textureAtlasList[path] as TextureAtlas
        } else {
            textureAtlasList[path] = TextureAtlas(path).also {
                it.textures.forEach {
                    it.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
                }
            }
            textureAtlasList[path] as TextureAtlas
        }
    }


    fun openTexture(path: FileHandle): Texture {
        return if (textureList.containsKey(path)) {
            textureList[path] as Texture
        } else {

            textureList[path] = Texture(path).also {
                it.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
            }
            textureList[path] as Texture
        }
    }

    fun dispose() {
        textureList.forEach {
            it.value.dispose()
        }
        textureAtlasList.forEach {
            it.value.textures.forEach { it2 ->
                it2.dispose()
            }
        }
        jsonAtlasList.forEach {
            it.value.dispose()

        }
    }
}