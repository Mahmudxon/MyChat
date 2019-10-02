package uz.mahmudxon.mychat.fragment

import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.database.*
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrConfig
import com.r0adkll.slidr.model.SlidrInterface
import com.r0adkll.slidr.model.SlidrPosition
import kotlinx.android.synthetic.main.fragment_verification.*
import uz.mahmudxon.mychat.MainActivity
import uz.mahmudxon.mychat.R
import uz.mahmudxon.mychat.extentions.BaseFragment
import uz.mahmudxon.mychat.extentions.toast
import uz.mahmudxon.mychat.model.User
import java.util.concurrent.TimeUnit

class VeficationFragment : BaseFragment(R.layout.fragment_verification) {
    var slidrInterface : SlidrInterface ?= null
    lateinit var auth : FirebaseAuth
    lateinit var storedVerificationId : String
    private lateinit var database: DatabaseReference

    val callbacks = object : OnVerificationStateChangedCallbacks()
    {
        override fun onVerificationCompleted(p0: PhoneAuthCredential) {

            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without

//           signInWithPhoneAuthCredential(p0)

        }

        override fun onVerificationFailed(p0: FirebaseException) {

        }

        override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
            storedVerificationId = p0
        }


    }


    override fun onCreatedView(senderData: Any?) {
        auth = FirebaseAuth.getInstance()
                vf_dec.text = "${vf_dec.text} ${senderData as String}"
        vf_back.setOnClickListener {
            finish()
        }

        vf_done.setOnClickListener {
            if(vf_code.text.toString().length == 6)
            {
                veryficationCode(vf_code.text.toString())
            }
        }


        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            senderData, // Phone number to verify
            120, // Timeout duration
            TimeUnit.SECONDS, // Unit of timeout
            requireActivity(), // Activity (for callback binding)
            callbacks) // OnVerificationStateChangedCallbacks
    }


    private fun veryficationCode(code : String)
    {
        val credential = PhoneAuthProvider.getCredential(storedVerificationId!!, code)
        signInWithPhoneAuthCredential(credential)
    }

    override fun onResume() {
        super.onResume()
        if(slidrInterface == null && view != null)
        {
            slidrInterface = Slidr.replace(view!!.findViewById(R.id.vf_layout), SlidrConfig.Builder().position(SlidrPosition.LEFT).build())
        }
    }


    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information


                    val user = task.result?.user


                    user?.let { done(it) }


                    // ...
                } else {
                    // Sign in failed, display a message and update the UI

                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid

                    }
                }
            }
    }

    private fun done(user: FirebaseUser) {
        if(User.isExist(user.uid))
            addFragment(RegisterFragment(), user.uid)
        else
        {
            (activity as MainActivity).succesRegistration()
        }
    }
}