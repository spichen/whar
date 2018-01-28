package com.zigzag.whar.ui.dashboard.feeds

import android.support.v4.app.Fragment
import com.zigzag.whar.di.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class FeedsFragment @Inject constructor() : Fragment()/* BaseFragment<FeedsContract.View, FeedsContract.Presenter>(), FeedsContract.View */{
/*
    @Inject lateinit var feedsPresenter : FeedsPresenter

    override fun getPresenterImpl(): FeedsContract.Presenter = feedsPresenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.initiate(ReactiveLocationProvider(activity))
        initObservations()
        initViews()
    }

    private fun initViews() {
        val query = FirebaseFirestore.getInstance()
                .collection("messages")
                .orderBy("g")
                .limit(50)
        val options = FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query, Message::class.java)
                .build()

        val adapter = object : FirestoreRecyclerAdapter<Message, MessageViewHolder>(options) {

            override fun onChildChanged(type: ChangeEventType, snapshot: DocumentSnapshot, newIndex: Int, oldIndex: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onBindViewHolder(holder: MessageViewHolder, position: Int, model: Message) {
                // Bind the Chat object to the ChatHolder
                // ...
                holder.setMessage(model.message)
            }

            override fun onCreateViewHolder(group: ViewGroup, i: Int): MessageViewHolder {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                val view = LayoutInflater.from(group.context)
                        .inflate(R.layout.item_message, group, false)

                return MessageViewHolder(view)
            }
        }

        val mLayoutManager = LinearLayoutManager(activity)
        rv_feeds.layoutManager = mLayoutManager
        rv_feeds.adapter = adapter

    }

    private fun initObservations() {
        btn_send.clicks()
                .subscribe {
                    presenter.sendMessage(et_new_message.text.toString())
                }
    }

    override fun displayFeeds() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun tempShowLocation(location : String) {
        tv_temp.text = location
    }*/

}