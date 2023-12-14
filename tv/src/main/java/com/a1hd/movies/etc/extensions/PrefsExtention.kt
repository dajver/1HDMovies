@file:Suppress("FunctionName", "PackageDirectoryMismatch")

package com.a1hd.movies.etc.extensions

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun SharedPreferences.getStr(key: String, defValue: String): String = getString(key, defValue)!!

@SuppressLint("ApplySharedPref")
fun SharedPreferences.edit(
    commitNow: Boolean = false,
    action: SharedPreferences.Editor.() -> Unit
) {
    edit().apply(action).apply {
        if (commitNow) {
            commit()
        } else {
            apply()
        }
    }
}

fun SharedPreferences.remove(key: String) = edit { remove(key) }

@JvmName("putNonNullString")
fun SharedPreferences.putString(key: String, value: String) = edit { putString(key, value) }

fun SharedPreferences.putInt(key: String, value: Int) = edit { putInt(key, value) }
fun SharedPreferences.putLong(key: String, value: Long) = edit { putLong(key, value) }
fun SharedPreferences.putFloat(key: String, value: Float) = edit { putFloat(key, value) }
fun SharedPreferences.putBoolean(key: String, value: Boolean) = edit { putBoolean(key, value) }

fun SharedPreferences.putString(key: String, value: String?) = edit {
    if (value != null) {
        putString(key, value)
    } else {
        remove(key)
    }
}

fun SharedPreferences.putInt(key: String, value: Int?) = edit {
    if (value != null) {
        putInt(key, value)
    } else {
        remove(key)
    }
}

fun SharedPreferences.putLong(key: String, value: Long?) = edit {
    if (value != null) {
        putLong(key, value)
    } else {
        remove(key)
    }
}

fun SharedPreferences.putFloat(key: String, value: Float?) = edit {
    if (value != null) {
        putFloat(key, value)
    } else {
        remove(key)
    }
}

fun SharedPreferences.putBoolean(key: String, value: Boolean?) = edit {
    if (value != null) {
        putBoolean(key, value)
    } else {
        remove(key)
    }
}

fun SharedPreferences.nullableString(key: String) = object : ReadWriteProperty<Any?, String?> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): String? = getString(key, null)
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: String?) =
        putString(key, value)
}

fun SharedPreferences.nullableString() = object : ReadWriteProperty<Any?, String?> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): String? =
        getString(property.name, null)

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: String?) =
        putString(property.name, value)
}

fun SharedPreferences.string(key: String, defValue: String) =
    object : ReadWriteProperty<Any?, String> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): String = getStr(key, defValue)
        override fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
            putString(key, value)
        }
    }

fun SharedPreferences.string(defValue: String) = object : ReadWriteProperty<Any?, String> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): String =
        getStr(property.name, defValue)

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: String) =
        putString(property.name, value)
}

fun SharedPreferences.int(key: String, defValue: Int = 0) = object : ReadWriteProperty<Any?, Int> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Int = getInt(key, defValue)
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) = putInt(key, value)
}

fun SharedPreferences.int(defValue: Int = 0) = object : ReadWriteProperty<Any?, Int> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Int =
        getInt(property.name, defValue)

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) =
        putInt(property.name, value)
}

fun SharedPreferences.long(key: String, defValue: Long = 0L) =
    object : ReadWriteProperty<Any?, Long> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): Long = getLong(key, defValue)
        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Long) =
            putLong(key, value)
    }

fun SharedPreferences.long(defValue: Long = 0L) = object : ReadWriteProperty<Any?, Long> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Long =
        getLong(property.name, defValue)

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Long) =
        putLong(property.name, value)
}

fun SharedPreferences.float(key: String, defValue: Float = 0f) =
    object : ReadWriteProperty<Any?, Float> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): Float =
            getFloat(key, defValue)

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Float) =
            putFloat(key, value)
    }

fun SharedPreferences.float(defValue: Float = 0f) = object : ReadWriteProperty<Any?, Float> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Float =
        getFloat(property.name, defValue)

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Float) =
        putFloat(property.name, value)
}

fun SharedPreferences.boolean(
    key: String,
    defValue: Boolean,
    liveData: MutableLiveData<Boolean>? = null
) =
    object : ReadWriteProperty<Any?, Boolean> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): Boolean =
            getBoolean(key, defValue)

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Boolean) {
            putBoolean(key, value)
            liveData?.postValue(value)
        }
    }

fun SharedPreferences.boolean(defValue: Boolean) = object : ReadWriteProperty<Any?, Boolean> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Boolean =
        getBoolean(property.name, defValue)

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Boolean) =
        putBoolean(property.name, value)
}

fun SharedPreferences.getDate(key: String, defValue: Date): Date = Date(getLong(key, defValue.time))
fun SharedPreferences.putDate(key: String, value: Date) = putLong(key, value.time)

fun SharedPreferences.date(key: String, defValue: Date) = object : ReadWriteProperty<Any?, Date> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Date = getDate(key, defValue)
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Date) = putDate(key, value)
}

fun SharedPreferences.date(defValue: Date) = object : ReadWriteProperty<Any?, Date> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Date =
        getDate(property.name, defValue)

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Date) =
        putDate(property.name, value)
}

inline fun <reified T> SharedPreferences.getJson(key: String, gson: Gson): T? =
    gson.fromJson(getStr(key, ""), T::class.java)

inline fun <reified T> SharedPreferences.putJson(key: String, gson: Gson, value: T) =
    putString(key, gson.toJson(value))

inline fun <reified T> SharedPreferences.json(key: String, gson: Gson, defValue: T) =
    object : ReadWriteProperty<Any?, T> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): T =
            getJson(key, gson) ?: defValue

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) =
            putJson(key, gson, value)
    }

inline fun <reified T> SharedPreferences.json(gson: Gson, defValue: T) =
    object : ReadWriteProperty<Any?, T> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): T =
            getJson(property.name, gson)
                ?: defValue

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) =
            putJson(property.name, gson, value)
    }

inline fun <reified T> SharedPreferences.json(
    key: String,
    gson: Gson,
    crossinline defValue: () -> T
) = object : ReadWriteProperty<Any?, T> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): T = getJson(key, gson)
        ?: defValue()

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) =
        putJson(key, gson, value)
}

inline fun <reified T> SharedPreferences.mutableList(key: String, gson: Gson, defValue: String) =
    object : ReadWriteProperty<Any?, MutableList<T>> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): MutableList<T> =
            getList(key, gson, defValue) ?: mutableListOf()

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: MutableList<T>) =
            putList(key, gson, value)
    }

inline fun <reified T> SharedPreferences.json(gson: Gson, crossinline defValue: () -> T) =
    object : ReadWriteProperty<Any?, T> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): T =
            getJson(property.name, gson)
                ?: defValue()

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) =
            putJson(property.name, gson, value)
    }

inline fun <reified E : Enum<E>> SharedPreferences.enum(key: String, defValue: E) =
    object : ReadWriteProperty<Any?, E> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): E =
            enumValues<E>()[getInt(key, defValue.ordinal)]

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: E) =
            putInt(key, value.ordinal)
    }

inline fun <reified E : Enum<E>> SharedPreferences.enum(
    key: String,
    defValue: E,
    liveData: MutableLiveData<E>? = null
) =
    object : ReadWriteProperty<Any?, E> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): E =
            enumValues<E>()[getInt(key, defValue.ordinal)]

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: E) {
            putInt(key, value.ordinal)
            liveData?.postValue(value)
        }
    }

inline fun <reified E : Enum<E>> SharedPreferences.enum(defValue: E) =
    object : ReadWriteProperty<Any?, E> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): E =
            enumValues<E>()[getInt(property.name, defValue.ordinal)]

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: E) =
            putInt(property.name, value.ordinal)
    }

fun <T> SharedPreferences.putList(spListKey: String, gson: Gson, list: List<T>?) {
    val listJson = gson.toJson(list)
    putString(spListKey, listJson)
}

inline fun <reified T> SharedPreferences.getList(spListKey: String, gson: Gson, defValue: String): MutableList<T>? {
    val listJson = getString(spListKey, defValue)
    return if (!listJson.isNullOrEmpty()) {
        val type = object : TypeToken<MutableList<T>>() {}.type
        gson.fromJson(listJson, type)
    } else {
        null
    }
}
