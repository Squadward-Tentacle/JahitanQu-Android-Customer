package com.example.jahitanqu_customer.presentation.views.main.account

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.jahitanqu_customer.JahitanQu
import com.example.jahitanqu_customer.R
import com.example.jahitanqu_customer.common.utils.Util
import com.example.jahitanqu_customer.model.Address
import com.example.jahitanqu_customer.model.Customer
import com.example.jahitanqu_customer.prefs
import com.example.jahitanqu_customer.presentation.viewmodel.AuthViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_account.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class AccountFragment : Fragment(), View.OnClickListener {

    private val OPEN_CAMERA_REQUEST_CODE = 77
    private val SELECT_FILE_FROM_STORAGE = 88
    private val REQUEST_READ_STORAGE_PERMISSION = 99
    lateinit var currentPhotoPath: String
    lateinit var photoFile: File

    @Inject
    lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as JahitanQu).applicationComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authViewModel.isUpdated.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it) {
                initEditText()
            }
        })
        photoFile = File("")
        initEditText()
        btnEdit.setOnClickListener(this)
        btnSave.setOnClickListener(this)
        btnCamera.setOnClickListener(this)
        btnCamera.isClickable = false
    }

    private fun initEditText() {
        etFirstname.setText(prefs.keyFirstname)
        etLastName.setText(prefs.keyLastName)
        etEmail.setText(prefs.keyEmail)
        etPlace.setText(prefs.keyAddressName)
        etPhoneNumber.setText(prefs.keyPhoneNumber)
        if (!prefs.keyPhotoUrl.isNullOrEmpty()) {
            Picasso.get()
                .load(prefs.keyPhotoUrl)
                .placeholder(R.drawable.ic_user_placehoder)
                .error(R.drawable.ic_user_placehoder)
                .into(profile_image)
        }
    }

    override fun onClick(p0: View?) {
        when (p0) {
            btnEdit -> {
                changeStateEditable()
            }
            btnSave -> {
                val email = etEmail.text.toString()
                val fName = etFirstname.text.toString()
                val lName = etLastName.text.toString()
                val phone = etPhoneNumber.text.toString()
                if (Util.validationInput(
                        email,
                        fName,
                        lName,
                        phone
                    )
                ) {
                    val customer = Customer(
                        email = email,
                        firstname = fName,
                        lastname = lName,
                        address = Address(
                            addressName = "Jl.x",
                            latitude = (-6.90777).toFloat(),
                            longitude = (-6.90777).toFloat()
                        ),
                        phone = phone
                    )
                    authViewModel.updateCustomer(customer, photoFile)
                } else {
                    println("CANNOT EDIT")
                }
                changeStateNotEditable()
            }
            btnCamera -> {
                selectImage()
            }
        }
    }

    fun changeStateEditable() {
        btnEdit.visibility = View.GONE
        btnSave.visibility = View.VISIBLE
        etFirstname.isEnabled = true
        etLastName.isEnabled = true
        etEmail.isEnabled = true
        etPhoneNumber.isEnabled = true
        btnCamera.isClickable = true
        btnPlace.isClickable = true
    }

    fun changeStateNotEditable() {
        btnEdit.visibility = View.VISIBLE
        btnSave.visibility = View.GONE
        etFirstname.isEnabled = false
        etLastName.isEnabled = false
        etEmail.isEnabled = false
        etPhoneNumber.isEnabled = false
        btnCamera.isClickable = false
        btnPlace.isClickable = false
    }


    private fun selectImage() {
        val items = arrayOf<CharSequence>(
            "Take Photo", "Choose from Library",
            "Cancel"
        )
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setItems(items) { dialog, item ->
            when {
                items[item] == "Take Photo" -> {
                    openCamera()
                }
                items[item] == "Choose from Library" -> {
                    checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    browseFile()
                }
                items[item] == "Cancel" -> {
                    dialog.dismiss()
                }
            }
        }
        builder.show()
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.resolveActivity(activity?.packageManager!!)
        photoFile = createImageFile()
        val photoURI =
            FileProvider.getUriForFile(
                activity?.applicationContext!!,
                "com.example.jahitanqu_customer.fileprovider",
                photoFile
            )
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        startActivityForResult(
            cameraIntent,
            OPEN_CAMERA_REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == OPEN_CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
            profile_image.setImageBitmap(imageBitmap)
        }
        if (requestCode == SELECT_FILE_FROM_STORAGE && resultCode == Activity.RESULT_OK) {
            val originalPath = getOriginalPathFromUri(data?.data!!)
            val imageFile = File(originalPath)
            photoFile = imageFile
            val imageBitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
            profile_image.setImageBitmap(imageBitmap)
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        val fileNya = File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
        currentPhotoPath = fileNya.absolutePath
        println("FILE VALUE : $currentPhotoPath")
        return fileNya
    }

    private fun browseFile() {
        val selectFileIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(
            selectFileIntent,
            SELECT_FILE_FROM_STORAGE
        )
    }

    private fun getOriginalPathFromUri(contentUri: Uri): String? {
        var originalPath: String? = null
        val projection =
            arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? =
            activity?.contentResolver?.query(contentUri, projection, null, null, null)
        if (cursor?.moveToFirst()!!) {
            val columnIndex: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            originalPath = cursor.getString(columnIndex)
        }
        return originalPath
    }

    private fun checkPermission(permission: String) {
        when (permission) {
            Manifest.permission.WRITE_EXTERNAL_STORAGE -> {
                if (activity?.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ),
                        REQUEST_READ_STORAGE_PERMISSION
                    )
                }
            }
        }

    }
}
