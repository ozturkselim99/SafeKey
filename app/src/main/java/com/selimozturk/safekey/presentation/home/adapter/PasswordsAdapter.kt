package com.selimozturk.safekey.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.selimozturk.safekey.domain.util.copyTextToClipboard
import com.selimozturk.safekey.databinding.ItemPasswordBinding
import com.selimozturk.safekey.domain.model.Password
import com.selimozturk.safekey.domain.util.toFormattedDateTime

class PasswordsAdapter(var onItemClicked: ((Password) -> Unit) = {}) :
    RecyclerView.Adapter<PasswordsAdapter.PasswordViewHolder>() {

    private var items: List<Password> = emptyList()

    inner class PasswordViewHolder(private val binding: ItemPasswordBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(password: Password) = with(binding) {
            tvTitle.text = password.name
            tvUsernameMailOrPhoneNumber.text = password.name
            tvLastEdited.text = "Last Edited: ${password.lastEditedDate.toFormattedDateTime()}"
            imgBtnCopyPassword.setOnClickListener {
                it.copyTextToClipboard("password", password.password)
            }
            layoutItemPassword.setOnClickListener {
                onItemClicked(password)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PasswordViewHolder {
        val binding =
            ItemPasswordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PasswordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PasswordViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun setData(newPostItems: List<Password>) {
        val diffUtil = PasswordsDiffUtil(items, newPostItems)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        items = newPostItems
        diffResults.dispatchUpdatesTo(this)
    }

}