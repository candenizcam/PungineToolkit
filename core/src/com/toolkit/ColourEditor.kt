package com.toolkit


import com.pungo.modules.basic.geometry.Rectangle
import modules.basic.Colour
import modules.simpleUi.Campus
import modules.simpleUi.DisplayBuilding
import modules.simpleUi.Displayer
import modules.simpleUi.SetButton
import modules.simpleUi.text.TextBox
import modules.simpleUi.text.TextEditor

class ColourEditor(name: String="type here"): Campus() {
    var visual = Displayer(Colour.BLACK)
    var activeColourSystem = ActiveColourSystem.RGB

    init {
        district.addFullPlot("bg").also {
            it.element = Displayer(Colour.rgba256(0,0,0,225))
        }


        district.splitToPlots("items", Rectangle(0.1f,0.9f,0.8f,0.9f),col=2).also {
            it[0].element = References.textBox("name:")
            it[1].element = TextEditor(References.textBox(name))
        }



        district.splitToPlots("systemButtons",Rectangle(0.1f,0.3f,0.3f,0.6f),3,1).also {
            listOf(ActiveColourSystem.RGB,ActiveColourSystem.HSV,ActiveColourSystem.HEX).forEachIndexed { index, it2->
                it[index].element = SetButton(References.buttonTextBox(it2.idString(),36)).also {
                    it.clicked = {
                        activeColourSystem = it2
                        activateColourSystem(it2.idString())
                    }
                }
            }

        }

        district.splitToPlots("rgb",Rectangle(0.3f,0.5f,0.3f,0.6f),3,2).also {
            for(i in 0..2){
                it[i*2].element = References.textBox(listOf("r:","g:","b:")[i])
                it[i*2+1].element = TextEditor(References.textBox("0"),digit = 3)
            }
        }

        district.splitToPlots("hsv",Rectangle(0.3f,0.5f,0.3f,0.6f),3,2).also {
            for(i in 0..2){
                it[i*2].element = References.textBox(listOf("h:","s:","v:")[i])
                it[i*2+1].element = TextEditor(References.textBox("0"),digit = 3)
            }
        }

        district.splitToPlots("hex",Rectangle(0.3f,0.6f,0.3f,0.6f),cols=listOf(0.3f,1f,5f)).also {
            it[1].element = References.textBox("#")
            it[2].element = TextEditor(References.textBox("000000",maxPunto = 36),digit = 6)
        }


        district.addFullPlot("undervisual",Rectangle(0.7f,0.9f,0.3f,0.6f)).also {
            it.element = TextEditor(References.textBox("invalid entry"))
        }

        district.addFullPlot("visual",Rectangle(0.7f,0.9f,0.3f,0.6f)).also {
            it.element = visual
        }

        district.addFullPlot("record", Rectangle(0.1f,0.3f,0.1f,0.2f)).also {
            it.element = SetButton(References.buttonTextBox("record"),0.7f,0.1f)
        }

        district.addFullPlot("delete",Rectangle(0.7f,0.9f,0.1f,0.2f)).also {
            it.element = SetButton(References.buttonTextBox("delete"),0.7f,0.1f)
        }

        activateColourSystem(activeColourSystem.toString())
    }

    private fun activateColourSystem(s: String){
        if(s == "rgb"){
            district.findPlots("rgb").forEach { it.visible = true }
            district.findPlots("hsv").forEach { it.visible = false }
            district.findPlots("hex").forEach { it.visible = false }
        }else if (s=="hsv"){
            district.findPlots("rgb").forEach { it.visible = false }
            district.findPlots("hsv").forEach { it.visible = true }
            district.findPlots("hex").forEach { it.visible = false }
        }else if(s=="hex"){
            district.findPlots("rgb").forEach { it.visible = false }
            district.findPlots("hsv").forEach { it.visible = false }
            district.findPlots("hex").forEach { it.visible = true }
        } else{
            district.findPlots("rgb").forEach { it.visible = false }
            district.findPlots("hsv").forEach { it.visible = false }
            district.findPlots("hex").forEach { it.visible = false }
        }
    }

    override fun update() {
        decode(activeColourSystem.idString()).also {
            val inputList = it
            if(it.size==3){
                district.findPlot("visual").visible = true
                when(activeColourSystem){
                    (ActiveColourSystem.RGB)->{
                        visual.recolour(Colour.rgba256(inputList[0],inputList[1],inputList[2]))
                    }
                    (ActiveColourSystem.HSV)->{
                        visual.recolour(Colour.hsva256(inputList[0],inputList[1],inputList[2]))
                    }
                    (ActiveColourSystem.HEX)->{
                        try {
                            val text =  (district.findPlot("hex",1,3).element as TextEditor).text
                            if((text.length==6)||(text.length==3)){
                                visual.recolour(Colour.byHex(text))
                            }else{
                                throw Exception("string len")
                            }
                        }catch(e: Exception){
                            district.findPlot("visual").visible = false
                        }
                    }
                }

            }else{
                district.findPlot("visual").visible = false
            }
        }
        super.update()
    }


    private fun decode(s: String): MutableList<Int> {
        val inputList = mutableListOf<Int>()
        var captured = false
        if(s=="hex") return mutableListOf<Int>(0,0,0)
        for  (i in 1..3){
            district.findPlot(s,i,2).also {
                val te = (it.element as TextEditor)
                captured = te.clickCaptured||captured
                try{
                    val value = te.text.toInt().coerceAtMost(255)
                    te.text = value.toString()
                    inputList.add(value)
                }catch (e: Exception){
                    if(te.text==""){
                        te.text = "0"
                        inputList.add(0)
                    }
                }
            }
        }
        return inputList
    }

    enum class ActiveColourSystem{
        RGB {
            override fun idString(): String {
                return "rgb"
            }

        },
        HSV {
            override fun idString(): String {
                return "hsv"
            }


        },
        HEX {
            override fun idString(): String {
                return "hex"
            }


        };

        abstract fun idString(): String
    }


}