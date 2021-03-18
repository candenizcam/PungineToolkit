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
    var recordClicked = false
    private var recordPair: Pair <String?, Colour?> = Pair(null,null)
    private val nameEditor = TextEditor(References.textBox(name))

    init {
        district.addFullPlot("bg").also {
            it.element = Displayer(Colour.rgba256(0,0,0,225))
        }


        district.splitToPlots("items", Rectangle(0.1f,0.9f,0.8f,0.9f),col=2).also {
            it[0].element = References.textBox("name:")
            it[1].element = nameEditor
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
            it.element = SetButton(References.buttonTextBox("record"),0.7f,0.1f).also {
                it.clicked = {
                    recordClicked = true
                    recordPair = Pair(nameEditor.text,visual.getColour())
                }
            }
        }

        district.addFullPlot("delete",Rectangle(0.7f,0.9f,0.1f,0.2f)).also {
            it.element = SetButton(References.buttonTextBox("delete"),0.7f,0.1f).also {
                it.clicked = {
                    recordClicked = true
                    recordPair = Pair(nameEditor.text,null)
                }
            }
        }

        activateColourSystem(activeColourSystem.toString())
    }

    fun isRecordClicked(): Boolean {
        return recordClicked.also {
            recordClicked = false
        }
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
                        activate(Colour.rgba256(inputList[0],inputList[1],inputList[2]))
                    }
                    (ActiveColourSystem.HSV)->{
                        activate(Colour.hsva256(inputList[0],inputList[1],inputList[2]))
                    }
                    (ActiveColourSystem.HEX)->{
                        try {
                            val text =  (district.findPlot("hex",1,3).element as TextEditor).text
                            if((text.length==6)||(text.length==3)){
                                activate(Colour.byHex(text))
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

    fun activate(name: String?, c: Colour){
        nameEditor.text = name ?: "type here"
        activate(c)
    }

    fun activate(c: Colour){
        visual.recolour(c)
        for(i in 1..3){
            (district.findPlot("rgb",i,2).element as TextEditor).text = (listOf(c.r,c.g,c.b)[i-1]*255).toInt().toString()
        }
        for(i in 1..3){
            (district.findPlot("hsv",i,2).element as TextEditor).text = (listOf(c.h,c.s,c.v)[i-1]*255).toInt().toString()
        }
        (district.findPlot("hex",1,3).element as TextEditor).text = c.hex.dropLast(2)
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

    fun getRecording(): Pair<String?, Colour?> {
        return recordPair.also {
            recordPair = Pair(null,null)
        }
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