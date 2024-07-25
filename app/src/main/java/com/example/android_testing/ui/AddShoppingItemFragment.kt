package com.example.android_testing.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.example.android_testing.R
import com.example.android_testing.databinding.FragmentAddShoppingItemBinding
import com.example.android_testing.databinding.FragmentShoppingBinding
import com.example.android_testing.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddShoppingItemFragment : Fragment(){
    private var _binding: FragmentAddShoppingItemBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var glide : RequestManager

    @Inject
    lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddShoppingItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
        binding.ivItem.setOnClickListener {
            findNavController().navigate(
                AddShoppingItemFragmentDirections.actionAddShoppingItemFragmentToImagePickFragment()
            )
        }
        binding.btnAddShoppingItem.setOnClickListener {
           viewModel.insertShoppingItem(
               binding.etShoppingItemName.text.toString(),
               binding.etShoppingItemAmount.text.toString(),
               binding.etShoppingItemPrice .text.toString()
           )
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.setCurrentImageUrl("")
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    private fun observe(){
        viewModel.currImageUrl.observe(viewLifecycleOwner, Observer {
            glide.load(it).into(binding.ivItem)
        })
        viewModel.insertShoppingItemStatus.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { res->
                when (res.status){
                    Status.LOADING -> {
                    }
                    Status.SUCCESS -> {
                        Toast.makeText(requireContext(), "Item added", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
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