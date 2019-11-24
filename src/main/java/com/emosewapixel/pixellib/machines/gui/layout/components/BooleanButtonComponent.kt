package com.emosewapixel.pixellib.machines.gui.layout.components

import com.emosewapixel.pixellib.machines.gui.layout.IPropertyGUIComponent
import com.emosewapixel.pixellib.machines.gui.textures.ButtonDrawMode
import com.emosewapixel.pixellib.machines.gui.textures.ButtonTextureGroup
import com.emosewapixel.pixellib.machines.properties.IVariableProperty
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

open class BooleanButtonComponent(override val property: IVariableProperty<Boolean>, val texture: ButtonTextureGroup, override var x: Int, override var y: Int) : IPropertyGUIComponent {
    override val tooltips = mutableListOf<String>()
    override var width = 16
    override var height = 16

    @OnlyIn(Dist.CLIENT)
    override fun drawInBackground(mouseX: Double, mouseY: Double, xOffset: Int, yOffset: Int) = texture.render(xOffset + x, yOffset + y, width, height, drawMode = when {
        property.value -> ButtonDrawMode.ON
        isSelected(mouseX - xOffset, mouseY - yOffset) -> ButtonDrawMode.SELECTED
        else -> ButtonDrawMode.OFF
    })

    @OnlyIn(Dist.CLIENT)
    override fun isSelected(mouseX: Double, mouseY: Double) = x < mouseX && mouseX < x + width && y < mouseY && mouseY < y + height

    @OnlyIn(Dist.CLIENT)
    override fun onMouseClicked(mouseX: Double, mouseY: Double, clickType: Int): Boolean {
        property.setValue(!property.value)
        return true
    }
}