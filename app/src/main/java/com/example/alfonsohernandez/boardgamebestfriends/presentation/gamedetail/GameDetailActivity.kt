package com.example.alfonsohernandez.boardgamebestfriends.presentation.gamedetail

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.injection.modules.PresentationModule
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Game
import com.example.alfonsohernandez.boardgamebestfriends.domain.setVisibility
import com.example.alfonsohernandez.boardgamebestfriends.presentation.App
import com.example.alfonsohernandez.boardgamebestfriends.presentation.utils.NotificationFilter
import com.google.firebase.messaging.RemoteMessage
import kotlinx.android.synthetic.main.activity_game_detail.*
import javax.inject.Inject

class GameDetailActivity : AppCompatActivity(), GameDetailContract.View {

    private val TAG = "GameDetailActivity"

    private var gameId = ""
    private var kind = ""

    @Inject
    lateinit var presenter: GameDetailPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_detail)

        setSupportActionBar(gameDetailToolbar)
        supportActionBar?.setTitle(getString(R.string.gameDetailToolbarTitle))
        supportActionBar?.setIcon(R.drawable.toolbarbgbf)

        val extras = intent.extras
        extras?.let {
            gameId = it.getString("id", "")
            kind = it.getString("kind", "")
        }

        injectDependencies()
        presenter.setView(this, gameId)
    }

    override fun showNotification(rm: RemoteMessage) {
        var nf = NotificationFilter(this,rm)
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
        presenter.setView(null, "")
        super.onDestroy()
    }

    override fun setData(game: Game) {
        Glide.with(this).load(game.photo).into(gameDetailIVphoto)
        gameDetailTVtitle.text = game.title
        gameDetailTVdescription.text = game.description
        gameDetailTVduration.text = game.playingTime.toString()
        gameDetailTVnumPlayers.text = game.minPlayers.toString() + " - " + game.maxPlayers.toString()

        if (kind.equals("")) {
            gameDetailIVuser.alpha = 0.4f
            gameDetailIVgroup.alpha = 0.4f
            gameDetailIVplace.alpha = 0.4f
        } else if (kind.contains("buddy")) {
            gameDetailIVgroup.alpha = 0.4f
            gameDetailIVplace.alpha = 0.4f
        } else if (kind.contains("group")) {
            gameDetailIVuser.alpha = 0.4f
            gameDetailIVplace.alpha = 0.4f
        } else if (kind.contains("place")) {
            gameDetailIVuser.alpha = 0.4f
            gameDetailIVgroup.alpha = 0.4f
        }

        gameDetailRatingBar.rating = game.rating.toFloat()
    }

    override fun showError(stringId: Int) {
        Toast.makeText(this, getString(stringId), Toast.LENGTH_SHORT).show()
    }

    override fun showSuccess(stringId: Int) {
        Toast.makeText(this, getString(stringId), Toast.LENGTH_SHORT).show()
    }

    override fun showProgress(isLoading: Boolean) {
        progressBarGameDetail?.setVisibility(isLoading)
        gameDetailFLall?.setVisibility(!isLoading)
    }

}
