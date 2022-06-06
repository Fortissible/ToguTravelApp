package com.example.togutravelapp.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.togutravelapp.data.DummyTourGuideData
import com.example.togutravelapp.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
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
    private lateinit var fbDatabase : FirebaseDatabase
    private lateinit var loadingBar : ProgressBar
    private lateinit var loginButton : SignInButton

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

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Initialize Firebase Auth
        auth = Firebase.auth
        fbDatabase = Firebase.database

        loadingBar = binding.loadingLogin
        loginButton = binding.signInButtonGoogle

        loginButton.setOnClickListener {
            signIn()
        }
        val btnLogin = binding.btnLogin
        btnLogin.setOnClickListener {
            val intent = Intent(this, MainActivity ::class.java)
            startActivity(intent)
        }
        val btnSignup = binding.signupButton
        btnSignup.setOnClickListener {
            val intent = Intent(this, SignUpActivity ::class.java)
            startActivity(intent)
        }

    }

    private fun signIn() {
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
                loginButton.visibility = View.INVISIBLE
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
                        tgUid = user.uid
                    )
                    val addUser = fbDatabase.reference
                        .child("listUsers")
                        .child(user.uid)

                    addUser.get().addOnCompleteListener {
                        if (it.result.childrenCount < 1)
                            addUser.push().setValue(usersLogin).addOnCompleteListener {
                                updateUI(user)
                            }.addOnFailureListener {
                                Toast.makeText(this,"Gagal Login",Toast.LENGTH_SHORT).show()
                                loadingBar.visibility = View.INVISIBLE
                                loginButton.visibility = View.VISIBLE
                            }
                        else
                            updateUI(user)
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null){
            loadingBar.visibility = View.INVISIBLE
            loginButton.visibility = View.VISIBLE
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            Log.d(TAG, "HALOOOOO")
            finish()
        } else {
            Log.d(TAG, "USER IS NULLLLL")
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}