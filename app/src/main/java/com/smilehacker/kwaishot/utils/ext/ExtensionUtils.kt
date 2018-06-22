package com.ushowmedia.mipha.hyrule.utils.ext

import android.app.Activity
import android.app.Notification
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.annotation.DrawableRes
import android.support.v4.app.Fragment
import android.support.v4.content.res.ResourcesCompat
import android.view.View
import android.view.inputmethod.InputMethodManager
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


/**
 * kotlin ext utils
 * Created by kleist on 16/8/3.
 */

fun String.isChinese(): Boolean {
    val c = this.first()
    return isChineseByBlock(c)
}

fun isChineseByBlock(c: Char): Boolean {
    val ub = Character.UnicodeBlock.of(c)
    return ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
            || ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
            || ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
//            || ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_C
//            || ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_D
            || ub === Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
            || ub === Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT
}


inline fun <T> T?.nullOr(value: T): T = if (this != null) this else value

fun <T> T.transform(action: (T) -> T): T = action(this)

fun <T, R> T.trans(action: (T) -> R): R = action(this)

fun Activity.showKeyboard(show: Boolean) {
    if (show) {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED)
    } else {
        var view = this.currentFocus
        if (view == null) {
            view = View(this)
        }
        (this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(view.windowToken, 0)

    }
}

fun Context?.isActivityFinished() : Boolean {
    this ?: return false
    if (this is Activity) {
        return this.isActivityFinish()
    } else {
        return true
    }
}

fun Activity.isActivityFinish(): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        return this.isFinishing || this.isDestroyed
    }
    return this.isFinishing
}


fun Notification.Builder.buildCompat(): Notification {
    if (Build.VERSION.SDK_INT < 16) {
        @Suppress("DEPRECATION")
        return this.notification
    } else {
        return this.build()
    }
}

fun Fragment.getDrawable(@DrawableRes resId: Int) = ResourcesCompat.getDrawable(this.resources, resId, this.context?.theme)

class LazyReadWriteProperty<T, V>(private val initializer: () -> ReadWriteProperty<T, V>)
    : ReadWriteProperty<T, V> {
    private object EMPTY

    private var mValue: Any = EMPTY

    override fun getValue(thisRef: T, property: KProperty<*>): V {
        if (mValue == EMPTY) {
            mValue = initializer()
        }
        return (mValue as ReadWriteProperty<T, V>).getValue(thisRef, property)
    }

    override fun setValue(thisRef: T, property: KProperty<*>, value: V) {
        if (mValue == EMPTY) {
            mValue = initializer()
        }
        (mValue as ReadWriteProperty<T, V>).setValue(thisRef, property, value)
    }
}

fun Boolean?.nullAsFalse(): Boolean {
    return if (this == null) false else this
}

fun <T> List<T>?.getElementIfNotNullOrEmpty(index: Int?): T? {
    if (this == null || this.isEmpty() || index == null || index >= this.size) {
        return null
    }
    return this[index]
}

fun <T> List<T>?.listEquals(list: List<T>?): Boolean {
    if (this == list) {
        return true
    }
    if (this == null || list == null) {
        return false
    }
    this!!
    list!!

    if (this.size != list.size) {
        return false
    }

    for (i in 0 until this.size) {
        if (this[i] != list[i]) {
            return false
        }
    }

    return true
}

fun <T> simpleLazy(initializer: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE, initializer)

fun Intent.readMaybeAsLong(key: String, default: Long): Long {
    val idStr = getStringExtra(key)
    return if (idStr.isNullOrEmpty()) {
        getLongExtra(key, default)
    } else {
        idStr.toLong()
    }
}
