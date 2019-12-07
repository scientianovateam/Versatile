package com.emosewapixel.pixellib.machines.gui.layout.components

import com.emosewapixel.pixellib.machines.gui.layout.IGUIComponent
import com.emosewapixel.pixellib.machines.gui.textures.BaseTextures
import com.emosewapixel.pixellib.machines.gui.textures.Direction2D
import com.emosewapixel.pixellib.machines.properties.IValueProperty
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

open class ProgressBarComponent(val property: IValueProperty<Double>) : IGUIComponent {
    var bar = BaseTextures.ARROW_BAR
    var direction = Direction2D.RIGHT
    override var x = 77
    override var y = 34
    override var width = 22
    override var height = 16

    @OnlyIn(Dist.CLIENT)
    override fun drawInBackground(mouseX: Double, mouseY: Double, xOffset: Int, yOffset: Int, guiLeft: Int, guiTop: Int) =
            bar.render(xOffset + x, yOffset + y, width, height, property.value, direction)

    override fun addProperties() = setOf(property)
}