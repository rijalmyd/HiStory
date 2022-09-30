package com.rijaldev.history.ui.addpost

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.rijaldev.history.R
import com.rijaldev.history.data.remote.Result
import com.rijaldev.history.data.remote.response.UploadResponse
import com.rijaldev.history.databinding.ActivityAddBinding
import com.rijaldev.history.databinding.LayoutBottomSheetBinding
import com.rijaldev.history.ui.main.MainActivity
import com.rijaldev.history.utils.MapsUtil.getAddressName
import com.rijaldev.history.utils.createCustomTempFile
import com.rijaldev.history.utils.reduceFileImage
import com.rijaldev.history.utils.rotateBitmap
import com.rijaldev.history.utils.uriToFile
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class AddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val addViewModel by viewModels<AddViewModel>()
    private var location: Location? = null

    private lateinit var currentPhotoPath: String
    private var mFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!isAllPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setUpToolbar()
        setUpView()
    }

    private fun setUpView() {
        binding.apply {
            contentUpload.apply {
                btnAddFoto.setOnClickListener { showBottomSheet() }
                imagePhoto.setOnClickListener { showBottomSheet() }
                btnClearPhoto.setOnClickListener {
                    mFile = null
                    ivPhoto.setImageDrawable(null)
                    isImageVisible(false)
                }
                tvLokasi.setOnClickListener {
                    getMyLastLocation()
                }
            }
            btnAddStory.buttonAdd.setOnClickListener {
                isFabOnLoading(true)
                uploadImage()
            }
        }
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }
    }

    private fun uploadImage() {
        if (mFile != null) {
            val file = reduceFileImage(mFile as File)
            val description = binding.contentUpload.edAddDescription.text.toString()
            if (description.isEmpty()) {
                binding.contentUpload.edAddDescription.error = getString(R.string.error_desc)
                isFabOnLoading(false)
                return
            }
            addViewModel.getToken().observe(this) {
                it?.let {
                    addViewModel.addStory(it, file, description, location).observe(this, uploadObserver)
                }
            }
        } else {
            isFabOnLoading(false)
            Toast.makeText(this, getString(R.string.error_choose_img), Toast.LENGTH_SHORT).show()
        }
    }

    private val uploadObserver = Observer<Result<UploadResponse>> {
        when (it) {
            is Result.Success -> {
                isFabOnLoading(false)
                Toast.makeText(this, it.data?.message, Toast.LENGTH_SHORT).show()

                val intent = Intent().apply {
                    putExtra("isUploaded", true)
                }
                setResult(MainActivity.UPLOAD_STATUS, intent)
                finish()
            }
            is Result.Error -> {
                isFabOnLoading(false)
                Toast.makeText(this, it.msg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startGallery() {
        val intent = Intent().apply {
            action = ACTION_GET_CONTENT
            type = "image/*"
        }
        val chooser = Intent.createChooser(intent, getString(R.string.choose_image))
        launcherIntentGallery.launch(chooser)
    }

    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(this).also {
            val photoUri: Uri = FileProvider.getUriForFile(
                this,
                "com.rijaldev.history.ui.addpost",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            mFile = myFile

            val result = rotateBitmap(
                BitmapFactory.decodeFile(mFile?.path),
                isBackCamera
            )

            isImageVisible(true)
            binding.contentUpload.ivPhoto.setImageBitmap(result)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            mFile = myFile

            val result = BitmapFactory.decodeFile(mFile?.path)
            isImageVisible(true)
            binding.contentUpload.ivPhoto.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val selectedImage: Uri = it.data?.data as Uri
            val myFile = uriToFile(selectedImage, this)
            mFile = myFile

            isImageVisible(true)
            binding.contentUpload.ivPhoto.setImageURI(selectedImage)
        }
    }

    private fun isAllPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!isAllPermissionsGranted()) {
                Toast.makeText(this, getString(R.string.error_permission), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun isImageVisible(isVisible: Boolean) {
        binding.contentUpload.apply {
            ivPhoto.isVisible = isVisible
            btnClearPhoto.isVisible = isVisible
            bgClearBtn.isVisible = isVisible
            btnAddFoto.isVisible = !isVisible
        }
    }

    private fun isFabOnLoading(isLoading: Boolean) {
        binding.btnAddStory.apply {
            ivArrow.isVisible = !isLoading
            progressBar.isVisible = isLoading
        }
    }

    private fun showBottomSheet() {
        val dialog = BottomSheetDialog(this, R.style.SheetDialog)
        val view = LayoutBottomSheetBinding.inflate(layoutInflater)
        dialog.setContentView(view.root)
        dialog.show()
        view.apply {
            ivGallery.setOnClickListener {
                startGallery()
                dialog.dismiss()
            }
            ivCamera.setOnClickListener {
                startCamera()
                dialog.dismiss()
            }
            ivCameraX.setOnClickListener {
                startCameraX()
                dialog.dismiss()
            }
        }
    }

    private fun getMyLastLocation() {
        if (isAllPermissionsGranted()) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    binding.contentUpload.tvLokasi.text =
                        getAddressName(location.latitude, location.longitude, this)
                    this.location = location
                }
                else Toast.makeText(this, getString(R.string.no_location), Toast.LENGTH_SHORT).show()
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                getMyLastLocation()
            }
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                getMyLastLocation()
            }
            else -> {}
        }
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}