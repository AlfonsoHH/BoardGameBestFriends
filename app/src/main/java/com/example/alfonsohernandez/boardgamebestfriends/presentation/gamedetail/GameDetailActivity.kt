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
import kotlinx.android.synthetic.main.activity_game_detail.*
import javax.inject.Inject

class GameDetailActivity : AppCompatActivity(),GameDetailContract.View {

    private val TAG = "GameDetailActivity"

    private var gameId = ""
    private var kind = ""

    @Inject
    lateinit var presenter: GameDetailPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_detail)

        setSupportActionBar(gameDetailToolbar)
        supportActionBar!!.setTitle(getString(R.string.gameDetailToolbarTitle))
        supportActionBar!!.setIcon(R.drawable.toolbarbgbf)

        if(savedInstanceState != null) {
            gameId = savedInstanceState.getString("id", "")
            kind = savedInstanceState.getString("kind", "")
        } else {
            val extras = intent.extras
            gameId = extras.getString("id", "")
            kind = extras.getString("kind", "")
        }

        injectDependencies()
        presenter.setView(this,gameId)
    }

    fun injectDependencies() {
        App.instance.component.plus(PresentationModule()).inject(this)
    }

    override fun onDestroy() {
        presenter.setView(null,"")
        super.onDestroy()
    }

    override fun setData(game: Game) {
        Glide.with(this).load(game.photo).into(gameDetailIVphoto)
        gameDetailTVtitle.text = game.title
        gameDetailTVdescription.text = game.description
        gameDetailTVduration.text = game.playingTime.toString()
        gameDetailTVnumPlayers.text = game.minPlayers.toString() + " - " + game.maxPlayers.toString()

        if(kind.equals("")){
            gameDetailIVuser.alpha = 0.4f
            gameDetailIVgroup.alpha = 0.4f
            gameDetailIVplace.alpha = 0.4f
        }else if (kind.contains("buddy")){
            gameDetailIVgroup.alpha = 0.4f
            gameDetailIVplace.alpha = 0.4f
        }else if (kind.contains("group")){
            gameDetailIVuser.alpha = 0.4f
            gameDetailIVplace.alpha = 0.4f
        }else if (kind.contains("place")){
            gameDetailIVuser.alpha = 0.4f
            gameDetailIVgroup.alpha = 0.4f
        }

        Log.d(TAG,game.rating.toString())
        gameDetailRatingBar.rating = game.rating.toFloat()
    }

    override fun showErrorLoading() {
        Toast.makeText(this, getString(R.string.gameDetailErrorLoading),Toast.LENGTH_SHORT).show()
    }

    override fun showProgressBar(boolean: Boolean) {
        progressBarGameDetail.setVisibility(boolean)
        gameDetailFLall.setVisibility(!boolean)
    }

}