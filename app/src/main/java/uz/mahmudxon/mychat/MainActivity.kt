package uz.mahmudxon.mychat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import uz.mahmudxon.mychat.dialog.ProgressDialog
import uz.mahmudxon.mychat.extentions.*
import uz.mahmudxon.mychat.fragment.MainFragment
import uz.mahmudxon.mychat.fragment.registration.EnterPhoneNumber
import uz.mahmudxon.mychat.fragment.registration.RegisterFragment
import uz.mahmudxon.mychat.fragment.registration.VeficationFragment
import uz.mahmudxon.mychat.model.PhoneNumber

class MainActivity : AppCompatActivity() {

    lateinit var database : DatabaseReference
    lateinit var phoneNumber : String
    lateinit var phone : PhoneNumber
    lateinit var dialog : ProgressDialog
    lateinit var registerFragment: RegisterFragment
    lateinit var mainFragment: MainFragment

    // fragment
    lateinit var enterPhoneNumber : EnterPhoneNumber
    lateinit var verificationFragment: VeficationFragment
    val timer =  Thread()
    {
        Thread.sleep(2000)
        if(FirebaseAuth.getInstance().currentUser != null)
        {
            startFragment(mainFragment)
        }
        else if(false){

        }
        else
        {
            enterPhoneNumber = EnterPhoneNumber()
            startFragment(enterPhoneNumber)
            enterPhoneNumber.listener = {
                phoneNumber = it
                verificationFragment =
                    VeficationFragment()
                addFragment(verificationFragment, phoneNumber)
                verificationFragment.listener = {
                   isNewUser(it)
                }
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainFragment = MainFragment()
        timer.start()
    }



    fun isNewUser(id : String)
    {
        dialog = ProgressDialog(this, "Checking user info...")
        dialog.show()
        database = FirebaseDatabase.getInstance().reference.child("Users")
        database.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.child(id).exists())
                {
                    clearRegistrationAndStartMainFragment()
                }
                else
                {
                    dialog.dismiss()
                    registerFragment = RegisterFragment()
                    phone = PhoneNumber(id, phoneNumber)
                    addFragment(registerFragment, phone)
                    registerFragment.listener = { isRegistired->
                        if (isRegistired)
                        {
                            clearRegistrationAndStartMainFragment()
                        }
                        else
                        {
                            toast("Registration error!!!")
                            FirebaseAuth.getInstance().signOut()
                            finish()
                        }
                    }
                }
            }
        })
    }

    private fun clearRegistrationAndStartMainFragment()
    {
        startFragment(mainFragment)
        clearFragmentManager()
        removeFragment(enterPhoneNumber)
        dialog.dismiss()
    }


    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount > 0)
        {
            supportFragmentManager.popBackStackImmediate()
            return
        }

        if(mainFragment.isDrawerOpen)
        {
            mainFragment.closeDrawer()
            return
        }

        super.onBackPressed()

    }
}
