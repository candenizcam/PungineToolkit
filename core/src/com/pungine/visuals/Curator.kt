package modules.visuals

import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import modules.simpleUi.images.PunSprite


/** This class curates a texture,
 *
 */
class Curator {
    constructor(path: FileHandle, visualPath: FileHandle?=null, filter: Texture.TextureFilter = Texture.TextureFilter.Linear){
        this.visualPath = visualPath?: path
        this.path = path
        this.filter = filter
    }

    private var textureAtlas: TextureAtlas? = null
    private var texture: Texture? = null
    private var animateJson: AnimateJson? = null
    private val filter: Texture.TextureFilter
    var path: FileHandle? = null
        private set
    var visualPath: FileHandle? = null
        private set


    fun contains(t: Texture): Boolean {
        return if(texture==null){
            false
        }else{
            texture==t
        }
    }

    fun contains(t: TextureAtlas): Boolean {
        return if(textureAtlas==null){
            false
        }else{
            textureAtlas==t
        }
    }

    fun contains(t: AnimateJson): Boolean {
        return if(animateJson==null){
            false
        }else{
            animateJson==t
        }
    }

    fun contains(p: PunSprite): Boolean {
        return contains(p.texture)
    }


    fun getAnimateJson(): AnimateJson{
        if(animateJson== null) {
            animateJson = AnimateJson(this.path!!,this.visualPath).also {
                texture = it.texture
            }
        }
        return animateJson!!
    }

    fun getTexture(): Texture{
        if(texture== null) {
            texture = Texture(this.visualPath).also {
                it.setFilter(filter,filter)
            }
        }
        return texture!!
    }


    fun getTextureAtlas(): TextureAtlas {
        if(textureAtlas== null) {
            textureAtlas = TextureAtlas(this.path).also {
                it.textures.forEach {
                    it.setFilter(filter,filter)
                    texture = it
                }
            }
        }
        return textureAtlas!!
    }

    fun dispose(){
        texture?.dispose()
    }
}