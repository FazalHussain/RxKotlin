package com.example.fazal.kotlindemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.fazal.kotlindemo.rxjava.Person
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

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

        //Subscribe the observable in the worker thread by default it is in UI {@link Thread}
        //observe the data on the main thread. When the data receive {@link onNext} call
        //when there is an error onError call. onComplete call 1 time when all the data recieved.
        getDataObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        object : Subscriber<List<Person>>() {
                            override fun onCompleted() {

                            }

                            override fun onError(e: Throwable?) {
                                Log.d(TAG, "onError" + e?.message)
                            }

                            override fun onNext(t: List<Person>?) {
                                //For each loop to iterate a collection of data with null safety check
                                t?.forEach { item ->
                                    Log.d(TAG, item.name.plus("-").plus(item.age))
                                    tv.text = item.name
                                }
                            }
                        })
    }

    /***
     * Using the just operator convert a list into an observable
     * that emits that collection of item.
     *
     * Defer operator restrict to create the Observable until the observer subscribes,
     * and create a fresh Observable for each observer
     *
     * @see Observable
     * @see List
     * @see Person
     *
     * @see <a href="http://reactivex.io/documentation/operators/defer.html">ReactiveX operators documentation: Defer</a>
     * @see <a href="http://reactivex.io/documentation/operators/just.html">ReactiveX operators documentation: Just</a>
     */
    private fun getDataObservable(): Observable<List<Person>> {
        return Observable.defer { Observable.just(getData()) }
    }

    /***
     * Fetch the list of data by creating a mutable list
     * and add 5 Person item into list then return the list.
     *
     * @See mutableListOf
     *
     * @return the collection of Person items.
     */
    private fun getData(): List<Person> {
        val list = mutableListOf<Person>()
        for (i in 1..5) {
            list.add(Person("Fazal", i))
        }

        return list
    }

}

