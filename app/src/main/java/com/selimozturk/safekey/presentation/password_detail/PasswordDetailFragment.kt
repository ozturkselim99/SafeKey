package com.selimozturk.safekey.presentation.password_detail

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.selimozturk.safekey.R
import com.selimozturk.safekey.databinding.DeletePasswordDialogBinding
import com.selimozturk.safekey.databinding.FragmentPasswordDetailBinding
import com.selimozturk.safekey.domain.util.copyTextToClipboard
import com.selimozturk.safekey.domain.util.toFormattedDateTime
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PasswordDetailFragment : Fragment() {

    private var _binding: FragmentPasswordDetailBinding? = null
    private val binding get() = _binding!!
    private val passwordDetailViewModel: PasswordDetailViewModel by viewModels()
    private var passwordId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPasswordDetailBinding.inflate(inflater, container, false)
        passwordId = arguments?.getInt("id")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getPasswordById()
        setupObservers()
        setupListeners()
    }

    private fun setupListeners() = with(binding) {
        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnDelete.setOnClickListener {
            deletePassword()
        }
        btnCopyPassword.setOnClickListener {
            it.copyTextToClipboard("password", tvPassword.text.toString())
        }
        btnUpdate.setOnClickListener {
            navigateToUpdatePassword()
        }
    }

    private fun navigateToUpdatePassword() {
        val bundle = Bundle()
        passwordId?.let { bundle.putInt("id", it) }
        findNavController().navigate(
            R.id.action_navigation_password_detail_to_navigation_add_password2, bundle
        )
    }

    private fun getPasswordById() {
        passwordId?.let { id -> passwordDetailViewModel.getPasswordById(id) }
    }

    private fun setupObservers() = with(binding) {
        passwordDetailViewModel.password.observe(viewLifecycleOwner) { password ->
            tvTitle.text = password.name
            tvUsernameMailOrPhoneNumber.text = password.userId
            tvCategory.text = password.category.displayName
            tvPassword.text = password.password
            tvCreatedDate.text = password.createdDate.toFormattedDateTime()
            tvLastEditedDate.text = password.lastEditedDate.toFormattedDateTime()
            switchAutoFill.isChecked = password.isAutoFillChecked
        }
    }

    private fun deletePassword() {
        val dialogBinding: DeletePasswordDialogBinding =
            DeletePasswordDialogBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext()).setView(dialogBinding.root).show()

        dialogBinding.btnDelete.setOnClickListener {
            passwordId?.let { id -> passwordDetailViewModel.deletePasswordById(id) }
            builder.dismiss()
            findNavController().popBackStack()
        }

        dialogBinding.btnCancel.setOnClickListener {
            builder.dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}