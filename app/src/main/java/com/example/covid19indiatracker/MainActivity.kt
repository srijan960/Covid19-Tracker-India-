package com.example.covid19indiatracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    lateinit var stateAdapter: StateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        list.addHeaderView(LayoutInflater.from(this).inflate(R.layout.item_header,list,false))
        fetchRequests()
    }

    private fun fetchRequests() {
        GlobalScope.launch {
        val response = withContext(Dispatchers.IO){
         Client.api.execute()
        }
            if(response.isSuccessful){
                val data = Gson().fromJson(response.body?.string(),Response::class.java)
                launch(Dispatchers.Main) {
                    bindCombineData(data.statewise[0])
                    bindStateWiseData(data.statewise.subList(1, data.statewise.size))
                }
            }
    }
    }

    private fun bindStateWiseData(subList: List<StatewiseItem>) {
        stateAdapter = StateAdapter(subList)
        list.adapter = stateAdapter
    }

    private fun bindCombineData(data: StatewiseItem) {
        val lastUpdatedTime = data.lastupdatedtime
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
        lastUpdatedTv.text = "Last updated\n ${getTimeAgo(simpleDateFormat.parse(lastUpdatedTime))}"

        confirmedTv.text = data.confirmed
        recoveredTv.text = data.recovered
        activeTv.text = data.active
        deceasedTv.text = data.deaths


    }

    fun getTimeAgo(past: Date):String{
        val now = Date()
        val seconds = TimeUnit.MILLISECONDS.toSeconds(now.time - past.time)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(now.time - past.time)
        val hours = TimeUnit.MILLISECONDS.toHours(now.time - past.time)

        return when{
            seconds < 60 -> {
                "Few seconds ago"
            }
            minutes < 60 ->{
                "$minutes minutes ago"
            }
            hours < 24 ->{
                "$hours hour ${minutes % 60} min ago"
            }
            else ->{
                SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(past).toString()
            }
        }
    }
}