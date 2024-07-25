package com.example.android_testing.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_testing.R
import com.example.android_testing.adapters.ImageAdapter
import com.example.android_testing.adapters.ItemAdapter
import com.example.android_testing.databinding.FragmentShoppingBinding
import com.example.android_testing.repositories.MainRepository
import com.example.android_testing.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ShoppingFragment : Fragment() {

    private var _binding: FragmentShoppingBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModel: MainViewModel

    @Inject
    lateinit var repo : MainRepository

    @Inject
    lateinit var itemAdapter: ItemAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShoppingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observe()
        binding.btnAddItem.setOnClickListener {
            findNavController().navigate(
                ShoppingFragmentDirections.actionShoppingFragmentToAddShoppingItemFragment()
            )
        }
    }

    private fun setupRecyclerView() {
        binding.rvShoppingItems.apply {
            adapter = itemAdapter
            layoutManager = LinearLayoutManager(requireContext())
            ItemTouchHelper(itemTouchCallback).attachToRecyclerView(this)
        }
    }

    private fun observe(){
        viewModel.shoppingItems.observe(viewLifecycleOwner, Observer {
            if (it!=null){
                itemAdapter.setData(it)
            }
        })
        viewModel.totalPrice.observe(viewLifecycleOwner, Observer {
            if (it!=null){
                binding.tvShoppingItemPrice.text = "Rs: $it"
            }
        })
    }

    //swipe to deleteFunctionality
    private val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(
        0, LEFT or RIGHT
    ){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ) = true

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val pos = viewHolder.layoutPosition
            val item = itemAdapter.items[pos]
            viewModel.deleteShoppingItem(item)
            Toast.makeText(requireContext(), "Item Deleted", Toast.LENGTH_SHORT).show()
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
