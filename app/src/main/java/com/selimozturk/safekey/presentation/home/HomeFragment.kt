package com.selimozturk.safekey.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.selimozturk.safekey.R
import com.selimozturk.safekey.databinding.FragmentHomeBinding
import com.selimozturk.safekey.domain.model.Password
import com.selimozturk.safekey.domain.util.setVisible
import com.selimozturk.safekey.presentation.home.adapter.PasswordsAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val adapter = PasswordsAdapter()
    private val homeViewModel: HomeViewModel by viewModels()
    private val passwordList: MutableList<Password> = ArrayList()
    private val selectedCategory: MutableList<String> = ArrayList()
    private var searchText: String = ""
    private val dateFormatter = SimpleDateFormat("d MMM, yyyy", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.getPasswords()
        setupObservers()
        setupRecyclerview()
        setupListeners()
        onItemClick()
        getCurrentDate()
    }

    private fun setupObservers() {
        homeViewModel.passwords.observe(viewLifecycleOwner) { passwords ->
            binding.layoutNoResult.root.setVisible(passwords.isEmpty())
            loadPasswords(passwords)
            passwordList.clear()
            passwordList.addAll(passwords)
        }
    }

    private fun setupListeners() = with(binding) {
        btnAddPassword.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_navigation_add_password)
        }
        edtTxtSearch.addTextChangedListener { text ->
            searchText = text.toString()
            adapter.setData(filterRecyclerView(passwordList, searchText, selectedCategory))
        }
        chipGroupCategories.setOnCheckedStateChangeListener { group, _ ->
            selectedCategory.clear()
            val checkedChipsIds = group.checkedChipIds
            for (chipId in checkedChipsIds) {
                val chip = group.findViewById<Chip>(chipId)
                selectedCategory.add(chip.text.toString())
            }
            adapter.setData(filterRecyclerView(passwordList, searchText, selectedCategory))
        }
    }

    private fun setupRecyclerview() = with(binding) {
        val layoutManager = LinearLayoutManager(requireContext())
        rvPasswords.layoutManager = layoutManager
    }

    private fun loadPasswords(passwords: List<Password>) = with(binding) {
        adapter.setData(passwords)
        rvPasswords.adapter = adapter
    }

    private fun onItemClick() {
        adapter.onItemClicked = { password ->
            val bundle = Bundle()
            bundle.putInt("id", password.id)
            findNavController().navigate(
                R.id.action_navigation_home_to_passwordDetailFragment,
                bundle
            )
        }
    }

    private fun filterRecyclerView(
        passwordList: List<Password>,
        query: String,
        selectedCategory: List<String>
    ): List<Password> {
        val filteredList = passwordList.filter { password ->
            var includePassword = true
            if (query.isNotEmpty()) {
                includePassword = password.name.contains(query, ignoreCase = true)
            }
            if (selectedCategory.isNotEmpty()) {
                includePassword = includePassword &&
                        selectedCategory.any { category ->
                            password.category.displayName.contains(
                                category
                            )
                        }
            }
            includePassword
        }

        binding.layoutNoResult.root.setVisible(filteredList.isEmpty())

        return filteredList
    }

    private fun getCurrentDate() {
        binding.tvCurrentDate.text = dateFormatter.format(Date())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}