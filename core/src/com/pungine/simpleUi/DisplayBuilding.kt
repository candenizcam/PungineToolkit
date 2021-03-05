package modules.simpleUi

import com.badlogic.gdx.graphics.Color
import java.awt.Rectangle

interface DisplayBuilding: Building {
    fun getColour(): Color
    fun recolour(c: Color)
    fun copy(): DisplayBuilding

}