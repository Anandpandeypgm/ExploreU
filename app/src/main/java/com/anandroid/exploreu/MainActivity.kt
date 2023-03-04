package com.anandroid.exploreu

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.anandroid.exploreu.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    private lateinit   var binding: ActivityMainBinding

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val drawerLayout: DrawerLayout =findViewById(R.id.drawerLayout)
        val navView : NavigationView =findViewById(R.id.nav_view)
        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener{
            when(it.itemId){
                R.id.rateUs -> Toast.makeText(applicationContext, "Clicked rate us", Toast.LENGTH_SHORT).show()

            }
            true

        }


        val navController =findNavController(R.id.fragment)
        NavigationUI.setupWithNavController(binding.bottomNavigationView,navController)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
      if(toggle.onOptionsItemSelected(item)){
          return true
      }

        return super.onOptionsItemSelected(item)
    }

}