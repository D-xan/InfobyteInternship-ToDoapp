package com.example.unitconverter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //This application is made by Dhruba Singha Roy for oasis infobyte internship.

        val inputkgvalue = findViewById<EditText>(R.id.kgbox)
        val outputvalue = findViewById<TextView>(R.id.gramtextbox)
        val convertbutton = findViewById<Button>(R.id.convertbutton)
        // Here is the logic of the app
        // It converts kg input to gram output
        convertbutton.setOnClickListener()
        {
            val kg = inputkgvalue.text.toString().toFloat()
            val gram = kg * 1000
            outputvalue.setText(gram.toString())
        }
    }
}