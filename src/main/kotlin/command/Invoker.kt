package command

interface Invoker<T: Command> {

    fun addCommand(command: T)
}