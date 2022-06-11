package com.example.togutravelapp.activity.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.togutravelapp.R
import com.example.togutravelapp.activity.DetailObjectActivity
import com.example.togutravelapp.adapter.ListObjectAdapter
import com.example.togutravelapp.data.DummyObjectData
import com.example.togutravelapp.data.ObjectWisataResponseItem
import com.example.togutravelapp.databinding.FragmentObjectBinding
import com.example.togutravelapp.viewmodel.ObjectListViewModel
import com.example.togutravelapp.viewmodel.ViewModelFactory

class ObjectFragment : Fragment() {
    private var _binding: FragmentObjectBinding? = null
    private val binding get() = _binding!!
    private lateinit var objectRv : RecyclerView
    private lateinit var progressBar : ProgressBar
    private val listObjectViewModel : ObjectListViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentObjectBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        objectRv = binding.locationDetailObjectRv
        objectRv.setHasFixedSize(true)

        progressBar = binding.loadingListobject
        listObjectViewModel.loadingScreen.observe(requireActivity()){
            if (it == true) progressBar.visibility = View.VISIBLE
            else progressBar.visibility = View.INVISIBLE
        }
        listObjectViewModel.getObjetWisata()
        listObjectViewModel.objectWisata.observe(requireActivity()){
            setObjectData(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setObjectData(result : List<ObjectWisataResponseItem>){
        objectRv.layoutManager = LinearLayoutManager(requireContext())
        val adapter = ListObjectAdapter(result)
        objectRv.adapter = adapter
        adapter.setOnItemClickCallback(object : ListObjectAdapter.OnItemClickCallback{
            override fun onItemClicked(data: ObjectWisataResponseItem) {
                val intentToObjectDetail = Intent(requireActivity(),DetailObjectActivity::class.java)
                intentToObjectDetail.putExtra(DetailObjectActivity.EXTRA_DETAIL_OBJECT,data)
                startActivity(intentToObjectDetail)
            }
        })
    }
}