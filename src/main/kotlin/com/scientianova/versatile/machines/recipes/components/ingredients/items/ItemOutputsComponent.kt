package com.scientianova.versatile.machines.recipes.components.ingredients.items

import com.scientianova.versatile.machines.BaseTileEntity
import com.scientianova.versatile.machines.gui.layout.IGUIComponent
import com.scientianova.versatile.machines.gui.layout.components.slots.ItemSlotComponent
import com.scientianova.versatile.machines.gui.layout.components.stacksuppliers.RecipeOutputItemStackSupplierSlot
import com.scientianova.versatile.machines.properties.ITEBoundProperty
import com.scientianova.versatile.machines.properties.implementations.items.TEItemInventoryProperty
import com.scientianova.versatile.machines.properties.implementations.items.TEItemOutputProperty
import com.scientianova.versatile.machines.properties.implementations.processing.handlers.ItemOutputsProcessingHandler
import com.scientianova.versatile.machines.recipes.Recipe
import com.scientianova.versatile.machines.recipes.components.IRecipeComponent
import com.scientianova.versatile.machines.recipes.components.grouping.RecipeComponentFamilies
import com.scientianova.versatile.machines.recipes.components.ingredients.recipestacks.ChancedRecipeStack
import net.minecraft.item.ItemStack
import kotlin.math.min

class ItemOutputsComponent(val max: Int, val min: Int = 0) : IRecipeComponent<List<ChancedRecipeStack<ItemStack>>> {
    override val name = "itemOutputs"
    override val family = RecipeComponentFamilies.OUTPUT_SLOTS

    override fun isRecipeValid(recipe: Recipe): Boolean {
        val handler = recipe[this] ?: return min <= 0
        return handler.value.size in min..max
    }

    override fun addDefaultProperty(te: BaseTileEntity, properties: MutableList<ITEBoundProperty>) {
        properties += TEItemOutputProperty(max, "itemOutputs", te)
    }

    override fun addGUIComponents(machine: BaseTileEntity?): List<IGUIComponent> =
            machine?.let {
                val property = it.teProperties["itemOutputs"] as? TEItemInventoryProperty ?: return emptyList()
                (0 until property.value.slots).map { index -> ItemSlotComponent(property, index) }
            } ?: (0 until max).map(::RecipeOutputItemStackSupplierSlot)

    override fun addRecipeGUIComponents(machine: BaseTileEntity?, recipe: Recipe): List<IGUIComponent> {
        val handler = recipe[this]?.value ?: return emptyList()
        return machine?.let {
            val property = it.teProperties["itemOutputs"] as? TEItemInventoryProperty ?: return emptyList()
            (0 until min(property.value.slots, handler.size)).map { index -> ItemSlotComponent(property, index) }
        } ?: (handler.indices).map(::RecipeOutputItemStackSupplierSlot)
    }

    override fun getProcessingHandler(machine: BaseTileEntity) = (machine.teProperties["itemOutputs"] as? TEItemOutputProperty)?.let {
        ItemOutputsProcessingHandler(it)
    }
}