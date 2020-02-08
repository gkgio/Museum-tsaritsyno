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

            createMockExhibitions()
        }
    }

    fun onSwipeToRefresh() {
        loadMuseums()
    }

    private fun loadMuseums() {
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

    private fun createMockExhibitions() {
        val exhibitionsList = mutableListOf<Exhibition>()
        val exhibitionAleksand2 = Exhibition(
            "1",
            "Колекция одного из петербургских дворцов - музеев в \"Царицыне\"",
            "http://tsaritsyno-museum.ru/uploads/2019/05/aleksandr-II-613x960.jpg",
            "19 ноября 2019 — 10 мая 2020",
            "Екатеринский копус Монплезира",
            "permanent",
            listOf()
        )

        val exhibitionSilverPantry = Exhibition(
            "2",
            "Произведения русского ювелирного искусства XVI-XX веков",
            "http://tsaritsyno-museum.ru/uploads/2017/09/797_mainfoto1_03.jpg",
            "12 декабря 2019 — 16 февраля 2020",
            "Серебрянная кладовая \"Царицына\"",
            "permanent",
            listOf()
        )
        exhibitionsList.add(exhibitionAleksand2)
        exhibitionsList.add(exhibitionSilverPantry)

        state.value = state.nonNullValue.copy(
            exhibitionsList = exhibitionsList
        )
    }

    fun onMuseumClick(museum: Museum) {

    }

    fun onExhibitionClick(exhibition: Exhibition) {

    }

    data class State(
        val isProgress: Boolean,
        val museumsList: MutableList<Museum>? = null,
        val exhibitionsList: MutableList<Exhibition>? = null
    )

}