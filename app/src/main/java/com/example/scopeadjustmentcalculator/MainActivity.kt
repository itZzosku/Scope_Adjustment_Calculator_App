package com.example.scopeadjustmentcalculator

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.AdapterView
import android.view.View
import kotlin.math.*
import android.view.MotionEvent
import android.view.inputmethod.EditorInfo



class MainActivity : AppCompatActivity() {

    private lateinit var distanceEditText: EditText
    private lateinit var distanceUnitSpinner: Spinner
    private lateinit var impactEditTextX: EditText
    private lateinit var impactUnitSpinnerX: Spinner
    private lateinit var impactEditTextY: EditText
    private lateinit var impactUnitSpinnerY: Spinner
    private lateinit var adjustmentSpinner: Spinner
    private lateinit var resultTextViewMoaVertical: TextView
    private lateinit var resultTextViewClicksVertical: TextView
    private lateinit var resultTextViewClicksRawVertical: TextView
    private lateinit var resultTextViewMoaHorizontal: TextView
    private lateinit var resultTextViewClicksHorizontal: TextView
    private lateinit var resultTextViewClicksRawHorizontal: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

// Find the distanceEditText view
        distanceEditText = findViewById(R.id.distanceEditText)

// Set an OnFocusChangeListener to clear the text when the EditText gains focus
        distanceEditText.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                distanceEditText.text.clear()
            }
        }

// Find the impactEditTextX view
        impactEditTextX = findViewById(R.id.impactEditTextX)

// Set an OnFocusChangeListener to clear the text when the EditText gains focus
        impactEditTextX.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                impactEditTextX.text.clear()
            }
        }

// Find the impactEditTextY view
        impactEditTextY = findViewById(R.id.impactEditTextY)

// Set an OnFocusChangeListener to clear the text when the EditText gains focus
        impactEditTextY.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                impactEditTextY.text.clear()
            }
        }



        distanceEditText = findViewById(R.id.distanceEditText)
        distanceUnitSpinner = findViewById(R.id.distanceUnitSpinner)
        impactEditTextX = findViewById(R.id.impactEditTextX)
        impactUnitSpinnerX = findViewById(R.id.impactUnitSpinnerX)
        impactEditTextY = findViewById(R.id.impactEditTextY)
        impactUnitSpinnerY = findViewById(R.id.impactUnitSpinnerY)
        adjustmentSpinner = findViewById(R.id.adjustmentSpinner)
        resultTextViewMoaVertical = findViewById(R.id.resultTextViewMoaVertical)
        resultTextViewClicksVertical = findViewById(R.id.resultTextViewClicksVertical)
        resultTextViewClicksRawVertical = findViewById(R.id.resultTextViewClicksRawVertical)
        resultTextViewMoaHorizontal = findViewById(R.id.resultTextViewMoaHorizontal)
        resultTextViewClicksHorizontal = findViewById(R.id.resultTextViewClicksHorizontal)
        resultTextViewClicksRawHorizontal = findViewById(R.id.resultTextViewClicksRawHorizontal)


        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.adjustment_options,
            R.layout.spinner_item_big
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adjustmentSpinner.adapter = adapter

        adjustmentSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                calculateClicks()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        val distanceUnitAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.distance_unit_options,
            R.layout.spinner_item_small
        )
        distanceUnitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        distanceUnitSpinner.adapter = distanceUnitAdapter

        val impactUnitAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.impact_unit_options,
            R.layout.spinner_item_small
        )
        impactUnitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        impactUnitSpinnerX.adapter = impactUnitAdapter
        impactUnitSpinnerY.adapter = impactUnitAdapter

        val textWatchers = arrayOf(distanceTextWatcher, impactTextWatcherX, impactTextWatcherY)
        distanceEditText.addTextChangedListener(textWatchers[0])
        impactEditTextX.addTextChangedListener(textWatchers[1])
        impactEditTextY.addTextChangedListener(textWatchers[2])

        val spinnerListeners = arrayOf(distanceUnitSpinnerListener, impactUnitSpinnerXListener, impactUnitSpinnerYListener)
        distanceUnitSpinner.onItemSelectedListener = spinnerListeners[0]
        impactUnitSpinnerX.onItemSelectedListener = spinnerListeners[1]
        impactUnitSpinnerY.onItemSelectedListener = spinnerListeners[2]
    }

    private val distanceTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            calculateClicks()
        }
    }

    private val impactTextWatcherX = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            calculateClicks()
        }
    }

    private val impactTextWatcherY = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            calculateClicks()
        }
    }

    private val distanceUnitSpinnerListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            calculateClicks()
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }

    private val impactUnitSpinnerXListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            calculateClicks()
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }

    private val impactUnitSpinnerYListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            calculateClicks()
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }

    @SuppressLint("SetTextI18n")
    private fun calculateClicks() {
        val distanceText = distanceEditText.text.toString()
        val impactTextX = impactEditTextX.text.toString()
        val impactTextY = impactEditTextY.text.toString()

        if (distanceText.isBlank() || impactTextX.isBlank() || impactTextY.isBlank()) {
            clearResults()
            return
        }

        val distanceUnit = distanceUnitSpinner.selectedItem.toString()
        val impactUnitX = impactUnitSpinnerX.selectedItem.toString()
        val impactUnitY = impactUnitSpinnerY.selectedItem.toString()

        val distance = convertToMeters(distanceText, distanceUnit)
        val impactX = convertToMillimeters(impactTextX, impactUnitX)
        val impactY = convertToMillimeters(impactTextY, impactUnitY)

        val adjustment = when (adjustmentSpinner.selectedItemPosition) {
            0 -> 0.25 // 1/4 MoA
            1 -> 0.5 // 1/2 MoA
            2 -> 1.0 // 1 MoA
            3 -> 0.34377492368197 // 0.1 MRAD
            4 -> 0.33689942520833 // 0.1 MIL
            else -> 0.0
        }

        val moaY = (21600 * impactY / 1000) / (2 * 3.141592654 * distance)
        val formattedMoaY = String.format("%.3f", moaY)
        resultTextViewMoaVertical.text = "MoA Vertical Y: $formattedMoaY"

        val clicksRawY = moaY * (1 / adjustment)
        val clicksY = round(clicksRawY).toInt()
        resultTextViewClicksVertical.text = "Clicks Vertical Y: $clicksY"

        val formattedClicksRawY = String.format("%.3f", clicksRawY)
        resultTextViewClicksRawVertical.text = "Clicks Raw Vertical Y: $formattedClicksRawY"

        val moaX = (21600 * impactX / 1000) / (2 * 3.141592654 * distance)
        val formattedMoaX = String.format("%.3f", moaX)
        resultTextViewMoaHorizontal.text = "MoA Horizontal X: $formattedMoaX"

        val clicksRawX = moaX * (1 / adjustment)
        val clicksX = round(clicksRawX).toInt()
        resultTextViewClicksHorizontal.text = "Clicks Horizontal X: $clicksX"

        val formattedClicksRawX = String.format("%.3f", clicksRawX)
        resultTextViewClicksRawHorizontal.text = "Clicks Raw Horizontal X: $formattedClicksRawX"
    }

    private fun convertToMeters(distanceText: String, unit: String): Double {
        val distance = distanceText.toDouble()

        return when (unit) {
            "Meters" -> distance // Already in meters
            "Yards" -> distance * 0.9144 // Convert yards to meters
            "Feet" -> distance * 0.304 // Convert Feet to meters
            else -> 0.0
        }
    }

    private fun convertToMillimeters(impactText: String, unit: String): Double {
        val impact = impactText.toDouble()

        return when (unit) {
            "Millimeters" -> impact // Already in millimeters
            "Centimeters" -> impact * 10 // Convert inches to millimeters
            "Inches" -> impact * 25.4 // Convert inches to millimeters
            else -> 0.0
        }
    }

    private fun clearResults() {
        resultTextViewMoaVertical.text = ""
        resultTextViewClicksVertical.text = ""
        resultTextViewClicksRawVertical.text = ""
        resultTextViewMoaHorizontal.text = ""
        resultTextViewClicksHorizontal.text = ""
        resultTextViewClicksRawHorizontal.text = ""
    }

}
