import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.geom.Ellipse2D
import java.awt.geom.Rectangle2D
import java.awt.geom.RoundRectangle2D
import javax.swing.*
import javax.swing.border.Border
import javax.swing.text.SimpleAttributeSet
import javax.swing.text.StyleConstants
import javax.swing.text.StyledDocument

object morseKey {
    const val DIT:String = "\u2022"
    const val DAH:String = "\uFF0D"
    val MORSE_CODE: Map<Char, String> = mapOf(
        'A' to DIT+DAH,
        'B' to DAH+DIT+DIT+DIT,
        'C' to DAH+DIT+DAH+DIT,
        'D' to DAH+DIT+DIT,
        'E' to DIT,
        'F' to DIT+DIT+DAH+DIT,
        'G' to DAH+DAH+DIT,
        'H' to DIT+DIT+DIT+DIT,
        'I' to DIT+DIT,
        'J' to DIT+DAH+DAH+DAH,
        'K' to DAH+DIT+DAH,
        'L' to DIT+DAH+DIT+DIT,
        'M' to DAH+DAH,
        'N' to DAH+DIT,
        'O' to DAH+DAH+DAH,
        'P' to DIT+DAH+DAH+DIT,
        'Q' to DAH+DAH+DIT+DAH,
        'R' to DIT+DAH+DIT,
        'S' to DIT+DIT+DIT,
        'T' to DAH,
        'U' to DIT+DIT+DAH,
        'V' to DIT+DIT+DIT+DAH,
        'W' to DIT+DAH+DAH,
        'X' to DAH+DIT+DIT+DAH,
        'Y' to DAH+DIT+DAH+DAH,
        'Z' to DAH+DAH+DIT+DIT,
        '0' to DAH+DAH+DAH+DAH+DAH,
        '1' to DIT+DAH+DAH+DAH+DAH,
        '2' to DIT+DIT+DAH+DAH+DAH,
        '3' to DIT+DIT+DIT+DAH+DAH,
        '4' to DIT+DIT+DIT+DIT+DAH,
        '5' to DIT+DIT+DIT+DIT+DIT,
        '6' to DAH+DIT+DIT+DIT+DIT,
        '7' to DAH+DAH+DIT+DIT+DIT,
        '8' to DAH+DAH+DAH+DIT+DIT,
        '9' to DAH+DAH+DAH+DAH+DIT
    )
}

object myColors{
    val LIGHT_BORDER = Color.DARK_GRAY
    val LIGHT_OFF = Color(136, 75, 0)
    val LIGHT_ON = Color(255, 150, 27)
    val BACKGROUND = Color(232,220,184)
    val TEXT_BACKGROUND = Color(240,230,200)
}

fun encode(encode_input: String): List<List<String?>> {
    val words = encode_input.uppercase().split(" ")
    val encoded = emptyList<List<String?>>().toMutableList()
    for ( word in words ){
        val temp = emptyList<String?>().toMutableList()
        for( letter in word )
            temp.add( morseKey.MORSE_CODE[letter])
        encoded.add(temp.toList())
    }
    return encoded.toList()
}


fun decode(encoded_input: List<List<String?>>): String{
    val decoded = emptyList<String>().toMutableList()
    for (word in encoded_input){
        val temp = emptyList<Char>().toMutableList()
        for (letter in word)
            temp.add(morseKey.MORSE_CODE.filterValues { it == letter }.keys.first())
        decoded.add(temp.joinToString(separator = ""))
    }
    return decoded.joinToString(separator = " ")
}


class LightPanel() : JComponent(){

    val light = Ellipse2D.Double(this.x + 10.0, this.y + 10.0, 80.0, 80.0)
    val border = Ellipse2D.Double(this.x.toDouble(), this.y.toDouble(), 100.0, 100.0)
    var lightColor = myColors.LIGHT_OFF

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)
        val g2D = g as Graphics2D

        g2D.color = myColors.LIGHT_BORDER
        g2D.fill(border)

        g2D.color = lightColor
        g2D.fill(light)

    }

    override fun getPreferredSize(): Dimension {
        return Dimension(100,100)
    }
}

class GhostBox(): JComponent(){

    val border = Ellipse2D.Double(this.x.toDouble(), this.y.toDouble(), 50.0, 10.0)

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)
        val g2D = g as Graphics2D

        g2D.color = Color.red
        g2D.fill(border)
    }

    override fun getPreferredSize(): Dimension {
        return Dimension(50, 10)
    }
}
class TextBox() : JEditorPane(){
    init {
        this.isEditable = false
        this.background = myColors.TEXT_BACKGROUND
        this.font = Font("Bookman Old Style", Font.BOLD, 24)
    }

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)
    }

    override fun getPreferredSize(): Dimension {
        return Dimension(100,100)
    }
}

class Key( val value:String): JButton(value){
    init {
        //this.font = Font("Cambria", Font.BOLD, 40)
        this.isFocusable = false
        this.isBorderPainted = false
        this.background = null
    }

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)
        if (g != null) {
            if (value == " "){
                g.color = Color.darkGray
                g.fillOval(5, 5, 90, 90)
                g.color = Color.lightGray
                g.fillOval(10, 10, 80, 80)

                g.color = Color.darkGray
                g.fillOval(405, 5, 90, 90)
                g.color = Color.lightGray
                g.fillOval(410, 10, 80, 80)

                g.color = Color.lightGray
                g.fillRect(50, 10, 400, 80)

                g.color = Color.darkGray
                g.fillRect(50, 5, 400, 5)
                g.fillRect(50, 90, 400, 5)
            }
            else {
                g.color = Color.darkGray
                g.fillOval(5, 5, 90, 90)
                g.color = Color.lightGray
                g.fillOval(10, 10, 80, 80)
                g.color = Color.black
                g.font = Font("Bookman Old Style", Font.BOLD, 50)
                g.drawChars(this.value.toCharArray(), 0, 1, 35, 65)
            }
        }
    }
    override fun getPreferredSize(): Dimension {
        return Dimension(if (value == " ") 500 else 100,100)
    }
}

class EncodeButton(): JButton("Encode!"){

}

class GameFrame(title:String) : JFrame(title), ActionListener, KeyListener{

    //var timer:Timer = Timer(1000, this)
    val yBound = 1024.0
    val xBound = 1024.0
    //val gameLayout = GameGridBagLayout()
    val light = LightPanel()
    val textBox = TextBox()
    val constraints:GridBagConstraints = GridBagConstraints()

    val encodeButton = EncodeButton()

    val keys = listOf(
        listOf<Key>( Key("1"),Key("2"), Key("3"), Key("4"), Key("5"), Key("6"), Key("7"), Key("8"), Key("9"), Key("0")),
        listOf<Key>( Key("Q"),Key("W"), Key("E"), Key("R"), Key("T"), Key("Y"), Key("U"), Key("I"), Key("O"), Key("P")),
        listOf<Key>( Key("A"),Key("S"), Key("D"), Key("F"), Key("G"), Key("H"), Key("J"), Key("K"), Key("L")),
        listOf<Key>( Key("Z"),Key("X"), Key("C"), Key("V"), Key("B"), Key("N"), Key("M")),
        listOf<Key>( Key(" ")))

    val backSpace = Key("\u232B")

    init {
        this.contentPane.background = myColors.BACKGROUND
        this.setSize(xBound.toInt(), yBound.toInt())
        this.defaultCloseOperation = EXIT_ON_CLOSE

        layout = GridBagLayout()

        for( i in 0 until 20) {
            val gbox = GhostBox()
            println(i)
            addComponent(i, 0, gbox)
        }

        addComponent(5,2,textBox, 10, 10)

        encodeButton.addActionListener(this)
        addComponent(16,2,encodeButton, 2)

        val padding = arrayOf( 0, 0, 1, 3, 5)
        for (row in 0 until keys.size) {
            for(key in 0 until keys[row].size) {
                keys[row][key].addActionListener(this)
                val gx = ((0+key) * 2) + padding[row]
                val gy = 3+row
                print("(${gx}, ${gy}), ")
                addComponent( gx, gy, keys[row][key], if(keys[row][key].value == " ") 10 else 2)
            }
            println()
        }

        backSpace.addActionListener(this)
        addComponent(16, 7, backSpace, 2)

        constraints.anchor = GridBagConstraints.NORTHWEST
        addComponent(9,1,light, 2, 10)
        
        this.pack()
        this.isVisible = true
        this.isResizable = false
    }

    private fun addComponent(gridX: Int, gridY: Int, comp: JComponent, gridWidth: Int = 1, yPad: Int = 0){
        constraints.ipady = yPad
        constraints.gridwidth = gridWidth
        constraints.gridx = gridX
        constraints.gridy = gridY
        constraints.fill = GridBagConstraints.HORIZONTAL
        this.add(comp, constraints)
    }

    override fun actionPerformed(e: ActionEvent?) {
        if (e != null) {
            if (e.source == encodeButton) {
                val encoded = encode(textBox.text)
                println(encoded)
                displayMessage(encoded)
            }
            else if (keys.any{ it.any { it == e.source } }){
                val letter = keys.filter { it.any{ it == e.source} }.first().find { it == e.source }?.value
                textBox.text += letter
            }
            else if( e.source == backSpace)
                textBox.text = textBox.text.dropLast(1)
        }
    }

    override fun keyTyped(e: KeyEvent?) {
        if (e != null){
            println("e.source.toString()")
        }
    }

    override fun keyPressed(e: KeyEvent?) {
        TODO("Not yet implemented")
    }

    override fun keyReleased(e: KeyEvent?) {
        TODO("Not yet implemented")
    }

    fun displayMessage(message: List<List<String?>>){
        val myThread = Thread( Runnable {
            val unitLength: Long = 500
            for (word in message) {
                for (letter in word) {
                    if (letter != null) {
                        for (signal in letter) {
                            light.lightColor = myColors.LIGHT_ON
                            light.repaint()
                            if (signal.toString() == morseKey.DIT)
                                Thread.sleep(unitLength)
                            else
                                Thread.sleep( unitLength * 3)
                            light.lightColor = myColors.LIGHT_OFF
                            light.repaint()
                            Thread.sleep(unitLength)
                        }
                    }
                    Thread.sleep(unitLength * 2)
                }
                Thread.sleep(unitLength * 4)
            }
        })
        myThread.start()
    }
}

fun main(args: Array<String>) {
    println("test")


    val message = "Hello World"
    println(message)
    val inMorse = encode(message)
    println(inMorse)
    val backFromMorse = decode(inMorse)
    println(backFromMorse)


    val game = GameFrame("Morse Code")
    //game.displayMessage(inMorse)
}
