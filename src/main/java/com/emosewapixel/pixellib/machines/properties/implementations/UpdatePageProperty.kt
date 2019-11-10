package com.emosewapixel.pixellib.machines.properties.implementations

import com.emosewapixel.pixellib.extensions.nbt
import com.emosewapixel.pixellib.machines.gui.BaseContainer
import com.emosewapixel.pixellib.machines.packets.NetworkHandler
import com.emosewapixel.pixellib.machines.packets.ReopenGUIPacket
import com.emosewapixel.pixellib.machines.properties.ITEBoundProperty
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.nbt.CompoundNBT
import net.minecraftforge.fml.network.PacketDistributor

class UpdatePageProperty(override val id: String) : ITEBoundProperty {
    override fun detectAndSendChanges(container: BaseContainer) {
        if (container.guiPage != container.te.guiLayout.current)
            NetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with { container.playerInv.player as ServerPlayerEntity }, ReopenGUIPacket(container.te.pos, container.type))
    }

    override fun copy() = this

    override fun deserializeNBT(nbt: CompoundNBT?) {}

    override fun serializeNBT() = nbt { }
}