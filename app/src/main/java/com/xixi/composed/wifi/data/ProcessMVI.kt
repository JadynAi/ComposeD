package com.xixi.composed.wifi.data


/**
 *Jairett since 2022/2/7
 *
 * Use an abstract data structure to describe a non-immediate behavioral interaction
 *
 * You can use overloaded symbols to determine the state：
 * @see [Success] > [Error] > [Init] > [Uninitialized]
 */
sealed class Process<out D>(val complete: Boolean, private val value: D?, val compareV: Int) :
    Comparable<Process<*>> {

    val isSuccess: Boolean get() = value is Process.Success<*>

    val isFailure: Boolean get() = value is Process.Error<*>

    override fun compareTo(other: Process<*>): Int {
        return compareValues(compareV, other.compareV)
    }

    infix fun <D> until(p: Process<D>): IntRange {
        return this.compareV until p.compareV
    }

    object Uninitialized : Process<Nothing>(false, null, 0)

    object Init : Process<Nothing>(false, null, 1)

    data class Error<out D>(val error: Throwable, val value: D? = null) :
        Process<D>(false, value, 2)

    data class Success<out D>(val value: D) : Process<D>(true, value, 3)
}

inline fun <T> Process<T>.onSuccess(action: (value: T) -> Unit): Process<T> {
    if (this is Process.Success) {
        action.invoke(value)
    }
    return this
}

inline fun <T> Process<T>.onError(action: (error: Throwable) -> Unit): Process<T> {
    if (this is Process.Error) {
        action.invoke(error)
    }
    return this
}

sealed class Async<out D>(val complete: Boolean, private val value: D?, val compareV: Int) {
    companion object {
        const val VALUE_UNINITIALIZED = 0
        const val VALUE_INIT = 1
        const val VALUE_LOADING = 2
        const val VALUE_ERROR = 3
        const val VALUE_SUCCEED = 4
    }

    object Uninitialized : Async<Nothing>(false, null, VALUE_UNINITIALIZED)

    object Init : Async<Nothing>(false, null, VALUE_INIT)

    data class Loading<out D>(val value: D? = null) : Async<D>(false, null, VALUE_LOADING){
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Loading<*>

            if (value != other.value) return false

            return true
        }

        override fun hashCode(): Int {
            return value?.hashCode() ?: 0
        }
    }

    data class Error<out D>(val error: Throwable, val value: D? = null) : Async<D>(true, value, VALUE_ERROR){
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Error<*>

            if (error != other.error) return false
            if (value != other.value) return false

            return true
        }

        override fun hashCode(): Int {
            var result = error.hashCode()
            result = 31 * result + (value?.hashCode() ?: 0)
            return result
        }
    }

    data class Success<out D>(val value: D) : Async<D>(true, value, VALUE_SUCCEED){
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Success<*>

            if (value != other.value) return false

            return true
        }

        override fun hashCode(): Int {
            return value?.hashCode() ?: 0
        }
    }
}