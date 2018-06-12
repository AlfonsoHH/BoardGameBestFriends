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
        supportActionBar?.setTitle(getString(R.string.chatToolbarTitle))
        supportActionBar?.setIcon(R.drawable.toolbarbgbf)

        val intent = getIntent().extras
        groupId = intent.getString("id", "")

        injectDependencies()
        presenter.setView(this, groupId)
        setupRecycler()

        chatIBmandar.setOnClickListener(this)
    }

    fun injectDependencies() {
        App.instance.component.plus(PresentationModule()).inject(this)
    }

    override fun onDestroy() {
        presenter.setView(null, "")
        super.onDestroy()
    }

    override fun setupRecycler() {
        val linearLayout = LinearLayoutManager(this)
        linearLayout.reverseLayout = true
        chatRV.layoutManager = linearLayout
        chatRV.adapter = adapter
    }

    override fun setData(group: Group, messages: ArrayList<Message>) {
        adapter.messageList.clear()
        adapter.messageList.addAll(messages)
        adapter.notifyDataSetChanged()
    }

    fun sendMessage() {
        presenter.getUserProfile()?.let {user ->
            presenter.sendMessage(Message(user.id, chatETmessage.text.toString(), sdf.format(Date()), null, user.userName))
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
