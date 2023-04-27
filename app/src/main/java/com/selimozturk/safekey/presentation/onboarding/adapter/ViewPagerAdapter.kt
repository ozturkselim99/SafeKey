package com.selimozturk.safekey.presentation.onboarding.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.selimozturk.safekey.R
import com.selimozturk.safekey.databinding.OnboardingContentBinding

class ViewPagerAdapter : RecyclerView.Adapter<ViewPagerAdapter.ContentViewHolder>() {

    private val titles = listOf(
        R.string.onboarding_title_1,
        R.string.onboarding_title_2,
        R.string.onboarding_title_3
    )

    private val descriptions = listOf(
        R.string.onboarding_description_1,
        R.string.onboarding_description_2,
        R.string.onboarding_description_3
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        val binding =
            OnboardingContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        with(holder.binding) {
            tvTitle.setText(titles[position])
            tvDescription.setText(descriptions[position])
        }
    }

    override fun getItemCount(): Int = 3

    inner class ContentViewHolder(val binding: OnboardingContentBinding) :
        RecyclerView.ViewHolder(binding.root)

}
