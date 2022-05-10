package com.example.monetriopiloto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import org.json.JSONObject
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {

    private lateinit var result: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        result = findViewById<TextView>(R.id.txt_result)

        val buttonConverter = findViewById<Button>(R.id.btn_converte)

        buttonConverter.setOnClickListener  {
            converter()
        }
    }

    private fun converter ()  {
        val selectedCurrency = findViewById<RadioGroup>(R.id. radio_group)

        val checked = selectedCurrency.checkedRadioButtonId

        val currency =when( checked) {
            R.id.radio_usd -> "USD"
            R.id.radio_eur -> "EUR"
            else           -> "CLP"
        }

        val editField = findViewById<EditText>(R.id.edit_field)

        val value = editField.text.toString()

        if (value.isEmpty())
            return

        result.text = value
        result.visibility = View.VISIBLE

        Thread {
            // aqui acontece em paralelo

            val url = URL ("https://free.currconv.com/api/v7/convert?q=${currency}_BRL&compact=ultra&apiKey=51380b3a97306998e227")
            val conn = url.openConnection() as HttpsURLConnection

            try {
                val data = conn.inputStream.bufferedReader().readText()

                val obj = JSONObject (data)

                runOnUiThread {
                    val res = obj.getDouble("${currency}_BRL")

                    result.text = "R$${"%.4F".format(value.toDouble() * res)}"
                    result.visibility = View.VISIBLE
                }
            } finally {
                conn.disconnect()
            }

        }.start()
    }
}