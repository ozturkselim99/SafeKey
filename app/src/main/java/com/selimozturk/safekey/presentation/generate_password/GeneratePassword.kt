package com.selimozturk.safekey.presentation.generate_password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.selimozturk.safekey.R
import com.selimozturk.safekey.databinding.FragmentGeneratePasswordBinding
import com.selimozturk.safekey.domain.util.copyTextToClipboard
import java.util.*

class GeneratePasswordFragment : Fragment() {

    companion object {
        const val uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        const val lowercase = "abcdefghijklmnopqrstuvwxyz"
        const val numbers = "0123456789"
        const val symbols = "!@#$%^&*()_+-=[]{}|;:,.<>?/~"
    }

    private var _binding: FragmentGeneratePasswordBinding? = null
    private val binding get() = _binding!!
    private val rand = Random()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGeneratePasswordBinding.inflate(inflater, container, false)
        setupListeners()
        return binding.root
    }

    private fun setupListeners() = with(binding) {
        layoutPassword.setEndIconOnClickListener {
            copyPassword()
        }

        edtTxtPassword.addTextChangedListener {
            it?.let {
                if (it.isNotEmpty()) {
                    layoutPassword.setEndIconDrawable(R.drawable.ic_copy_password)
                    layoutPassword.isEndIconVisible = true
                }
            }
        }

        sliderPasswordLength.addOnChangeListener { _, value, _ ->
            tvPasswordLength.text = value.toInt().toString()
        }

        btnGeneratePassword.setOnClickListener {
            edtTxtPassword.setText(
                generatePassword(
                    length = sliderPasswordLength.value.toInt(),
                    isNumbers = checkBoxNumbers.isChecked,
                    isSymbols = checkBoxSymbols.isChecked,
                    isLowercase = checkBoxLowercase.isChecked,
                    isUppercase = checkBoxUppercase.isChecked
                )
            )
        }
    }

    private fun copyPassword() = with(binding) {
        val password = edtTxtPassword.text.toString()
        if (password.isNotEmpty()) view?.copyTextToClipboard("password", password)
    }

    private fun generatePassword(
        length: Int,
        isNumbers: Boolean,
        isSymbols: Boolean,
        isLowercase: Boolean,
        isUppercase: Boolean
    ): String {
        var chars = ""
        var password = ""

        if (!isNumbers && !isSymbols && !isLowercase && !isUppercase) return ""
        if (isNumbers) chars += numbers
        if (isSymbols) chars += symbols
        if (isLowercase) chars += lowercase
        if (isUppercase) chars += uppercase

        repeat(length) {
            password += chars[rand.nextInt(chars.length)]
        }

        return password
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}