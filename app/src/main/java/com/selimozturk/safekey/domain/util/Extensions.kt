package com.selimozturk.safekey.domain.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.selimozturk.safekey.R
import java.text.SimpleDateFormat
import java.util.*

fun View.copyTextToClipboard(label: String, password: String) {
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = ClipData.newPlainText(label, password)
    clipboardManager.setPrimaryClip(clipData)
    Snackbar.make(this, "Text copied to clipboard", Snackbar.LENGTH_SHORT)
        .setTextColor(ContextCompat.getColor(this.context, R.color.white))
        .setBackgroundTint(ContextCompat.getColor(this.context, R.color.dark_night_blue))
        .show()
}

fun checkEditTexts(vararg editTexts: EditText): Boolean {
    return editTexts.all {
        it.isNullorEmpty("Required field")
    }
}

fun View.setVisible(visible: Boolean) {
    visibility = if (visible) {
        View.VISIBLE
    } else {
        View.INVISIBLE
    }
}

fun EditText.isNullorEmpty(errorString: String): Boolean {
    val textInputLayout = this.parent.parent as TextInputLayout
    return if (text.toString().trim().isNotEmpty()) {
        textInputLayout.isErrorEnabled = false
        true
    } else {
        textInputLayout.error = errorString
        false
    }
}

fun Long.toFormattedDateTime(): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.getDefault())
    return formatter.format(this)
}
