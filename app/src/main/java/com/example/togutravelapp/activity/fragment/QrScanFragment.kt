package com.example.togutravelapp.activity.fragment

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.budiyev.android.codescanner.*
import com.example.togutravelapp.R
import com.example.togutravelapp.activity.DetailObjectActivity
import com.example.togutravelapp.databinding.FragmentQrScanBinding
import com.example.togutravelapp.viewmodel.QRCodeViewModel



@Suppress("DEPRECATION")
class QrScanFragment : Fragment() {
    private var _binding : FragmentQrScanBinding? = null
    private val binding get() = _binding!!
    private lateinit var codeScanner: CodeScanner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQrScanBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPermissionn()
        codeScanner()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    private fun codeScanner(){
        val scannerView = binding.scannerView
        codeScanner = CodeScanner(requireContext(), scannerView)

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                activity?.runOnUiThread{
                    val delimiter = "-"
                    val text = it.text.split(delimiter)
                    val lokasi = text[0]
                    val id = text[1]
                    val nama = text[2]
                    Toast.makeText(requireContext(), "Scan result : ${nama}", Toast.LENGTH_SHORT).show()
                    val QRViewModel = ViewModelProvider(this@QrScanFragment,ViewModelProvider.NewInstanceFactory())[QRCodeViewModel::class.java]
                    QRViewModel.getObjetWisata(lokasi)
                    QRViewModel.objectWisata.observe(requireActivity()){ item ->
                        item.forEach{
                            if(id == it.id.toString()){
                                val intent = Intent(requireActivity(), DetailObjectActivity::class.java)
                                intent.putExtra("result",it)
                                startActivity(intent)
                                requireActivity().finish()
                            }else{
                                val scanningPros : TextView = binding.scanning
                                scanningPros.text = getString(R.string.scanningPros)
                            }
                        }
                    }

                }
            }
            codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
                activity?.runOnUiThread {
                    Toast.makeText(requireContext(),
                        "Camera initialization error: ${it.message}",
                        Toast.LENGTH_LONG).show()
                }
            }
        }
        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    private fun setupPermissionn(){
        val permission = ContextCompat.checkSelfPermission(requireContext(),
            android.Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED){
            makeRequest()
        }
    }

    private fun makeRequest(){
        ActivityCompat.requestPermissions(requireActivity(),
            arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isEmpty()|| grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(requireContext(), "kamu harus memberi ijin untuk menggunakan aplikasi ini!", Toast.LENGTH_SHORT).show()
                }else{
                    //success
                }
            }
        }
    }

    companion object {
        const val CAMERA_REQUEST_CODE = 101
    }
}