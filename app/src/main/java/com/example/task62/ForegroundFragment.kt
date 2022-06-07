package com.example.task62

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.coroutines.*
import java.lang.Math.sqrt
import java.lang.Runnable
import java.math.BigDecimal
import java.math.MathContext
import kotlin.math.pow


@OptIn(DelicateCoroutinesApi::class)
class ForegroundFragment : Fragment() {

    private lateinit var job: Job
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_foreground, container, false)

        val textPi = view.findViewById<TextView>(R.id.text_pi)

        var k = 2.0
        var sum = BigDecimal(2.0)

        parentFragmentManager.setFragmentResultListener("requestKey", this) { key, bundle ->
            when (bundle.getString("state")!!) {

                "start" -> {
                    job = GlobalScope.launch(Dispatchers.IO) {
                        while(job.isActive){

                            sum = (sum.times(BigDecimal(k)).times(BigDecimal(k))).divide(
                                BigDecimal((k-1)*(k+1)),1000,0)

                            if (k % 1000 == 0.0) {
                               withContext(Dispatchers.Main) {
                                    textPi.text = sum.toString()
                                }
                            }

                            k += 2.0
                        }
                    }
                }

                "stop" -> {
                    job.cancel()
                }

                "reset" -> {
                    job.cancel()
                    job = GlobalScope.launch(Dispatchers.IO) {
                        k = 2.0
                        sum = BigDecimal(2.0)
                        while(job.isActive){

                            sum = (sum.times(BigDecimal(k)).times(BigDecimal(k))).divide(
                                BigDecimal((k-1)*(k+1)),1000,0)

                            if (k % 1000 == 0.0) {
                                withContext(Dispatchers.Main){
                                    textPi.text = sum.toString()
                                }
                            }

                            k += 2.0
                        }
                    }
                }
            }


        }

        return view
    }


}