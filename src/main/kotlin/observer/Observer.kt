package observer

fun interface Observer<T> {

    fun onChanged(newValue: T)
}