package com.whx.jetpacktest.mvi

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.reflect.KProperty1

/**
 * 监听一个属性
 */
fun <T, A> LiveData<T>.observeState(lifecycleOwner: LifecycleOwner, prop1: KProperty1<T, A>, action: (A) -> Unit) {
    this.map {
        StateTuple1(prop1.get(it))
    }.distinctUntilChanged().observe(lifecycleOwner, Observer { (a) ->
        action.invoke(a)
    })
}

/**
 * 监听两个属性
 */
fun <T, A, B> LiveData<T>.observeState(
    lifecycleOwner: LifecycleOwner,
    prop1: KProperty1<T, A>,
    prop2: KProperty1<T, B>,
    action: (A, B) -> Unit
) {
    this.map {
        StateTuple2(prop1.get(it), prop2.get(it))
    }.distinctUntilChanged().observe(lifecycleOwner, Observer { (a, b) ->
        action.invoke(a, b)
    })
}

/**
 * 更新State
 */
fun <T> MutableLiveData<T>.setState(reducer: T.() -> T) {
    this.value = this.value?.reducer()
}

fun <T> MutableLiveData<T>.postState(reducer: T.() -> T) {
    postValue(this.value?.reducer())
}

/**
 * 封装 flow collect 方法<br/>
 * <B>注意：</B>此方法会新启一个协程
 */
inline fun <T> Flow<T>.launchAndCollectIn(
    owner: LifecycleOwner,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline action: suspend CoroutineScope.(T) -> Unit
) = owner.lifecycleScope.launch {
    owner.repeatOnLifecycle(minActiveState) {
        collect { action(it) }
    }
}

data class StateTuple1<A>(val a: A)
data class StateTuple2<A, B>(val a: A, val b: B)