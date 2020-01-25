package com.example.socialgramgabriel

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController

class MainActivity : AppCompatActivity() {

    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        textView = findViewById(R.id.message)

     //   moveToFragment(HomeFragment())
    }

    private fun moveToFragment(fragment: Fragment) {
        val fragmentTrans = supportFragmentManager.beginTransaction()
     //   fragmentTrans.replace(R.id.fragment_container, fragment)
        fragmentTrans.commit()
    }

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    textView.text = "Home"
                  //  moveToFragment(HomeFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_search -> {
                    textView.text = "Search"
                 //   moveToFragment(SearchFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_add_post -> {
                    textView.text = "Add post"
                 //   item.isChecked = false
                   // startActivity(Intent(this@MainActivity, AddPostActivity::class.java))
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_notifications -> {
                    textView.text = "Notificationn"
                //    moveToFragment(NotificationsFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_profile -> {
                    textView.text = "Profile"
                  //  moveToFragment(ProfileFragment())
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }



}
