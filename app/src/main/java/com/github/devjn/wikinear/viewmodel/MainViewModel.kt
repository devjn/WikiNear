package com.github.devjn.wikinear.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.devjn.wikinear.App
import com.github.devjn.wikinear.api.WikiApi
import com.github.devjn.wikinear.api.WikiApiService
import com.github.devjn.wikinear.cluster.Cluster
import com.github.devjn.wikinear.cluster.KMeans
import com.github.devjn.wikinear.cluster.KMeansListener
import com.google.android.gms.location.LocationServices
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


/**
 * Created by @author Jahongir on 26-Jan-19
 * devjn@jn-arts.com
 * MainViewModel
 */
class MainViewModel : ViewModel() {

    val data = MutableLiveData<String>()
    val progressBarVisibility = ObservableInt(View.GONE)

    private val service = WikiApi.createService(WikiApiService::class.java)

    private var lastList: List<String> = emptyList()
    private lateinit var disposable: Disposable

    init {
        doRequest()
    }

    fun doRequest() {
        disposable = getLastLocation(App.appContext)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .flatMap { getArticlesByCoordinate(it.latitude, it.longitude) }
            .flatMapObservable { Observable.fromIterable(it.query.geosearch) } // convert articles to observable
            .flatMapSingle { service.getArticleImages(it.pageid) } // get images of each article
            .observeOn(Schedulers.computation())
            .flatMap { Observable.fromIterable(it.query.pages.values.first().images) } // combine all images
            .flatMapSingle { Single.just(it.title) } // get only title
            .toList()
            .flatMap { findMostSimilarTitles(it) }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { progressBarVisibility.set(View.VISIBLE) }
            .doFinally { progressBarVisibility.set(View.GONE) }
            .subscribe({ result ->
                data.value = lastList.slice(result.biggestCluster.memberIndexes.asIterable()).joinToString(separator = "\n")
            }, { e: Throwable -> Log.w(App.TAG, "Error", e) })
    }

    private fun getArticlesByCoordinate(
        lat: Double,
        lng: Double
    ) = service.getArticlesByCoordinates("$lat|$lng")

    private fun findMostSimilarTitles(list: List<String>): Single<KMeans> {
        lastList = list
        return Single.create<KMeans> {
            val kMeans = KMeans(list.toTypedArray(), 20, 10, 123)
            kMeans.addKMeansListener(object : KMeansListener {
                override fun kmeansMessage(message: String?) {
                }

                override fun kmeansComplete(clusters: Array<out Cluster>, executionTime: Long) {
                    it.onSuccess(kMeans)
                }

                override fun kmeansError(t: Throwable) {
                    it.onError(t)
                }
            })
            kMeans.run()
        }
    }

    private fun getLastLocation(context: Context): Single<Location> {
        return Single.create { e ->
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                LocationServices.getFusedLocationProviderClient(context).lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        e.onSuccess(location)
                    } else
                        e.onError(Throwable("Your location settings is turned off"))
                }
            } else
                e.onError(Throwable("You haven't given the permissions"))
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }

}