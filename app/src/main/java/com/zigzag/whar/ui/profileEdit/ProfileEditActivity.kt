package com.zigzag.whar.ui.profileEdit

import android.os.Bundle
import com.jakewharton.rxbinding2.view.clicks
import com.mlsdev.rximagepicker.RxImageConverters
import com.mlsdev.rximagepicker.RxImagePicker
import com.mlsdev.rximagepicker.Sources
import com.salah.rxdatetimepicker.RxDateConverters
import com.salah.rxdatetimepicker.RxDateTimePicker
import com.zigzag.whar.R
import com.zigzag.whar.arch.BaseActivity
import kotlinx.android.synthetic.main.activity_profile_edit.*
import javax.inject.Inject
import io.reactivex.Observable
import java.text.SimpleDateFormat


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
                RxDateTimePicker
                    .with(this@ProfileEditActivity)
                    .pickDateOnly()
                    .show()
                    .flatMap { date ->
                        RxDateConverters.toString(date,"dd MMM yyyy")
                    }
                    .subscribe { date ->
                        et_dob.setText(date)
                    }
            }

        btn_submit.clicks()
            .subscribe {
                startSubmitButtonAnimation()
                profile_image.isDrawingCacheEnabled = true
                profile_image.buildDrawingCache()
                presenter.updateUserDetails(et_display_name.text.toString(),profile_image.drawingCache)
            }
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
}
