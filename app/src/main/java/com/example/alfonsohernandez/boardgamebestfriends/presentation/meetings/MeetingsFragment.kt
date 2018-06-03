package com.example.alfonsohernandez.boardgamebestfriends.presentation.meetings


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
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
import com.example.alfonsohernandez.boardgamebestfriends.presentation.meetingdetail.MeetingDetailActivity
import kotlinx.android.synthetic.main.fragment_meetings.*
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */

class MeetingsFragment : Fragment(),
        MeetingsContract.View,
        SearchView.OnQueryTextListener,
        SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var presenter: MeetingsPresenter

    var adapter = AdapterMeetings()

//    var host = false
//    var playing = false

    var kind: String = ""

    private val TAG = "MeetingsFragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_meetings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(savedInstanceState!=null)
            kind = savedInstanceState.getString("kind","")
        else{
            var intent = activity!!.intent.extras
            kind = intent.getString("kind","")
        }

        injectDependencies()
        setupRecycler()
        presenter.setView(this, kind)

        searchMeeting.setOnQueryTextListener(this)
        swipeContainerMeetings.setOnRefreshListener(this)

        fab.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                startAddMeeting()
            }
        })
    }

    fun startAddMeeting(){
        val intent = Intent(context, AddMeetingActivity::class.java)
        startActivityForResult(intent,1)
    }

    override fun onResume() {
        super.onResume()
        clearData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.setView(null,"")
    }

    fun injectDependencies() {
        App.instance.component.plus(PresentationModule()).inject(this)
    }

    override fun setupRecycler() {
        rvMeetingsFragment.layoutManager = LinearLayoutManager(context)
        rvMeetingsFragment.adapter = adapter

        adapter.onMeetingClickedListener = { meeting ->
            itemDetail(meeting.id)
        }

        adapter.onLongClickListener = { meeting ->
            if(meeting.creatorId.equals(presenter.getUserProfile()!!.id)) {
                val alertDilog = AlertDialog.Builder(context!!.applicationContext).create()
                alertDilog.setTitle(getString(R.string.meetingsDialogAlert))
                alertDilog.setMessage(getString(R.string.meetingsDialogRemove))

                alertDilog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.meetingsDialogAccept), { dialogInterface, i ->
                    presenter.removeMeeting(meeting.id)
                    adapter.meetingsList.remove(meeting)
                    adapter.notifyDataSetChanged()
                })

                alertDilog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.meetingsDialogCancel), { dialogInterface, i ->
                    alertDilog.dismiss()
                })
                alertDilog.show()
            }else{
                Toast.makeText(context, getString(R.string.meetingsDialogError), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun itemDetail(id: String) {
        val intent = Intent(context, MeetingDetailActivity::class.java)
        intent.putExtra("id", id)
        startActivity(intent)
    }

    override fun setData(meetings: Collection<Meeting>?) {
        //clearData()
        adapter.meetingsList.addAll(meetings!!)
        adapter.notifyDataSetChanged()
    }

    override fun clearData() {
        adapter.meetingsList.clear()
    }

    override fun setSingleData(meeting: Meeting) {
        adapter.meetingsList.add(meeting)
        var meetingList = ArrayList(adapter.meetingsList.sortedWith(compareBy({ it.date.substring(it.date.length-2,it.date.length) }, { it.date.substring(it.date.length-5,it.date.length-3) }, { it.date.substring(it.date.length-8,it.date.length-6) }, { it.date.substring(0,it.date.lastIndexOf("_")) })))
        adapter.meetingsList.clear()
        adapter.meetingsList.addAll(meetingList)
        adapter.notifyDataSetChanged()
    }

    override fun showErrorLoading() {
        Toast.makeText(context, getString(R.string.meetingsErrorLoading), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorRemoving() {
        Toast.makeText(context, getString(R.string.meetingsErrorRemoving), Toast.LENGTH_SHORT).show()
    }

    override fun successRemoving() {
        Toast.makeText(context, getString(R.string.meetingsSuccessRemoving), Toast.LENGTH_SHORT).show()
    }

    override fun successAdding() {
        Toast.makeText(context, getString(R.string.meetingsSuccessAdding), Toast.LENGTH_SHORT).show()
    }

    override fun showProgressBar(isLoading:Boolean) {
        progressBar.setVisibility(isLoading)
        rvMeetingsFragment.setVisibility(!isLoading)
        if (!isLoading)
            swipeContainerMeetings.isRefreshing = false
    }

    override fun onRefresh() {
        presenter.dataChooser()
        swipeContainerMeetings.isRefreshing = false
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        presenter.dataChooser()
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        presenter.dataChooser()
        return true
    }

    override fun noResult() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSearchData(): String {
        return searchMeeting.query.toString()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK) {
            successAdding()
            presenter.dataChooser()
        }
    }
}
