package net.nicbell.dogbreeds.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import net.nicbell.dogbreeds.R
import net.nicbell.dogbreeds.databinding.ActivityMainBinding


/**
 * Main activity, holds fragment navigation.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setSupportActionBar(binding.toolbar)
        NavigationUI.setupActionBarWithNavController(this, findNavController(R.id.navHost))
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}