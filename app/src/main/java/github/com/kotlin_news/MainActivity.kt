package github.com.kotlin_news

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity

class MainActivity : AppCompatActivity() {


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {

//                val switchTimeStrToLong = "2017-07-24 16:01:26".switchTimeStrToLong()
//                val switchTimeStrToLong = TimeUtil.switchTimeStrToLong("2017-07-24 16:01:26")
//                log(switchTimeStrToLong.toString())
//                javaCodeTest.main(null)
//                val sdf= SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//                log(sdf.format(switchTimeStrToLong))
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {

                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navigation = findViewById(R.id.navigation) as BottomNavigationView
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        val mainNewFragment = MainNewFragment()
        supportFragmentManager.beginTransaction().add(R.id.content,mainNewFragment).commit()
    }

}
