package com.EmosewaPixel.pixellib.tiles.containers;

import com.EmosewaPixel.pixellib.tiles.AbstractRecipeBasedTE;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.*;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class RecipeBasedMachineContainer<T extends AbstractRecipeBasedTE> extends Container {
    protected T te;
    protected IItemHandler itemHandler;

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return te.canInteractWith(playerIn);
    }

    public RecipeBasedMachineContainer(PlayerInventory playerInventory, T te, ContainerType<?> type, int id) {
        super(type, id);
        this.te = te;

        te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> itemHandler = handler);

        addMachineSlots();

        addPlayerSlots(playerInventory);
    }

    protected void addMachineSlots() {
        for (int i = 0; i < te.getRecipeList().getMaxInputs(); i++)
            this.addSlot(new SlotItemHandler(itemHandler, i, te.getRecipeList().getMaxInputs() == 1 ? 56 : 38 + i * 18, 35));

        for (int i = 0; i < te.getRecipeList().getMaxOutputs(); i++)
            this.addSlot(new SlotItemHandler(itemHandler, te.getSlotCount() - i - 1, 116, te.getRecipeList().getMaxOutputs() == 1 ? 35 : 48 - i * 22));
    }

    private void addPlayerSlots(IInventory playerInventory) {
        for (int i = 0; i < 3; ++i)
            for (int j = 0; j < 9; ++j)
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));

        for (int k = 0; k < 9; ++k)
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack stack1 = slot.getStack();
            itemstack = stack1.copy();

            if (index < te.getSlotCount()) {
                if (!this.mergeItemStack(stack1, te.getSlotCount(), this.inventorySlots.size(), true))
                    return ItemStack.EMPTY;
            } else if (!this.mergeItemStack(stack1, 0, te.getSlotCount(), false))
                return ItemStack.EMPTY;

            if (stack1.isEmpty())
                slot.putStack(ItemStack.EMPTY);
            else
                slot.onSlotChanged();
        }

        return itemstack;
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (IContainerListener listener : listeners) {
            listener.sendWindowProperty(this, 0, te.getProgress());
            listener.sendWindowProperty(this, 1, te.getCurrentRecipe().getTime());
        }
    }

    @Override
    public void updateProgressBar(int id, int data) {
        switch (id) {
            case 0:
                te.setProgress(data);
                break;
            case 1:
                te.getCurrentRecipe().setTime(data);
                break;
        }
    }
}