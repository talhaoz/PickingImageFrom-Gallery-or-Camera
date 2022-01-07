package com.talhaoz.pickimagefromgalleryorcamera

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.talhaoz.pickimagefromgalleryorcamera.databinding.ActivityMainBinding
import com.talhaoz.pickimagefromgalleryorcamera.databinding.FragmentPickImageBinding
import java.io.File
import java.util.ArrayList


class PickImageFragment : Fragment() {

    private val rotateOpen : Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.rotate_open_anim) }
    private val rotateClose : Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.rotate_close_anim) }
    private val fromBottom : Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.from_bottom_anim) }
    private val toBottom : Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.to_bottom_anim) }
    private var clicked = false
    private val PHOTO_NAME = "photo"
    private lateinit var photoFile : File

    private var _binding: FragmentPickImageBinding? = null
    private val binding get() = _binding!!

    private val selectedImages = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uriList ->
        binding.selectedImage.setImageURI(uriList[0])
        onAddButtonClicked()
    }


    private val galleryPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            chooseImageFromGallery()
        }
        else {
            Toast.makeText(context,"App needs your permission to access gallery!", Toast.LENGTH_SHORT).show()
        }
    }

    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            //val data : Intent? = result.data
            val takenPhoto = BitmapFactory.decodeFile(photoFile.absolutePath)
            binding.selectedImage.setImageBitmap(takenPhoto)
            onAddButtonClicked()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPickImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickListeners()
    }


    private fun initClickListeners() {
        binding.AddNewImageButton.setOnClickListener {
            onAddButtonClicked()
        }

        binding.buttonChoosePhoto.setOnClickListener {
            Toast.makeText(context,"Choose Photo",Toast.LENGTH_SHORT).show()
            if(ActivityCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                galleryPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            } else {
                galleryPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }

        binding.buttonTakePhoto.setOnClickListener {
            Toast.makeText(context,"Take Photo",Toast.LENGTH_SHORT).show()
            val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            photoFile = getTakenPhoto(PHOTO_NAME)
            val fileProvider = FileProvider.getUriForFile(requireContext(),"com.talhaoz.pickimagefromgalleryorcamera.fileprovider",photoFile)
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT,fileProvider)
            cameraLauncher.launch(takePhotoIntent)

        }
    }

    private fun getTakenPhoto(fileName : String): File {
        val storageDir = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg",storageDir)
    }

    private fun chooseImageFromGallery() {
        selectedImages.launch("image/*")
    }

    private fun onAddButtonClicked(){
        setVisibility(clicked)
        setAnimation(clicked)
        setClickable(clicked)
        clicked = !clicked
    }

    private fun setVisibility(clicked : Boolean) {
        if(!clicked) {
            binding.buttonChoosePhoto.visible()
            binding.buttonTakePhoto.visible()
        } else {
            binding.buttonChoosePhoto.gone()
            binding.buttonTakePhoto.gone()
        }
    }

    private fun setAnimation(clicked : Boolean) {
        if(!clicked) {
            binding.buttonChoosePhoto.startAnimation(fromBottom)
            binding.buttonTakePhoto.startAnimation(fromBottom)
            binding.AddNewImageButton.startAnimation(rotateOpen)
        } else {
            binding.buttonChoosePhoto.startAnimation(toBottom)
            binding.buttonTakePhoto.startAnimation(toBottom)
            binding.AddNewImageButton.startAnimation(rotateClose)
        }
    }

    private fun setClickable(clicked : Boolean) {
        if(clicked) {
            binding.buttonChoosePhoto.isClickable = false
            binding.buttonTakePhoto.isClickable = false
        } else {
            binding.buttonChoosePhoto.isClickable = true
            binding.buttonTakePhoto.isClickable = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}