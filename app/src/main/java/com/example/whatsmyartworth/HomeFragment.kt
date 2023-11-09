package com.example.whatsmyartworth

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.text.InputFilter.LengthFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import coil.load
import com.example.whatsmyartworth.databinding.FragmentHomeBinding
import com.example.whatsmyartworth.utils.sha256
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserInfo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.security.MessageDigest
import java.util.*

//Data class to storage users information in the Realtime database.
data class UserInformation(
    val date: Long = 0,
    val uuid: String = "",
    val price: Double = 0.0,
    val priceCheck: Boolean = false,
    val email: String = "",
)

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private var photourl: String? = null
    private var cameraLauncher : ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                photourl = it.data!!.getStringExtra("PHOTO_URL")
                binding.btnUpload.isEnabled = true
                binding.imageView.load(photourl)
            }
        }

    }
    //moving from home fragment to takepicture activity, leaves if picture taken is successful
    //Setting up the user information for Firebase Realtime Database

// Data class used to tell the python script that there is something that needs to be evaluated by the AI
    data class UserUUID(
        val sha256: String,
        val priceCheck: Boolean
    )
//Ends the camera function when the fragment is changed
    override fun onDestroy() {
        super.onDestroy()
        cameraLauncher = null
    }
//creating a binding view to different buttons, giving them functionality
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.btnTakePicture.setOnClickListener {
            cameraLauncher?.launch(Intent(context, TakePictureActivity::class.java))
        }
        binding.btnUpload.setOnClickListener {


            val email = Firebase.auth.currentUser?.email
            email?.let {nonNullEmail ->
                // Name, email address, and profile photo Url

                val uuid = UUID.randomUUID().toString()
                val ref: StorageReference = FirebaseStorage.getInstance().reference
                    .child(
                        "$uuid"
                    ) //Displays user email with a random UUID spaced by "!"
                // on below line adding a file to our storage.
                val photouri = Uri.parse(photourl)
                ref.putFile(photouri).addOnSuccessListener {
                    // this method is called when file is uploaded.
                    // in this case we are dismissing our progress dialog and displaying a toast message
                    Toast.makeText(requireContext(), "Image Uploaded..", Toast.LENGTH_SHORT).show()

                    // Write a message to the database
                    val database = Firebase.database.reference
                   database.child("UserEmails").child(nonNullEmail.sha256()).child(uuid).setValue(UserInformation(date = System.currentTimeMillis(), uuid = uuid, price = 0.0, priceCheck = false, email = nonNullEmail))
                    database.child("UserUUIDs").child(uuid).setValue(UserUUID(priceCheck = false, sha256 = nonNullEmail.sha256()))

                }.addOnFailureListener {
                    // this method is called when there is failure in file upload.
                    Toast.makeText(requireContext(), "We Failed ${it.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
        return binding.root
    }
}