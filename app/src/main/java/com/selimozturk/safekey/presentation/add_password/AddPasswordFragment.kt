package com.selimozturk.safekey.presentation.add_password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.selimozturk.safekey.databinding.FragmentAddPasswordBinding
import com.selimozturk.safekey.domain.model.Password
import com.selimozturk.safekey.domain.util.PasswordCategory
import com.selimozturk.safekey.domain.util.checkEditTexts
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddPasswordFragment : Fragment() {

    private var _binding: FragmentAddPasswordBinding? = null
    private val binding get() = _binding!!
    private val addPasswordViewModel: AddPasswordViewModel by viewModels()
    private var updatedPasswordId: Int? = null
    private var selectedPasswordCategory: Int = -1
    private lateinit var tempPassword: Password
    private var autoFillChecked: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPasswordBinding.inflate(inflater, container, false)
        updatedPasswordId = arguments?.getInt("id")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setupObservers()
        setupSpinners()
        setupListeners()
    }

    private fun initViews() = with(binding) {
        if (updatedPasswordId != null) {
            btnAdd.text = "Update"
            tvTitle.text = "Update Password"
            getPasswordById()
        }
    }

    private fun setupObservers() = with(binding) {
        addPasswordViewModel.password.observe(viewLifecycleOwner) { password ->
            tempPassword = password
            selectedPasswordCategory = password.category.value
            spinnerCategories.setText(password.category.displayName, false)
            spinnerCategories.setSelection(password.category.value)
            edtTxtName.setText(password.name)
            edtTxtEmailAndUsername.setText(password.userId)
            edtTxtPassword.setText(password.password)
            switchAutoFill.isChecked = password.isAutoFillChecked
        }
    }

    private fun getPasswordById() {
        updatedPasswordId?.let { id -> addPasswordViewModel.getPasswordById(id) }
    }

    private fun setupListeners() = with(binding) {
        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        btnAdd.setOnClickListener {
            if (updatedPasswordId != null) {
                updatePassword()
            } else {
                insertPassword()
            }
        }
        switchAutoFill.setOnCheckedChangeListener { _, isChecked ->
            autoFillChecked = isChecked
        }
    }

    private fun insertPassword() = with(binding) {
        if (checkEditTexts(edtTxtEmailAndUsername, edtTxtName, edtTxtPassword, spinnerCategories)) {
            val password = Password(
                id = 0,
                userId = edtTxtEmailAndUsername.text.toString().trim(),
                name = edtTxtName.text.toString().trim(),
                createdDate = System.currentTimeMillis(),
                password = edtTxtPassword.text.toString().trim(),
                lastEditedDate = System.currentTimeMillis(),
                category = PasswordCategory.values()[selectedPasswordCategory],
                isAutoFillChecked = autoFillChecked
            )
            addPasswordViewModel.insertPassword(password)
            findNavController().popBackStack()
        }
    }

    private fun updatePassword() = with(binding) {
        if (checkEditTexts(edtTxtEmailAndUsername, edtTxtName, edtTxtPassword, spinnerCategories)) {
            val password = tempPassword.apply {
                userId = edtTxtEmailAndUsername.text.toString().trim()
                name = edtTxtName.text.toString().trim()
                password = edtTxtPassword.text.toString().trim()
                lastEditedDate = System.currentTimeMillis()
                category = PasswordCategory.values()[selectedPasswordCategory]
                isAutoFillChecked = autoFillChecked
            }
            addPasswordViewModel.updatePassword(password)
            findNavController().popBackStack()
        }
    }

    private fun setupSpinners() = with(binding) {
        val categories = PasswordCategory.values().map { category -> category.displayName }
        val passwordCategoriesAdapter = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            categories
        )
        spinnerCategories.setAdapter(passwordCategoriesAdapter)

        spinnerCategories.setOnItemClickListener { _, _, position, _ ->
            selectedPasswordCategory = position
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}