package uz.mahmudxon.mychat.fragment.registration

import android.content.Context
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_enter_phone_number.*
import uz.mahmudxon.mychat.R
import uz.mahmudxon.mychat.extentions.BaseFragment
import uz.mahmudxon.mychat.extentions.toast

class EnterPhoneNumber : BaseFragment(R.layout.fragment_enter_phone_number) {

    var isVailed = false

    var listener: ((String)->Unit)? = null

    override fun onCreatedView(senderData: Any?) {

        cpp.registerCarrierNumberEditText(number)
        cpp.setOnCountryChangeListener {

            setCountryCode()

        }

        cpp.setPhoneNumberValidityChangeListener {
            isVailed = it
            btn_continue.isEnabled = it
            if(it)
            {
                val imm =  activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view?.windowToken, 0)
            }
        }

        btn_continue.setOnClickListener {
            if(!isVailed)
            {
                toast("Wrong number!!!")
                return@setOnClickListener
            }



            listener?.invoke(cpp.fullNumberWithPlus)
        }
    }

    fun setCountryCode()
    {
        if(cpp.selectedCountryCode != null) {
            code_county.setText("+${cpp.selectedCountryCode}", TextView.BufferType.EDITABLE)
            number.setText("", TextView.BufferType.EDITABLE)
            number.isEnabled = true
        }
        else
        {
            number.setText("Country not selected", TextView.BufferType.EDITABLE)
            number.isEnabled = false
        }
    }
}