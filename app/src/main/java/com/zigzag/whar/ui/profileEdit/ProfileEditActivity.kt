package com.zigzag.whar.ui.profileEdit

import android.os.Bundle
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.textChanges
import com.mlsdev.rximagepicker.RxImageConverters
import com.mlsdev.rximagepicker.RxImagePicker
import com.mlsdev.rximagepicker.Sources
import com.salah.rxdatetimepicker.RxDateConverters
import com.salah.rxdatetimepicker.RxDateTimePicker
import com.zigzag.whar.R
import com.zigzag.whar.arch.BaseActivity
import com.zigzag.whar.common.Constants.PROFILE_IMAGE
import kotlinx.android.synthetic.main.activity_profile_edit.*
import javax.inject.Inject
import android.graphics.Bitmap
import java.io.ByteArrayOutputStream
import android.R.attr.bitmap
import android.R.attr.name
import android.content.Intent
import android.graphics.BitmapFactory
import com.zigzag.whar.ui.dashboard.DashboardActivity
import java.nio.ByteBuffer


class ProfileEditActivity : BaseActivity<ProfileEditActivityContract.View, ProfileEditActivityContract.Presenter>(), ProfileEditActivityContract.View{
    @Inject lateinit var profileEditActivityPresenter : ProfileEditActivityPresenter

    override fun getPresenterImpl(): ProfileEditActivityContract.Presenter = profileEditActivityPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_edit)
        initViews()
        initObservations()

    }

    private fun initViews() {
        disableSubmitButton()
    }

    private fun initObservations() {
        profile_image.clicks()
            .subscribe {
                // @TODO improve image picker - add crop
                RxImagePicker
                    .with(this@ProfileEditActivity)
                    .requestImage(Sources.CAMERA)
                    .flatMap{ uri -> RxImageConverters.uriToBitmap(this@ProfileEditActivity, uri)
                    }
                    .subscribe{ bitmap ->
                        profile_image.setImageBitmap(bitmap)
                    }
            }

        et_dob.clicks()
            .subscribe {
                hideKeyboard()
                RxDateTimePicker
                    .with(this@ProfileEditActivity)
                    .pickDateOnly()
                    .show()
                    .flatMap { date -> RxDateConverters.toString(date,"dd MMM yyyy") }
                    .subscribe { date ->
                        et_dob.setText(date)
                        validate()
                    }
            }

        et_display_name.textChanges()
                .subscribe {
                    validate()
                }

        btn_submit.clicks()
            .subscribe {
                startSubmitButtonAnimation()
                profile_image.isDrawingCacheEnabled = true
                profile_image.buildDrawingCache()
                presenter.updateUserDetails(et_display_name.text.toString(),et_dob.text.toString(),profile_image.drawingCache)
            }
    }

    private fun validate() {
        if(!et_dob.text.toString().isEmpty() && !et_display_name.text.toString().isEmpty())
            enableSubmitButton()
        else
            disableSubmitButton()
    }

    override fun disableSubmitButton() {
        btn_submit.isEnabled = false
        btn_submit.alpha = 0.5f
    }

    override fun enableSubmitButton() {
        btn_submit.isEnabled = true
        btn_submit.alpha = 1f
    }

    override fun startSubmitButtonAnimation() {
        btn_submit.startAnimation()
    }

    override fun revertSubmitButtonAnimation() {
        btn_submit.revertAnimation()
    }

    override fun gotoEditProfile() {
        //NO-OP
    }

    override fun onComplete() {
        startActivity(Intent(this@ProfileEditActivity,DashboardActivity::class.java))
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        validate()
        if(savedInstanceState.getByteArray(PROFILE_IMAGE)!=null){
            profile_image.setImageBitmap(BitmapFactory.decodeByteArray(savedInstanceState.getByteArray(PROFILE_IMAGE), 0, savedInstanceState.getByteArray(PROFILE_IMAGE).size))
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        if(profile_image.drawingCache!=null){
            val stream = ByteArrayOutputStream()
            profile_image.drawingCache.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()
            outState!!.putByteArray(PROFILE_IMAGE, byteArray)
        }
    }
}
