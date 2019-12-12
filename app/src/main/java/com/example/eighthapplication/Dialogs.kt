package com.example.eighthapplication

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment


class TimeAlertDialog : DialogFragment() {
    interface Listener {
        fun cancelAlarm()
    }

    private var listener: Listener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        when (context) {
            is Listener -> listener = context
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setMessage("退勤時間になりました。" + "\n" + "今日の勤務時間を保存しましょう。")
        return builder.create()
    }
}

