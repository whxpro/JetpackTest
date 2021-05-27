package com.whx.jetpacktest

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun test() = runBlocking {
        println("thread1: ${Thread.currentThread().name}")
        launch {
            println("thread2: ${Thread.currentThread().name}")
            (1..5).forEach {
                println("I'm not blocked $it")
//                delay(200)
                Thread.sleep(200)
            }
        }
        simple().collect { println(it) }
    }

    private fun simple(): Flow<Int> = flow {
        println("thread3: ${Thread.currentThread().name}")
        (1..30).forEach {
            delay(200)
//            Thread.sleep(200)
            emit(it)
        }
    }
}
