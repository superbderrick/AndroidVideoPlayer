package com.superbderrick.github.io.summerplayerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private val JOB_TIMEOUT = 6000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener{
            setNewText("Click!")

            CoroutineScope(Dispatchers.IO).launch {
                fakeAPIRequestWithTimeOut()
            }

            logThread("CLICK")

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

    private suspend fun fakeAPIRequestWithTimeOut() {

        logThread("fakeApiRequest")
        withContext(Dispatchers.IO) {

            val job = withTimeoutOrNull(JOB_TIMEOUT) {

                val result1 = getResult1FromAPI() // wait until job is done
                setTextOnMainThread("Got $result1")

                val result2 = getResult2FromAPI() // wait until job is done
                setTextOnMainThread("Got $result2")

            } // waiting for job to complete...

            if(job == null){
                val cancelMessage = "Cancelling job...Job took longer than $JOB_TIMEOUT ms"
                println("debug: ${cancelMessage}")
                setTextOnMainThread(cancelMessage)
            }

        }




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
        delay(3000) //Does not block thread. Just suspends the coruthin sin die the thread
        return "Result #1"
    }

    private suspend fun getResult2FromAPI() : String {
        logThread("getResult2FromAPI")
        delay(1000) //Does not block thread. Just suspends the coruthin sin die the thread
        return "Result #2"
    }

    private fun logThread(methodName:String) {
        println("derrick: ${methodName}: ${Thread.currentThread().name}")
    }

}
