package com.anelcc.simplelogin.views

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.anelcc.simplelogin.data.Event
import com.anelcc.simplelogin.data.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Exception
import javax.inject.Inject

const val USERS = "users"

@HiltViewModel
class SimpleLoginViewModel @Inject constructor(
    val auth: FirebaseAuth,
    val db : FirebaseFirestore,
    val storage: FirebaseStorage
) : ViewModel() {


    var isInProgress = mutableStateOf(false)
    var singIn = mutableStateOf(false)
    var userData = mutableStateOf<UserData?>(null)
    var popNotification = mutableStateOf<Event<String>?>(null)

    init {
        //auth.signOut()
        val currentUser = auth.currentUser
        singIn.value = currentUser != null
        currentUser?.uid?.let { uid ->
           getUserData(uid)
        }
    }

    fun onSignOut() {
        auth.signOut()
    }

    fun onSignUp(userName: String, email: String, pass: String,  name: String,  website: String) {
        if (userName.isEmpty() or email.isEmpty() or pass.isEmpty()) {
            handledException(customMessage = "Please fill in all fields")
            return
        }

        isInProgress.value = true

        db.collection(USERS).whereEqualTo("username", userName.replace(" ","")).get()
            .addOnSuccessListener { documents ->
                if (documents.size() > 0) {
                    handledException(customMessage = "Username already exist")
                    isInProgress.value = false
                } else {
                    auth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                singIn.value = true
                                createOrUpdateProfile(userName = userName,
                                    name = name,
                                    email = email,
                                    website = website)
                            } else {
                                handledException(customMessage = "signed failed")
                            }
                            isInProgress.value = false
                        }
                        .addOnFailureListener {  }
                }
            }
            .addOnFailureListener {  }
    }

    private fun createOrUpdateProfile(
        name: String? = null,
        userName: String? = null,
        email: String? = null,
        website: String? = null) {
        val uid = auth.currentUser?.uid
        val userData = UserData(
            userId = uid,
            name = name ?: userData.value?.name,
            email = email ?: userData.value?.email,
            username = userName ?: userData.value?.username,
            website = website ?: userData.value?.website,
        )
        uid?.let {
            isInProgress.value = true
            db.collection(USERS).document(uid).get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        it.reference.update(userData.toMap())
                            .addOnSuccessListener {
                                this.userData.value = userData
                                isInProgress.value = false
                            }
                            .addOnFailureListener {
                                handledException(customMessage = "Cannot Update user")
                                isInProgress.value =false

                            }
                    } else {
                        db.collection(USERS).document(uid).set(userData)
                        getUserData(uid)
                        isInProgress.value = false
                    }
                }
                .addOnFailureListener { exception ->
                    handledException(exception, "Cannot create user")
                    isInProgress.value = false
                }
        }
    }

    private fun getUserData(uid: String) {
        isInProgress.value = true
        db.collection(USERS).document(uid).get()
            .addOnSuccessListener {
                val user = it.toObject<UserData>()
                userData.value = user
                isInProgress.value = false
//                popNotification.value = Event("User data retrieve Successfully")
            }
            .addOnFailureListener { exception ->
                handledException(exception, "Cannot retrieve user data")
                isInProgress.value = false
            }
    }

    fun onLogin(email: String, pass: String) {
        if (email.isEmpty() or pass.isEmpty()) {
            handledException(customMessage = "Please fill in all fields")
            return
        }
        isInProgress.value = true
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    singIn.value = true
                    isInProgress.value = false
                    auth.currentUser?.uid?.let { uid ->
//                        handledException(customMessage = "Login success")
                        getUserData(uid)
                    }
                } else {
                    handledException(task.exception, "Login failed")
                    isInProgress.value = false
                }
            }
            .addOnFailureListener { exc ->
                handledException(exc, "Login failed")
                isInProgress.value = false
            }
    }

    private fun handledException(exception: Exception? = null, customMessage: String = "") {
        exception?.printStackTrace()
        val errorMsg =  exception?.message ?: ""
        val message = if (customMessage.isEmpty()) { errorMsg } else { "$customMessage: $errorMsg" }
        popNotification.value = Event(message)
    }

}