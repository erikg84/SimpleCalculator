package com.batch.myapplication

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Double.parseDouble
import java.lang.Integer.parseInt
import java.lang.NumberFormatException
import java.math.RoundingMode
import java.text.DecimalFormat


class MainActivity : AppCompatActivity() {

    private val numbersMap = mapOf(
        "button_zero" to "0",
        "button_one" to "1",
        "button_two" to "2",
        "button_three" to "3",
        "button_four" to "4",
        "button_five" to "5",
        "button_six" to "6",
        "button_seven" to "7",
        "button_eight" to "8",
        "button_nine" to "9",
        "button_period" to "."
    )

    private var sentinel = false
    private lateinit var displayModel: MainViewModel
    private lateinit var inputModel: MainViewModel
    private lateinit var operatorModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        display_textView.setMovementMethod(ScrollingMovementMethod())

        displayModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        inputModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        operatorModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        disableOperators()

        val display: LiveData<String> = displayModel.getData()
        display.observe(this, Observer {
            display_textView.setText(it)
        })

        val input: LiveData<String> = inputModel.getInputData()
        input.observe(this, Observer {
            input_editText.setText(it)
        })

    }

    fun buttonNumberPressed(view: View) {
        val resourceName = view.context.resources.getResourceEntryName(view.id)
        var number: String = ""
        for ((key, value) in numbersMap) {
            if (key == resourceName)
                number = value
        }
        input_editText.append(number)
        inputModel.setInputData(input_editText.text.toString())
        if (number.contains(".")) {
            button_period.isEnabled = false
        }
        enableOperators()
    }

    fun operatorPressed(view: View) {

        button_period.isEnabled = true
        val resourceName = view.context.resources.getResourceEntryName(view.id)
        operatorModel.processOperator(resourceName, sentinel, input_editText.text.toString())
        input_editText.text.clear()
    }

    fun backSpace(view: View) {
        val length: Int = input_editText.length()
        if (length > 0) {
            input_editText.text.delete(length - 1, length)
            inputModel.setInputData(input_editText.text.toString())
        }

    }

    fun deleteAll(view: View) {
        input_editText.text.clear()
        inputModel.setData("")
    }

    fun clearAll(view: View) {
        input_editText.text.clear()
        display_textView.text = ""
        inputModel.setInputData("")
        displayModel.setData("")
    }

    fun enableOperators() {
        button_equals.isEnabled = true
        button_modulus.isEnabled = true
        button_subtract.isEnabled = true
        button_multiply.isEnabled = true
        button_divide.isEnabled = true
        button_add.isEnabled = true
    }

    fun disableOperators() {
        button_equals.isEnabled = false
        button_modulus.isEnabled = false
        button_subtract.isEnabled = false
        button_multiply.isEnabled = false
        button_divide.isEnabled = false
        button_add.isEnabled = false
    }
}
