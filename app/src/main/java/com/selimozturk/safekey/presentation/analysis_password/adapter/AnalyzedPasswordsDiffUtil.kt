package com.selimozturk.safekey.presentation.analysis_password.adapter

import androidx.recyclerview.widget.DiffUtil
import com.selimozturk.safekey.domain.model.Password

class AnalyzedPasswordsDiffUtil(
    private val oldList: List<Password>,
    private val newList: List<Password>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id != newList[newItemPosition].id
    }

}