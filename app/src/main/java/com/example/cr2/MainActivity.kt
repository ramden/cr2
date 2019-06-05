package com.example.cr2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var model: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        model = ViewModelProviders.of(this).get(MainViewModel::class.java)

        setupSpinner()

        setupButton()

        val textView: TextView = findViewById(R.id.txtLog)

        model.whenCurrentLog.observe(this, Observer {
            textView.text = it
        })
    }

    private fun setupButton() {
        val button: Button = findViewById(R.id.btnGo)
        button.setOnClickListener {
            model.processAction(spinner.selectedItem.toString())
        }
    }

    private fun setupSpinner() {
        val spinner: Spinner = findViewById(R.id.spinner)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.options_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
    }
}
