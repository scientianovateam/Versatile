package com.scientianova.versatile.machines.properties

import com.scientianova.versatile.machines.BaseTileEntity
import com.scientianova.versatile.machines.gui.BaseContainer
import com.google.common.reflect.MutableTypeToInstanceMap
import net.minecraft.nbt.CompoundNBT
import net.minecraftforge.common.capabilities.ICapabilityProvider
import net.minecraftforge.common.util.INBTSerializable

interface ITEBoundProperty : IMachineProperty, INBTSerializable<CompoundNBT> {
    val id: String
    val te: BaseTileEntity
    fun update() {}
    fun tick() {}
    fun detectAndSendChanges(container: BaseContainer)
    fun addCapability(map: MutableTypeToInstanceMap<ICapabilityProvider>) {}
    fun clear() {}
}