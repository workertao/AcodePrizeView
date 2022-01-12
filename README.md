# AcodePrizeView
中奖跑马灯动画 效果
## 效果图##
![效果图](https://github.com/workertao/acode_emoji_example/raw/master/images/GIF.gif)

## 使用方法 ##
在XML布局中声明
    <com.flakesnet.acodeprizeview.PrizeAnimView
        android:id="@+id/prizeView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"/>
        
在Activity中使用
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