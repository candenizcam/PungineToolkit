package modules.simpleUi

import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.pungo.modules.basic.geometry.Rectangle
import modules.visuals.TextureCache


class PunSprite: Sprite {
    constructor(): super()
    constructor(sprite: Sprite): super(sprite)
    constructor(punSprite: PunSprite): super(punSprite)
    constructor(t: Texture) : super(t)
    constructor(p: Pixmap): super(Texture(p)){
        TextureCache.addToPixmapTextures(texture)
    }


    fun setRectangle(r: Rectangle){
        setSize(r.width,r.height)
        setCenter(r.centre.x,r.centre.y)
    }

    fun copy(): PunSprite {
        return PunSprite(this)
    }

}