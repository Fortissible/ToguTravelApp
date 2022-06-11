package com.example.togutravelapp.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.togutravelapp.R
import com.example.togutravelapp.data.CustomPassword
import com.example.togutravelapp.data.RegisterForm
import com.example.togutravelapp.databinding.ActivitySignUpBinding
import com.example.togutravelapp.viewmodel.LoginRegisterViewModel
import com.example.togutravelapp.viewmodel.ViewModelFactory
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var profileBinding: CircleImageView
    private lateinit var emailEditText : TextInputEditText
    private lateinit var nameEditText : TextInputEditText
    private lateinit var telpEditText : TextInputEditText
    private lateinit var usernameEditText: TextInputEditText
    private lateinit var passEditText : CustomPassword
    private lateinit var confirmPassEditText : CustomPassword
    private lateinit var genderSelected : AutoCompleteTextView
    private lateinit var signupButton : Button
    private lateinit var progressBar : ProgressBar
    private lateinit var auth: FirebaseAuth
    private var imageUri : Uri? = null
    private val signupViewModel : LoginRegisterViewModel by viewModels {
        ViewModelFactory.getInstance(this@SignUpActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        auth = Firebase.auth
        if (auth.currentUser == null) {
            auth.signInAnonymously().addOnSuccessListener {
            }.addOnFailureListener {
            }
        }

        val btnCancel = binding.CancelButton
        btnCancel.setOnClickListener {
            val intent = Intent(this, LoginActivity ::class.java)
            startActivity(intent)
            finish()
        }

        profileBinding = binding.addPhotoProfile
        profileBinding.setOnClickListener {
            setImage()
        }

        emailEditText = binding.emailEditText
        nameEditText = binding.nameEditText
        telpEditText = binding.telpEditText
        usernameEditText = binding.usernameEditText
        passEditText = binding.passwordEditText
        confirmPassEditText = binding.confirmPasswordEditText
        genderSelected = binding.genderSelect
        progressBar = binding.progressBarRegister
        signupButton = binding.signupButton
        signupButton.setOnClickListener {
            checkForm()
        }

        setDropDown()

        signupViewModel.registerError.observe(this){
            progressBar.visibility = View.GONE
            Log.d("Observer Triggered", "result : $it")
            if (it == "false"){
                Toast.makeText(this,"Sukses Registrasi Akun",Toast.LENGTH_SHORT).show()
                val intent = Intent(this@SignUpActivity,LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            else
                Toast.makeText(this,"Gagal Registrasi Akun",Toast.LENGTH_SHORT).show()
        }
    }

    private fun setImage(){
        activityResult.launch("image/*")
    }

    private val activityResult = registerForActivityResult(
        ActivityResultContracts.GetContent()) {
        profileBinding.setImageURI(it)
        if (it != null) {
            imageUri = it
        }
        else
            imageUri = null
    }

    private fun setDropDown(){
        val dropdownItemGender = resources.getStringArray(R.array.gender_select)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item_gender,dropdownItemGender)
        binding.genderSelect.setAdapter(arrayAdapter)
    }

    private fun uploadData(filename : String, Uri: Uri? = null){
        progressBar.visibility = View.VISIBLE
        val formRegister = RegisterForm(
            nama = nameEditText.text.toString(),
            username = usernameEditText.text.toString(),
            notelp = telpEditText.text.toString(),
            email = emailEditText.text.toString(),
            password = passEditText.text.toString(),
            role = "user",
            gender =  genderSelected.text.toString().lowercase()
        )
        val storageFB = FirebaseStorage.getInstance().getReference("profile_images/$filename")
        if (imageUri != null)
            storageFB.putFile(Uri!!).addOnSuccessListener {
                Toast.makeText(this,"Sukses Upload Foto",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this,"Gagal Upload Foto",Toast.LENGTH_SHORT).show()
            }
        signupViewModel.registerUser(formRegister)
    }

    private fun checkForm(){
        if (emailEditText.text.isNullOrBlank() || nameEditText.text.isNullOrBlank() || telpEditText.text.isNullOrBlank()
            || genderSelected.text.isNullOrBlank() || passEditText.text.toString().length <= 8 ||
            passEditText.text.toString() != confirmPassEditText.text.toString()
        ) {
            Toast.makeText(this,"Semua form harus diisi dan tidak boleh kosong!",Toast.LENGTH_SHORT).show()
        }
        else {
            uploadData(emailEditText.text.toString(), imageUri)
        }
    }
}