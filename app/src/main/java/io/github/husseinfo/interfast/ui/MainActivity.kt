package io.github.husseinfo.interfast.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.github.husseinfo.interfast.R
import io.github.husseinfo.interfast.databinding.ActivityMainBinding
import io.github.husseinfo.interfast.isFirstRun
import io.github.husseinfo.interfast.saveFirstRun

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host)
        navView.setupWithNavController(navController)

        if (isFirstRun(this)) {
            navController.navigate(R.id.navigation_settings)
            saveFirstRun(this)
        }
    }
}
