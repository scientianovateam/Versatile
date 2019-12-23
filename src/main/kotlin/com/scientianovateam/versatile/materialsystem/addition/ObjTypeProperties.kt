package com.scientianovateam.versatile.materialsystem.addition

import com.scientianovateam.versatile.common.extensions.json
import com.scientianovateam.versatile.common.extensions.toResLoc
import com.scientianovateam.versatile.fluids.IFluidPairHolder
import com.scientianovateam.versatile.fluids.MaterialFluidAttributes
import com.scientianovateam.versatile.materialsystem.main.Material
import com.scientianovateam.versatile.materialsystem.properties.HarvestTier
import com.scientianovateam.versatile.materialsystem.properties.ObjTypeProperty
import com.scientianovateam.versatile.materialsystem.properties.merge
import com.google.gson.JsonObject
import com.scientianovateam.versatile.Versatile
import net.minecraft.block.Block
import net.minecraft.block.SoundType
import net.minecraft.item.Item
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.ToolType
import net.minecraftforge.fluids.FluidAttributes
import net.minecraft.block.material.Material as BlockMaterial

object ObjTypeProperties {
    @JvmStatic
    val BUCKET_VOLUME = ObjTypeProperty("versatile:bucket_volume".toResLoc(), ::merge) { 0 }
    @JvmStatic
    val REGISTRY_NAME_FUM = ObjTypeProperty<(Material) -> ResourceLocation>("versatile:registry_name_fun".toResLoc(), ::merge) { type ->
        { mat -> "versatile:${mat.name}_${type.name}".toResLoc() }
    }
    @JvmStatic
    val ITEM_TAG = ObjTypeProperty("versatile:item_tag".toResLoc(), ::merge) { type -> "forge:${type}s" }
    @JvmStatic
    val BLOCK_TAG = ObjTypeProperty("versatile:block_tag".toResLoc(), ::merge) { type -> "forge:${type}s" }
    @JvmStatic
    val FLUID_TAG = ObjTypeProperty("versatile:fluid_tag".toResLoc(), ::merge) { type -> "forge:${type}_" }
    @JvmStatic
    val COLOR_FUN = ObjTypeProperty<(Material) -> Int>("versatile:color_fun".toResLoc(), ::merge) { Material::color }
    @JvmStatic
    val DENSITY_MULTIPLIER = ObjTypeProperty("versatile:density_multiplier".toResLoc(), ::merge) { 1f }
    @JvmStatic
    val IS_GAS = ObjTypeProperty<(Material) -> Boolean>("versatile:is_gas".toResLoc(), ::merge) { Material::isGas }
    @JvmStatic
    val TEMPERATURE = ObjTypeProperty<(Material) -> Int>("versatile:temperature".toResLoc(), ::merge) { { mat -> mat.fluidTemperature.let { if (it > 0) it else 300 } } }
    @JvmStatic
    val SINGLE_TEXTURE_TYPE = ObjTypeProperty("versatile:single_texture_type".toResLoc(), ::merge) { false }
    @JvmStatic
    val BURN_TIME = ObjTypeProperty<(Material) -> Int>("versatile:burn_time".toResLoc(), ::merge) { type -> { mat -> (mat.standardBurnTime * (type.bucketVolume / 144f)).toInt() } }
    @JvmStatic
    val ITEM_MODEL = ObjTypeProperty<(Material) -> JsonObject>("versatile:item_model".toResLoc(), ::merge) { type ->
        { mat ->
            json {
                "parent" to "versatile:item/materialitems/" + (if (type.singleTextureType) "" else "${mat.textureType}/") + type.name
            }
        }
    }
    @JvmStatic
    val BLOCKSTATE_JSON = ObjTypeProperty<(Material) -> JsonObject>("versatile:blockstate_json".toResLoc(), ::merge) { type ->
        { mat ->
            json {
                "variants" {
                    "" {
                        "model" to "versatile:block/materialblocks/" + (if (type.singleTextureType) "" else "${mat.textureType}/") + type.name
                    }
                }
            }
        }
    }
    @JvmStatic
    val ITEM_CONSTRUCTOR = ObjTypeProperty<((Material) -> Item)?>("versatile:item_constructor".toResLoc(), ::merge) { null }
    @JvmStatic
    val ITEM_PROPERTIES = ObjTypeProperty<(Material) -> Item.Properties>("versatile:item_properties".toResLoc(), ::merge) { { Item.Properties().group(Versatile.MAIN) } }
    @JvmStatic
    val BLOCK_CONSTRUCTOR = ObjTypeProperty<((Material) -> Block)?>("versatile:block_constructor".toResLoc(), ::merge) { null }
    @JvmStatic
    val BLOCK_PROPERTIES = ObjTypeProperty<(Material) -> Block.Properties>("versatile:block_properties".toResLoc(), ::merge) {
        { mat -> Block.Properties.create(BlockMaterial.IRON).sound(SoundType.METAL).fromTier(mat.harvestTier).harvestTool(ToolType.PICKAXE) }
    }
    @JvmStatic
    val FLUID_CONSTRUCTOR = ObjTypeProperty<((Material) -> IFluidPairHolder)?>("versatile:fluid_constructor".toResLoc(), ::merge) { null }
    @JvmStatic
    val FLUID_ATTRIBUTES = ObjTypeProperty<(Material) -> FluidAttributes.Builder>("versatile:fluid_attributes".toResLoc(), ::merge) { type ->
        { mat ->
            MaterialFluidAttributes.Builder(mat, type, "versatile:fluid/${type.name}".toResLoc()).apply { if (type.isGas(mat)) gaseous() }
        }
    }
    @JvmStatic
    val TYPE_PRIORITY = ObjTypeProperty("versatile:type_priority".toResLoc(), ::merge) { 0 }
}

fun Block.Properties.fromTier(tier: HarvestTier): Block.Properties = this.hardnessAndResistance(tier.hardness, tier.resistance).harvestLevel(tier.harvestLevel)