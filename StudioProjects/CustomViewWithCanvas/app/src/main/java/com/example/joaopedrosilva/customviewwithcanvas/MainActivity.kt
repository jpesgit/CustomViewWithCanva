package com.example.joaopedrosilva.customviewwithcanvas

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var graph :Graph
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    graph = findViewById (R.id.graph)
setPoint()
    }
fun setPoint(){
    Handler().postDelayed({
        //doSomethingHere()
    }, 10000)
    graph.setPoints(arrayListOf(Graph.Points("160", 1f),Graph.Points("60", 0.5f),Graph.Points("160", 0.2f) ))
}

}
