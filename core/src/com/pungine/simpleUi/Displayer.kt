package modules.simpleUi

import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.pungo.modules.basic.geometry.Point
import com.pungo.modules.visuals.PixmapGenerator
import modules.basic.Colour
import modules.uiPlots.DrawData
import modules.visuals.PuxMap
import modules.visuals.TextureCache


class Displayer: DisplayBuilding {
    constructor(imageCollection: ImageCollection){
        this.imageCollection = imageCollection
    }

    constructor(ps: PunSprite){
        imageCollection.add(ps)
    }

    constructor(fileHandle: FileHandle){
        imageCollection.add(PunSprite(TextureCache.openTexture(fileHandle)))
    }

    constructor(pixmap: PuxMap, colour: Color = Color.WHITE){
        imageCollection.add(PunSprite(pixmap).also {
            it.color = colour
        })
    }

    constructor(colour: Colour){
        imageCollection.add(PunSprite(PixmapGenerator.singleColour()).also {
            it.colour = colour
        })
    }

    var imageCollection = ImageCollection()


    override fun getColour(): Colour {
        return imageCollection.getColour()
    }

    override fun recolour(c: Colour) {
        imageCollection.recolour(c)
    }

    fun recolour(c: Colour, index: Int){
        imageCollection.recolour(c,index)
    }

    override fun update() {
        imageCollection.update()
    }

    override fun draw(batch: SpriteBatch, drawData: DrawData, alpha: Float) {
        imageCollection.yieldImage()?.also {
            val limited = drawData.targetPxRectangle.getIntersection(drawData.drawLimits)
            it.setRectangle(limited)
            it.setUVRect(drawData.cropRectangle.getSubRectangle(drawData.targetPxRectangle.getRatedRectangle(limited)))
            it.draw(batch,alpha)
        }
    }


    override fun hoverFunction(hovering: Boolean, relativePoint: Point?) {

    }


    override fun copy(): DisplayBuilding {
        return Displayer(imageCollection.copy())
    }
}