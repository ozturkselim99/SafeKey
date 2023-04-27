package com.selimozturk.safekey.presentation.analysis_password.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.selimozturk.safekey.R
import com.selimozturk.safekey.databinding.ItemAnalyzedPasswordBinding
import com.selimozturk.safekey.domain.model.Password

class AnalyzedPasswordsAdapter(var onItemClicked: ((Password) -> Unit) = {}) :
    RecyclerView.Adapter<AnalyzedPasswordsAdapter.PasswordViewHolder>() {

    private var items: List<Password> = emptyList()

    inner class PasswordViewHolder(private val binding: ItemAnalyzedPasswordBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(password: Password) = with(binding) {
            val safePasswordColor =
                ContextCompat.getColor(itemView.context, R.color.safe_password_color)
            val weakPasswordColor =
                ContextCompat.getColor(itemView.context, R.color.weak_password_color)
            val riskPasswordColor =
                ContextCompat.getColor(itemView.context, R.color.risk_password_color)

            val passwordStrength = ratePassword(password.password)

            tvTitle.text = password.name
            tvPassword.text = password.password

            linearProgressIndicator.setIndicatorColor(
                when (passwordStrength) {
                    in 70..100 -> safePasswordColor
                    in 50..69 -> weakPasswordColor
                    else -> riskPasswordColor
                }
            )

            cardViewPasswords.backgroundTintList =
                ColorStateList.valueOf(
                    when (passwordStrength) {
                        in 70..100 -> safePasswordColor
                        in 50..69 -> weakPasswordColor
                        else -> riskPasswordColor
                    }
                )

            tvPasswordStrengh.text = passwordStrength.toString()
            linearProgressIndicator.progress = passwordStrength

            layoutItemAnalyzedPassword.setOnClickListener {
                onItemClicked(password)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PasswordViewHolder {
        val binding =
            ItemAnalyzedPasswordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PasswordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PasswordViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun setData(newPostItems: List<Password>) {
        val diffUtil = AnalyzedPasswordsDiffUtil(items, newPostItems)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        items = newPostItems
        diffResults.dispatchUpdatesTo(this)
    }

    fun ratePassword(password: String): Int {
        var score = 0
        val uppercaseCount = countUppercaseLetters(password)
        val lowercaseCount = countLowercaseLetters(password)
        val specialCharCount = countSpecialChar(password)
        val digitCount = countDigit(password)

        // Number of Characters
        score += password.length * 4

        // Uppercase Letters
        if (uppercaseCount > 0) score += (password.length - uppercaseCount) * 2

        // Lowercase Letters
        if (lowercaseCount > 0) score += (password.length - lowercaseCount) * 2

        // Symbols
        if (specialCharCount >= 2) score += specialCharCount * 6

        // Numbers
        if (lowercaseCount > 0 || uppercaseCount > 0 || specialCharCount > 0) score += digitCount * 4

        // Middle Numbers or Symbols
        score += calculateMiddleNumberOrSymbolScore(password) * 2

        // Requirements
        score += if (digitCount > 0 && lowercaseCount > 0 && uppercaseCount > 0 && specialCharCount > 0 && password.length >= 8) 10 else 0

        // Letters Only
        if (password.all { it.isLetter() }) {
            score -= password.length
        }

        // 	Numbers Only
        if (password.all { it.isDigit() }) {
            score -= password.length
        }

        // Consecutive Uppercase Letters
        score -= countConsecutiveUppercase(password) * 2

        // Consecutive Lowercase Letters
        score -= countConsecutiveLowercase(password) * 2

        // Consecutive Numbers
        score -= countConsecutiveNumbers(password) * 2

        // Sequential Letters (3+)
        score -= countSequentialLetters(password) * 3

        // Sequential Numbers (3+)
        score -= countSequentialNumbers(password) * 3

        // Sequential Symbols (3+)
        score -= countSequentialSymbols(password) * 3

        return when {
            score < 0 -> 0
            score > 100 -> 100
            else -> score
        }
    }


    private fun countConsecutiveUppercase(str: String): Int {
        var count = 0
        var currentCount = 0
        for (c in str) {
            if (c.isUpperCase()) {
                currentCount++
            } else {
                if (currentCount > 1) {
                    count += currentCount - 1
                }
                currentCount = 0
            }
        }
        if (currentCount > 1) {
            count += currentCount - 1
        }
        return count
    }

    private fun countConsecutiveLowercase(input: String): Int {
        var count = 0
        var maxCount = 0

        for (i in input.indices) {
            if (input[i].isLowerCase()) {
                count++
            } else {
                maxCount = maxOf(maxCount, count)
                count = 0
            }
        }
        return maxOf(maxCount, count)
    }

    private fun countUppercaseLetters(password: String): Int {
        return password.count { it.isUpperCase() }
    }

    private fun countLowercaseLetters(password: String): Int {
        return password.count { it.isLowerCase() }
    }

    private fun countSpecialChar(password: String): Int {
        return password.count { !it.isLetterOrDigit() }
    }

    private fun countDigit(password: String): Int {
        return password.count { it.isDigit() }
    }

    private fun calculateMiddleNumberOrSymbolScore(password: String): Int {
        return password.substring(1, password.length - 1)
            .count { it.isDigit() || !it.isLetterOrDigit() }
    }

    private fun countConsecutiveNumbers(password: String): Int {
        val consecutiveNumbersRegex = "\\d(?=\\d)".toRegex()
        return consecutiveNumbersRegex.findAll(password).count()
    }

    private fun countSequentialLetters(password: String): Int {
        val sequentialLettersRegex =
            "abc|bcd|cde|def|efg|fgh|ghi|hij|ijk|jkl|klm|lmn|nop|opq|pqr|qrs|rst|stu|tuv|uvw|vwx|wxy|xyz".toRegex(
                RegexOption.IGNORE_CASE
            )
        return sequentialLettersRegex.findAll(password).count()
    }

    private fun countSequentialNumbers(password: String): Int {
        val sequentialNumbersRegex =
            "(123|234|345|456|567|678|789|890|012|321|432|543|654|765|876|987|098|210)".toRegex()
        return sequentialNumbersRegex.findAll(password).count()
    }

    private fun countSequentialSymbols(password: String): Int {
        val sequentialSymbolsRegex = "[$&+,:;=?@#|'<>.^*()%!-]{3,}".toRegex()
        return sequentialSymbolsRegex.findAll(password).count()
    }

}