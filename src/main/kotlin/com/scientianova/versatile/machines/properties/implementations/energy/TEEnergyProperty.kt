package com.scientianova.versatile.machines.properties.implementations.energy

import com.scientianova.versatile.common.extensions.getOrAddInstance
import com.scientianova.versatile.common.extensions.nbt
import com.scientianova.versatile.machines.BaseTileEntity
import com.scientianova.versatile.machines.capabilities.energy.EnergyCapabilityWrapper
import com.scientianova.versatile.machines.capabilities.energy.EnergyHandler
import com.scientianova.versatile.machines.capabilities.energy.IEnergyStorageModifiable
import com.scientianova.versatile.machines.gui.BaseContainer
import com.scientianova.versatile.machines.packets.NetworkHandler
import com.scientianova.versatile.machines.packets.primitives.UpdateIntPacket
import com.scientianova.versatile.machines.properties.ITEBoundProperty
import com.scientianova.versatile.machines.properties.IValueProperty
import com.google.common.reflect.MutableTypeToInstanceMap
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.nbt.CompoundNBT
import net.minecraftforge.common.capabilities.ICapabilityProvider
import net.minecraftforge.fml.network.NetworkDirection

open class TEEnergyProperty(override val value: EnergyHandler, override val id: String, override val te: BaseTileEntity) : IValueProperty<IEnergyStorageModifiable>, ITEBoundProperty {
    constructor(capacity: Int, id: String, te: BaseTileEntity) : this(object : EnergyHandler(capacity) {
        override fun onUpdate() {
            te.update()
            te.markDirty()
        }
    }, id, te)

    override fun clone(): TEEnergyProperty {
        val handler = EnergyHandler(value.maxEnergyStored)
        handler.energyStored = value.energyStored
        return TEEnergyProperty(handler, id, te)
    }

    override fun detectAndSendChanges(container: BaseContainer) {
        if ((container.clientProperties[id] as TEEnergyProperty).value.energyStored != value.energyStored) {
            NetworkHandler.CHANNEL.sendTo(UpdateIntPacket(id, value.energyStored), (container.playerInv.player as ServerPlayerEntity).connection.networkManager, NetworkDirection.PLAY_TO_CLIENT)
            (container.clientProperties[id] as TEEnergyProperty).value.energyStored = value.energyStored
        }
    }

    override fun deserializeNBT(nbt: CompoundNBT?) = value.deserializeNBT(nbt?.getCompound(id))

    override fun serializeNBT() = nbt {
        id to value.serializeNBT()
    }

    override fun addCapability(map: MutableTypeToInstanceMap<ICapabilityProvider>) {
        map.getOrAddInstance(EnergyCapabilityWrapper::class.java, EnergyCapabilityWrapper()).addHandler(value)
    }
}