package modules.simpleUi

import com.pungo.modules.basic.geometry.FastGeometry
import com.pungo.modules.basic.geometry.Rectangle
import modules.uiPlots.SceneDistrict

/** Campus implements building, but it also adds a pointer that checks the estate of the plot the element is in
 * This pointer is updated at plot level (in the setter of element)
 * Admittedly this is not, rock solid, but i think everybody can benefit from some reflection
 */
abstract class Campus: Building {
    var estatePointer: Rectangle = FastGeometry.unitSquare()
}