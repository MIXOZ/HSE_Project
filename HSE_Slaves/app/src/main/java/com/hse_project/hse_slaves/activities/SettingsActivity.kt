package com.hse_project.hse_slaves.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.hse_project.hse_slaves.MainViewModel
import com.hse_project.hse_slaves.MainViewModelFactory
import com.hse_project.hse_slaves.R
import com.hse_project.hse_slaves.current.IS_TMP_USER
import com.hse_project.hse_slaves.current.TMP_USER_ID
import com.hse_project.hse_slaves.current.USER_ID
import com.hse_project.hse_slaves.current.USER_TOKEN
import com.hse_project.hse_slaves.image.getBitmapByString
import com.hse_project.hse_slaves.image.getStringByUri
import com.hse_project.hse_slaves.model.User
import com.hse_project.hse_slaves.model.UserRegistration
import com.hse_project.hse_slaves.repository.Repository
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.add_image
import kotlinx.android.synthetic.main.activity_register.cancel
import kotlinx.android.synthetic.main.activity_register.editTextTextDate
import kotlinx.android.synthetic.main.activity_register.editTextTextDescription
import kotlinx.android.synthetic.main.activity_register.editTextTextEmailAddress
import kotlinx.android.synthetic.main.activity_register.editTextTextFirstName
import kotlinx.android.synthetic.main.activity_register.editTextTextGeoData
import kotlinx.android.synthetic.main.activity_register.editTextTextPassword
import kotlinx.android.synthetic.main.activity_register.gallery
import kotlinx.android.synthetic.main.activity_register.specialization_
import kotlinx.android.synthetic.main.activity_register.submit
import kotlinx.android.synthetic.main.activity_register.user_role
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {
    //TODO для спинеров правильно выставлять начальное значение
    private lateinit var viewModel: MainViewModel
    private lateinit var data: User


    private var images: ArrayList<Uri> = ArrayList()
    private var position = 0
    private val PICK_IMAGE = 1

    private var userRole: String = "USER"
    private var specialization: String = "ART"

    private val imagesStringArray: ArrayList<String> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        addImages()
        addSpinners()
        initApi()
        run()
    }

    private fun loadUser() {
        val inflater = LayoutInflater.from(this)
        editTextTextEmailAddress.setText(data.username)
        editTextTextPassword.setText(data.password)
        editTextTextFirstName.setText(data.firstName)
        editTextTextGeoData.setText(data.lastName)
        editTextTextDate.setText(data.patronymic)
        editTextTextDescription.setText(data.description)
        for (i in 0 until data.images.size) {
            val view = inflater.inflate(R.layout.item_event_photo, gallery, false)

            val imageView: ImageView = view.findViewById<ImageButton>(R.id.imageView)

            imageView.setImageBitmap(getBitmapByString(data.images[i]))

            gallery.addView(view)
        }
        imagesStringArray.addAll(data.images)
        position = data.images.size
    }

    private fun addImages() {
        images = ArrayList()

        add_image.setOnClickListener {
            pickImagesIntent()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val inflater = LayoutInflater.from(this)
        if (requestCode == PICK_IMAGE) {
            if (requestCode == Activity.RESULT_FIRST_USER) {

                if (data!!.clipData != null) {
                    val count = data.clipData!!.itemCount
                    for (i in 0 until count) {
                        val imageUri = data.clipData!!.getItemAt(i).uri
                        images.add(imageUri)
                    }
                } else {
                    val imageUri = data.data
                    images.add(imageUri!!)
                }

                for (i in position until images.size) {
                    val str = getStringByUri(images[i], this)
                    imagesStringArray.add(str!!)

                    val view = inflater.inflate(R.layout.item_event_photo, gallery, false)

                    val imageView: ImageView = view.findViewById<ImageButton>(R.id.imageView)

                    imageView.setImageBitmap(getBitmapByString(str))

                    gallery.addView(view)
                }
                position = images.size
            }
        }
    }

    private fun pickImagesIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE)
    }

    private fun addSpinners() {
        val optionsSpecialization = arrayOf("ART", "LITERATURE", "MUSIC", "PHOTOGRAPHY")
        specialization_.adapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, optionsSpecialization)
        specialization_.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                specialization = optionsSpecialization[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                specialization = optionsSpecialization[0]
            }
        }

        val optionsUserRole = arrayOf("USER", "ORGANIZER", "CREATOR")
        user_role.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, optionsUserRole)
        user_role.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                userRole = optionsUserRole[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                userRole = optionsUserRole[0]
            }
        }
    }

    private fun run() {
        cancel.setOnClickListener {
            //startActivity(Intent(this@SettingsActivity, UserProfileActivity::class.java))
            onBackPressed()
        }

        logout.setOnClickListener {
            USER_TOKEN = ""
            USER_ID = 0
            TMP_USER_ID = 0
            IS_TMP_USER = false
            startActivity(Intent(this@SettingsActivity, LoginActivity::class.java))
        }

        submit.setOnClickListener {
            when {
                TextUtils.isEmpty(editTextTextEmailAddress.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@SettingsActivity,
                        "Please enter email.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                TextUtils.isEmpty(editTextTextPassword.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@SettingsActivity,
                        "Please enter password.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                TextUtils.isEmpty(editTextTextFirstName.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@SettingsActivity,
                        "Please enter first name.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                TextUtils.isEmpty(editTextTextGeoData.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@SettingsActivity,
                        "Please enter last name.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                TextUtils.isEmpty(editTextTextDate.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@SettingsActivity,
                        "Please enter patronymic.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                TextUtils.isEmpty(editTextTextDescription.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@SettingsActivity,
                        "Please enter description.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    val userName: String =
                        editTextTextEmailAddress.text.toString().trim { it <= ' ' }
                    val password: String = editTextTextPassword.text.toString().trim { it <= ' ' }
                    val firstName: String = editTextTextFirstName.text.toString().trim { it <= ' ' }
                    val lastName: String = editTextTextGeoData.text.toString().trim { it <= ' ' }
                    val patronymic: String =
                        editTextTextDate.text.toString().trim { it <= ' ' }
                    val description: String =
                        editTextTextDescription.text.toString().trim { it <= ' ' }
                    assert(imagesStringArray.size != 0)
                    //TODO заменить это на вызов метода, который будет менять пользователя и потом открывать профиль
                    viewModel.changeUser(
                        UserRegistration(
                            userRole,
                            firstName,
                            lastName,
                            patronymic,
                            userName,
                            password,
                            specialization,
                            description,
                            imagesStringArray,
                        )
                    )
                    viewModel.registerResponse.observe(this, { response ->
                        if (response.isSuccessful) {
                            Log.d("AAAAAa", "BBBBBb")
                        } else {
                            Log.d("AAAAAA", "convert(response)")
                            throw RuntimeException(response.toString())
                        }
                    })
                    startActivity(Intent(this@SettingsActivity, MainActivity::class.java))
                }
            }
        }
    }

    fun initApi() {
        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        viewModel.getMyUser()
        viewModel.userResponse.observe(this, { response ->
            if (response.isSuccessful) {
                data = response.body()!!
                loadUser()
            } else {
                throw RuntimeException(response.toString())
            }
        })
    }
}