package com.kotlin.fazal.kotlindemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.toObservable
import rx.Observable
import kotlinx.android.synthetic.main.activity_main.*
import rx.Subscription

/***
 * MainActivity is a Demo Activity for beginners who have recently shift in Kotlin.
 * MainActivity just simply provide a demo for rx-kotlin (reactivex programming)
 *
 * @see <a href="http://reactivex.io/documentation">ReactiveX Documentation</a>
 * @see <a href="https://www.youtube.com/watch?v=7IEPrihz1-E&t=554s">ReactiveX introductory video in java</a>
 *
 * @author Fazal Hussain
 */

class MainActivity : AppCompatActivity() {

    private lateinit var subscription: Subscription

    /***
     * Companion object is an object which is used to make variable & function static.
     * In Kotlin static keyword is not allowed. If you want to use static
     * method use companion object.
     *
     * Companion object is also like static initializer in Java.
     * If you want to use companion object variable in Java use anotation JvmStatic
     *
     * @see JvmStatic
     *
     */
    companion object {
        private val TAG: String? = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Search Button Click listener _ is used for unused parameter that is view.
        searchBtn.setOnClickListener { _ ->
            if (!inputEt.text.isEmpty()) {
                tv.text = ""
                //Subscribe the observable When the data receive {@link onNext} call
                //when there is an error onError call. onComplete call 1 time when all the data recieved.
                getDataObservable()
                        .subscribeBy(
                                onNext = { item -> tv.append(item + "\n") },
                                onComplete = { Log.d(TAG, "Completed") },
                                onError = { }
                        )
            }

        }

    }

    /***
     * Using Filter to emit only those items from an Observable that pass a predicate test
     *
     * @see Observable
     *
     * @see <a href="http://reactivex.io/documentation/operators/defer.html">ReactiveX operators documentation: Defer</a>
     * @see <a href="http://reactivex.io/documentation/operators/just.html">ReactiveX operators documentation: Just</a>
     */
    private fun getDataObservable(): io.reactivex.Observable<String> {
        return getData().toList().toObservable().filter { item ->
            item.toLowerCase().contains(inputEt.text)
        }
    }

    /***
     * Fetch the collection of data by creating an array of String
     * and initialize an array with some values.
     *
     * @See arrayOf
     *
     * @return the array of String.
     */
    private fun getData(): Array<String> {
        return arrayOf("Fazal", "Faz", "Ali", "Fa", "Raza")
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!subscription.isUnsubscribed) {
            subscription.unsubscribe()
        }
    }

}

