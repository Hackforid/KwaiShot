package com.ushowmedia.mipha.hyrule.utils.ext

/**
 * Created by quan.zhou on 2017/9/7.
 */
fun <T> List<T>?.isNullOrEmpty() = this == null || this.isEmpty()

fun <T> Array<out T>?.isNullOrEmpty(): Boolean = this == null || this.isEmpty()

fun <T> List<T>?.haveItem(predicate: (T) -> Boolean): Boolean {
    if (this.isNullOrEmpty()) {
        return false
    }
    this?.find(predicate)?.let {
        return true
    }
    return false
}