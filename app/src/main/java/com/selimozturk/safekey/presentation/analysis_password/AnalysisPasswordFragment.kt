package com.selimozturk.safekey.presentation.analysis_password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.futured.donut.DonutSection
import com.selimozturk.safekey.R
import com.selimozturk.safekey.databinding.FragmentAnalysisPasswordBinding
import com.selimozturk.safekey.domain.model.Password
import com.selimozturk.safekey.domain.util.setVisible
import com.selimozturk.safekey.presentation.analysis_password.adapter.AnalyzedPasswordsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnalysisPasswordFragment : Fragment() {

    private var _binding: FragmentAnalysisPasswordBinding? = null
    private val binding get() = _binding!!
    private val adapter = AnalyzedPasswordsAdapter()
    private val analysisPasswordViewModel: AnalysisPasswordViewModel by viewModels()
    private val passwordList: MutableList<Password> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnalysisPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerview()
        onItemClick()
        setupListeners()
        setupObservers()
        analysisPasswordViewModel.getPasswords()
    }

    private fun passwordCountByStrength(passwords: List<Password>) = with(binding) {
        val passwordStrengthCount = IntArray(3)
        passwords.forEach { password ->
            when (adapter.ratePassword(password.password)) {
                in 70..100 -> passwordStrengthCount[0]++
                in 50..69 -> passwordStrengthCount[1]++
                in 0..49 -> passwordStrengthCount[2]++
            }
        }
        tvWeakPasswordCount.text = passwordStrengthCount[1].toString()
        tvSafePasswordCount.text = passwordStrengthCount[0].toString()
        tvRiskPasswordCount.text = passwordStrengthCount[2].toString()
        createDonutView(
            passwordStrengthCount[0].toFloat(),
            passwordStrengthCount[1].toFloat(),
            passwordStrengthCount[2].toFloat()
        )
    }

    private fun setupListeners() = with(binding) {
        edtTxtSearch.addTextChangedListener { text ->
            searchPassword(text.toString())
        }
    }

    private fun setupObservers() {
        analysisPasswordViewModel.passwords.observe(viewLifecycleOwner) { passwords ->
            binding.layoutNoResult.root.setVisible(passwords.isEmpty())
            loadPasswords(passwords)
            passwordCountByStrength(passwords)
            passwordList.clear()
            passwordList.addAll(passwords)
        }
    }

    private fun setupRecyclerview() = with(binding) {
        val layoutManager = LinearLayoutManager(requireContext())
        rwAnalyzedPassword.layoutManager = layoutManager
    }

    private fun loadPasswords(passwords: List<Password>) = with(binding) {
        adapter.setData(passwords)
        rwAnalyzedPassword.adapter = adapter
    }

    private fun searchPassword(query: String) {
        val filteredList =
            passwordList.filter { password -> password.name.contains(query, ignoreCase = true) }
        binding.layoutNoResult.root.setVisible(filteredList.isEmpty())
        adapter.setData(filteredList)
    }

    private fun onItemClick() {
        adapter.onItemClicked = { password ->
            val bundle = Bundle()
            bundle.putInt("id", password.id)
            findNavController().navigate(
                R.id.action_navigation_analysis_password_to_navigation_password_detail,
                bundle
            )
        }
    }

    private fun createDonutView(
        safePasswordCount: Float,
        weakPasswordCount: Float,
        riskPasswordCount: Float
    ) {
        val section1 = DonutSection(
            name = "section_1",
            color = ContextCompat.getColor(requireContext(), R.color.safe_password_color),
            amount = safePasswordCount,
        )
        val section2 = DonutSection(
            name = "section_2",
            color = ContextCompat.getColor(requireContext(), R.color.weak_password_color),
            amount = weakPasswordCount,
        )
        val section3 = DonutSection(
            name = "section_3",
            color = ContextCompat.getColor(requireContext(), R.color.risk_password_color),
            amount = riskPasswordCount,
        )
        binding.donutView.submitData(listOf(section1, section2, section3))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}