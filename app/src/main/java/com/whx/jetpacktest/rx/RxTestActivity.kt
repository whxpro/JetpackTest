package com.whx.jetpacktest.rx

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.whx.jetpacktest.R
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class RxTestActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "RxTestActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_rxtest)

        Observable.create<Int> { emitter ->
            Log.d(TAG, "emit 1")
            emitter.onNext(1)
            Log.d(TAG, "emit 2")
            emitter.onNext(2)
            Log.d(TAG, "emit 3")
            emitter.onNext(3)
            Log.d(TAG, "emit complete")
            emitter.onComplete()
            Log.d(TAG, "emit 4")
            emitter.onNext(4)
        }.subscribe(object : Observer<Int> {
            private var mDisposable: Disposable? = null

            override fun onSubscribe(d: Disposable) {
                Log.w(TAG, "subscribe")
                mDisposable = d
            }

            override fun onNext(t: Int) {
                Log.w(TAG, "value: $t")
                if (t == 2) {
                    Log.d(TAG, "dispose")
                    mDisposable?.dispose()
                    Log.d(TAG, "isDisposed : " + mDisposable?.isDisposed)
                }
            }

            override fun onError(e: Throwable) {
                Log.w(TAG, e.message ?: "error")
            }

            override fun onComplete() {
                Log.w(TAG, "complete")
            }
        })
    }

}