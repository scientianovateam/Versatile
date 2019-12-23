package com.scientianovateam.versatile.materialsystem.addition

import com.scientianovateam.versatile.common.extensions.toResLoc
import com.scientianovateam.versatile.fluids.MaterialFluidAttributes
import com.scientianovateam.versatile.materialsystem.builders.blockType
import com.scientianovateam.versatile.materialsystem.builders.fluidType
import com.scientianovateam.versatile.materialsystem.builders.itemType
import com.scientianovateam.versatile.materialsystem.main.Material
import com.scientianovateam.versatile.materialsystem.properties.BlockCompaction
import net.minecraft.block.Block
import net.minecraft.block.SoundType
import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundEvents
import net.minecraftforge.common.ToolType
import net.minecraft.block.material.Material as BlockMaterial

object BaseObjTypes {
    @JvmField
    val DUST = itemType("dust", Material::hasDust) {
        bucketVolume = 144
        typePriority = 1
    }
    @JvmField
    val GEM = itemType("gem", Material::isGemMaterial) {
        bucketVolume = 144
        typePriority = 2
    }
    @JvmField
    val INGOT = itemType("ingot", Material::isIngotMaterial) {
        bucketVolume = 144
        typePriority = 2
    }
    @JvmField
    val NUGGET = itemType("nugget", Material::isIngotMaterial) {
        bucketVolume = 16
    }
    @JvmField
    val BLOCK = blockType("storage_block", { it.isItemMaterial && it.blockCompaction != BlockCompaction.NONE }) {
        registryName = { ResourceLocation("versatile:${it.name}_block") }
        burnTime = { it.standardBurnTime * 10 }
        bucketVolume = 1296
    }
    @JvmField
    val ORE = blockType("ore", Material::hasOre) {
        blockProperties = { Block.Properties.create(BlockMaterial.ROCK).sound(SoundType.STONE).fromTier(it.harvestTier).harvestTool(ToolType.PICKAXE) }
        color = Material::unrefinedColor
        indexBlackList += 1
        bucketVolume = 144
        burnTime = { 0 }
    }
    @JvmField
    val FLUID = fluidType("fluid", Material::isFluidMaterial) {
        registryName = { ResourceLocation("versatile:${it.name}") }
        fluidTagName = "forge:"
        itemTagName = "forge:buckets"
        fluidAttributes = { mat ->
            MaterialFluidAttributes.Builder(mat, this, "minecraft:block/water".toResLoc()).apply {
                overlay("minecraft:block/water_overlay".toResLoc())
                if (isGas(mat)) gaseous()
            }
        }
    }
    @JvmField
    val MOLTEN_FLUID = fluidType("molten", { it.isItemMaterial && it.fluidTemperature > 0 }) {
        registryName = { ResourceLocation("versatile:molten_${it.name}") }
        fluidAttributes = { mat ->
            ObjTypeProperties.FLUID_ATTRIBUTES.default(this)(mat).sound(SoundEvents.ITEM_BUCKET_FILL_LAVA, SoundEvents.ITEM_BUCKET_EMPTY_LAVA)
        }
    }
}