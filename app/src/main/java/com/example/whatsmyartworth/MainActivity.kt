package com.example.whatsmyartworth

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.whatsmyartworth.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    //view binding
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        //connection

        setContentView(binding.root)
        NetworkMonitor.register(binding.root)
        val homeFragment = HomeFragment()
        val favoritesFragment = FavoritesFragment()
        val settingsFragment = SettingsFragment()




        switchFragments(homeFragment)
        //add notifactions
        binding.bottomNavigationMenu.getOrCreateBadge(R.id.ic_favorites).number = 99
        binding.bottomNavigationMenu.getOrCreateBadge(R.id.ic_settings).number = 9

        binding.bottomNavigationMenu.setOnNavigationItemSelectedListener {
            when (it.itemId) {

                R.id.ic_home -> {
                    switchFragments(homeFragment)
                }
                R.id.ic_favorites -> {
                    switchFragments(favoritesFragment)
                }
                R.id.ic_settings -> {
                    switchFragments(settingsFragment)
                }
            }
            true
        }
    }

    //goes from one fragment to another
    private fun switchFragments(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout, fragment)
            commit()
        }
    }
    //Connection method
    override fun onDestroy() {
        super.onDestroy()
        NetworkMonitor.unregister(this)
    }
}