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
    private lateinit var impactEditText: EditText
    private lateinit var adjustmentSpinner: Spinner
    private lateinit var resultTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        distanceEditText = findViewById(R.id.distanceEditText)
        impactEditText = findViewById(R.id.impactEditText)
        adjustmentSpinner = findViewById(R.id.adjustmentSpinner)
        resultTextView = findViewById(R.id.resultTextView)

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.adjustment_options,
            R.layout.spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adjustmentSpinner.adapter = adapter

        distanceEditText.addTextChangedListener(textWatcher)
        impactEditText.addTextChangedListener(textWatcher)
        adjustmentSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                calculateClicks()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // Do nothing
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            calculateClicks()
        }

        override fun afterTextChanged(s: Editable?) {
            // Do nothing
        }
    }

    private fun calculateClicks() {
        val distanceText = distanceEditText.text.toString()
        val impactText = impactEditText.text.toString()

        if (distanceText.isBlank() || impactText.isBlank()) {
            resultTextView.text = ""
            return
        }

        val distance = distanceText.toDouble()
        val impact = impactText.toDouble()

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

        val clicksraw = (moa / adjustment)
        val clicks = round(moa / adjustment).toInt()

        resultTextView.text = "MoA: $formattedMoa\nClicks: $clicks\nClicksRaw: $clicksraw"
    }
}
