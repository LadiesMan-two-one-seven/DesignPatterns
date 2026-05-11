package users

import java.awt.Dimension
import java.awt.Font
import java.awt.Insets
import javax.swing.JFrame
import javax.swing.JScrollPane
import javax.swing.JTextArea

class Display2 {

    fun show() {
        val textArea = JTextArea().apply {
            isEditable = false
            font = Font(Font.SERIF, Font.PLAIN, 20)
            margin = Insets(32, 32, 32, 32)
        }
        val scrollPane = JScrollPane(textArea)
        JFrame().apply {
            isVisible = true
            size = Dimension(800, 600)
            isResizable = false
            add(scrollPane)
        }
        UsersRepository2.getInstance("qwerty").registerObserver {
            textArea.text = it.joinToString("\n")
        }
    }
}