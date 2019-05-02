package com.EmosewaPixel.pixellib.materialSystem.lists;

import com.EmosewaPixel.pixellib.materialSystem.materials.IMaterialItem;
import com.EmosewaPixel.pixellib.materialSystem.materials.Material;
import com.EmosewaPixel.pixellib.materialSystem.types.ObjectType;
import com.google.common.collect.HashBasedTable;
import net.minecraft.item.Item;

import java.util.Collection;

public class MaterialItems {
    private static HashBasedTable<Material, ObjectType, Item> materialItems = HashBasedTable.create();

    public static Item getItem(Material material, ObjectType type) {
        return materialItems.get(material, type);
    }

    public static boolean contains(Material material, ObjectType type) {
        return getItem(material, type) != null;
    }

    public static void addItem(Material mat, ObjectType type, Item item) {
        materialItems.put(mat, type, item);
    }

    public static void addItem(IMaterialItem item) {
        if (item instanceof Item)
            addItem(item.getMaterial(), item.getObjType(), (Item) item);
    }

    public static Collection<Item> getAllItems() {
        return materialItems.values();
    }
}