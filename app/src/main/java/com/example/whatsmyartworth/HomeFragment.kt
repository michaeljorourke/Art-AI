package com.example.whatsmyartworth

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputFilter.LengthFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import coil.load
import com.example.whatsmyartworth.databinding.FragmentHomeBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*


class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private var photourl: String? = null
    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                photourl = it.data!!.getStringExtra("PHOTO_URL")
                binding.btnUpload.isEnabled = true
                binding.imageView.load(photourl)
                //Toast.makeText(context, it.data!!.getStringExtra("PHOTO_URL"), Toast.LENGTH_SHORT)//saving photo to url and showing user path
                //  .show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    //moving from home fragment to takepicture activity, leaves if picture taken is successful
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)


        binding.btnTakePicture.setOnClickListener {
            cameraLauncher.launch(Intent(context, TakePictureActivity::class.java))
        }
        binding.btnUpload.setOnClickListener {
            val ref: StorageReference = FirebaseStorage.getInstance().getReference()

                .child(UUID.randomUUID().toString())
            // on below line adding a file to our storage.
            val photouri = Uri.parse(photourl)
            ref.putFile(photouri).addOnSuccessListener {
                // this method is called when file is uploaded.
                // in this case we are dismissing our progress dialog and displaying a toast message

                Toast.makeText(requireContext(), "Image Uploaded..", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                // this method is called when there is failure in file upload.
                Toast.makeText(requireContext(), "We Failed ${it.message}", Toast.LENGTH_SHORT)
                    .show()
            }

        }
        return binding.root
    }
}
