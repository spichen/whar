package com.zigzag.whar.ui.dashboard.feeds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zigzag.whar.R
import com.zigzag.whar.arch.BaseFragment
import com.zigzag.whar.di.ActivityScoped
import kotlinx.android.synthetic.main.fragment_dashboard.*
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider
import javax.inject.Inject

@ActivityScoped
class FeedsFragment @Inject constructor(): BaseFragment<FeedsContract.View, FeedsContract.Presenter>(), FeedsContract.View {

    @Inject lateinit var feedsPresenter : FeedsPresenter

    override fun getPresenterImpl(): FeedsContract.Presenter = feedsPresenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.initiate(ReactiveLocationProvider(activity))
    }

    override fun displayFeeds() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun tempShowLocation(location : String) {
        tv_temp.text = location
    }


}