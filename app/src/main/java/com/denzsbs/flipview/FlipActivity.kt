package com.denzsbs.flipview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class FlipActivity : AppCompatActivity() {

    lateinit var adapter: FlipViewSampleAdapter
    var adapterItems = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flip)
        for (i in 0..5) {
            adapterItems.add("flipView")
        }

        adapter = FlipViewSampleAdapter(adapterItems)
    }
}
