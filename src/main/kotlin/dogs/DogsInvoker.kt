package dogs

import command.Command
import command.Invoker
import java.util.concurrent.LinkedBlockingQueue
import kotlin.concurrent.thread

object DogsInvoker: Invoker<AdministratorCommand> {

    private val commands = LinkedBlockingQueue<Command>()

    override fun addCommand(command: AdministratorCommand) {
        println("New command: $command")
        commands.add(command)
    }

    init {
        thread {
            while (true) {
                println("Waiting...")
                val command = commands.take()
                println("Executing: $command...")
                command.execute()
                println("Executed: $command")
            }
        }
    }
}