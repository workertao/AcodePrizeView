package com.flakesnet.acodeprizeview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    lateinit var prizeAnimView: PrizeAnimView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        prizeAnimView = findViewById(R.id.prizeView)
    }

    override fun onResume() {
        super.onResume()
        initPrizeData()
    }

    private fun initPrizeData() {
        val data = mutableListOf<PrizeModel>()
        val model = PrizeModel("第一个")
        val model1 = PrizeModel("骑着蜗牛找大象")
        val model2 = PrizeModel("我是你爸爸")
        val model3 = PrizeModel("你信不信")
        val model4 = PrizeModel("敢挂你机")
        data.add(model)
        data.add(model1)
        data.add(model2)
        data.add(model3)
        data.add(model4)
        prizeAnimView.data = data
        prizeAnimView.create()
    }
}