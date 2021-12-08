import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.geom.Ellipse2D
import java.io.File
import javax.swing.*

/**
 * Object to store the key for encoding strings into Morse code and visa versa.
 */
object MorseKey {
    const val DIT:String = "\u2022"
    const val DAH:String = "\u2013"
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

/**
 * Object used to store custom colors.
 */
object MyColors{
    val LIGHT_BORDER = Color.DARK_GRAY
    val LIGHT_OFF = Color(136, 75, 0)
    val LIGHT_ON = Color(255, 150, 27)
    val BACKGROUND = Color(232,220,184)
    val TEXT_BACKGROUND = Color(240,230,200)
}

/**
 * Object used to store custom fonts.
 */
object MyFonts{
    val textFont = Font("Bookman Old Style", Font.BOLD, 24)
    val menuFont = Font( "Bookman Old Style", Font.BOLD, 18)
    val keyFont = Font("Bookman Old Style", Font.BOLD, 50)
}

/**
 * This function cleans and then encodes strings into Morse Code.
 *
 * @param encode_input
 * @return encoded message
 */
fun encode(encode_input: String): List<List<String?>> {
    val words = encode_input.uppercase().split(" ")
    val encoded = emptyList<List<String?>>().toMutableList()
    for ( word in words ){
        val temp = emptyList<String?>().toMutableList()
        for( letter in word )
            temp.add( MorseKey.MORSE_CODE[letter])
        encoded.add(temp.toList())
    }
    return encoded.toList()
}

/**
 * This function decodes Morse code messages.
 *
 * @param encoded_input
 * @return decoded message
 */
fun decode(encoded_input: List<List<String?>>): String{
    val decoded = emptyList<String>().toMutableList()
    for (word in encoded_input){
        val temp = emptyList<Char>().toMutableList()
        for (letter in word)
            temp.add(MorseKey.MORSE_CODE.filterValues { it == letter }.keys.first())
        decoded.add(temp.joinToString(separator = ""))
    }
    return decoded.joinToString(separator = " ")
}

/**
 * This class houses all code necessary to illustrate the blinking light.
 */
class LightPanel() : JComponent(){

    val light = Ellipse2D.Double(this.x + 10.0, this.y + 10.0, 80.0, 80.0)
    val border = Ellipse2D.Double(this.x.toDouble(), this.y.toDouble(), 100.0, 100.0)
    var lightColor = MyColors.LIGHT_OFF

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)
        val g2D = g as Graphics2D

        g2D.color = MyColors.LIGHT_BORDER
        g2D.fill(border)

        g2D.color = lightColor
        g2D.fill(light)
    }

    override fun getPreferredSize(): Dimension {
        return Dimension(100,100)
    }
}

/**
 * This class has all code used to set up the ghost boxes.
 * Ghost boxes were used to facilitate alignment of the GridBag.
 */
class GhostBox(): JComponent(){

    val border = Ellipse2D.Double(this.x.toDouble(), this.y.toDouble(), 50.0, 10.0)

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)
        val g2D = g as Graphics2D

        g2D.color = MyColors.BACKGROUND
        g2D.fill(border)
    }

    override fun getPreferredSize(): Dimension {
        return Dimension(50, 10)
    }
}

/**
 * Class that has all components of the text box that displays user input.
 */
class TextBox() : JEditorPane(){
    init {
        this.isEditable = false
        this.background = MyColors.TEXT_BACKGROUND
        this.font = MyFonts.textFont
    }

    fun add(c: Char){
        this.text = this.text + c
    }

    fun backSpace(){
        this.text = this.text.dropLast(1)
    }

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)
    }

    override fun getPreferredSize(): Dimension {
        return Dimension(100,100)
    }
}

/**
 * Class that displays all keys of the keyboard including A-Z, 0-9, backspace and space bar.
 *
 * @param value
 */
class Key( val value: String): JButton(value){
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
                g.font = MyFonts.keyFont
                g.drawChars(this.value.toCharArray(), 0, this.value.length, 35, 65)
            }
        }
    }
    override fun getPreferredSize(): Dimension {
        return Dimension(if (value == " ") 500 else 100,100)
    }
}

/**
 * Class contains code for the encode/check button.
 */
class EncodeButton(): JButton("Encode!"){
    init {
        this.isFocusable = false
        this.isBorderPainted = false
        this.background = null
    }

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)
        if (g != null) {
            g.color = Color.darkGray
            g.fillRect(5, 5, 240, 90)

            g.color = Color.lightGray
            g.fillRect(10, 10, 230, 80)

            g.color = Color.black
            g.font = MyFonts.keyFont
            g.drawChars(this.text.toCharArray(), 0, this.text.length, 25, 65)
        }
    }

    override fun getPreferredSize(): Dimension {
        return Dimension(250, 100)
    }
}

/**
 * Frame that contains all code necessary for the layout and action of the game.
 */
class GameFrame() : JFrame("Morse Key!"), ActionListener, KeyListener{

    private val yBound = 1024.0
    private val xBound = 1024.0

    private val constraints:GridBagConstraints = GridBagConstraints()

    private val light = LightPanel()
    private val textBox = TextBox()
    private val encodeButton = EncodeButton()

    private val keys = listOf(
        listOf<Key>( Key("1"),Key("2"), Key("3"), Key("4"), Key("5"), Key("6"), Key("7"), Key("8"), Key("9"), Key("0")),
        listOf<Key>( Key("Q"),Key("W"), Key("E"), Key("R"), Key("T"), Key("Y"), Key("U"), Key("I"), Key("O"), Key("P")),
        listOf<Key>( Key("A"),Key("S"), Key("D"), Key("F"), Key("G"), Key("H"), Key("J"), Key("K"), Key("L")),
        listOf<Key>( Key("Z"),Key("X"), Key("C"), Key("V"), Key("B"), Key("N"), Key("M")),
        listOf<Key>( Key(" ")))
    private val backSpace = Key("\u2190")

    private val menuBar = JMenuBar()

    private val speedMenu = JMenu("Speed")
    private val speeds = ButtonGroup()
    private val slow = JRadioButtonMenuItem("Slow")
    private val medium = JRadioButtonMenuItem("Medium")
    private val fast = JRadioButtonMenuItem("Fast")

    private val cheatsheet = JMenu("Cheatsheet")

    private val gameMenu = JMenu("Game")
    private val games = ButtonGroup()
    private val freePlay = JRadioButtonMenuItem( "Free Play")
    private val challenge = JRadioButtonMenuItem("Challenge")

    private val difficultyMenu = JMenu("Difficulty")
    private val difficulties = ButtonGroup()
    private val easy = JRadioButtonMenuItem("Easy")
    private val hard = JRadioButtonMenuItem("Hard")

    private val packButton = JButton("Pack")

    private val pack = emptyList<List<String>>().toMutableList()

    private val playButton = JButton("Play")

    private var storedWord: String? = null

    /**
     * Initialized all components needed.
     */
    init {
        // Basic setup
        this.contentPane.background = MyColors.BACKGROUND
        this.setSize(xBound.toInt(), yBound.toInt())
        this.defaultCloseOperation = EXIT_ON_CLOSE

        // Gridbag setup
        layout = GridBagLayout()

        for( i in 0 until 20) {
            val gbox = GhostBox()
            addComponent(i, 0, gbox)
        }

        // Text box setup
        textBox.addKeyListener(this)
        addComponent(3,2,textBox, 10, 10)

        // Encode button setup
        encodeButton.addActionListener(this)
        addComponent(14,2,encodeButton, 5)

        // Keyboard setup
        val padding = arrayOf( 0, 0, 1, 3, 5)
        for (row in 0 until keys.size) {
            for(key in 0 until keys[row].size) {
                keys[row][key].addActionListener(this)
                val gx = ((0+key) * 2) + padding[row]
                val gy = 3+row
                addComponent( gx, gy, keys[row][key], if(keys[row][key].value == " ") 10 else 2)
            }
        }

        // Backspace setup
        backSpace.addActionListener(this)
        addComponent(16, 7, backSpace, 2)

        // More gridbag setup
        constraints.anchor = GridBagConstraints.NORTHWEST
        addComponent(9,1,light, 2, 10)

        // Speed menu setup
        speeds.add(slow)
        speeds.add(medium)
        speeds.add(fast)

        speedMenu.add(slow)
        speedMenu.add(medium)
        speedMenu.add(fast)

        slow.font = MyFonts.menuFont
        medium.font = MyFonts.menuFont
        fast.font = MyFonts.menuFont
        speedMenu.font = MyFonts.menuFont

        slow.background = MyColors.TEXT_BACKGROUND
        medium.background = MyColors.TEXT_BACKGROUND
        fast.background = MyColors.TEXT_BACKGROUND
        speedMenu.background = MyColors.TEXT_BACKGROUND

        slow.isBorderPainted = false
        medium.isBorderPainted = false
        fast.isBorderPainted = false
        speedMenu.isBorderPainted = false

        slow.isSelected = true

        menuBar.add(speedMenu)

        // Game Mode setup
        games.add(freePlay)
        games.add(challenge)

        gameMenu.add(freePlay)
        gameMenu.add(challenge)

        freePlay.font = MyFonts.menuFont
        challenge.font = MyFonts.menuFont
        gameMenu.font = MyFonts.menuFont

        freePlay.background = MyColors.TEXT_BACKGROUND
        challenge.background = MyColors.TEXT_BACKGROUND
        gameMenu.background = MyColors.TEXT_BACKGROUND

        freePlay.isBorderPainted = false
        challenge.isBorderPainted = false
        gameMenu.isBorderPainted = false

        freePlay.isSelected = true

        freePlay.addActionListener(this)
        challenge.addActionListener(this)

        menuBar.add(gameMenu)

        // Difficulty menu setup
        difficulties.add(easy)
        difficulties.add(hard)

        difficultyMenu.add(easy)
        difficultyMenu.add(hard)

        easy.font = MyFonts.menuFont
        hard.font = MyFonts.menuFont
        difficultyMenu.font = MyFonts.menuFont

        easy.background = MyColors.TEXT_BACKGROUND
        hard.background = MyColors.TEXT_BACKGROUND
        difficultyMenu.background = MyColors.TEXT_BACKGROUND

        easy.isBorderPainted = false
        hard.isBorderPainted = false
        difficultyMenu.isBorderPainted = false

        easy.isSelected = true

        difficultyMenu.isVisible = true

        menuBar.add(difficultyMenu)

        // Pack loader set up
        packButton.font = MyFonts.menuFont
        packButton.isBorderPainted = false
        packButton.isFocusable = false
        packButton.isOpaque = false
        packButton.isContentAreaFilled = false
        packButton.addActionListener(this)
        packButton.isVisible = true
        menuBar.add(packButton)

        // Play button set up
        playButton.font = MyFonts.menuFont
        playButton.isBorderPainted = false
        playButton.isFocusable = false
        playButton.isOpaque = false
        playButton.isContentAreaFilled = false
        playButton.addActionListener(this)
        playButton.isVisible = true
        menuBar.add(playButton)

        // Cheatsheet setup
        ToolTipManager.sharedInstance().initialDelay = 0
        ToolTipManager.sharedInstance().dismissDelay = Int.MAX_VALUE
        menuBar.add(Box.createHorizontalGlue())

        cheatsheet.isFocusable = false
        cheatsheet.font = MyFonts.menuFont
        val DIT = MorseKey.DIT + ' '
        val DAH = MorseKey.DAH + ' '
        cheatsheet.toolTipText = "<html>" +
                                 "A " + DIT + DAH + "<br>"  +
                                 "B " + DAH + DIT + DIT + DIT + "<br>" +
                                 "C " + DAH + DIT + DAH + DIT + "<br>" +
                                 "D " + DAH + DIT + DIT + "<br>" +
                                 "E " + DIT + "<br>" +
                                 "F " + DIT + DIT + DAH + DIT + "<br>" +
                                 "G " + DAH + DAH + DIT + "<br>" +
                                 "H " + DIT + DIT + DIT + DIT + "<br>" +
                                 "I " + DIT + DIT + "<br>" +
                                 "J " + DIT + DAH + DAH + DAH + "<br>" +
                                 "K " + DAH + DIT + DAH + "<br>" +
                                 "L " + DIT + DAH + DIT + DIT + "<br>" +
                                 "M " + DAH + DAH + "<br>" +
                                 "N " + DAH + DIT + "<br>" +
                                 "O " + DAH + DAH + DAH + "<br>" +
                                 "P " + DIT + DAH + DAH + DIT + "<br>" +
                                 "Q " + DAH + DAH + DIT + DAH + "<br>" +
                                 "R " + DIT + DAH + DIT + "<br>" +
                                 "S " + DIT + DIT + DIT + "<br>" +
                                 "T " + DAH + "<br>" +
                                 "U " + DIT + DIT + DAH + "<br>" +
                                 "V " + DIT + DIT + DIT + DAH + "<br>" +
                                 "W " + DIT + DAH + DAH + "<br>" +
                                 "X " + DAH + DIT + DIT + DAH + "<br>" +
                                 "Y " + DAH + DIT + DAH + DAH + "<br>" +
                                 "Z " + DAH + DAH + DIT + DIT + "<br>" +
                                 "0 " + DAH + DAH + DAH + DAH + DAH + "<br>" +
                                 "1 " + DIT + DAH + DAH + DAH + DAH + "<br>" +
                                 "2 " + DIT + DIT + DAH + DAH + DAH + "<br>" +
                                 "3 " + DIT + DIT + DIT + DAH + DAH + "<br>" +
                                 "4 " + DIT + DIT + DIT + DIT + DAH + "<br>" +
                                 "5 " + DIT + DIT + DIT + DIT + DIT + "<br>" +
                                 "6 " + DAH + DIT + DIT + DIT + DIT + "<br>" +
                                 "7 " + DAH + DAH + DIT + DIT + DIT + "<br>" +
                                 "8 " + DAH + DAH + DAH + DIT + DIT + "<br>" +
                                 "9 " + DAH + DAH + DAH + DAH + DIT + "<br>" +
                                 DIT + " one unit <br>" +
                                 DAH + " three units <br>" +
                                 "one unit between letter parts <br>" +
                                 "three units between letters <br>" +
                                 "seven units between words </html>"
        UIManager.put("ToolTip.background", MyColors.TEXT_BACKGROUND)
        UIManager.put("ToolTip.border", MyColors.LIGHT_OFF)
        UIManager.put("ToolTip.foreground", Color.black)
        UIManager.put("ToolTip.font", MyFonts.menuFont)

        menuBar.add(cheatsheet)

        // Menubar setup
        menuBar.background = MyColors.BACKGROUND
        menuBar.isBorderPainted = false
        this.jMenuBar = menuBar

        // More basic setup
        this.pack()
        this.isVisible = true
        this.isResizable = false

        // Disabling buttons to fit gamemode
        difficultyMenu.isVisible = false
        packButton.isVisible = false
        playButton.isVisible = false
    }

    /**
     * Adds components to the gridbad based on parameters
     *
     * @param gridX
     * @param gridY
     * @param comp
     * @param gridWidth
     * @param yPad
     */
    private fun addComponent(gridX: Int, gridY: Int, comp: JComponent, gridWidth: Int = 1, yPad: Int = 0){
        constraints.ipady = yPad
        constraints.gridwidth = gridWidth
        constraints.gridx = gridX
        constraints.gridy = gridY
        constraints.fill = GridBagConstraints.HORIZONTAL
        this.add(comp, constraints)
    }

    /**
     * Checks for performed actions and deals with each accordingly.
     *
     * @param e
     */
    override fun actionPerformed(e: ActionEvent?) {
        // Makes sure the action event is not null
        if (e != null) {
            // Encode Button
            if (e.source == encodeButton) {
                // Determines gamemode
                if (encodeButton.text == "Encode!")
                    displayMessage(encode(textBox.text))
                else{
                    if (textBox.text == storedWord){
                        val response = JOptionPane.showOptionDialog(this, "Correct!", "",
                            JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                            listOf<String>("New word", "Exit").toTypedArray(), null)
                        textBox.text = ""
                        if(response == 1 || response == -1){
                            playButton.text = "Play"
                        }
                        else if (response == 0){
                            storedWord = randomPackWord()
                            displayMessage(encode(storedWord!!))
                        }
                    }
                    else{
                        val response = JOptionPane.showOptionDialog(this, "Incorrect!", "",
                            JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                            listOf<String>("Try again?", "Exit").toTypedArray(), null)
                        if(response == 1 || response == -1){
                            textBox.text = ""
                            playButton.text = "Play"
                        }
                        else if (response == 0){
                            displayMessage(encode(storedWord!!))
                        }
                    }
                }
            }
            // Keyboard keys
            else if (keys.any{ it.any { it == e.source } }){
                val letter = keys.filter { it.any{ it == e.source} }.first().find { it == e.source }?.value
                textBox.add(letter?.get(0) ?: ' ')
            }
            // Backspace
            else if( e.source == backSpace) {
                textBox.backSpace()
            }
            // Pack selection
            else if( e.source ==  packButton){
                val fc = JFileChooser()
                val returnValue = fc.showOpenDialog(this)

                if(returnValue == JFileChooser.APPROVE_OPTION){
                    val tempPack = fc.selectedFile
                    loadPack(tempPack)
                }
            }
            // Play Button
            else if ( e.source == playButton){
                if (playButton.text == "Play") {
                    storedWord = randomPackWord()
                }
                if (storedWord != null) {
                    playButton.text = "Repeat"
                    displayMessage(encode(storedWord!!))
                }
            }
            // Free play game mode
            else if (e.source == freePlay){
                difficultyMenu.isVisible = false
                packButton.isVisible = false
                playButton.isVisible = false
                encodeButton.text = "Encode!"
                textBox.text = ""
            }
            // Challenge game mode
            else if (e.source == challenge){
                difficultyMenu.isVisible = true
                packButton.isVisible = true
                playButton.isVisible = true
                encodeButton.text = "Check!"
                textBox.text = ""
            }
        }
    }

    /**
     * Listens to the keyboard and sends letters or digits to the text box.
     *
     * @param e
     */
    override fun keyTyped(e: KeyEvent?) {
        if (e != null){
            if( e.keyChar.isLetterOrDigit())
                textBox.add(e.keyChar.uppercaseChar())
        }
    }

    /**
     * Unused, but must be included due to inheritance.
     */
    override fun keyPressed(e: KeyEvent?) {
    }

    /**
     * Checks for backspace or enter.
     * Backspace removes one letter.
     * Enter encodes/checks.
     *
     * @param e
     */
    override fun keyReleased(e: KeyEvent?) {
        if(e != null){
            if( e.keyCode == KeyEvent.VK_BACK_SPACE)
                textBox.backSpace()
            else if( e.keyCode == KeyEvent.VK_ENTER)
                encodeButton.doClick()
        }
    }

    /**
     * Displays an encoded message in the light.
     *
     * @param message
     */
    private fun displayMessage(message: List<List<String?>>){
        // Starts a new thread so that it does not interfere with other processes.
        val myThread = Thread( Runnable {
            // Determines playback speed
            val unitLength: Long = if (slow.isSelected) 500 else if (medium.isSelected) 200 else 50

            // Loops through lists and displays timed messages.
            for (word in message) {
                for (letter in word) {
                    if (letter != null) {
                        for (signal in letter) {
                            light.lightColor = MyColors.LIGHT_ON
                            Toolkit.getDefaultToolkit().beep()
                            light.repaint()
                            if (signal.toString() == MorseKey.DIT)
                                Thread.sleep(unitLength)
                            else
                                Thread.sleep( unitLength * 3)
                            light.lightColor = MyColors.LIGHT_OFF
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

    /**
     * Takes a file from the user and determines if it is a valid pack, then loads it.
     *
     * @param file
     * @return if pack is valid
     */
    private fun loadPack(file: File): Boolean {
        if (!file.isFile || file.extension != "csv")
            return false

        var lines = file.readLines()

        if (!lines[0].contains( "# EASY"))
            return false
        lines = lines.drop(1)

        val easyWords = emptyList<String>().toMutableList()
        val hardWords = emptyList<String>().toMutableList()

        var diff = false
        val regex = Regex("[^A-Za-z0-9 ]")

        for (line in lines){
            if( line == "# HARD")
                diff = true
            else {
                if (diff)
                    hardWords.add(regex.replace(line, "").uppercase())
                else
                    easyWords.add(regex.replace(line, "").uppercase())
            }
        }
        pack.clear()
        pack.add(easyWords)
        pack.add(hardWords)
        return diff
    }

    /**
     * Gets a random word within the specified dificulty from the loaded pack.
     *
     * @return random word, if pack is loaded
     */
    private fun randomPackWord():String?{
        if (pack.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No pack loaded!")
            return null
        }
        var words:List<String>? = null
        if (easy.isSelected)
            words = pack[0]
        else
            words = pack[1]

        return words.random()
    }
}

/**
 * Main method to start the project.
 *
 * @param args
 */
fun main(args: Array<String>) {
    val game = GameFrame()
}
