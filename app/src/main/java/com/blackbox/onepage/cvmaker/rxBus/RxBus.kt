package com.blackbox.onepage.cvmaker.rxBus

/**
 * Created by umair on 06/06/2017.
 */
import rx.Observable
import rx.subjects.PublishSubject
import rx.subjects.SerializedSubject
import rx.subjects.Subject

class RxBus {

    //private final PublishSubject<Object> _bus = PublishSubject.create();

    // If multiple threads are going to emit events to this
    // then it must be made thread-safe like this instead
    private val _bus = SerializedSubject(PublishSubject.create<Any>())

    fun send(o: Any) {
        _bus.onNext(o)
    }

    fun toObserverable(): Observable<Any> {
        return _bus
    }

    fun hasObservers(): Boolean {
        return _bus.hasObservers()
    }
}
