package com.gkgio.museum.feature.museum

import android.content.SharedPreferences
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.gkgio.horoscope.utils.SingleLiveEvent
import com.gkgio.museum.activity.MainViewModel
import com.gkgio.museum.base.BaseViewModel
import com.gkgio.museum.ext.isInitialized
import com.gkgio.museum.ext.isNonInitialized
import com.gkgio.museum.ext.nonNullValue
import com.gkgio.museum.feature.model.Exhibition
import com.gkgio.museum.feature.model.Museum
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.firestore.BuildConfig
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.storage.FirebaseStorage
import ru.terrakok.cicerone.Router
import timber.log.Timber
import javax.inject.Inject

class MuseumsViewModel @Inject constructor(
    private val prefs: SharedPreferences,
    private val router: Router
) : BaseViewModel() {

    private val firestoreDB = FirebaseFirestore.getInstance().apply {
        FirebaseFirestore.setLoggingEnabled(BuildConfig.DEBUG)
        firestoreSettings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
    }

    val state = MutableLiveData<State>()
    val errorEvent = SingleLiveEvent<String>()

    init {
        if (state.isNonInitialized()) {
            state.value = State(isProgress = true)

            loadMuseums()
        }
    }

    fun loadMuseums() {
        firestoreDB.collection(com.gkgio.museum.BuildConfig.TABLE_MUSEUMS_FIRESTORE_PATH)
            .get()
            .addOnSuccessListener { snapshot ->
                val museumList = mutableListOf<Museum>()
                val museumsListSnapshot = snapshot.documentChanges
                museumsListSnapshot.forEach {
                    val museum = Museum(
                        it.document.get("_id") as String,
                        it.document.get("exhibitions") as? ArrayList<Exhibition> ?: arrayListOf(),
                        it.document.get("image_url") as String,
                        it.document.get("latitude") as String,
                        it.document.get("longitude") as String,
                        it.document.get("title") as String
                    )
                    museumList.add(museum)
                }
                state.value = state.nonNullValue.copy(
                    isProgress = false,
                    museumsList = museumList
                )
            }
            .addOnFailureListener { reason ->
                Timber.e(reason.localizedMessage)
                errorEvent.value = reason.message
                state.value = state.nonNullValue.copy(
                    isProgress = false
                )
            }
    }

    fun onMuseumClick(museum: Museum) {

    }

    data class State(
        val isProgress: Boolean,
        val museumsList: MutableList<Museum>? = null
    )

}