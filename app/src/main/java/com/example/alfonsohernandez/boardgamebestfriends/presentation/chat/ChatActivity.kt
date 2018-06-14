package com.example.alfonsohernandez.boardgamebestfriends.presentation.chat

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.injection.modules.PresentationModule
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Group
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Message
import com.example.alfonsohernandez.boardgamebestfriends.domain.setVisibility
import com.example.alfonsohernandez.boardgamebestfriends.presentation.App
import com.example.alfonsohernandez.boardgamebestfriends.presentation.adapters.AdapterChat
import com.example.alfonsohernandez.boardgamebestfriends.presentation.utils.NotificationFilter
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.RemoteMessage
import kotlinx.android.synthetic.main.activity_chat.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject





class ChatActivity : AppCompatActivity(),
        ChatContract.View,
        View.OnClickListener{

    private val TAG = "ChatActivity"
    private var groupId = ""
    private val sdf = SimpleDateFormat("HH:mm-dd/MM/yy")

    var adapter = AdapterChat()

    @Inject
    lateinit var presenter: ChatPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        setSupportActionBar(chatToolbar)
        supportActionBar?.setIcon(R.drawable.toolbarbgbf)

        val intent = getIntent().extras
        intent?.let {
            groupId = it.getString("id", "")
        }

        injectDependencies()
        presenter.setView(this, groupId)
        setupRecycler()

        chatIBmandar.setOnClickListener(this)
    }

    override fun showNotification(rm: RemoteMessage) {
        var nf = NotificationFilter(this,rm)
        if(!nf.topic.equals(groupId))
            nf.chat()
        nf.groupUser()
        nf.groupRemoved()
        nf.meetingModified()
        nf.meetingRemoved()
    }

    fun injectDependencies() {
        App.instance.component.plus(PresentationModule()).inject(this)
    }

    override fun onDestroy() {
        presenter.unsetView()
        super.onDestroy()
    }

    override fun setupRecycler() {
        val linearLayout = LinearLayoutManager(this)
        chatRV.layoutManager = linearLayout
        chatRV.adapter = adapter
        chatRV.scrollToPosition(adapter.getItemCount()-1)

        chatRV.postDelayed(Runnable { chatRV.scrollToPosition(chatRV.adapter.itemCount - 1) }, 300)
    }

    override fun setData(group: Group, messages: ArrayList<Message>) {
//        adapter.messageList.clear()
//        for(message in messages){
//            adapter.messageList.add(message)
//        }
//        adapter.notifyDataSetChanged()
        adapter.messageList.clear()
        adapter.messageList.addAll(messages)
        adapter.notifyDataSetChanged()
        chatRV.scrollToPosition(adapter.getItemCount()-1)
        supportActionBar?.setTitle(getString(R.string.chatToolbarTitle) + " " + group.title)
    }

    fun sendMessage() {
        presenter.getUserProfile()?.let {user ->
            presenter.sendMessage(Message(user.id, chatETmessage.text.toString(), sdf.format(Date())))
            chatETmessage.setText("")
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.chatIBmandar -> { sendMessage() }
        }
    }

    override fun showError(stringId: Int) {
        Toast.makeText(this, getString(stringId), Toast.LENGTH_SHORT).show()
    }

    override fun showSuccess(stringId: Int) {
        Toast.makeText(this, getString(stringId), Toast.LENGTH_SHORT).show()
    }

    override fun showProgress(isLoading: Boolean) {
        progressBarChat?.setVisibility(isLoading)
        chatLLall?.setVisibility(!isLoading)
    }
}
