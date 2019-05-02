package com.EmosewaPixel.pixellib.jei.categories;

import com.EmosewaPixel.pixellib.blocks.BlockRegistry;
import com.EmosewaPixel.pixellib.recipes.MachineRecipe;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class InfusionCategory extends MachineBaseCategory {
    public InfusionCategory(IGuiHelper helper) {
        super(helper, "infusion", BlockRegistry.INFUSION_TABLE.asItem(), RecipeTypes.InfusionRecipe.class);
        this.backGround = helper.createDrawable(new ResourceLocation("pixellib:textures/gui/container/infusion_table.png"), 37, 16, 100, 54);
    }

    @Override
    public void setRecipe(IRecipeLayout layout, MachineRecipe recipe, IIngredients ingredients) {
        List<List<ItemStack>> inputs = recipe.getInputsAsList();
        List<ItemStack> outputs = recipe.getOutputsAsList();

        layout.getItemStacks().init(0, true, 0, 18);
        layout.getItemStacks().set(0, inputs.get(0));

        layout.getItemStacks().init(1, true, 18, 18);
        layout.getItemStacks().set(1, inputs.get(1));

        layout.getItemStacks().init(2, false, 78, 18);
        layout.getItemStacks().set(2, outputs.get(0));
    }

    @Override
    public void draw(MachineRecipe recipe, double mouseX, double mouseY) {
        arrow.draw(42, 18);
    }
}