package com.example.calculatorkotlin
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var history: TextView? = null
    private var isEqually = false
    private var isAction = false
    private var display: EditText? = null
    private var num1 = 0.0
    private var num2 = 0.0

    private var operator = 0.toChar()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        display = findViewById<EditText>(R.id.display)
        history = findViewById<TextView>(R.id.historyLabel)
    }

    fun zeroTrimming(num: Double): String {
        val s = num.toString()
        if (s.endsWith(".0")) {
            return s.substring(0, s.length - 2)
        }
        return s
    }

    fun addNumberAction(view: View) {
        if (display!!.text.toString() == "0") {
            display!!.setText("")
        }
        if (isAction) {
            display!!.setText((view as Button).text)
        } else {
            display!!.append((view as Button).text)
        }
        setFontForContent()
        isEqually = false
        isAction = false
    }

    fun setFontForContent() {
        if (display!!.text.toString().length >= 16) {
            display!!.post {
                var textSize = display!!.textSize
                while (display!!.paint
                        .measureText(display!!.text.toString()) > display!!.width
                ) {
                    textSize--
                    display!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
                }
            }
        } else {
            display!!.textSize = 45f
        }
    }

    fun plusAction(view: View) {
        if (!java.lang.Double.isNaN(operator.code.toDouble()) && !isAction) {
            equallyAction(view)
        }

        operator = (view as Button).text[0]
        isAction = true
        val h = zeroTrimming(num1) + " " + operator + " "
        history!!.text = h
    }

    fun clearClick(view: View?) {
        display!!.setText("0")
        num1 = 0.0
        num2 = Double.NaN
        operator = ' '
        history!!.text = ""
        display!!.textSize = 45f
    }

    fun equallyAction(view: View?) {
        if (java.lang.Double.isNaN(num1)) {
            return
        }
        if (!isEqually) {
            num2 = display!!.text.toString().toDouble()
        }
        isEqually = true
        if (operator == '+') {
            display!!.setText(zeroTrimming(num1 + num2))
        } else if (operator == '-') {
            display!!.setText(zeroTrimming(num1 - num2))
        } else if (operator == '*') {
            display!!.setText(zeroTrimming(num1 * num2))
        } else if (operator == '/') {
            if (num2 == 0.0) {
                clearClick(view)
                return
            }
            display!!.setText(zeroTrimming(num1 / num2))
        }
        var h = ""
        h = if (operator != ' ') {
            zeroTrimming(num1) + " " + operator + " " + zeroTrimming(num2) + " = "
        } else {
            zeroTrimming(num1) + " = "
        }
        history!!.text = h
        num1 = display!!.text.toString().toDouble()
        isAction = true
        if (display!!.text.toString().lowercase().contains("infinity")) {
            clearClick(display)
        }
        setFontForContent()
    }

    @SuppressLint("SetTextI18n")
    fun addDot(view: View?) {
        if (display!!.text.toString().contains(".")) {
            return
        }
        isAction = false
        display!!.setText(display!!.text.toString() + ".")
    }
}