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
import com.gkgio.museum.feature.model.Item
import com.gkgio.museum.feature.model.Museum
import com.gkgio.museum.navigation.Screens
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.firestore.BuildConfig
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.storage.FirebaseStorage
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen
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

    @Suppress("UNCHECKED_CAST")
    private fun loadMuseums() {
        firestoreDB.collection(com.gkgio.museum.BuildConfig.TABLE_MUSEUMS_FIRESTORE_PATH)
            .get()
            .addOnSuccessListener { snapshot ->
                val museumList = mutableListOf<Museum>()
                val museumsListSnapshot = snapshot.documentChanges
                museumsListSnapshot.forEach {
                    val museum = Museum(
                        it.document.get("_id") as String,
                        getExhibitions(it.document.get("exhibitions") as? ArrayList<HashMap<String, Any>>?),
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

    @Suppress("UNCHECKED_CAST")
    private fun getExhibitions(exhibitionsMap: ArrayList<HashMap<String, Any>>?): List<Exhibition>? {
        if (exhibitionsMap.isNullOrEmpty()) return null

        val exhibitionsList = mutableListOf<Exhibition>()
        exhibitionsMap.forEach {
            val exhibition = Exhibition(
                it["_id"] as String,
                it["description"] as String,
                it["image_url"] as String,
                it["time"] as String,
                it["title"] as String,
                it["type"] as String,
                getItems(it["items"] as? ArrayList<HashMap<String, Any>>?)
            )
            exhibitionsList.add(exhibition)
        }

        return exhibitionsList
    }

    @Suppress("UNCHECKED_CAST")
    private fun getItems(itemsMap: ArrayList<HashMap<String, Any>>?): List<Item>? {
        if (itemsMap.isNullOrEmpty()) return null

        val itemList = mutableListOf<Item>()
        itemsMap.forEach {
            val item = Item(
                it["_id"] as String,
                it["author"] as String,
                it["ibeacon_uuid"] as String,
                it["images"] as ArrayList<String>,
                it["description"] as? String?,
                it["ibeacon_minor_id"] as String,
                it["ibeacon_major_id"] as String,
                it["audio_files"] as ArrayList<String>,
                it["title"] as? String?
            )
            itemList.add(item)
        }
        return itemList
    }


    private fun createMockExhibitions() {
        val exhibitionsList = mutableListOf<Exhibition>()
        val exhibitionAleksand2 = Exhibition(
            "1",
            "Влюбленная на кентавре",
            "http:/tsaritsyno-museum.ru/uploads/2019/12/vlyublennaya-zhaba-3-1800x1200.jpg",
            "19 ноября 2019 — 10 мая 2020",
            "Влюбленная жаба и другие",
            "permanent",
            listOf()
        )

        val exhibitionSilverPantry = Exhibition(
            "2",
            "12 декабря в рамках выставки «Екатерининский корпус Монплезира. Путешествие из Петергофа в Царицыно» открывается экспозиция уникальных документов из Государственного архива РФ (ГАРФ)",
            "http://tsaritsyno-museum.ru/uploads/2019/12/1.jpg",
            "20 декабря 2019 — 16 февраля 2020",
            "Отказ от трона. Цесаревич Константин Павлович и император Александр I",
            "permanent",
            listOf()
        )

        val exhibition3 = Exhibition(
            "2",
            "Собрание скульптуры из Московского музея-усадьбы Останкино с 13 ноября 2014 года можно увидеть в музее-заповеднике «Царицыно», в залах открытого после реставрации Оперного дома.  Выставка шедевров скульптуры из Останкинского дворца в стенах царицынского Оперного дома — это попытка воплотить в одном проекте две мечты, которые не удалось реализовать в XVIII веке.  Средний дворец (название «Оперный дом» появилось в XIX веке)",
            "http://tsaritsyno-museum.ru/uploads/2017/09/1521_mainfoto1_03-1.jpg",
            "20 декабря 2019 — 16 февраля 2020",
            "Мраморные головы загадочно смотрящие",
            "permanent",
            listOf()
        )
        exhibitionsList.add(exhibitionAleksand2)
        exhibitionsList.add(exhibitionSilverPantry)
        exhibitionsList.add(exhibition3)

        state.value = state.nonNullValue.copy(
            exhibitionsList = exhibitionsList
        )
    }

    fun onMuseumClick(museum: Museum) {
        if (museum.exhibitions != null) {
            router.navigateTo(Screens.MuseumDetailContainerScreen(museum.title, museum.exhibitions))
        }
    }

    fun onExhibitionClick(exhibition: Exhibition) {

    }

    data class State(
        val isProgress: Boolean,
        val museumsList: MutableList<Museum>? = null,
        val exhibitionsList: MutableList<Exhibition>? = null
    )

}