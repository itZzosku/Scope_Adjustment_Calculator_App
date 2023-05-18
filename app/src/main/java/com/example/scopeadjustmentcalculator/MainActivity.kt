package com.example.scopeadjustmentcalculator

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

class MainActivity : AppCompatActivity() {

    private lateinit var distanceEditText: EditText
    private lateinit var distanceUnitSpinner: Spinner
    private lateinit var impactEditText: EditText
    private lateinit var impactUnitSpinner: Spinner
    private lateinit var adjustmentSpinner: Spinner
    private lateinit var resultTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        distanceEditText = findViewById(R.id.distanceEditText)
        distanceUnitSpinner = findViewById(R.id.distanceUnitSpinner)
        impactEditText = findViewById(R.id.impactEditText)
        impactUnitSpinner = findViewById(R.id.impactUnitSpinner)
        adjustmentSpinner = findViewById(R.id.adjustmentSpinner)
        resultTextView = findViewById(R.id.resultTextView)

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.adjustment_options,
            R.layout.spinner_item_big
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adjustmentSpinner.adapter = adapter

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
        impactUnitSpinner.adapter = impactUnitAdapter

        val textWatchers = arrayOf(distanceTextWatcher, impactTextWatcher)
        distanceEditText.addTextChangedListener(textWatchers[0])
        impactEditText.addTextChangedListener(textWatchers[1])

        val spinnerListeners = arrayOf(distanceUnitSpinnerListener, impactUnitSpinnerListener)
        distanceUnitSpinner.onItemSelectedListener = spinnerListeners[0]
        impactUnitSpinner.onItemSelectedListener = spinnerListeners[1]
    }

    private val distanceTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            calculateClicks()
        }
    }

    private val impactTextWatcher = object : TextWatcher {
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

    private val impactUnitSpinnerListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            calculateClicks()
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }

    private fun calculateClicks() {
        val distanceText = distanceEditText.text.toString()
        val impactText = impactEditText.text.toString()

        if (distanceText.isBlank() || impactText.isBlank()) {
            resultTextView.text = ""
            return
        }

        val distanceUnit = distanceUnitSpinner.selectedItem.toString()
        val impactUnit = impactUnitSpinner.selectedItem.toString()

        val distance = convertToMeters(distanceText, distanceUnit)
        val impact = convertToMillimeters(impactText, impactUnit)

        val adjustment = when (adjustmentSpinner.selectedItemPosition) {
            0 -> 0.25 // 1/4 MoA
            1 -> 0.5 // 1/2 MoA
            2 -> 1.0 // 1 MoA
            3 -> 0.34377492368197 // 0.1 MRAD
            4 -> 0.33689942520833 // 0.1 MIL
            else -> 0.0
        }

        val moa = (21600 * impact / 1000) / (2 * 3.141592654 * distance)
        val formattedMoa = String.format("%.3f", moa)

        val clicksRaw = moa * (1 / adjustment)
        val clicks = round(clicksRaw).toInt()

        resultTextView.text = "MoA: $formattedMoa\nClicks: $clicks\nClicksRaw: $clicksRaw"
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

}
