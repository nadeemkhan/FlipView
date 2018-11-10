package com.denzsbs.flipview


import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.dingmouren.layoutmanagergroup.skidright.SkidRightLayoutManager
import kotlinx.android.synthetic.main.activity_flip.*

class FlipViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flip)

        frontView.setOnClickListener {
            startActivity(Intent(applicationContext, FlipViewWithRecyclerViewActivity::class.java))
        }
    }
}
