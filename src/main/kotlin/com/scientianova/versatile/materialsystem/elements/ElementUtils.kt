package com.scientianova.versatile.materialsystem.elements

import com.scientianova.versatile.materialsystem.addition.BaseElements
import com.scientianova.versatile.materialsystem.properties.CompoundType
import com.scientianova.versatile.materialsystem.main.Material
import com.scientianova.versatile.materialsystem.main.Form

//This class contains functions used for determining the elemental properties of compounds
object ElementUtils {
    @JvmStatic
    fun getElementalComposition(mat: Material): List<ElementStack> {
        if (mat.composition.isEmpty())
            return listOf(mat.element.toStack())

        val map = mat.fullComposition.groupBy { it.material.element }.mapValues { (_, value) -> value.map { it.count }.sum() }

        return if (BaseElements.NULL in map) listOf(ElementStack.EMPTY) else map.entries.map { it.key * it.value }
    }

    @JvmStatic
    fun getTotalProtons(mat: Material) = getElementalComposition(mat).map { (element, count) -> element.protons * count }.sum()

    @JvmStatic
    fun getTotalNeutrons(mat: Material) = getElementalComposition(mat).map { (element, count) -> element.neutrons * count }.sum()

    @JvmStatic
    fun getMolarMass(mat: Material) = getElementalComposition(mat).map { (element, count) -> element.atomicMass * count }.sum()

    @JvmStatic
    fun getTotalDensity(mat: Material, type: Form): Double {
        val list = getElementalComposition(mat).map { (element, count) -> element.density * count }
        return (if (mat.compoundType === CompoundType.CHEMICAL) list.sum() else list.average()) * type.densityMultiplier * mat.densityMultiplier
    }
}