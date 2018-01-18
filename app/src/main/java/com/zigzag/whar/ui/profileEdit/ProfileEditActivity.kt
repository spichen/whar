package com.zigzag.whar.ui.profileEdit

import android.os.Bundle
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.textChanges
import com.mlsdev.rximagepicker.RxImagePicker
import com.mlsdev.rximagepicker.Sources
import com.salah.rxdatetimepicker.RxDateConverters
import com.salah.rxdatetimepicker.RxDateTimePicker
import com.zigzag.whar.R
import com.zigzag.whar.arch.BaseActivity
import kotlinx.android.synthetic.main.activity_profile_edit.*
import javax.inject.Inject
import android.content.Intent
import android.net.Uri
import com.jakewharton.rxbinding2.widget.editorActions
import com.squareup.picasso.Picasso
import com.zigzag.whar.ui.dashboard.DashboardActivity

class ProfileEditActivity : BaseActivity<ProfileEditActivityContract.View, ProfileEditActivityContract.Presenter>(), ProfileEditActivityContract.View{
    @Inject lateinit var profileEditActivityPresenter : ProfileEditActivityPresenter

    private lateinit var profileImageUri : Uri

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
                    .subscribe{ uri ->
                        profileImageUri = uri
                        Picasso.with(this@ProfileEditActivity)
                                .load(uri)
                                .into(profile_image)
                    }.track()
            }.track()

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
                    }.track()
            }.track()

        et_first_name.textChanges()
                .subscribe {
                    validate()
                }.track()

        et_first_name.editorActions()
                .subscribe {
                    validate()
                    et_last_name.requestFocus()
                }.track()

        et_last_name.editorActions()
                .subscribe {
                    validate()
                    et_dob.performClick()
                }.track()

        btn_submit.clicks()
            .subscribe {
                startSubmitButtonAnimation()
                profile_image.isDrawingCacheEnabled = true
                profile_image.buildDrawingCache()
                presenter.updateUserDetails(et_first_name.text.toString(),
                        et_last_name.text.toString(),
                        et_dob.text.toString(),
                        profileImageUri)
            }.track()
    }

    private fun validate() {
        if(!et_dob.text.toString().isEmpty() && !et_first_name.text.toString().isEmpty())
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
        finish()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        validate()
    }
}
