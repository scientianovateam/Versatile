package com.scientianova.versatile.materialsystem.lists

import com.scientianova.versatile.materialsystem.main.ObjectType

object ObjTypes {
    private val objTypes = hashMapOf<String, ObjectType>()

    @JvmStatic
    val all  get() = objTypes.values

    @JvmStatic
    fun add(type: ObjectType) = objTypes[type.name]?.merge(type) ?: objTypes.put(type.name, type)

    @JvmStatic
    operator fun get(name: String): ObjectType? = objTypes[name]

    @JvmStatic
    operator fun contains(name: String) = name in objTypes
}