package uz.mahmudxon.mychat.dialog

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.progress_dialog.*
import kotlinx.android.synthetic.main.progress_dialog.view.*
import uz.mahmudxon.mychat.R

class ProgressDialog: AlertDialog {

    constructor(context: Context, message : String) : super(context)
    {
        val view = LayoutInflater.from(context).inflate(R.layout.progress_dialog, null, false)
        view.apply {
            text_pd.text = message
        }
        setView(view)
    }

    fun setMessage(s : String)
    {
        text_pd?.text = s
    }
}