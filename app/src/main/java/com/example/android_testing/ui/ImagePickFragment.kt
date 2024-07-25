package com.example.android_testing.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.android_testing.R
import com.example.android_testing.adapters.ImageAdapter
import com.example.android_testing.data.remote.response.ImageResponse
import com.example.android_testing.databinding.FragmentImagePickBinding
import com.example.android_testing.databinding.FragmentShoppingBinding
import com.example.android_testing.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ImagePickFragment : Fragment() {

    private var _binding: FragmentImagePickBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModel: MainViewModel

    @Inject
    lateinit var imageAdapter: ImageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentImagePickBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        imageSearch()
        observe()
        imageAdapter.setOnItemClickListener {
            viewModel.setCurrentImageUrl(it)
            findNavController().popBackStack()
        }
    }

    private fun setupRecyclerView() {
        binding.rvImages.apply {
            adapter = imageAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
        }
    }

    private fun imageSearch(){
        // Add TextWatcher to the EditText
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed here
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Perform the search here
                if (s.toString().length>=3){
                    viewModel.searchForImage(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // No action needed here
            }
        })
    }

    private fun observe(){
        viewModel.images.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { res->
                when (res.status){
                    Status.LOADING -> {
                    }
                    Status.SUCCESS -> {
                        imageAdapter.setData(res.data!!)
                    }
                    Status.ERROR -> {
                        Toast.makeText(requireContext(), res.message?:"", Toast.LENGTH_SHORT).show()
                    }
                    else->{}
                }
            }
        })
    }
}