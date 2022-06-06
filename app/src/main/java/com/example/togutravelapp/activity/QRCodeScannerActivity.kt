package com.example.togutravelapp.activity

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.togutravelapp.R
import com.example.togutravelapp.databinding.ActivityQrcodeScannerBinding

private const val CAMERA_REQUEST_CODE = 101
class QRCodeScannerActivity : AppCompatActivity() {
    private lateinit var codeScanner: CodeScanner
    private lateinit var binding: ActivityQrcodeScannerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrcodeScannerBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)
        setupPermissionn()
        codeScanner()
    }
    private fun codeScanner(){

        val scannerView = binding.scannerView
        codeScanner = CodeScanner(this, scannerView)

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                runOnUiThread{

                    if(it.text == "kris"){
                        val intent = Intent(this@QRCodeScannerActivity, DetailObjectActivity::class.java)
                        Toast.makeText(this@QRCodeScannerActivity, "Scan result : ${it.text}", Toast.LENGTH_SHORT).show()
                        startActivity(intent)
                        finish()
                    }else{
                        val scanningPros : TextView = binding.scanning
                        scanningPros.text = getString(R.string.scanningPros)
                    }
                }
            }
            codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
                runOnUiThread {
                    Toast.makeText(this@QRCodeScannerActivity,
                        "Camera initialization error: ${it.message}",
                        Toast.LENGTH_LONG).show()
                }
            }
        }
        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }
    override fun onResume(){
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
    private fun setupPermissionn(){
        val permission = ContextCompat.checkSelfPermission(this,
            android.Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED){
            makeReques()
        }
    }
    private fun makeReques(){
        ActivityCompat.requestPermissions(this,
            arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
                CAMERA_REQUEST_CODE -> {
                    if (grantResults.isEmpty()|| grantResults[0] != PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "kamu harus memberi ijin untuk menggunakan aplikasi ini!", Toast.LENGTH_SHORT).show()
                    }else{
                        //success
                }
            }
        }
    }
}