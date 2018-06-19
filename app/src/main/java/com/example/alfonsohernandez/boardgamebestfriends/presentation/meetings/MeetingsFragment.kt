package com.example.alfonsohernandez.boardgamebestfriends.presentation.meetings

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.injection.modules.PresentationModule
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Meeting
import com.example.alfonsohernandez.boardgamebestfriends.domain.setVisibility
import com.example.alfonsohernandez.boardgamebestfriends.presentation.App
import com.example.alfonsohernandez.boardgamebestfriends.presentation.adapters.AdapterMeetings
import com.example.alfonsohernandez.boardgamebestfriends.presentation.addmeeting.AddMeetingActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.chat.ChatActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.dialogs.DialogFactory
import com.example.alfonsohernandez.boardgamebestfriends.presentation.groupdetail.GroupDetailActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.meetingdetail.MeetingDetailActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.tab.TabActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.utils.NotificationFilter
import com.example.alfonsohernandez.boardgamebestfriends.presentation.utils.Snacktory
import com.google.firebase.messaging.RemoteMessage
import kotlinx.android.synthetic.main.fragment_meetings.*
import javax.inject.Inject

class MeetingsFragment : Fragment(),
        MeetingsContract.View,
        View.OnClickListener,
        SearchView.OnQueryTextListener,
        SwipeRefreshLayout.OnRefreshListener {

    var topic = ""
    var title = ""
    var text = ""

    @Inject
    lateinit var presenter: MeetingsPresenter

    var adapter = AdapterMeetings()

    var kind: String = ""

    private val TAG = "MeetingsFragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_meetings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val intent = activity?.intent?.extras
        intent?.let {
            kind = it.getString("kind", "")
        }

        injectDependencies()
        setupRecycler()
        presenter.setView(this, kind)

        searchMeeting.setOnQueryTextListener(this)
        swipeContainerMeetings.setOnRefreshListener(this)
        fab.setOnClickListener(this)
    }

    override fun showNotification(rm: RemoteMessage) {
        topic = rm.from!!
        topic = topic.substring(8, topic.length)
        title = rm.notification!!.title!!
        text = rm.notification!!.title + "\n" + rm.notification!!.body

        chat()
        goToGroupDetail()
        goToGroups()
        goToMeetingDetail()
        addedToNewGroup()

        if(title.contains("Meeting removed")){
            activity!!.runOnUiThread(object: Runnable {
                override fun run() {
                    presenter.initialDataChooser()
                    Snacktory.snacktoryNoAction(activity!!, text)
                }
            })
        }
    }

    fun startAddMeeting(){
        val intent = Intent(context, AddMeetingActivity::class.java)
        startActivityForResult(intent,1)
    }

    override fun onDestroy() {
        presenter.unsetView()
        super.onDestroy()
    }

    override fun onDestroyView() {
        presenter.unsetViewFragment()
        super.onDestroyView()
    }

    fun injectDependencies() {
        App.instance.component.plus(PresentationModule()).inject(this)
    }

    fun setupRecycler() {
        rvMeetingsFragment.layoutManager = LinearLayoutManager(context)
        rvMeetingsFragment.adapter = adapter

        adapter.onMeetingClickedListener = { meeting ->
            itemDetail(meeting.id)
        }

        adapter.onLongClickListener = { meeting ->
            if(meeting.creatorId.equals(presenter.getUserProfile()?.id)) {
                context?.let {
                    DialogFactory.buildConfirmDialog(it, getString(R.string.meetingsDialogRemove), Runnable {
                        presenter.removeMeeting(meeting.id)
                        adapter.meetingsList.remove(meeting)
                        adapter.notifyDataSetChanged()
                    }).show()
                }
            }else{
                Toast.makeText(context, getString(R.string.meetingsDialogError), Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun itemDetail(id: String) {
        val intent = Intent(context, MeetingDetailActivity::class.java)
        intent.putExtra("id", id)
        startActivity(intent)
    }

    override fun setData(meetings: Collection<Meeting>?) {
        meetings?.let {
            adapter.meetingsList.clear()
            adapter.meetingsList.addAll(it)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.fab -> { startAddMeeting() }
        }
    }

    override fun showError(stringId: Int) {
        Toast.makeText(context, getString(stringId), Toast.LENGTH_SHORT).show()
    }

    override fun showSuccess(stringId: Int) {
        Toast.makeText(context, getString(stringId), Toast.LENGTH_SHORT).show()
    }

    override fun showProgress(isLoading:Boolean) {
        progressBar?.setVisibility(isLoading)
        rvMeetingsFragment?.setVisibility(!isLoading)
        if (!isLoading)
            swipeContainerMeetings?.isRefreshing = false
    }

    override fun onRefresh() {
        presenter.initialDataChooser()
        swipeContainerMeetings?.isRefreshing = false
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        presenter.cacheDataChooser()
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        presenter.cacheDataChooser()
        return true
    }

    override fun getSearchData(): String? {
        return searchMeeting?.query.toString()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK) {
            showSuccess(R.string.meetingsSuccessAdding)
            presenter.cacheDataChooser()
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.cacheDataChooser()
    }

    fun chat() {
        if (title.contains("Chat")) {
            Snacktory.snacktoryBase(activity!!, text, Runnable {
                var intent = Intent(activity!!, ChatActivity::class.java)
                intent.putExtra("id", topic)
                startActivity(intent)
            })
        }
    }

    fun goToGroupDetail(){
        if(title.contains("Group user")) {
            Snacktory.snacktoryBase(activity!!, text, Runnable {
                var intent = Intent(activity!!, GroupDetailActivity::class.java)
                intent.putExtra("id", topic)
                startActivity(intent)
            })
        }
    }

    fun goToGroups(){
        if(title.contains("Group removed")) {
            Snacktory.snacktoryBase(activity!!, text, Runnable {
                var intent = Intent(activity!!, TabActivity::class.java)
                intent.putExtra("otherTab", 1)
                startActivity(intent)
            })
        }
    }

    fun addedToNewGroup(){
        if(title.contains("Added to:")){
            Snacktory.snacktoryNoAction(activity!!,text)
        }
    }

    fun goToMeetingDetail(){
        if(title.contains("Meeting modified") || title.contains("Meeting starting soon")) {
            Snacktory.snacktoryBase(activity!!, text, Runnable {
                var intent = Intent(activity!!, MeetingDetailActivity::class.java)
                intent.putExtra("id", topic)
                startActivity(intent)
            })
        }
    }
}
