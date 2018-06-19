package com.example.alfonsohernandez.boardgamebestfriends.presentation.groups

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.injection.modules.PresentationModule
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Group
import com.example.alfonsohernandez.boardgamebestfriends.domain.setVisibility
import com.example.alfonsohernandez.boardgamebestfriends.presentation.App
import com.example.alfonsohernandez.boardgamebestfriends.presentation.adapters.AdapterGroups
import com.example.alfonsohernandez.boardgamebestfriends.presentation.addgroup.AddGroupActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.groupdetail.GroupDetailActivity
import kotlinx.android.synthetic.main.fragment_groups.*
import javax.inject.Inject
import android.app.Activity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.chat.ChatActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.dialogs.DialogFactory
import com.example.alfonsohernandez.boardgamebestfriends.presentation.meetingdetail.MeetingDetailActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.tab.TabActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.utils.NotificationFilter
import com.example.alfonsohernandez.boardgamebestfriends.presentation.utils.Snacktory
import com.google.firebase.messaging.RemoteMessage

/**
 * A simple [Fragment] subclass.
 */
class GroupsFragment : Fragment(),
        GroupsContract.View,
        View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    var topic = ""
    var title = ""
    var text = ""

    @Inject
    lateinit var presenter: GroupsPresenter

    var adapter = AdapterGroups()

    private val TAG = "GroupsFragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_groups, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        injectDependencies()
        presenter.setView(this)
        setupRecycler()

        swipeContainerGroups.setOnRefreshListener(this)
        fab.setOnClickListener(this)
    }

    override fun onDestroyView() {
        presenter.unsetViewFragment()
        super.onDestroyView()
    }

    override fun onDestroy() {
        presenter.unsetView()
        super.onDestroy()
    }

    override fun showNotification(rm: RemoteMessage) {
        topic = rm.from!!
        topic = topic.substring(8, topic.length)
        title = rm.notification!!.title!!
        text = rm.notification!!.title + "\n" + rm.notification!!.body

        chat()
        goToGroupDetail()
        goToMeetings()
        goToMeetingDetail()
        addedToNewGroup()

        if(title.contains("Group removed")){
            activity!!.runOnUiThread(object: Runnable {
                override fun run() {
                    presenter.getGroupsDataRx()
                    Snacktory.snacktoryNoAction(activity!!, text)
                }
            })

        }
    }

    fun injectDependencies() {
        App.instance.component.plus(PresentationModule()).inject(this)
    }

    fun setupRecycler() {
        rvGroupFragment.layoutManager = LinearLayoutManager(context)
        rvGroupFragment.adapter = adapter

        adapter.onGroupClickedListener = { group ->
            itemDetail(group.id)
        }

        adapter.onLongClickListener = { group ->
            presenter.getUserProfile()?.let {user ->
                context?.let { ctx ->
                    if (group.creator.equals(user.id)) {
                        DialogFactory.buildConfirmDialog(ctx, getString(R.string.groupsDialogDelete), Runnable { presenter.removeGroup(group) }).show()
                    } else {
                        DialogFactory.buildConfirmDialog(ctx, getString(R.string.groupsDialog), Runnable { presenter.removeUserFromGroup(group) }).show()
                    }
                }
            }
        }
    }

    fun startAddGroup(){
        val intent = Intent(context, AddGroupActivity::class.java)
        intent.putExtra("id","")
        startActivityForResult(intent,1)
    }

    fun itemDetail(id: String) {
        val intent = Intent(context, GroupDetailActivity::class.java)
        intent.putExtra("id", id)
        startActivity(intent)
    }

    override fun setData(groups: ArrayList<Group>) {
        adapter.groupList.clear()
        adapter.groupList.addAll(groups)
        adapter.notifyDataSetChanged()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.fab -> { startAddGroup() }
        }
    }

    override fun showError(stringId: Int) {
        Toast.makeText(context, getString(stringId), Toast.LENGTH_SHORT).show()
    }

    override fun showSuccess(stringId: Int) {
        Toast.makeText(context, getString(stringId), Toast.LENGTH_SHORT).show()
    }

    override fun showProgress(isLoading: Boolean) {
        progressBar?.setVisibility(isLoading)
        rvGroupFragment?.setVisibility(!isLoading)
        if (!isLoading)
            swipeContainerGroups?.isRefreshing = false
    }

    override fun onRefresh() {
        adapter.groupList.clear()
        presenter.getGroupsDataRx()
        swipeContainerGroups?.isRefreshing = false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK) {
            showSuccess(R.string.groupsSuccessAdding)
            presenter.updateGroupsFromCache()
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.updateGroupsFromCache()
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

    fun addedToNewGroup(){
        if(title.contains("Added to:")){
            activity!!.runOnUiThread(object: Runnable {
                override fun run() {
                    presenter.getGroupsDataRx()
                    Snacktory.snacktoryNoAction(activity!!,text)
                }
            })
        }
    }

    fun goToMeetings(){
        if(title.contains("Meeting removed")) {
            Snacktory.snacktoryBase(activity!!, text, Runnable {
                var intent = Intent(activity!!, TabActivity::class.java)
                startActivity(intent)
            })
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
