package com.zigzag.whar.ui.dashboard.feeds

import android.annotation.SuppressLint
import android.util.Log
import com.google.android.gms.location.LocationRequest
import com.google.firebase.firestore.GeoPoint
import com.salah.geohash.GeoHash
import com.zigzag.whar.old.BasePresenter
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider
import javax.inject.Inject

/**
 * Created by salah on 30/12/17.
 */

class FeedsPresenter @Inject constructor() : BasePresenter<FeedsContract.View>(), FeedsContract.Presenter  {

    companion object {
        val TAG = "DashboardPresenter"
    }

    var currentGeoHash : String = ""

    @SuppressLint("MissingPermission")
    override fun initiate(locationProvider: ReactiveLocationProvider) {
        val request = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setNumUpdates(5)
                .setInterval(100)

        locationProvider.getUpdatedLocation(request)
                .distinctUntilChanged()
                .subscribe { location ->
                    Log.d(TAG,location.latitude.toString() + "," + location.longitude.toString())
                    val p = GeoPoint(location.latitude,location.longitude)
                    currentGeoHash = GeoHash(p).geoHashString
                    view?.tempShowLocation(currentGeoHash)
                }
    }

    override fun sendMessage(text: String) {

        val messageData = HashMap<String,Any>()
        messageData["g"] = currentGeoHash
        messageData["message"] = text

        rxFirebaseFirestore
                .add("messages",messageData)
                .subscribe { documentReference ->
                    Log.d(TAG,documentReference.id)
                }
    }
}
