package com.example.togutravelapp.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.togutravelapp.R
import com.example.togutravelapp.data.CustomPassword
import com.example.togutravelapp.data.DummyTourGuideData
import com.example.togutravelapp.data.LoginForm
import com.example.togutravelapp.data.repository.UserRepository
import com.example.togutravelapp.databinding.ActivityLoginBinding
import com.example.togutravelapp.viewmodel.LoginRegisterViewModel
import com.example.togutravelapp.viewmodel.ViewModelFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText : CustomPassword
    private lateinit var fbDatabase : FirebaseDatabase
    private lateinit var loadingBar : ProgressBar
    private lateinit var gmailLoginButton : SignInButton
    private lateinit var loginButton : Button
    private val loginViewModel : LoginRegisterViewModel by viewModels {
        ViewModelFactory.getInstance(this@LoginActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        // Configure Google Sign In
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("628388227660-f6smkp93dr5bn1ud9bh0neh58nn9n12h.apps.googleusercontent.com")
            .requestEmail()
            .build()

        Glide.with(this)
            .load(R.drawable.logo)
            .centerCrop()
            .into(binding.locationDetailImage)

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Initialize Firebase Auth
        auth = Firebase.auth
        fbDatabase = Firebase.database

        loadingBar = binding.loadingLogin
        gmailLoginButton = binding.signInButtonGoogle
        loginButton = binding.btnLogin
        emailEditText = binding.emailEditText
        passwordEditText = binding.passwordEditText

        gmailLoginButton.setOnClickListener {
            signInWithGoogle()
        }

        loginButton.setOnClickListener {
            signIn()
        }

        val btnSignup = binding.signupButton
        btnSignup.setOnClickListener {
            val intent = Intent(this, SignUpActivity ::class.java)
            startActivity(intent)
        }

        loginViewModel.isLoginInProgress.observe(this){ isLoading ->
            if (isLoading) loadingBar.visibility = View.VISIBLE
            else loadingBar.visibility = View.GONE
        }

        loginViewModel.tokenSession.observe(this){
            Log.d(TAG, "UPDATE UI FROM API ACCOUNT OBSERVED")
            val repo = UserRepository(this)
            updateUIWithAPIAccount(it)
            val usersLogin = DummyTourGuideData(
                tgName = repo.getUserLoginInfoSession().nama,
                tgUrl = repo.getUserProfileImage().toString(),
                tgEmail = repo.getUserLoginInfoSession().email
            )
            if (!it.isNullOrEmpty() || it.toString() != "")
                pushToFirebaseRealtimeDatabase(null, usersLogin, pushType = 1)
        }
    }

    private fun signIn(){
        val loginForm = LoginForm(
            email = emailEditText.text.toString(),
            password = passwordEditText.text.toString()
        )
        loginViewModel.loginUser(loginForm)
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        resultLauncher.launch(signInIntent)
    }

    private var resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
                loadingBar.visibility = View.VISIBLE
                gmailLoginButton.visibility = View.INVISIBLE
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    val usersLogin = DummyTourGuideData(
                        tgName = user!!.displayName,
                        tgUrl = user.photoUrl.toString(),
                        tgEmail = user.email.toString()
                    )
                    pushToFirebaseRealtimeDatabase(user, usersLogin, pushType = 2)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUIWithGoogleAuth(null)
                }
            }
    }

    private fun updateUIWithGoogleAuth(currentUser: FirebaseUser?) {
        if (currentUser != null){
            loadingBar.visibility = View.INVISIBLE
            gmailLoginButton.visibility = View.VISIBLE
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Log.d(TAG, "USER IS NULL")
        }
    }

    private fun updateUIWithAPIAccount(token : String?) {
        if (!token.isNullOrEmpty()){
            loadingBar.visibility = View.INVISIBLE
            gmailLoginButton.visibility = View.VISIBLE
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Log.d(TAG, "EMAIL IS NULL")
        }
    }

    private fun pushToFirebaseRealtimeDatabase(userGoogle : FirebaseUser?, usersLogin: DummyTourGuideData, pushType : Int){
        val validEmail: String = if (pushType == 1) {
            val invalidEmail = usersLogin.tgEmail.toString()+"-1"
            invalidEmail.replace(".","dot")
        } else {
            val invalidEmail = usersLogin.tgEmail.toString()+"-2"
            invalidEmail.replace(".","dot")
        }

        val addUser = fbDatabase.reference
            .child("listUsers")
            .child(validEmail)
        val newUserLoginData = DummyTourGuideData(
            tgName = usersLogin.tgName.toString(),
            tgEmail = validEmail,
            tgUrl = usersLogin.tgUrl
        )
        addUser.get().addOnCompleteListener {
            if (it.result.childrenCount < 1){
                Log.d("WEH GAK KETEMUUUU", "pushToFirebaseRealtimeDatabase: ")
                addUser.push().setValue(newUserLoginData).addOnCompleteListener {
                    updateUIWithGoogleAuth(userGoogle)
                }.addOnFailureListener {
                    Toast.makeText(this,"Gagal Login",Toast.LENGTH_SHORT).show()
                    loadingBar.visibility = View.INVISIBLE
                    gmailLoginButton.visibility = View.VISIBLE
                }

            }
            else{
                Log.d("WEH KETEMUUUU", "pushToFirebaseRealtimeDatabase: ")
                updateUIWithGoogleAuth(userGoogle)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) updateUIWithGoogleAuth(currentUser)
        else {
            loginViewModel.getUserTokenSession()
        }
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}