package com.emosewapixel.pixellib.machines.recipes.components.ingredients.fluids

import com.emosewapixel.pixellib.machines.recipes.components.IRecipeComponentHandler
import com.emosewapixel.pixellib.machines.recipes.components.ingredients.recipestacks.ChancedRecipeStack
import net.minecraftforge.fluids.FluidStack

class FluidOutputsHandler(override val value: List<ChancedRecipeStack<FluidStack>>) : IRecipeComponentHandler<List<ChancedRecipeStack<FluidStack>>> {
    override val pairedComponentType = FluidOutputsComponent::class.java
}