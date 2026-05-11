package observer

interface Observer<T> {

    fun onChanged(newValue: T)
}