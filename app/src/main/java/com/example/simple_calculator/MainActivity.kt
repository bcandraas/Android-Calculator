package com.example.simple_calculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var resultTextView: TextView
    private var lastNumeric: Boolean = false
    private var stateError: Boolean = false
    private var lastDot: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultTextView = findViewById(R.id.resultTextView)
        setNumericButtonListeners()
        setOperatorButtonListeners()
    }

    private fun setNumericButtonListeners() {
        val buttons = arrayOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3,
            R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7,
            R.id.btn8, R.id.btn9
        )

        for (id in buttons) {
            findViewById<Button>(id).setOnClickListener { onDigit(it as Button) }
        }
    }

    private fun setOperatorButtonListeners() {
        val buttons = arrayOf(
            R.id.btnAdd, R.id.btnSubtract, R.id.btnMultiply, R.id.btnDivide
        )

        for (id in buttons) {
            findViewById<Button>(id).setOnClickListener { onOperator(it as Button) }
        }

        findViewById<Button>(R.id.btnEquals).setOnClickListener { onEqual() }
        findViewById<Button>(R.id.btnClear).setOnClickListener { onClear() }
    }

    private fun onDigit(view: Button) {
        if (stateError) {
            resultTextView.text = view.text
            stateError = false
        } else {
            resultTextView.append(view.text)
        }
        lastNumeric = true
    }

    private fun onOperator(view: Button) {
        if (lastNumeric && !stateError) {
            resultTextView.append(view.text)
            lastNumeric = false
            lastDot = false
        }
    }

    private fun onEqual() {
        if (lastNumeric && !stateError) {
            val text = resultTextView.text.toString()
            val expression = ExpressionBuilder(text).build()

            try {
                val result = expression.evaluate()
                resultTextView.text = result.toString()
                lastDot = true
            } catch (ex: ArithmeticException) {
                resultTextView.text = "Error"
                stateError = true
                lastNumeric = false
            }
        }
    }

    private fun onClear() {
        resultTextView.text = ""
        lastNumeric = false
        stateError = false
        lastDot = false
    }
}
