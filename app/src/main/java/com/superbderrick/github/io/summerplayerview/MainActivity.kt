package com.superbderrick.github.io.summerplayerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener{
            setNewText("Click!")

            CoroutineScope(Dispatchers.IO).launch {
                fakeAPIRequest()
            }

        }

    }

    private suspend fun setTextOnMainThread(input: String) {
        withContext (Dispatchers.Main) {
            setNewText(input)
        }
    }

    private fun setNewText(input:String) {
        val newText = text.text.toString() + "\n$input"
        text.text = newText
    }


    private suspend fun fakeAPIRequest() {

        logThread("fakeApiRequest")

        val result1 = getResult1FromAPI() // wait until job is done

        if ( result1.equals("Result #1")) {

            setTextOnMainThread("Got $result1")

            val result2 = getResult2FromAPI() // wait until job is done

            if (result2.equals("Result #2")) {
                setTextOnMainThread("Got $result2")
            } else {
                setTextOnMainThread("Couldn't get Result #2")
            }
        } else {
            setTextOnMainThread("Couldn't get Result #1")
        }

    }

    private suspend fun getResult1FromAPI() : String {
        logThread("getResult1FromAPI")
        delay(1000) //Does not block thread. Just suspends the coruthin sin die the thread
        return "Result #1"
    }

    private suspend fun getResult2FromAPI() : String {
        logThread("getResult2FromAPI")
        delay(1000) //Does not block thread. Just suspends the coruthin sin die the thread
        return "Result #2"
    }

    private fun logThread(methodName:String) {
        println("debug: ${methodName}: ${Thread.currentThread().name}")
    }



}
