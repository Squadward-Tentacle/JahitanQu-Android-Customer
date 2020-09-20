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
import android.widget.Button
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.jahitanqu_customer.JahitanQu
import com.example.jahitanqu_customer.R
import com.example.jahitanqu_customer.common.Common
import com.example.jahitanqu_customer.common.utils.Constant
import com.example.jahitanqu_customer.common.utils.Util
import com.example.jahitanqu_customer.model.Address
import com.example.jahitanqu_customer.model.Customer
import com.example.jahitanqu_customer.prefs
import com.example.jahitanqu_customer.presentation.viewmodel.AuthViewModel
import com.example.jahitanqu_customer.presentation.views.maps.MapsActivity
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_account.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class AccountFragment : Fragment(), View.OnClickListener {

    lateinit var currentPhotoPath: String
    lateinit var photoFile: File

    @Inject
    lateinit var authViewModel: AuthViewModel

    @Inject
    lateinit var firebaseAuth: FirebaseAuth


    lateinit var address: Address

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
                Common.dismissProgressDialog()
                Common.showPopUpDialog(
                    requireContext(),
                    getString(R.string.success),
                    getString(R.string.edit_account),
                    SweetAlertDialog.SUCCESS_TYPE
                )
            }
        })
        init()
    }

    private fun init() {
        photoFile = File("")
        initEditText()
        address = Address(
            prefs.keyAddressName!!,
            prefs.keyLatitude!!,
            prefs.keyLongitude!!,
            1
        )
        btnEdit.setOnClickListener(this)
        btnSave.setOnClickListener(this)
        btnCamera.setOnClickListener(this)
        btnLogout.setOnClickListener(this)
        btnCamera.isClickable = false
        btnPlace.isClickable = false
        btnPlace.isEnabled = false
        btnPlace.setOnClickListener(this)
        btnCancel.setOnClickListener(this)
    }

    private fun initEditText() {
        if (prefs.keyIdCustomer.isNullOrEmpty()) {
            btnEdit.visibility = View.GONE
        } else {
            btnEdit.visibility = View.VISIBLE
        }
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
            btnLogout -> {
                firebaseAuth.signOut()
                prefs.clear()
                activity?.finish()
            }
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
                    if (photoFile.length() > 0) {
                        val customer = Customer(
                            email = email,
                            firstname = fName,
                            lastname = lName,
                            address = address,
                            phone = phone
                        )
                        Common.showProgressDialog(this.requireContext())
                        authViewModel.updateCustomer(customer, photoFile)
                    } else {
                        Toast.makeText(
                            this.context,
                            "For now you must to change your foto before edit",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(this.context, "Please Fill all Field", Toast.LENGTH_SHORT).show()
                }
                changeStateNotEditable()
            }
            btnCamera -> {
                selectImage()
            }
            btnPlace -> {
                if (activity?.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    startActivityForResult(
                        Intent(this.context, MapsActivity::class.java),
                        Constant.REQUEST_CODE_MAPS
                    )
                } else {
                    checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }
            btnCancel -> {
                changeStateNotEditable()
                initEditText()
            }
        }
    }

    private fun changeStateEditable() {
        btnEdit.visibility = View.GONE
        layoutAccountEditable.visibility = View.VISIBLE
        etFirstname.isEnabled = true
        etLastName.isEnabled = true
        etEmail.isEnabled = true
        etPhoneNumber.isEnabled = true
        btnCamera.isClickable = true
        btnPlace.isClickable = true
        btnPlace.isEnabled = true
    }

    private fun changeStateNotEditable() {
        btnEdit.visibility = View.VISIBLE
        layoutAccountEditable.visibility = View.GONE
        etFirstname.isEnabled = false
        etLastName.isEnabled = false
        etEmail.isEnabled = false
        etPhoneNumber.isEnabled = false
        btnCamera.isClickable = false
        btnPlace.isClickable = false
        btnPlace.isEnabled = false
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
                    if (activity?.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        openCamera()
                    } else {
                        checkPermission(Manifest.permission.CAMERA)
                    }
                }
                items[item] == "Choose from Library" -> {
                    if (activity?.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        browseFile()
                    } else {
                        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    }
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
            Constant.OPEN_CAMERA_REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.OPEN_CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
            profile_image.setImageBitmap(imageBitmap)
        }
        if (requestCode == Constant.SELECT_FILE_FROM_STORAGE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val originalPath = getOriginalPathFromUri(data?.data!!)
                val imageFile = File(originalPath)
                photoFile = imageFile
                val imageBitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
                profile_image.setImageBitmap(imageBitmap)
            }
        }
        if (requestCode == Constant.REQUEST_CODE_MAPS) {
            if (data != null) {
                val latitude = data!!.getDoubleExtra(Constant.KEY_LATITUDE, 0.0)
                val longitude = data.getDoubleExtra(Constant.KEY_LONGITUDE, 0.0)
                val addresses = data.getStringExtra(Constant.KEY_ADDRESS)
                address = Address(
                    addresses,
                    latitude.toFloat(),
                    longitude.toFloat(),
                    2
                )
                etPlace.setText(addresses)
            }
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
            Constant.SELECT_FILE_FROM_STORAGE
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
                        Constant.REQUEST_READ_STORAGE_PERMISSION
                    )
                }
            }
            Manifest.permission.CAMERA -> {
                if (activity?.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.CAMERA
                        ),
                        Constant.REQUEST_READ_CAMERA_PERMISSION
                    )
                }
            }
            Manifest.permission.ACCESS_FINE_LOCATION -> {
                if (activity?.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ),
                        Constant.REQUEST_READ_FINE_LOCATION_PERMISSION
                    )
                }
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constant.REQUEST_READ_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                Toast.makeText(context, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == Constant.REQUEST_READ_STORAGE_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                browseFile()
            } else {
                Toast.makeText(context, "storage permission denied", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == Constant.REQUEST_READ_FINE_LOCATION_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivityForResult(
                    Intent(this.context, MapsActivity::class.java),
                    Constant.REQUEST_CODE_MAPS
                )
            } else {
                Toast.makeText(context, "location permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

}
