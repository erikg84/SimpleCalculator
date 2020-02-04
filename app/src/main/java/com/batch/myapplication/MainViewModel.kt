package com.batch.myapplication

import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Double.parseDouble
import java.lang.NumberFormatException
import java.lang.reflect.MalformedParametersException
import java.math.RoundingMode
import java.text.DecimalFormat

class MainViewModel : ViewModel() {
    private var displayLiveData = MutableLiveData<String>()
    private var inputLiveData = MutableLiveData<String>()
    private var num1 = 0.0
    private var num2 = 0.0
    private var operation = ""

    init {
        displayLiveData.value = ""
        inputLiveData.value = ""
    }

    fun setData(data: String) {
        displayLiveData.value = data
    }

    fun getData(): MutableLiveData<String> {
        return displayLiveData
    }

    fun getInputData(): MutableLiveData<String> {
        return inputLiveData
    }

    fun setInputData(data: String) {
        inputLiveData.value = data
    }

    fun processOperator(resourceName: String, sentinel: Boolean, input: String) {

        if (!sentinel) {
            when (resourceName) {
                "button_divide" -> checkInput("/", input)
                "button_multiply" -> checkInput("*", input)
                "button_add" -> checkInput("+", input)
                "button_subtract" -> checkInput("-", input)
                "button_modulus" -> checkInput("%", input)
                "button_equals" -> arithmeticOperation(input)
            }
        }

    }

    private fun arithmeticOperation(input: String) {

        if (input.isNotEmpty()) {
            checkInput("=", input)

            var result = 0.0
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.CEILING

            try {

                when (operation) {
                    "*" -> result = num1 * num2
                    "+" -> result = num1 + num2
                    "-" -> result = num1 - num2
                    "/" -> result = num1 / num2
                    "%" -> result = num1 % num2
                }
                val formattedResult = df.format(result)
                val tempSecondNumber = if (num2 % 1 == 0.0) num2.toInt() else num2
                val tempResult =
                    if (formattedResult.toDouble() % 1 == 0.0) formattedResult.toInt() else formattedResult
                displayLiveData.value += "$tempSecondNumber = $tempResult\n"
            } catch(a:ArithmeticException){

            }catch (e:NumberFormatException){

            }
        }
    }

    private fun checkInput(operand: String, input: String) {

        if (input.isNotEmpty()) {
            var flag = false
            if (operand != "=")
                operation = operand
            else
                flag = true

            val numberInput = parseDouble(input)

            if (!flag) {
                num1 = numberInput
                val temp = if (numberInput % 1 == 0.0) numberInput.toInt() else numberInput
                displayLiveData.value += "$temp $operation"
            } else
                num2 = numberInput

        }

    }

}