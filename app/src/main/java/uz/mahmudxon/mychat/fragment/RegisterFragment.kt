package uz.mahmudxon.mychat.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_register.*
import uz.mahmudxon.mychat.MainActivity
import uz.mahmudxon.mychat.R
import uz.mahmudxon.mychat.extentions.BaseFragment

class RegisterFragment : BaseFragment(R.layout.fragment_register) {

    var url = "https://firebasestorage.googleapis.com/v0/b/my-chat-a6349.appspot.com/o/ProfPic%2FplaceHolder.png?alt=media&token=687998f0-e64d-42d5-8c65-11338b755dd7"
    lateinit var userId : String
    override fun onCreatedView(senderData: Any?) {

        userId = senderData as String

        rf_back.setOnClickListener {
            finish()
        }
        rf_img_prof.setOnClickListener {
            CropImage.activity()
                .setAspectRatio(16, 9)
                .start(context!!, this@RegisterFragment)
        }
        rf_done.setOnClickListener {
            val reference = FirebaseDatabase.getInstance().reference.child("Users")

            val id =  reference.push().key!!
            val hashMap : HashMap<String, Any> = HashMap()
            hashMap["id"] = userId
            hashMap["firstname"] =  rf_first_name.text.toString()
            hashMap["lastname"] = rf_last_name.text.toString()
            hashMap["phone"] = "+998998002198"
            hashMap["imgUrl"] = url
            // (val id : String, var firstname : String, var lastname : String, var phone : String, var imgUrl : String)
            reference.child(id).setValue(hashMap).addOnSuccessListener {
                (activity as MainActivity).succesRegistration()
            }
        }
    }

    private fun uploadImg(imgUri : Uri){


        val fileReference = FirebaseStorage.getInstance().reference.child("ProfPic/${System.currentTimeMillis()}.jpg")

        fileReference.putFile(imgUri)
            .continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation fileReference.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    url = downloadUri.toString()

                } else {
                    Toast.makeText(requireContext(), "Fayl yuklash xatosi", Toast.LENGTH_SHORT).show()
                }
            }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                uploadImg(result.uri)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(requireContext(), "Something is error, while read images", Toast.LENGTH_SHORT).show()
            }
        }
    }


}