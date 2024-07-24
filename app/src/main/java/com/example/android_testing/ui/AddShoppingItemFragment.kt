package com.example.android_testing.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android_testing.R
import com.example.android_testing.databinding.FragmentAddShoppingItemBinding
import com.example.android_testing.databinding.FragmentShoppingBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddShoppingItemFragment : Fragment(){
    private var _binding: FragmentAddShoppingItemBinding? = null
    private val binding get() = _binding!!

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

        binding.ivItem.setOnClickListener {
            findNavController().navigate(
                AddShoppingItemFragmentDirections.actionAddShoppingItemFragmentToImagePickFragment()
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
}