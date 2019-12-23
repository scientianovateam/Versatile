package com.scientianovateam.versatile.machines.properties.implementations.processing

import com.scientianovateam.versatile.common.extensions.nbt
import com.scientianovateam.versatile.machines.BaseTileEntity
import com.scientianovateam.versatile.machines.gui.BaseContainer
import com.scientianovateam.versatile.machines.properties.ITEBoundProperty
import com.scientianovateam.versatile.machines.properties.implementations.recipes.RecipeProperty
import com.scientianovateam.versatile.machines.recipes.Recipe
import net.minecraft.nbt.CompoundNBT

open class StandardProcessingProperty(override val id: String, override val te: BaseTileEntity, val recipeProperty: RecipeProperty) : ITEBoundProperty {
    override fun detectAndSendChanges(container: BaseContainer) {}

    override fun clone() = this

    override fun deserializeNBT(nbt: CompoundNBT?) {}

    override fun serializeNBT() = nbt {}

    protected val processingHandlers by lazy {
        recipeProperty.recipeList.recipeComponents.values.mapNotNull { it.getProcessingHandler(te) }
    }

    protected var isFinishing = false

    override fun tick() {
        if (te.world?.isRemote != false) return
        recipeProperty.value?.let { recipe ->
            if (processingHandlers.all(IProcessingHandler::isProcessing)) {
                if (processingHandlers.all { it.shouldFinishProcessing(recipe) }) {
                    isFinishing = true
                    processingHandlers.forEach { it.finishProcessingAutomation(recipe) }
                    isFinishing = false
                    startProcessing(recipe)
                } else if (processingHandlers.all { it.canContinueProcessing(recipe) })
                    processingHandlers.forEach { it.processTick(recipe) }
            }
        }
    }

    override fun update() {
        if (isFinishing) return
        if (te.world?.isRemote != false) return
        if (recipeProperty.value == null)
            recipeProperty.setValue(recipeProperty.recipeList.findRecipe(te), true)
        val recipe = recipeProperty.value ?: return
        if (!processingHandlers.all { it.isProcessing() })
            startProcessing(recipe)
    }

    private fun startProcessing(recipe: Recipe) {
        if (processingHandlers.all { it.canStartProcessingStandard(recipe) })
            processingHandlers.forEach {
                it.startProcessingStandard(recipe)
            }
        else {
            val newRecipe = recipeProperty.recipeList.findRecipe(te) ?: return
            recipeProperty.setValue(newRecipe, true)
            processingHandlers.forEach {
                it.startProcessingStandard(newRecipe)
            }
        }
    }
}