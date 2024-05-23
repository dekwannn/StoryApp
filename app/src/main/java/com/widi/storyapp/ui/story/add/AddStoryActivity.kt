package com.widi.storyapp.ui.story.add

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.widi.storyapp.R
import com.widi.storyapp.data.Result
import com.widi.storyapp.databinding.ActivityAddStoryBinding
import com.widi.storyapp.ui.camera.CameraActivity
import com.widi.storyapp.ui.camera.CameraActivity.Companion.CAMERAX_RESULT
import com.widi.storyapp.ui.main.ViewModelFactory
import com.widi.storyapp.ui.story.StoryActivity
import com.widi.storyapp.utils.reduceFileImage
import com.widi.storyapp.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody


class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private var currentImageUri: Uri? = null
    private var lat:Float? = null
    private var lon:Float? = null

    private val factory = ViewModelFactory.getInstance(this)
    private val addStoryViewModel by viewModels<AddStoryViewModel> {
        factory
    }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.apply {
            buttonGallery.setOnClickListener { startGallery() }
            buttonCamera.setOnClickListener { startCamera() }
            buttonAdd.setOnClickListener {
                if (edDescription.text.toString().isEmpty()) {
                    showToast(getString(R.string.empty_description_warning))
                    return@setOnClickListener
                }
                uploadStory()
            }
        }

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setView(R.layout.loading)
        val dialog: AlertDialog = builder.create()
        val intentHome = Intent(this,StoryActivity::class.java)

        addStoryViewModel.responseResult.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    dialog.show()
                }
                is Result.Success -> {
                    dialog.dismiss()
                    intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intentHome)
                    finish()
                }
                is Result.Error -> {
                    dialog.dismiss()
                    showToast(result.error)
                }

                else -> {}
            }
        }

    }

    private val launcherGallery =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            if (it != null) {
                currentImageUri = it
                showImage()
            } else {
                showToast(getString(R.string.failed_to_pick_image))
            }
        }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.imagePreview.setImageURI(it)
        }
    }

    private fun startCamera() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCamera.launch(intent)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERA_IMAGE)?.toUri()
            showImage()
        }
    }

    private fun uploadStory() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")
            val description = binding.edDescription.text.toString()
            val requestBodyDescription = description.toRequestBody("text/plain".toMediaType())
            val latRequestBody = lat?.toString()?.toRequestBody("text/plain".toMediaType())
            val lonRequestBody = lon?.toString()?.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )
            addStoryViewModel.addStory(multipartBody,requestBodyDescription,latRequestBody,lonRequestBody)
        } ?: showToast(getString(R.string.empty_image_warning))
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }

}