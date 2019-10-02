package uz.mahmudxon.mychat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import uz.mahmudxon.mychat.extentions.addFragment
import uz.mahmudxon.mychat.extentions.clearFragmentManager
import uz.mahmudxon.mychat.extentions.startFragment
import uz.mahmudxon.mychat.fragment.MainFragment
import uz.mahmudxon.mychat.fragment.EnterPhoneNumber
import uz.mahmudxon.mychat.model.User

class MainActivity : AppCompatActivity() {

    lateinit var database : DatabaseReference

    lateinit var regFrag : EnterPhoneNumber

    val timer =  Thread()
    {
        Thread.sleep(2000)
        if(FirebaseAuth.getInstance().currentUser != null && User.isExist("${FirebaseAuth.getInstance().currentUser?.uid}"))
        {
            startFragment(MainFragment())
        }
        else
        {
            regFrag = EnterPhoneNumber()
            startFragment(regFrag)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        database = FirebaseDatabase.getInstance().reference.child("Users")
        database.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                    User.users.clear()
                for (snapshot in p0.children)
                            User.users.add(snapshot.getValue(User::class.java)!!)
            }
        })

        timer.start()
    }


    fun succesRegistration()
    {
            startFragment(MainFragment())
        clearFragmentManager()
        supportFragmentManager.beginTransaction().remove(regFrag).commit()
    }
}
