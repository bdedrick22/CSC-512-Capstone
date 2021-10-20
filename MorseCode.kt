import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.geom.Ellipse2D
import javax.swing.*

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
class TextBox() : JEditorPane(){
    init {
        this.text = "Test"
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
        this.font = Font("Cambria", Font.BOLD, 40)
        this.foreground = Color.red
    }
    override fun getPreferredSize(): Dimension {
        return Dimension(100,100)
    }
}

class GameFrame(title:String) : JFrame(title), ActionListener{

    //var timer:Timer = Timer(1000, this)
    val yBound = 1024.0
    val xBound = 1024.0
    //val gameLayout = GameGridBagLayout()
    val light = LightPanel()
    val textBox = TextBox()
    val constraints:GridBagConstraints = GridBagConstraints()

    val encodeButton = JButton("Encode!")

    val keys = listOf(
        listOf<Key>( Key("1"),Key("2"), Key("3"), Key("4"), Key("5"), Key("6"), Key("7"), Key("8"), Key("9"), Key("0")),
        listOf<Key>( Key("Q"),Key("W"), Key("E"), Key("R"), Key("T"), Key("Y"), Key("U"), Key("I"), Key("O"), Key("P")),
        listOf<Key>( Key("A"),Key("S"), Key("D"), Key("F"), Key("G"), Key("H"), Key("J"), Key("K"), Key("L")),
        listOf<Key>( Key("Z"),Key("X"), Key("C"), Key("V"), Key("B"), Key("N"), Key("M"))
    )

    init {
        this.setSize(xBound.toInt(), yBound.toInt())
        this.defaultCloseOperation = EXIT_ON_CLOSE

        layout = GridBagLayout()

        constraints.gridx = 0
        constraints.gridy = 0
        constraints.fill = GridBagConstraints.BOTH

        this.add(textBox, constraints)

        constraints.gridx = 1
        constraints.gridy = 0
        constraints.fill = GridBagConstraints.BOTH

        encodeButton.addActionListener(this)
        this.add(encodeButton, constraints)

        for (row in 0 until keys.size) {
            for (key in 0 until keys[row].size) {
                keys[row][key].addActionListener(this)
                constraints.gridx = 0 + key
                constraints.gridy = 2 + row
                constraints.fill = GridBagConstraints.BOTH
                this.add(keys[row][key], constraints)
            }
        }

        constraints.gridx = 0
        constraints.gridy = 1
        constraints.anchor = GridBagConstraints.NORTHWEST
        this.add(light, constraints)

        this.pack()
        this.isVisible = true
        this.isResizable = false
        this.background = myColors.BACKGROUND
    }

    override fun actionPerformed(e: ActionEvent?) {
        if (e != null) {
            if (e.source == encodeButton) {
                val encoded = encode(textBox.text)
                println(encoded)
                //displayMessage(encoded)
            }
            else if (keys.any{ it.any { it == e.source } }){
                val letter = keys.filter { it.any{ it == e.source} }.first().find { it == e.source }?.value
                textBox.text += letter
            }
        }
    }

    fun displayMessage(message: List<List<String?>>){
        val unitLength:Long = 500
        for ( word in message){
            for (letter in word){
                if (letter != null) {
                    for (signal in letter){
                        light.lightColor = myColors.LIGHT_ON
                        light.repaint()
                        if(signal.toString() == morseKey.DIT)
                            Thread.sleep(unitLength)
                        else
                            Thread.sleep(unitLength * 3)
                        light.lightColor = myColors.LIGHT_OFF
                        light.repaint()
                        Thread.sleep(unitLength)
                    }
                }
                Thread.sleep(unitLength * 2)
            }
            Thread.sleep(unitLength * 4)
        }
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
    Thread.sleep(1000)
    game.displayMessage(inMorse)

}






























