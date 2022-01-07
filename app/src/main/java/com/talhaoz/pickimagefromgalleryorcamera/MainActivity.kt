package com.talhaoz.pickimagefromgalleryorcamera

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.talhaoz.pickimagefromgalleryorcamera.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFragment()
    }

    private fun setFragment() {
        val pickImageFragment = PickImageFragment()
        supportFragmentManager
            .beginTransaction()
            .add(binding.fragmentHolder.id, pickImageFragment, "PICK_IMAGE_FRAGMENT")
            .commit()
    }
}