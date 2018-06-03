package com.example.alfonsohernandez.boardgamebestfriends.presentation.chat

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.injection.modules.PresentationModule
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Group
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Message
import com.example.alfonsohernandez.boardgamebestfriends.domain.setVisibility
import com.example.alfonsohernandez.boardgamebestfriends.presentation.App
import kotlinx.android.synthetic.main.activity_chat.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ChatActivity : AppCompatActivity(), ChatContract.View {

    private val TAG = "ChatActivity"
    private var groupId = ""

    @Inject
    lateinit var presenter: ChatPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        setSupportActionBar(chatToolbar)
        supportActionBar!!.setTitle(getString(R.string.chatToolbarTitle))
        supportActionBar!!.setIcon(R.drawable.toolbarbgbf)

        if (savedInstanceState != null){
            groupId = savedInstanceState.getString("id", "")
        }else{
            var intent = getIntent().extras
            groupId = intent.getString("id", "")
        }

        injectDependencies()
        presenter.setView(this,groupId)

        chatIBmandar.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                sendMessage()
            }
        })
    }

    fun injectDependencies() {
        App.instance.component.plus(PresentationModule()).inject(this)
    }

    override fun onDestroy() {
        presenter.setView(null,"")
        super.onDestroy()
    }

    override fun setData(group: Group, messages: ArrayList<Message>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun sendMessage(){
        presenter.sendMessage(Message(presenter.getUserProfile()!!.id,chatETmessage.text.toString(),SimpleDateFormat("HH:mm-dd/MM/yy").format(Date())))
    }

    override fun showErrorAdding() {
        Toast.makeText(this, getString(R.string.chatErrorAddingMessage), Toast.LENGTH_SHORT).show()
    }

    override fun showErrorLoading() {
        Toast.makeText(this, getString(R.string.chatErrorLoadingChat), Toast.LENGTH_SHORT).show()
    }

    override fun showProgressBar(boolean: Boolean) {
        progressBarChat.setVisibility(boolean)
        chatLLall.setVisibility(!boolean)
    }
}
