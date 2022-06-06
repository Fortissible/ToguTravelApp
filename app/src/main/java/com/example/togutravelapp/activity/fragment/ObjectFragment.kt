package com.example.togutravelapp.activity.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.togutravelapp.R
import com.example.togutravelapp.activity.DetailObjectActivity
import com.example.togutravelapp.adapter.ListObjectAdapter
import com.example.togutravelapp.data.DummyObjectData
import com.example.togutravelapp.databinding.FragmentObjectBinding

class ObjectFragment : Fragment() {
    private var _binding: FragmentObjectBinding? = null
    private val binding get() = _binding!!
    private lateinit var objectRv : RecyclerView

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
        setObjectData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setObjectData(){
        val dummyListObjectData = getDummyListObjectData()
        val adapter = ListObjectAdapter(dummyListObjectData)
        objectRv.layoutManager = LinearLayoutManager(requireActivity())
        objectRv.adapter = adapter
        adapter.setOnItemClickCallback(object : ListObjectAdapter.OnItemClickCallback{
            override fun onItemClicked(data: DummyObjectData) {
                val intentToObjectDetail = Intent(requireActivity(),DetailObjectActivity::class.java)
                intentToObjectDetail.putExtra(DetailObjectActivity.EXTRA_DETAIL_OBJECT,data)
                startActivity(intentToObjectDetail)
            }
        })
    }

    private fun getDummyListObjectData():List<DummyObjectData>{
        val objectTitleList = resources.getStringArray(R.array.object_title)
        val objectDescList = resources.getStringArray(R.array.object_desc)
        val objectImageURLList = resources.getStringArray(R.array.object_url)
        val listObject = ArrayList<DummyObjectData>()
        for (i in objectTitleList.indices){
            listObject.add(
                DummyObjectData(
                    objectTitle = objectTitleList[i],
                    objectDesc = objectDescList[i],
                    objectUrl = objectImageURLList[i]
                )
            )
        }
        return listObject
    }
}