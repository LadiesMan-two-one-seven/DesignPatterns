package observer

import observer.Observer

interface Observable<T> {

    val currentValue: T
    val observers: List<Observer<T>>

    fun registerObserver(observer: Observer<T>)

    fun unregisterObserver(observer: Observer<T>)

    fun notifyObservers() {
        for (observer in observers) {
            observer.onChanged(currentValue)
        }
    }
}