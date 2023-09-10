package com.example.simple_calculator

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.simple_calculator.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityMainBinding
    private var canAddOperation = false
    private var canAddDecimal = true

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


    }

    fun numberAction(view: View)
    {
        if (view is Button)
        {
            if(view.text == ".") {
                if (canAddDecimal)
                    binding.workingTV.append(view.text)
                canAddDecimal = false
            }
            else
                binding.workingTV.append(view.text)

            canAddOperation = true
        }
    }
    fun operatorAction(view: View) {
        if (view is Button && canAddOperation){
            binding.workingTV.append(view.text)
            canAddOperation = false
            canAddDecimal = true
        }
    }

    fun equalAction(view: View) {
        binding.resultsTV.text = calculateResults()
    }
    fun allClearAction(view: View) {
        binding.workingTV.text = ""
        binding.resultsTV.text = ""
    }
    fun clearAction(view: View) {
        val length = binding.workingTV.text.length
        if (length > 0) {
            if(binding.workingTV.text[length - 1] == '.')
                canAddDecimal = true
            else if (binding.workingTV.text[length - 1] != '.')
                canAddOperation = true

            binding.workingTV.text = binding.workingTV.text.subSequence(0, length - 1)
        }
    }
    private fun calculateResults(): String
    {
      val digitsOperators = digitsOperators()
      if(digitsOperators.isEmpty()) return ""

      val timesDivision = timesDivisionCalculate(digitsOperators)
        if(timesDivision.isEmpty()) return ""

      val  result = addSubtractCalculate(timesDivision)
        return result.toString()
    }

    private fun digitsOperators(): MutableList<Any> {
       val list = mutableListOf<Any>()
        var currentDigit = ""
        for (char in binding.workingTV.text)
        {
            if (char.isDigit() || char == '.')
                currentDigit += char
            else
            {
                list.add(currentDigit.toFloat())
                list.add(char)
                currentDigit = ""
            }
        }
        if (currentDigit != "")
            list.add(currentDigit.toFloat())
        return list
    }

    private fun timesDivisionCalculate(passedList: MutableList<Any>): MutableList<Any> {
        var list = passedList
        while(list.contains('*') || list.contains('/'))
        {
            list = calTimesDiv(list)
        }
        return list
    }

    private fun calTimesDiv(passedList: MutableList<Any>): MutableList<Any> {
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size

        for(i in passedList.indices) {
            if (passedList[i] is Char && i != passedList.lastIndex && i < restartIndex) {
                val operator = passedList[i]
                val prevDigit = passedList[i - 1] as Float
                val nextDigit = passedList[i + 1] as Float
                when (operator) {
                    '*' -> {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i + 1
                    }

                    '/' -> {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i + 1
                    }
                    '+' -> {
                        newList.add(prevDigit + nextDigit)
                        restartIndex = i + 1
                    }
                    '-' -> {
                        newList.add(prevDigit - nextDigit)
                        restartIndex = i + 1
                    }
                }
            }
            if (i > restartIndex)
                newList.add(passedList[i])
        }
        return newList
    }

    private fun addSubtractCalculate(passedList: MutableList<Any>): Float {
        var result = passedList[0] as Float
        for (i in passedList.indices)
        {
            if (passedList[i] is Char && i != passedList.lastIndex)
            {
                val operator = passedList[i]
                val nextDigit = passedList[i + 1] as Float
                if (operator == '+')
                    result += nextDigit
                else if (operator == '-')
                    result -= nextDigit
            }
        }
        return result
    }

    fun openTwitter(view: View) {
        val url = "https://twitter.com/chottuthejimmy"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }
}