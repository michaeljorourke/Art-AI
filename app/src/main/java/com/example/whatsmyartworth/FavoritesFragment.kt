package com.example.whatsmyartworth

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.load
import com.example.whatsmyartworth.databinding.FragmentFavoritesBinding
import com.example.whatsmyartworth.utils.sha256
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.IOException



class FavoritesFragment : Fragment(R.layout.fragment_favorites) {
    private lateinit var binding: FragmentFavoritesBinding
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val storageReference = storage.reference
    private val PICK_IMAGE_REQUEST = 123

    //creating a list of the information found on the realtime database
    private val userInformations = mutableListOf<UserInformation>()
    //creating a query to add information too
    private var query: Query? = null
    private val eventListener = object: ChildEventListener{
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            Log.d("FavoritesFragment", "onChildAddedCalled")
            val userInformation: UserInformation = snapshot.getValue<UserInformation>()!!
            Log.d("FavoritesFragment", userInformation.toString())
            if (userInformations.size < 6){
                val index = userInformations.size
                userInformations.add(index, userInformation)
                downloadPhoto(index, userInformation.uuid)
                showPrice(index, userInformation.price)
            } else {
                // YOU SHOULD REALLY CHECK THAT THE 0TH element is old
                userInformations.removeAt(0)
                userInformations.add(userInformations.size - 1, userInformation)
                userInformations.forEachIndexed { index, userInformation ->
                    downloadPhoto(index, userInformation.uuid)
                    showPrice(index, userInformation.price)
                }
            }
        }
        //getting the data from the realtime database and handling as needed
        override fun onChildRemoved(snapshot: DataSnapshot) {
            // FILL IN HERE THAT IF THIS IS CALLED YOU FIND AND REMOVE THAT CHILD FROM userInformations
            Log.d("FavoritesFragment", "onChildRemovedCalled")
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d("FavoritesFragment", "onCancelledCalled")
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            Log.d("FavoritesFragment", "onChildChangedCalled")
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            Log.d("FavoritesFragment", "onChildMovedCalled")
        }
    }
    //creating a binding view for the fragment
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }
    //querying through the realtime data base to get the six most recent images and there prices
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val nonNullEmail = Firebase.auth.currentUser?.email
        nonNullEmail ?: return
        val database = Firebase.database.reference
        // NICER IF WE COULD SAY .child("uuids")
        query = database.child("UserEmails").child(nonNullEmail.sha256()).orderByChild("date").limitToLast(6)
        query?.addChildEventListener(eventListener)
    }
    //clearing information if the fragment is switch
    override fun onDestroyView() {
        super.onDestroyView()
        query?.removeEventListener(eventListener)
        userInformations.clear()
    }
    //binding a image to a specific imageView
    private fun downloadPhoto(index: Int, uuid:String){
        try {
            // Get the current user's UID as a unique directory
            val email = firebaseAuth.currentUser?.email
            if (email != null) {
                val userStorageReference: StorageReference =
                    FirebaseStorage.getInstance().reference

                val imageReference = userStorageReference.child("$uuid")

                // Create a unique filename
                val filePath = File.createTempFile("${System.currentTimeMillis()}",".jpg")

                imageReference.getFile(filePath)
                    .addOnSuccessListener { taskSnapshot ->
                        val imageView = when(index) {
                            0 -> binding.imageView2
                            1 -> binding.imageView3
                            2 -> binding.imageView4
                            3 -> binding.imageView5
                            4 -> binding.imageView6
                            5 -> binding.imageView7
                            else -> error("Something is not right")
                        }
                        //Using Coil to hydrate a imageview
                        imageView.load(filePath)
                    }
                    .addOnFailureListener { exception ->
                        // Handle errors
                        Log.d("FavoritesFragment", "Something is not right " + exception.message)
                    }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    //Binding indexed prices to textView
    private fun showPrice(index: Int, price:Double){
        val textView = when(index) {
            0 -> binding.textView18
            1 -> binding.textView19
            2 -> binding.textView20
            3 -> binding.textView21
            4 -> binding.textView22
            5 -> binding.textView23
            else -> error("Something is not right")
        }

        textView.text = price.toString()
    }
}




