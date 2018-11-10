package com.denzsbs.flipview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.OrientationHelper
import com.dingmouren.layoutmanagergroup.skidright.SkidRightLayoutManager
import com.dingmouren.layoutmanagergroup.viewpager.ViewPagerLayoutManager
import kotlinx.android.synthetic.main.activity_flipview_with_recyclerview.*

class FlipViewWithRecyclerViewActivity : AppCompatActivity() {

    lateinit var adapter: FlipViewSampleAdapter
    var adapterItems = ArrayList<String>()
    lateinit var skidRightLayoutManager: ViewPagerLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flipview_with_recyclerview)
        for (i in 0..5) {
            adapterItems.add("flipView")
        }
        adapter = FlipViewSampleAdapter(adapterItems)
        skidRightLayoutManager = ViewPagerLayoutManager(this, OrientationHelper.VERTICAL)
        flipViewList.adapter = adapter
        flipViewList.layoutManager = skidRightLayoutManager
    }
}
