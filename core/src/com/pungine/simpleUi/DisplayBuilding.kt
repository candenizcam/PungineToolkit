package modules.simpleUi


import modules.basic.Colour

interface DisplayBuilding: Building {
    fun getColour(): Colour
    fun recolour(c: Colour)
    fun copy(): DisplayBuilding

}