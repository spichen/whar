package com.zigzag.whar.rx

import android.app.DatePickerDialog
import android.content.Context
import com.mlsdev.rximagepicker.RxImagePicker
import io.reactivex.Observable
import java.util.*

/**
 * Created by salah on 14/1/18.
 */
class RxDatePicker(private var context: Context) {

    private var instance: RxDatePicker? = null

    @Synchronized
    public fun with(context: Context): RxDatePicker {
        if (instance == null) {
            instance = RxDatePicker(context.applicationContext)
        }
        return instance!!
    }

    fun showDatePicker() : Observable<Calendar>{
        return Observable.create { emitter ->
            val myCalendar = Calendar.getInstance()
            DatePickerDialog(context, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                emitter.onNext(myCalendar)
            }, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }
    }


}