package modules.uiPlots

import com.pungo.modules.basic.geometry.Rectangle
import modules.application.PuniversalValues

/**
 * targetPunRectangle: the rectangle of pun coordinates
 * targetPxRectangle: The pixel area on which the visual will be drawn
 * cropRectangle: Crop rectangle carries the information about the part of the texture to be used
 * drawLimits: The limits in which a drawing can be made, this may or may not be the screen
 */

class DrawData(var targetPunRectangle: Rectangle,var targetPxRectangle: Rectangle, var cropRectangle: Rectangle, var drawLimits: Rectangle, ) {

    fun getPxRatedCopy(r: Rectangle): DrawData {
        return DrawData(targetPunRectangle,targetPxRectangle.getSubRectangle(r),cropRectangle,drawLimits)
    }

    fun toBeDrawn():Boolean{
        return targetPxRectangle.overlaps(drawLimits)
    }

    val punWidth: Float
        get() {return targetPunRectangle.width}
    val punHeight: Float
        get() {return targetPunRectangle.height}
    val expandedFrame: Rectangle // the rectangle that'd be expanded version of the target pixels if the zoom wouldn't be cropped
        get() {
            return targetPxRectangle.invertSubRectangle(cropRectangle)
        }

}