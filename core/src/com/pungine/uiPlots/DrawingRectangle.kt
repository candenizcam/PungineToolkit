package modules.uiPlots

import com.pungo.modules.basic.geometry.Rectangle

/** This class contains various methods to ease drawing functions
 * Hopefully this will not overcomplicate things
 */
data class DrawingRectangle(val screenCanvas: Rectangle, val punWidth: Float, val punHeight: Float, val canvasRatio: Rectangle){
    val screenSegment: Rectangle = screenCanvas.getSubRectangle(canvasRatio)
    val u1 = (screenCanvas.left-screenSegment.left).coerceAtLeast(0f)/screenSegment.width // corresponds to u values of Sprite
    val u2 = 1 + (screenCanvas.right-screenSegment.right).coerceAtMost(0f)/screenSegment.width // crops a contained texture
    val v1 = - (screenCanvas.top-screenSegment.top).coerceAtMost(0f)/screenSegment.height // u are width, h are height
    val v2 = 1 - (screenCanvas.bottom-screenSegment.bottom).coerceAtLeast(0f)/screenSegment.height // and of course h are inverse coordinated but thats handled there
    val croppedSegment = Rectangle( //the rectangle that'd be used for drawing, must be used with toBeDrawn()
        screenSegment.left.coerceAtLeast(screenCanvas.left),
        screenSegment.right.coerceAtMost(screenCanvas.right),
        screenSegment.bottom.coerceAtLeast(screenCanvas.bottom),
        screenSegment.top.coerceAtMost(screenCanvas.top))
    val baseWidth = punWidth*screenSegment.width/screenCanvas.width
    val baseHeight = punHeight*screenSegment.height/screenCanvas.height


    /** if true then the cropped segment is in the drawing field
     *
     */
    fun toBeDrawn(): Boolean {
        return croppedSegment.overlaps(screenCanvas)
    }


    /** Returns this drawing rectangle with new ratio
     */
    fun newRatio(r: Rectangle): DrawingRectangle {
        return DrawingRectangle(screenCanvas,punWidth,punHeight,r)
    }

    /** like new ratio but uses rated rectangle to scale the this canvasRatio
     */
    fun ratedCopy(r: Rectangle):DrawingRectangle{
        return DrawingRectangle(screenCanvas,punWidth,punHeight,canvasRatio.getSubRectangle(r))
    }
}
